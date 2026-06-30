from __future__ import annotations

import time
from pathlib import Path

from selenium import webdriver
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.common.by import By

ROOT = Path(__file__).resolve().parents[1]
SHOT_DIR = ROOT / "deliverables" / "screenshots"
SHOT_DIR.mkdir(parents=True, exist_ok=True)

BASE = "http://localhost:8080/app"


def screenshot(driver, name: str):
    path = SHOT_DIR / name
    driver.save_screenshot(str(path))
    print(path)


def login(driver, username: str, password: str = "Bookstore@123"):
    driver.get(f"{BASE}/login")
    driver.find_element(By.NAME, "username").send_keys(username)
    driver.find_element(By.NAME, "password").send_keys(password)
    driver.find_element(By.CSS_SELECTOR, "button[type=submit]").click()
    time.sleep(0.5)


def main():
    opts = Options()
    opts.add_argument("--headless=new")
    opts.add_argument("--disable-gpu")
    opts.add_argument("--window-size=1280,900")
    opts.add_argument("--no-sandbox")
    opts.add_argument("--disable-dev-shm-usage")
    opts.add_argument("--hide-scrollbars")

    driver = webdriver.Chrome(service=Service(), options=opts)
    driver.set_window_size(1280, 900)

    try:
        # 01 public books
        driver.get(f"{BASE}/books")
        time.sleep(0.5)
        screenshot(driver, "01-public-books.png")

        # Login as customer
        login(driver, "customer")

        # Add a book to cart from public list
        driver.get(f"{BASE}/books")
        time.sleep(0.5)
        # Find the first add-to-cart form and submit
        forms = driver.find_elements(By.CSS_SELECTOR, "form[action*='cart/add']")
        if forms:
            forms[0].find_element(By.CSS_SELECTOR, "button[type=submit]").click()
            time.sleep(0.5)

        # 02 cart
        driver.get(f"{BASE}/cart")
        time.sleep(0.5)
        screenshot(driver, "02-customer-cart-updated.png")

        # Checkout
        driver.find_element(By.CSS_SELECTOR, "form[action*='checkout'] button[type=submit]").click()
        time.sleep(0.5)

        # 03 customer order detail (newest order) — capture URL so 03b can return to it.
        driver.get(f"{BASE}/orders")
        time.sleep(0.5)
        links = driver.find_elements(By.CSS_SELECTOR, "a[href*='orders/detail']")
        detail_url = links[0].get_attribute("href") if links else None
        if detail_url:
            driver.get(detail_url)
            time.sleep(0.3)
        screenshot(driver, "03-customer-order-detail.png")

        # Logout and login as operator
        driver.get(f"{BASE}/logout")
        time.sleep(0.3)
        login(driver, "operator")

        # 04 admin dashboard
        driver.get(f"{BASE}/admin")
        time.sleep(0.5)
        screenshot(driver, "04-admin-dashboard.png")

        # Create a new book
        driver.get(f"{BASE}/admin/books/new")
        time.sleep(0.3)
        driver.find_element(By.NAME, "title").send_keys("演示新增图书")
        driver.find_element(By.NAME, "author").send_keys("演示作者")
        driver.find_element(By.NAME, "publisher").send_keys("演示出版社")
        driver.find_element(By.NAME, "isbn").send_keys("9780000000001")
        driver.find_element(By.NAME, "price").clear()
        driver.find_element(By.NAME, "price").send_keys("59.00")
        driver.find_element(By.NAME, "stock").clear()
        driver.find_element(By.NAME, "stock").send_keys("20")
        driver.find_element(By.CSS_SELECTOR, "button[type=submit]").click()
        time.sleep(0.5)

        # 05 admin book list
        driver.get(f"{BASE}/admin/books")
        time.sleep(0.5)
        screenshot(driver, "05-admin-book-created.png")

        # 06 categories
        driver.get(f"{BASE}/admin/categories")
        time.sleep(0.5)
        screenshot(driver, "06-admin-categories.png")

        # Ship a pending-ship order
        driver.get(f"{BASE}/admin/orders")
        time.sleep(0.5)
        rows = driver.find_elements(By.CSS_SELECTOR, "table tbody tr")
        ship_href = None
        for row in rows:
            if "待发货" in row.text:
                link = row.find_element(By.CSS_SELECTOR, "a[href*='orders/detail']")
                ship_href = link.get_attribute("href")
                break
        if ship_href:
            driver.get(ship_href)
            time.sleep(0.3)
            tracking = driver.find_element(By.NAME, "trackingNo")
            tracking.send_keys("YT202606300099")
            driver.find_element(By.CSS_SELECTOR, "button[type=submit]").click()
            time.sleep(0.5)

        # Customer confirms receipt so 03b shows a truly completed order.
        driver.get(f"{BASE}/logout")
        time.sleep(0.3)
        login(driver, "customer")
        if detail_url:
            driver.get(detail_url)
            time.sleep(0.3)
            driver.find_element(By.CSS_SELECTOR, "form[action*='orders/confirm'] button[type=submit]").click()
            time.sleep(0.5)
        screenshot(driver, "03b-customer-order-completed.png")

        # Back to operator for remaining admin screenshots
        driver.get(f"{BASE}/logout")
        time.sleep(0.3)
        login(driver, "operator")

        # 07 order shipped
        driver.get(f"{BASE}/admin/orders")
        time.sleep(0.5)
        screenshot(driver, "07-admin-order-shipped.png")

        # 08 users
        driver.get(f"{BASE}/admin/users")
        time.sleep(0.5)
        screenshot(driver, "08-admin-users-mask.png")

        # 09 stats
        driver.get(f"{BASE}/admin/stats")
        time.sleep(1.0)
        screenshot(driver, "09-admin-sales-stats.png")

        # Logout and login as auditor
        driver.get(f"{BASE}/logout")
        time.sleep(0.3)
        login(driver, "auditor")

        # 10 audit log
        driver.get(f"{BASE}/admin/audit")
        time.sleep(0.5)
        screenshot(driver, "10-audit-log.png")

        # 11 auditor 403 on stats
        driver.get(f"{BASE}/admin/stats")
        time.sleep(0.5)
        screenshot(driver, "11-auditor-stats-denied.png")

    finally:
        driver.quit()


if __name__ == "__main__":
    main()
