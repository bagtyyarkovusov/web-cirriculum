from __future__ import annotations

import base64
import json
import subprocess
import time
import urllib.request
from pathlib import Path

import websocket

ROOT = Path(__file__).resolve().parents[1]
SHOT_DIR = ROOT / "deliverables" / "screenshots"
SHOT_DIR.mkdir(parents=True, exist_ok=True)

BASE = "http://localhost:8080/app"
DEBUG_PORT = 9223


def start_chrome():
    cmd = [
        "/Applications/Google Chrome.app/Contents/MacOS/Google Chrome",
        f"--remote-debugging-port={DEBUG_PORT}",
        "--remote-allow-origins=*",
        "--headless=new",
        "--disable-gpu",
        "--window-size=1280,900",
        "--no-sandbox",
        "--disable-dev-shm-usage",
        "--hide-scrollbars",
        "about:blank",
    ]
    proc = subprocess.Popen(cmd, stdout=subprocess.DEVNULL, stderr=subprocess.DEVNULL)
    # Wait for debugger
    for _ in range(30):
        try:
            urllib.request.urlopen(f"http://localhost:{DEBUG_PORT}/json", timeout=1)
            return proc
        except Exception:
            time.sleep(0.5)
    raise RuntimeError("Chrome debugger did not start")


def get_ws_url():
    with urllib.request.urlopen(f"http://localhost:{DEBUG_PORT}/json") as resp:
        pages = json.loads(resp.read())
    for page in pages:
        if page["type"] == "page":
            return page["webSocketDebuggerUrl"]
    raise RuntimeError("No page found")


class CDP:
    def __init__(self):
        self.ws = websocket.create_connection(get_ws_url())
        self._id = 0
        self.ws.send(json.dumps({"id": self.next_id(), "method": "Page.enable"}))
        self.ws.send(json.dumps({"id": self.next_id(), "method": "Runtime.enable"}))
        self._drain()

    def next_id(self):
        self._id += 1
        return self._id

    def _drain(self):
        # Read any queued messages without blocking for long
        self.ws.settimeout(0.5)
        try:
            while True:
                self.ws.recv()
        except Exception:
            pass
        self.ws.settimeout(30)

    def send(self, method: str, params: dict | None = None, wait_for: str | None = None):
        msg = {"id": self.next_id(), "method": method}
        if params:
            msg["params"] = params
        self.ws.send(json.dumps(msg))
        # Wait for response with matching id
        start = time.time()
        while time.time() - start < 30:
            try:
                data = json.loads(self.ws.recv())
            except Exception:
                continue
            if data.get("id") == msg["id"]:
                return data
            if wait_for and data.get("method") == wait_for:
                return data
        return None

    def navigate(self, url: str):
        self.send("Page.navigate", {"url": url}, wait_for="Page.loadEventFired")
        time.sleep(0.7)

    def eval(self, expr: str):
        return self.send("Runtime.evaluate", {"expression": expr, "awaitPromise": True})

    def screenshot(self, name: str):
        res = self.send("Page.captureScreenshot", {"format": "png", "fromSurface": True})
        b64 = res["result"]["data"]
        path = SHOT_DIR / name
        path.write_bytes(base64.b64decode(b64))
        print(path)

    def close(self):
        self.ws.close()


def login(cdp: CDP, username: str):
    cdp.navigate(f"{BASE}/login")
    cdp.eval(f"document.querySelector('input[name=username]').value = {json.dumps(username)}")
    cdp.eval("document.querySelector('input[name=password]').value = 'Bookstore@123'")
    cdp.eval("document.querySelector('form[action*=\\\"login\\\"] button[type=submit]').click()")
    time.sleep(0.7)


def main():
    proc = start_chrome()
    try:
        cdp = CDP()

        # 01 public books
        cdp.navigate(f"{BASE}/books")
        cdp.screenshot("01-public-books.png")

        # Customer flow
        login(cdp, "customer")

        # Add first book to cart
        cdp.navigate(f"{BASE}/books")
        cdp.eval("document.querySelector('form[action*=\\\"cart/add\\\"] button[type=submit]').click()")
        time.sleep(0.7)

        # 02 cart
        cdp.navigate(f"{BASE}/cart")
        cdp.screenshot("02-customer-cart-updated.png")

        # Checkout
        cdp.eval("document.querySelector('form[action*=\\\"checkout\\\"] button[type=submit]').click()")
        time.sleep(0.7)

        # 03 newest order detail — capture the detail URL so 03b can return to it.
        cdp.navigate(f"{BASE}/orders")
        time.sleep(0.5)
        detail_url = None
        for _ in range(10):
            res = cdp.eval("(document.querySelector('a[href*=\\\"orders/detail\\\"]') || {}).href")
            result = res.get("result", {}).get("result", {})
            detail_url = result.get("value")
            if detail_url:
                break
            time.sleep(0.3)
        if not detail_url:
            raise RuntimeError("No order detail link found on /orders")
        cdp.navigate(detail_url)
        time.sleep(0.3)
        cdp.screenshot("03-customer-order-detail.png")

        # Logout / operator
        cdp.navigate(f"{BASE}/logout")
        time.sleep(0.3)
        login(cdp, "operator")

        # 04 admin dashboard
        cdp.navigate(f"{BASE}/admin")
        cdp.screenshot("04-admin-dashboard.png")

        # Create book
        cdp.navigate(f"{BASE}/admin/books/new")
        cdp.eval("document.querySelector('input[name=title]').value = '演示新增图书'")
        cdp.eval("document.querySelector('input[name=author]').value = '演示作者'")
        cdp.eval("document.querySelector('input[name=publisher]').value = '演示出版社'")
        cdp.eval("document.querySelector('input[name=isbn]').value = '9780000000001'")
        cdp.eval("document.querySelector('input[name=price]').value = '59.00'")
        cdp.eval("document.querySelector('input[name=stock]').value = '20'")
        cdp.eval("document.querySelector('form[action*=\\\"books/save\\\"] button[type=submit]').click()")
        time.sleep(0.7)

        # 05 book list
        cdp.navigate(f"{BASE}/admin/books")
        cdp.screenshot("05-admin-book-created.png")

        # 06 categories
        cdp.navigate(f"{BASE}/admin/categories")
        cdp.screenshot("06-admin-categories.png")

        # Ship pending order
        cdp.navigate(f"{BASE}/admin/orders")
        cdp.eval("""
            const rows = document.querySelectorAll('table tbody tr');
            for (const row of rows) {
                if (row.textContent.includes('待发货')) {
                    row.querySelector('a[href*="orders/detail"]').click();
                    break;
                }
            }
        """)
        time.sleep(0.7)
        cdp.eval("document.querySelector('input[name=trackingNo]').value = 'YT202606300099'")
        cdp.eval("document.querySelector('form[action*=\\\"orders/ship\\\"] button[type=submit]').click()")
        time.sleep(0.7)

        # Customer confirms receipt so 03b can show a truly completed order.
        cdp.navigate(f"{BASE}/logout")
        time.sleep(0.3)
        login(cdp, "customer")
        cdp.navigate(detail_url)
        time.sleep(0.3)
        cdp.eval("document.querySelector('form[action*=\\\"orders/confirm\\\"] button[type=submit]').click()")
        time.sleep(0.7)
        cdp.screenshot("03b-customer-order-completed.png")

        # Back to operator for remaining admin screenshots
        cdp.navigate(f"{BASE}/logout")
        time.sleep(0.3)
        login(cdp, "operator")

        # 07 orders shipped
        cdp.navigate(f"{BASE}/admin/orders")
        cdp.screenshot("07-admin-order-shipped.png")

        # 08 users
        cdp.navigate(f"{BASE}/admin/users")
        cdp.screenshot("08-admin-users-mask.png")

        # 09 stats
        cdp.navigate(f"{BASE}/admin/stats")
        time.sleep(1.0)
        cdp.screenshot("09-admin-sales-stats.png")

        # Logout / auditor
        cdp.navigate(f"{BASE}/logout")
        time.sleep(0.3)
        login(cdp, "auditor")

        # 10 audit log
        cdp.navigate(f"{BASE}/admin/audit")
        cdp.screenshot("10-audit-log.png")

        # 11 auditor denied stats
        cdp.navigate(f"{BASE}/admin/stats")
        cdp.screenshot("11-auditor-stats-denied.png")

        cdp.close()
    finally:
        proc.terminate()
        proc.wait()


if __name__ == "__main__":
    main()
