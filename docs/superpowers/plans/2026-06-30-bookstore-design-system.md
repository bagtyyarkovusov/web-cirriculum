# 网上书店 Design System + Full Seed Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Give the Servlet/JSP bookstore one coherent light, clean-blue design system across all pages, populate the seed data so it reads like a real store, and regenerate the three reports with the student name/ID and fresh screenshots.

**Architecture:** One vendored `app.css` (OKLCH tokens + component classes) plus three shared `.jspf` includes (head/header/footer) and an inline SVG icon sprite. Every JSP drops its private `<style>` and uses shared classes. Seed data expands in pure SQL (no crypto run). Reports regenerate via the existing python-docx script.

**Tech Stack:** Java 17, Servlet/JSP/JSTL, plain CSS (custom properties, OKLCH), MySQL, python-docx. No new dependencies, no build tooling, no CDN.

**Source of truth:** `docs/superpowers/specs/2026-06-30-bookstore-design-system.md`. Read it first.

**Build note:** No system JRE. Build with `JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home`.

---

## File Structure

Create:
- `src/main/webapp/static/css/app.css` — the entire design system (tokens + components)
- `src/main/webapp/static/img/icons.svg` — inline SVG symbol sprite
- `src/main/webapp/WEB-INF/views/common/head.jspf` — doctype/head/open body
- `src/main/webapp/WEB-INF/views/common/header.jspf` — sticky top bar
- `src/main/webapp/WEB-INF/views/common/footer.jspf` — close main/body + credit

Modify (drop private `<style>`, include shared parts, apply classes):
- `index.jsp` (redirect — verify only)
- `WEB-INF/views/book/list.jsp`, `cart/list.jsp`, `order/list.jsp`, `order/detail.jsp`
- `WEB-INF/views/auth/login.jsp`, `auth/register.jsp`
- `WEB-INF/views/admin/dashboard.jsp`, `admin/book/list.jsp`, `admin/book/form.jsp`,
  `admin/category/list.jsp`, `admin/order/list.jsp`, `admin/order/detail.jsp`,
  `admin/user/list.jsp`, `admin/stats.jsp`, `admin/audit/list.jsp`
- `src/main/resources/seed.sql` — expand
- `scripts/build_final_deliverables.py` — name/ID on covers + footer

Each phase is one commit. Build after structural phases. Re-screenshot + report regen are the last phases (need MySQL + browser).

---

## Task 1: Design tokens + base CSS

**Files:** Create `src/main/webapp/static/css/app.css`

- [ ] **Step 1: Create `app.css` with tokens, reset, base type, layout, top bar**

```css
@charset "UTF-8";
/* 网上书店 design system — product register, light, restrained clean-blue. */
:root {
  --accent: oklch(0.55 0.15 255);
  --accent-hover: oklch(0.49 0.15 255);
  --accent-weak: oklch(0.95 0.03 255);
  --accent-ring: oklch(0.70 0.12 255 / 0.40);
  --bg: oklch(0.985 0.004 255);
  --surface: oklch(0.995 0.002 255);
  --panel: oklch(0.97 0.005 255);
  --border: oklch(0.92 0.005 255);
  --text: oklch(0.32 0.02 260);
  --muted: oklch(0.55 0.02 260);
  --subtle: oklch(0.68 0.015 260);
  --pay-fg: oklch(0.50 0.10 75);  --pay-bg: oklch(0.95 0.05 80);
  --ship-fg: oklch(0.48 0.13 285); --ship-bg: oklch(0.95 0.04 285);
  --pending-fg: oklch(0.48 0.15 255); --pending-bg: oklch(0.95 0.03 255);
  --done-fg: oklch(0.48 0.10 150); --done-bg: oklch(0.95 0.05 150);
  --cancel-fg: oklch(0.50 0.01 260); --cancel-bg: oklch(0.95 0.004 260);
  --danger: oklch(0.55 0.16 25);
  --radius: 8px; --radius-sm: 6px;
  --shadow: 0 1px 2px oklch(0.5 0.02 260 / 0.06), 0 4px 12px oklch(0.5 0.02 260 / 0.05);
  --ease: cubic-bezier(0.22, 1, 0.36, 1); --dur: 160ms;
}
* { box-sizing: border-box; }
body {
  margin: 0; background: var(--bg); color: var(--text);
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", system-ui, "PingFang SC", "Microsoft YaHei", sans-serif;
  font-size: 16px; line-height: 1.6; -webkit-font-smoothing: antialiased;
}
a { color: var(--accent); text-decoration: none; }
a:hover { text-decoration: underline; }
h1, h2, h3 { color: var(--text); line-height: 1.25; margin: 0 0 0.5em; font-weight: 600; }
h1 { font-size: 1.75rem; font-weight: 650; letter-spacing: -0.01em; }
h2 { font-size: 1.375rem; }
h3 { font-size: 1.125rem; }
.num { font-variant-numeric: tabular-nums; }
.muted { color: var(--muted); }
.subtle { color: var(--subtle); font-size: 0.875rem; }
.container { max-width: 1080px; margin: 0 auto; padding: 24px 24px 56px; }
.page-head { display: flex; align-items: baseline; justify-content: space-between; gap: 16px; margin: 8px 0 20px; }
.page-head p { margin: 4px 0 0; }
.topbar { position: sticky; top: 0; z-index: 10; background: var(--panel); border-bottom: 1px solid var(--border); }
.topbar-inner { max-width: 1080px; margin: 0 auto; display: flex; align-items: center; gap: 16px; padding: 10px 24px; }
.brand { display: inline-flex; align-items: center; gap: 8px; font-weight: 650; color: var(--text); }
.brand .icon { color: var(--accent); }
.topnav { display: flex; align-items: center; gap: 2px; }
.topnav a { color: var(--muted); padding: 6px 10px; border-radius: var(--radius-sm); }
.topnav a:hover { color: var(--text); background: var(--accent-weak); text-decoration: none; }
.topnav a.active { color: var(--accent); }
.topbar-spacer { flex: 1; }
.whoami { display: inline-flex; align-items: center; gap: 8px; color: var(--muted); font-size: 0.9rem; }
.icon { width: 20px; height: 20px; display: inline-block; vertical-align: -4px; fill: none; stroke: currentColor; stroke-width: 1.5; stroke-linecap: round; stroke-linejoin: round; }
.icon-sm { width: 16px; height: 16px; vertical-align: -3px; }
@media (prefers-reduced-motion: reduce) { * { transition: none !important; } }
```

- [ ] **Step 2: Commit**

```bash
git add src/main/webapp/static/css/app.css
git commit -m "Add design-system tokens and base CSS"
```

---

## Task 2: Component CSS (buttons, forms, tables, pills, storefront, tiles, flash, stats)

**Files:** Modify `src/main/webapp/static/css/app.css` (append)

- [ ] **Step 1: Append component classes**

```css
/* buttons */
.btn { display: inline-flex; align-items: center; gap: 6px; font: inherit; font-size: 0.9rem; font-weight: 500; line-height: 1; padding: 9px 14px; border-radius: var(--radius-sm); border: 1px solid transparent; cursor: pointer; transition: background var(--dur) var(--ease), border-color var(--dur) var(--ease); }
.btn:focus-visible { outline: 3px solid var(--accent-ring); outline-offset: 1px; }
.btn-primary { background: var(--accent); color: oklch(0.99 0.01 255); }
.btn-primary:hover { background: var(--accent-hover); text-decoration: none; }
.btn-secondary { background: var(--surface); color: var(--text); border-color: var(--border); }
.btn-secondary:hover { background: var(--accent-weak); text-decoration: none; }
.btn-ghost { background: transparent; color: var(--accent); padding: 8px 10px; }
.btn-ghost:hover { background: var(--accent-weak); text-decoration: none; }
.btn-danger { background: transparent; color: var(--danger); border-color: color-mix(in oklab, var(--danger) 30%, var(--border)); }
.btn-danger:hover { background: oklch(0.96 0.03 25); text-decoration: none; }
.btn[disabled], .btn:disabled { opacity: 0.5; cursor: not-allowed; }
.btn-sm { padding: 6px 10px; font-size: 0.85rem; }
/* forms */
.field { display: block; margin-bottom: 14px; }
.field > label { display: block; font-size: 0.85rem; font-weight: 500; color: var(--muted); margin-bottom: 6px; }
input[type=text], input[type=password], input[type=number], input:not([type]), select, textarea {
  width: 100%; font: inherit; font-size: 0.95rem; color: var(--text); background: var(--surface);
  border: 1px solid var(--border); border-radius: var(--radius-sm); padding: 9px 11px;
  transition: border-color var(--dur) var(--ease), box-shadow var(--dur) var(--ease); }
input:focus, select:focus, textarea:focus { outline: none; border-color: var(--accent); box-shadow: 0 0 0 3px var(--accent-ring); }
.form-panel { max-width: 420px; margin: 32px auto; background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius); box-shadow: var(--shadow); padding: 28px; }
.form-panel h1 { font-size: 1.375rem; margin-bottom: 18px; }
/* tables — hairline rows only */
.table { width: 100%; border-collapse: collapse; background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius); overflow: hidden; }
.table thead th { text-align: left; font-size: 0.8rem; font-weight: 600; color: var(--muted); background: var(--panel); padding: 10px 14px; border-bottom: 1px solid var(--border); }
.table tbody td { padding: 12px 14px; border-bottom: 1px solid var(--border); }
.table tbody tr:last-child td { border-bottom: 0; }
.table tbody tr { transition: background var(--dur) var(--ease); }
.table tbody tr:hover { background: var(--accent-weak); }
.table .right { text-align: right; }
.price { font-weight: 600; font-variant-numeric: tabular-nums; }
/* pills */
.pill { display: inline-flex; align-items: center; gap: 5px; font-size: 0.78rem; font-weight: 500; padding: 3px 9px; border-radius: 999px; background: var(--cancel-bg); color: var(--cancel-fg); white-space: nowrap; }
.pill-pay { background: var(--pay-bg); color: var(--pay-fg); }
.pill-pending { background: var(--pending-bg); color: var(--pending-fg); }
.pill-ship { background: var(--ship-bg); color: var(--ship-fg); }
.pill-done { background: var(--done-bg); color: var(--done-fg); }
.pill-cancel { background: var(--cancel-bg); color: var(--cancel-fg); }
/* storefront */
.book-list { display: flex; flex-direction: column; gap: 2px; }
.book-row { display: grid; grid-template-columns: 48px 1fr auto; gap: 16px; align-items: center; padding: 14px; border-radius: var(--radius); transition: background var(--dur) var(--ease); }
.book-row:hover { background: var(--surface); }
.cover-chip { width: 48px; height: 64px; border-radius: 4px; display: flex; align-items: flex-end; padding: 5px; font-size: 0.55rem; line-height: 1.15; overflow: hidden; box-shadow: inset 0 0 0 1px var(--border); background: var(--accent-weak); color: var(--accent); }
.cover-1 { background: oklch(0.95 0.03 255); color: oklch(0.45 0.12 255); }
.cover-2 { background: oklch(0.95 0.04 25);  color: oklch(0.47 0.13 25); }
.cover-3 { background: oklch(0.95 0.04 150); color: oklch(0.44 0.11 150); }
.cover-4 { background: oklch(0.95 0.04 285); color: oklch(0.46 0.12 285); }
.cover-5 { background: oklch(0.95 0.05 80);  color: oklch(0.46 0.10 80); }
.cover-6 { background: oklch(0.95 0.04 200); color: oklch(0.45 0.11 200); }
.cover-7 { background: oklch(0.95 0.04 330); color: oklch(0.46 0.12 330); }
.cover-8 { background: oklch(0.95 0.05 50);  color: oklch(0.47 0.11 50); }
.book-meta .title { font-weight: 600; }
.book-meta .by { color: var(--muted); font-size: 0.9rem; }
.book-actions { display: flex; align-items: center; gap: 8px; }
.qty { width: 64px; }
/* dashboard tiles */
.tiles { display: grid; grid-template-columns: repeat(auto-fill, minmax(240px, 1fr)); gap: 12px; }
.tile { display: flex; align-items: center; gap: 12px; padding: 16px; background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius); color: var(--text); transition: border-color var(--dur) var(--ease), background var(--dur) var(--ease); }
.tile:hover { border-color: var(--accent); background: var(--accent-weak); text-decoration: none; }
.tile .icon { color: var(--accent); flex: none; }
.tile .label { font-weight: 600; }
.tile .desc { color: var(--muted); font-size: 0.85rem; }
.tile .chev { margin-left: auto; color: var(--subtle); }
/* flash + empty + panel */
.flash { display: flex; align-items: center; gap: 8px; padding: 10px 14px; border-radius: var(--radius-sm); margin-bottom: 16px; font-size: 0.9rem; }
.flash-ok { background: var(--done-bg); color: var(--done-fg); }
.flash-err { background: oklch(0.95 0.04 25); color: var(--danger); }
.empty { text-align: center; color: var(--muted); padding: 48px 16px; }
.empty .icon { width: 32px; height: 32px; color: var(--subtle); margin-bottom: 8px; }
.panel { background: var(--surface); border: 1px solid var(--border); border-radius: var(--radius); padding: 20px; }
.stat-row { display: flex; flex-wrap: wrap; gap: 32px; margin-bottom: 16px; }
.stat .figure { font-size: 1.5rem; font-weight: 650; font-variant-numeric: tabular-nums; }
.stat .cap { color: var(--muted); font-size: 0.85rem; }
#chart { width: 100%; height: 360px; }
```

- [ ] **Step 2: Commit**

```bash
git add src/main/webapp/static/css/app.css
git commit -m "Add design-system component CSS"
```

---

## Task 3: Icon sprite

**Files:** Create `src/main/webapp/static/img/icons.svg`

- [ ] **Step 1: Create the sprite (stroke icons, referenced via `#i-name`)**

```xml
<svg xmlns="http://www.w3.org/2000/svg" style="display:none">
  <symbol id="i-book" viewBox="0 0 24 24"><path d="M4 5a2 2 0 0 1 2-2h12v18H6a2 2 0 0 1-2-2z"/><path d="M8 3v16"/></symbol>
  <symbol id="i-cart" viewBox="0 0 24 24"><circle cx="9" cy="20" r="1"/><circle cx="18" cy="20" r="1"/><path d="M2 3h3l2.4 12.4a2 2 0 0 0 2 1.6h7.7a2 2 0 0 0 2-1.6L23 7H6"/></symbol>
  <symbol id="i-user" viewBox="0 0 24 24"><circle cx="12" cy="8" r="4"/><path d="M4 21a8 8 0 0 1 16 0"/></symbol>
  <symbol id="i-logout" viewBox="0 0 24 24"><path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4"/><path d="M16 17l5-5-5-5"/><path d="M21 12H9"/></symbol>
  <symbol id="i-box" viewBox="0 0 24 24"><path d="M21 8l-9-5-9 5v8l9 5 9-5z"/><path d="M3 8l9 5 9-5"/><path d="M12 13v8"/></symbol>
  <symbol id="i-chart" viewBox="0 0 24 24"><path d="M3 3v18h18"/><path d="M7 14v3"/><path d="M12 9v8"/><path d="M17 5v12"/></symbol>
  <symbol id="i-shield" viewBox="0 0 24 24"><path d="M12 3l8 3v6c0 5-3.5 8-8 9-4.5-1-8-4-8-9V6z"/></symbol>
  <symbol id="i-tag" viewBox="0 0 24 24"><path d="M3 12V5a2 2 0 0 1 2-2h7l9 9-9 9z"/><circle cx="8" cy="8" r="1.5"/></symbol>
  <symbol id="i-plus" viewBox="0 0 24 24"><path d="M12 5v14"/><path d="M5 12h14"/></symbol>
  <symbol id="i-check" viewBox="0 0 24 24"><path d="M20 6L9 17l-5-5"/></symbol>
  <symbol id="i-x" viewBox="0 0 24 24"><path d="M18 6 6 18"/><path d="M6 6l12 12"/></symbol>
  <symbol id="i-chev" viewBox="0 0 24 24"><path d="M9 6l6 6-6 6"/></symbol>
</svg>
```

- [ ] **Step 2: Commit**

```bash
git add src/main/webapp/static/img/icons.svg
git commit -m "Add inline SVG icon sprite"
```

---

## Task 4: Shared JSP includes

**Files:** Create `WEB-INF/views/common/{head,header,footer}.jspf`

- [ ] **Step 1: `head.jspf`** (set `pageTitle` before including it)

```jsp
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>网上书店 · <c:out value="${pageTitle}"/></title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/app.css">
</head>
<body>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<jsp:include page="/static/img/icons.svg"/>
```

Note: `<use href="#i-...">` resolves only when the sprite is inlined into the page DOM. `jsp:include` writes the static `.svg` bytes into the body, which works under the container default servlet. If icons render empty, inline the Task 3 sprite markup directly into `head.jspf` instead of using `jsp:include`.

- [ ] **Step 2: `header.jspf`** (sticky top bar, context-aware)

```jsp
<header class="topbar">
  <div class="topbar-inner">
    <a class="brand" href="${ctx}/app/books"><svg class="icon"><use href="#i-book"/></svg>网上书店</a>
    <nav class="topnav">
      <c:choose>
        <c:when test="${not empty sessionScope.currentUser and fn:startsWith(pageScope.section, 'admin')}">
          <a href="${ctx}/app/admin">管理后台</a>
        </c:when>
        <c:otherwise>
          <a href="${ctx}/app/books">图书</a>
          <c:if test="${not empty sessionScope.currentUser}">
            <a href="${ctx}/app/cart">购物车</a>
            <a href="${ctx}/app/orders">我的订单</a>
          </c:if>
        </c:otherwise>
      </c:choose>
    </nav>
    <span class="topbar-spacer"></span>
    <c:choose>
      <c:when test="${not empty sessionScope.currentUser}">
        <span class="whoami"><svg class="icon icon-sm"><use href="#i-user"/></svg>
          <c:out value="${sessionScope.currentUser.username}"/>
          <span class="pill pill-pending"><c:out value="${sessionScope.currentUser.role}"/></span>
        </span>
        <a class="btn btn-ghost btn-sm" href="${ctx}/app/logout"><svg class="icon icon-sm"><use href="#i-logout"/></svg>退出</a>
      </c:when>
      <c:otherwise>
        <a class="btn btn-ghost btn-sm" href="${ctx}/app/login">登录</a>
        <a class="btn btn-primary btn-sm" href="${ctx}/app/register">注册</a>
      </c:otherwise>
    </c:choose>
  </div>
</header>
<main class="container">
```

Note: `header.jspf` uses `fn:` — ensure including pages declare `<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>`, or simplify the `<c:when>` test to `${pageScope.section == 'admin'}` and set `<c:set var="section" value="admin"/>` on admin pages before the include. Prefer the `section` variable approach (no `fn:` needed):

```jsp
<c:when test="${not empty sessionScope.currentUser and section == 'admin'}">
```

- [ ] **Step 3: `footer.jspf`**

```jsp
</main>
<footer class="container subtle" style="padding-top:0;padding-bottom:32px">
  网上书店管理系统课程设计 · Bagtyyar Kovusov 312024805043
</footer>
</body>
</html>
```

- [ ] **Step 4: Commit**

```bash
git add src/main/webapp/WEB-INF/views/common
git commit -m "Add shared head/header/footer JSP includes"
```

---

## Task 5: Apply system to storefront views

**Files:** Modify `book/list.jsp`, `cart/list.jsp`, `order/list.jsp`, `order/detail.jsp`

Pattern for every view: set `pageTitle` (and `section` for admin), `<%@ include file="/WEB-INF/views/common/head.jspf" %>`, `<%@ include file="/WEB-INF/views/common/header.jspf" %>`, page content inside `<main>` (opened by header), then `<%@ include file="/WEB-INF/views/common/footer.jspf" %>`. Delete the old `<head>`, `<style>`, `<nav>`, `<body>`, `</html>`.

- [ ] **Step 1: Rewrite `book/list.jsp` (storefront archetype)**

```jsp
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="在售图书"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head">
  <div><h1>在售图书</h1><p class="muted">共 <strong class="num">${books.size()}</strong> 本图书</p></div>
</div>
<div class="book-list">
  <c:forEach var="b" items="${books}">
    <div class="book-row">
      <div class="cover-chip cover-${(b.categoryId == null ? 1 : ((b.categoryId - 1) % 8) + 1)}"><c:out value="${b.title}"/></div>
      <div class="book-meta">
        <div class="title"><c:out value="${b.title}"/></div>
        <div class="by"><c:out value="${b.author}"/> · <c:out value="${b.publisher}"/></div>
        <div class="subtle">ISBN <c:out value="${b.isbn}"/></div>
      </div>
      <div class="book-actions">
        <span class="price">￥${b.price}</span>
        <c:choose>
          <c:when test="${b.stock <= 0}"><span class="pill pill-cancel">缺货</span></c:when>
          <c:when test="${not empty sessionScope.currentUser}">
            <form method="post" action="${ctx}/app/cart/add" class="book-actions">
              <input type="hidden" name="bookId" value="${b.id}">
              <input class="qty num" type="number" name="qty" min="1" value="1">
              <button class="btn btn-primary btn-sm" type="submit"><svg class="icon icon-sm"><use href="#i-cart"/></svg>加入购物车</button>
            </form>
          </c:when>
          <c:otherwise><a class="btn btn-secondary btn-sm" href="${ctx}/app/login">登录后购买</a></c:otherwise>
        </c:choose>
      </div>
    </div>
  </c:forEach>
</div>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
```

- [ ] **Step 2: Rewrite `cart/list.jsp`** — same head/header/footer wrapper. Use a `.table` (columns 书名/单价/数量/小计/操作) with `.right .num` numeric cells and a `.btn-danger btn-sm` remove button. If the cart is empty, render:

```jsp
<div class="empty"><svg class="icon"><use href="#i-cart"/></svg><p>购物车是空的。</p><a class="btn btn-primary" href="${ctx}/app/books">去挑选图书</a></div>
```

Total row uses `<strong class="price">`. Keep the existing form actions/names; only restructure markup + classes.

- [ ] **Step 3: Rewrite `order/list.jsp`** — `.table` with columns 订单号/下单时间/金额/状态/操作. Render status with the pill mapping macro (see Task 7 Step 1). Empty state mirrors Step 2 with `#i-box` and "还没有订单。".

- [ ] **Step 4: Rewrite `order/detail.jsp`** — `<div class="page-head">` with order no + a status pill; a `.panel` for receiver/tracking/timestamps; a `.table` for line items; keep the confirm-receipt form as `.btn-primary`.

- [ ] **Step 5: Visual check + commit**

After Task 11 build, these are verified by screenshots. For now:

```bash
git add src/main/webapp/WEB-INF/views/book src/main/webapp/WEB-INF/views/cart src/main/webapp/WEB-INF/views/order
git commit -m "Apply design system to storefront views"
```

---

## Task 6: Apply system to auth views

**Files:** Modify `auth/login.jsp`, `auth/register.jsp`

- [ ] **Step 1: Rewrite `auth/login.jsp` (form archetype)**

```jsp
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="登录"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="form-panel">
  <h1>用户登录</h1>
  <c:if test="${not empty message}"><div class="flash flash-ok"><svg class="icon icon-sm"><use href="#i-check"/></svg><c:out value="${message}"/></div></c:if>
  <c:if test="${not empty error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${error}"/></div></c:if>
  <form method="post" action="${ctx}/app/login">
    <div class="field"><label>用户名</label><input name="username" value="${fn:escapeXml(username)}" required></div>
    <div class="field"><label>密码</label><input name="password" type="password" required></div>
    <button class="btn btn-primary" type="submit">登录</button>
  </form>
  <p class="subtle" style="margin-top:16px">还没有账号？<a href="${ctx}/app/register">注册新账号</a></p>
</div>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
```

- [ ] **Step 2: Rewrite `auth/register.jsp`** — same `.form-panel` pattern; one `.field` per input; keep all existing field names and validation messages; submit `.btn-primary`.

- [ ] **Step 3: Commit**

```bash
git add src/main/webapp/WEB-INF/views/auth
git commit -m "Apply design system to auth views"
```

---

## Task 7: Apply system to admin views

**Files:** Modify the nine `admin/**` views. Set `<c:set var="section" value="admin"/>` before the header include on each.

- [ ] **Step 1: Rewrite `admin/dashboard.jsp` (tiles archetype)** and define the reusable status-pill fragment used by order views.

Dashboard body:

```jsp
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="管理后台"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head"><div><h1>管理后台</h1>
  <p class="muted">当前用户 <strong><c:out value="${sessionScope.currentUser.username}"/></strong>
  <span class="pill pill-pending"><c:out value="${sessionScope.currentUser.role}"/></span></p></div></div>
<c:choose>
  <c:when test="${sessionScope.currentUser.role == 'AUDIT_ADMIN'}">
    <div class="tiles">
      <a class="tile" href="${ctx}/app/admin/audit"><svg class="icon"><use href="#i-shield"/></svg>
        <span><span class="label">审计日志</span><br><span class="desc">查看最近操作记录</span></span>
        <svg class="icon chev"><use href="#i-chev"/></svg></a>
    </div>
  </c:when>
  <c:otherwise>
    <div class="tiles">
      <a class="tile" href="${ctx}/app/admin/books"><svg class="icon"><use href="#i-book"/></svg><span><span class="label">图书管理</span><br><span class="desc">上下架与库存</span></span><svg class="icon chev"><use href="#i-chev"/></svg></a>
      <a class="tile" href="${ctx}/app/admin/categories"><svg class="icon"><use href="#i-tag"/></svg><span><span class="label">分类管理</span><br><span class="desc">维护图书分类</span></span><svg class="icon chev"><use href="#i-chev"/></svg></a>
      <a class="tile" href="${ctx}/app/admin/orders"><svg class="icon"><use href="#i-box"/></svg><span><span class="label">订单管理</span><br><span class="desc">发货与物流</span></span><svg class="icon chev"><use href="#i-chev"/></svg></a>
      <a class="tile" href="${ctx}/app/admin/stats"><svg class="icon"><use href="#i-chart"/></svg><span><span class="label">销售统计</span><br><span class="desc">ECharts 图表</span></span><svg class="icon chev"><use href="#i-chev"/></svg></a>
      <a class="tile" href="${ctx}/app/admin/users"><svg class="icon"><use href="#i-user"/></svg><span><span class="label">用户管理</span><br><span class="desc">账号与状态</span></span><svg class="icon chev"><use href="#i-chev"/></svg></a>
    </div>
  </c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
```

Status-pill mapping (use inline in order list/detail; `o.status` is the order status string):

```jsp
<c:choose>
  <c:when test="${o.status == 'PENDING_PAY'}"><span class="pill pill-pay">待支付</span></c:when>
  <c:when test="${o.status == 'PENDING_SHIP'}"><span class="pill pill-pending">待发货</span></c:when>
  <c:when test="${o.status == 'SHIPPED'}"><span class="pill pill-ship">已发货</span></c:when>
  <c:when test="${o.status == 'COMPLETED'}"><span class="pill pill-done">已完成</span></c:when>
  <c:otherwise><span class="pill pill-cancel">已取消</span></c:otherwise>
</c:choose>
```

- [ ] **Step 2: Rewrite `admin/user/list.jsp` (admin-table archetype, keeps `<shop:mask>`)**

```jsp
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="用户管理"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head"><div><h1>用户管理</h1>
  <p class="muted">运营管理员只能管理普通客户；系统管理员可管理非系统管理员账号。</p></div></div>
<table class="table">
  <thead><tr><th>ID</th><th>用户名</th><th>姓名</th><th>角色</th><th>状态</th><th class="right">失败次数</th><th>操作</th></tr></thead>
  <tbody>
    <c:forEach var="u" items="${users}">
      <tr>
        <td class="num">${u.id}</td>
        <td><c:out value="${u.username}"/></td>
        <td><shop:mask type="name" value="${u.realName}"/></td>
        <td><span class="pill pill-pending"><c:out value="${u.role}"/></span></td>
        <td><c:choose><c:when test="${u.status == 'ACTIVE'}"><span class="pill pill-done">ACTIVE</span></c:when><c:otherwise><span class="pill pill-cancel">DISABLED</span></c:otherwise></c:choose></td>
        <td class="right num">${u.failCount}</td>
        <td><!-- keep existing enable/disable form, button class .btn .btn-secondary .btn-sm --></td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
```

Preserve the existing `<%@ taglib prefix="shop" uri=... %>` declaration from the original file. Keep all existing form actions and parameter names; only change classes/markup.

- [ ] **Step 3: Rewrite the remaining admin views to the same archetype**

For each, set `section`/`pageTitle`, include head+header, wrap in `page-head` + `.table` (or `.panel`/form), include footer. Field-by-field they keep existing data bindings and form params:
- `admin/book/list.jsp` — table 书名/作者/价格(right)/库存(right)/状态(ON→pill-done, OFF→pill-cancel)/操作; top-right `<a class="btn btn-primary" href="${ctx}/app/admin/books/new"><svg class="icon icon-sm"><use href="#i-plus"/></svg>新增图书</a>`.
- `admin/book/form.jsp` — `.form-panel` wider (set `style="max-width:560px"`), one `.field` per column (title/author/publisher/isbn/price/stock/category/status/intro), submit `.btn-primary`.
- `admin/category/list.jsp` — table 分类/操作 + an inline add form (`.field` + `.btn-primary`), delete as `.btn-danger btn-sm`.
- `admin/order/list.jsp` — table 订单号/客户/金额(right)/状态(pill via Step 1 mapping)/下单时间/操作.
- `admin/order/detail.jsp` — `page-head` with order no + status pill; `.panel` for receiver/tracking; `.table` line items; ship form with tracking input `.field` + `.btn-primary`.
- `admin/stats.jsp` — keep the `<script src="${ctx}/static/js/echarts.min.js">` and the chart init JS unchanged; wrap chart in `<div class="panel"><div id="chart"></div></div>`; keep the fallback `.table`; optional `.stat-row` of quiet totals.
- `admin/audit/list.jsp` — keep the filter form (wrap inputs in `.field`s inline), results in `.table` 时间/用户/动作/详情/IP; empty state with `#i-shield` "没有匹配的审计记录。".

- [ ] **Step 4: Commit**

```bash
git add src/main/webapp/WEB-INF/views/admin
git commit -m "Apply design system to admin views"
```

---

## Task 8: index.jsp + static-serving check

**Files:** `src/main/webapp/index.jsp`

- [ ] **Step 1:** Open `index.jsp`. If it only redirects to `/app/books`, leave it. If it renders visible HTML, apply the head/header/footer wrapper.
- [ ] **Step 2: Commit if changed**

```bash
git add src/main/webapp/index.jsp && git commit -m "Tidy index.jsp" || echo "no change"
```

---

## Task 9: Expand seed data

**Files:** Modify `src/main/resources/seed.sql`

- [ ] **Step 1: Add categories 6-8** (after the existing category insert):

```sql
INSERT INTO category (id, name, parent_id) VALUES
 (6, '历史',     NULL),
 (7, '经济管理', NULL),
 (8, '童书',     NULL);
```

- [ ] **Step 2: Grow books to ~50.** Keep rows 1-9 unchanged (row 9 stays `OFF`). Append rows 10-50 across all 8 categories, varied price (19-199) and stock (include a few single-digit values and 2 more `OFF`), `cover_path` NULL. Follow this exact column order and shape:

```sql
INSERT INTO book (id, title, author, publisher, isbn, price, stock, category_id, cover_path, intro, status) VALUES
 (10, '代码大全（第2版）', 'Steve McConnell', '电子工业出版社', '9787121026003', 128.00, 22, 1, NULL, '软件构建实践经典。', 'ON'),
 (11, '重构（第2版）', 'Martin Fowler', '人民邮电出版社', '9787115508645', 99.00, 5, 1, NULL, '改善既有代码设计。', 'ON');
 -- ...continue ids 12-50. Distribution target: 计算机 ~12, 文学艺术 ~8, 科技 ~6,
 -- 外语 ~4, 考试辅导 ~4, 历史 ~6, 经济管理 ~6, 童书 ~4. Two more rows status 'OFF'.
```

- [ ] **Step 3: Add ~5 customers (ids 5-9).** Reuse customer id 1's `salt` and `password_sm3` verbatim (same demo password), `phone_enc`/`address_enc` NULL:

```sql
INSERT INTO `user`
 (id, username, password_sm3, salt, real_name, gender, phone_enc, address_enc, role, status, pwd_changed_at) VALUES
 (5, 'wangfang', 'da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93', '74ae25abc1b9080dcc90715881962b30', '王芳',   'F', NULL, NULL, 'CUSTOMER', 'ACTIVE', NOW()),
 (6, 'lihua',    'da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93', '74ae25abc1b9080dcc90715881962b30', '李华',   'M', NULL, NULL, 'CUSTOMER', 'ACTIVE', NOW()),
 (7, 'zhaolei',  'da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93', '74ae25abc1b9080dcc90715881962b30', '赵磊',   'M', NULL, NULL, 'CUSTOMER', 'ACTIVE', NOW()),
 (8, 'sunli',    'da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93', '74ae25abc1b9080dcc90715881962b30', '孙丽',   'F', NULL, NULL, 'CUSTOMER', 'ACTIVE', NOW()),
 (9, 'zhouyu',   'da4886d49f5694d5d252baf1d90d8bc02030469eba613ff499d0eb1b6e13ad93', '74ae25abc1b9080dcc90715881962b30', '周宇',   'M', NULL, NULL, 'CUSTOMER', 'ACTIVE', NOW());
```

- [ ] **Step 4: Add ~18 orders + their items across all 5 states.** Worked examples (one per state); replicate the shape for the rest, attributing to users 1 and 5-9, 1-3 items each, `total` = sum of item `price_snapshot*qty`, `order_no` = `O` + a unique 14-digit timestamp-like string + 4-digit counter:

```sql
INSERT INTO orders (id, order_no, user_id, total, status, receiver_snapshot, tracking_no, created_at, paid_at, shipped_at, completed_at) VALUES
 (2, 'O2026061509120000002001', 5, 128.00, 'PENDING_PAY',  '王芳 / 北京',   NULL,             '2026-06-15 09:12:00', NULL,                  NULL,                  NULL),
 (3, 'O2026061814030000003001', 6, 198.00, 'PENDING_SHIP', '李华 / 上海',   NULL,             '2026-06-18 14:03:00', '2026-06-18 14:05:00', NULL,                  NULL),
 (4, 'O2026062010450000004001', 7, 168.00, 'SHIPPED',      '赵磊 / 广州',   'YT202606200001', '2026-06-20 10:45:00', '2026-06-20 10:46:00', '2026-06-21 09:00:00', NULL),
 (5, 'O2026062511300000005001', 8, 90.00,  'COMPLETED',    '孙丽 / 成都',   'YT202606250002', '2026-06-25 11:30:00', '2026-06-25 11:31:00', '2026-06-26 08:30:00', '2026-06-28 16:00:00'),
 (6, 'O2026062812150000006001', 9, 45.00,  'CANCELLED',    '周宇 / 武汉',   NULL,             '2026-06-28 12:15:00', NULL,                  NULL,                  NULL);
 -- ...continue ids 7-19 spread across the five states (aim ~4 COMPLETED, ~4 SHIPPED,
 -- ~4 PENDING_SHIP, ~3 PENDING_PAY, ~3 CANCELLED). Keep order id 1 (the existing one) intact.

INSERT INTO order_item (order_id, book_id, title_snapshot, price_snapshot, qty) VALUES
 (2, 10, '代码大全（第2版）', 128.00, 1),
 (3, 5,  '三体（全三册）',     168.00, 1), (3, 6, '时间简史', 30.00, 1),
 (4, 5,  '三体（全三册）',     168.00, 1),
 (5, 8,  '考研数学复习全书',   89.00,  1),
 (6, 6,  '时间简史',           45.00,  1);
 -- ...add items for orders 7-19 to match each total.
```

- [ ] **Step 5: Add a few active carts** (respect `uk_cart_user_book`):

```sql
INSERT INTO cart_item (user_id, book_id, qty) VALUES
 (5, 11, 1), (5, 3, 2),
 (6, 4, 1),
 (7, 7, 3);
```

- [ ] **Step 6: Reset the seed DB and sanity-check counts**

Run (MySQL must be up):

```bash
mysql -uroot < src/main/resources/schema.sql && mysql -uroot < src/main/resources/seed.sql
mysql -uroot -e "USE bookstore; SELECT (SELECT COUNT(*) FROM book) books, (SELECT COUNT(*) FROM user) users, (SELECT COUNT(*) FROM orders) orders;"
```

Expected: books ~50, users ~9, orders ~19. No FK errors.

- [ ] **Step 7: Commit**

```bash
git add src/main/resources/seed.sql
git commit -m "Expand seed data to a fully populated demo store"
```

---

## Task 10: Add student name + ID to reports

**Files:** Modify `scripts/build_final_deliverables.py`

- [ ] **Step 1: Add cover rows.** In `add_cover`, extend the `rows` list so it includes the student identity. Change:

```python
    rows = [
        ("文档类型", doc_type),
        ("项目名称", "网上书店管理系统"),
        ("技术栈", "Servlet + JSP + JavaBean + JDBC + MySQL"),
        ("安全设计", "SM3 密码摘要、SM4 敏感字段加密、RBAC、审计日志"),
        ("完成日期", "2026 年 6 月 30 日"),
    ]
```

to:

```python
    rows = [
        ("文档类型", doc_type),
        ("项目名称", "网上书店管理系统"),
        ("学生姓名", "Bagtyyar Kovusov"),
        ("学号", "312024805043"),
        ("技术栈", "Servlet + JSP + JavaBean + JDBC + MySQL"),
        ("安全设计", "SM3 密码摘要、SM4 敏感字段加密、RBAC、审计日志"),
        ("完成日期", "2026 年 6 月 30 日"),
    ]
```

- [ ] **Step 2: Add the footer credit.** In `configure_document`, change the footer run text from `"网上书店管理系统课程设计"` to:

```python
    run = footer.add_run("网上书店管理系统课程设计 · Bagtyyar Kovusov 312024805043")
```

- [ ] **Step 3: Commit**

```bash
git add scripts/build_final_deliverables.py
git commit -m "Add student name and ID to report covers and footer"
```

---

## Task 11: Build verification

- [ ] **Step 1: Build**

```bash
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home \
  mvn -f /Users/bagtyyar/Projects/web-cirriculum/pom.xml clean package
```

Expected: `Tests run: 61, Failures: 0, Errors: 0`, `BUILD SUCCESS`, `target/bookstore.war` built. (Views/seed/report changes do not affect unit tests.)

---

## Task 12: Boot, smoke test, re-screenshot

- [ ] **Step 1: Start MySQL** (per the final-delivery handoff workaround if Homebrew does not bind 3306), load schema + new seed (Task 9 Step 6).
- [ ] **Step 2: Run the app**

```bash
JAVA_HOME=/opt/homebrew/opt/openjdk@17/libexec/openjdk.jdk/Contents/Home \
  mvn -q -f /Users/bagtyyar/Projects/web-cirriculum/pom.xml compile exec:java -Dexec.mainClass=com.bookstore.Launcher
```

- [ ] **Step 3: Confirm CSS loads.** `curl -sI http://localhost:8080/app/../static/css/app.css` (or load a page and check devtools) returns `200`.
- [ ] **Step 4: Re-shoot the 12 screenshots** into `deliverables/screenshots/` (same filenames as the index in `deliverables/smoke-test-report.md`): `01-public-books` ... `11-auditor-stats-denied`, now showing the redesigned, populated store. Use the browser MCP or the prior screenshot method.
- [ ] **Step 5: Commit screenshots**

```bash
git add deliverables/screenshots
git commit -m "Re-shoot screenshots from redesigned populated store"
```

---

## Task 13: Regenerate reports + refresh delivery docs

- [ ] **Step 1: Regenerate docx + pdf**

```bash
python3 scripts/build_final_deliverables.py
# then re-render PDFs with the existing pipeline used for deliverables/reports/*.pdf
```

- [ ] **Step 2: Update counts** in `deliverables/README.md` and `deliverables/smoke-test-report.md` to the new data (books ~50, users ~9, orders ~19 across states). Update the new DB backup file under `deliverables/db/` if the smoke flow regenerates it.
- [ ] **Step 3: Verify PDFs** valid and page counts sane:

```bash
for f in deliverables/reports/*.pdf; do printf "%s %s\n" "$f" "$(mdls -name kMDItemNumberOfPages -raw "$f")"; done
```

- [ ] **Step 4: Commit**

```bash
git add deliverables scripts
git commit -m "Regenerate reports with name/ID and new screenshots"
```

---

## Task 14: Final verification

- [ ] **Step 1: No private styles remain**

```bash
grep -rl "<style" src/main/webapp/WEB-INF/views && echo "STYLE STILL PRESENT (fix)" || echo "OK: no inline <style> in views"
```

- [ ] **Step 2: No banned patterns**

```bash
grep -rnE "border-left:|border-right:" src/main/webapp/static/css/app.css && echo "CHECK: side-stripe?" || echo "OK: no side-stripe borders"
```

- [ ] **Step 3: Build once more** (Task 11) → 61 tests pass.
- [ ] **Step 4: Push** (only if the user has authorized pushing):

```bash
git push origin main
```

---

## Verification checklist (maps to spec section 12)

- [ ] `mvn clean package` → 61 tests pass, WAR built (Task 11, 14)
- [ ] Every page links `app.css` (200) and shows the shared top bar (Task 12)
- [ ] No page keeps a private `<style>` block (Task 14)
- [ ] No `border-left/right` accent stripes; no em dashes in UI copy (Task 14)
- [ ] Books ~50, customers ~6, orders ~18 across all 5 states; non-empty carts (Task 9)
- [ ] 12 screenshots retaken from redesigned store (Task 12)
- [ ] 3 reports regenerated with Bagtyyar Kovusov / 312024805043 on covers + footer; PDFs valid (Task 13)
