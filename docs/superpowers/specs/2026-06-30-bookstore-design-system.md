# Design Spec — 网上书店 Design System + Full Seed Data

Date: 2026-06-30
Status: Approved (design), pending implementation
Author context: course project owner Bagtyyar Kovusov (学号 312024805043)

## 1. Goal

Give the existing Servlet/JSP bookstore a single, coherent visual design system so
every page looks like a real, fully functioning store, and expand the seed data so
the store is populated (many books, customers in carts, orders in every state). Then
regenerate the three course reports with the student name and ID and fresh screenshots.

This is additive presentation work plus data seeding. No business logic, routing,
DAO, or security behavior changes.

## 2. Constraints

- **Offline only.** No CDN, no web fonts, no external JS/CSS. Fonts are the system
  stack; icons are vendored inline SVG. (The app already vendors `echarts.min.js`.)
- **Course stack stays.** Servlet + JSP + JSTL + JDBC. No build tooling, no frontend
  framework, no preprocessor. Plain CSS with custom properties.
- **No new dependencies** in `pom.xml`.
- The 61 unit tests must still pass (they do not touch views or seed rows).
- Register: **product** (the UI serves the task). Theme: **light** (classroom-projector
  demo plus desktop browsing under daylight). Color strategy: **Restrained** (tinted
  neutrals plus one blue accent, accent under ~10% of any surface).

## 3. Design tokens

All colors OKLCH. Every neutral is tinted toward the blue hue (255-260). Never `#000`
or `#fff`. Define once in `static/css/app.css` under `:root`.

```css
:root {
  /* accent */
  --accent:        oklch(0.55 0.15 255);
  --accent-hover:  oklch(0.49 0.15 255);
  --accent-weak:   oklch(0.95 0.03 255);   /* selected row, info tint */
  --accent-ring:   oklch(0.70 0.12 255 / 0.40);

  /* neutrals (tinted) */
  --bg:        oklch(0.985 0.004 255);
  --surface:   oklch(0.995 0.002 255);
  --panel:     oklch(0.970 0.005 255);     /* top bar, table header */
  --border:    oklch(0.920 0.005 255);     /* hairlines ONLY */
  --text:      oklch(0.32 0.02 260);
  --muted:     oklch(0.55 0.02 260);
  --subtle:    oklch(0.68 0.015 260);

  /* status (soft tint bg + readable text) */
  --pay-fg:    oklch(0.52 0.10 75);   --pay-bg:    oklch(0.95 0.05 80);
  --ship-fg:   oklch(0.50 0.13 285);  --ship-bg:   oklch(0.95 0.04 285);
  --pending-fg:oklch(0.50 0.15 255);  --pending-bg:oklch(0.95 0.03 255);
  --done-fg:   oklch(0.50 0.10 150);  --done-bg:   oklch(0.95 0.05 150);
  --cancel-fg: oklch(0.50 0.02 260);  --cancel-bg: oklch(0.95 0.004 260);
  --danger:    oklch(0.55 0.16 25);

  /* shape + motion */
  --radius:    8px;
  --radius-sm: 6px;
  --shadow:    0 1px 2px oklch(0.5 0.02 260 / 0.06), 0 4px 12px oklch(0.5 0.02 260 / 0.05);
  --ease:      cubic-bezier(0.22, 1, 0.36, 1);
  --dur:       160ms;
}
@media (prefers-reduced-motion: reduce) { * { transition: none !important; } }
```

Spacing scale (use as raw values, not a token soup): 4, 8, 12, 16, 24, 32, 48 px.
Vary spacing for rhythm; do not pad everything identically.

## 4. Typography

- One family, system stack, CJK-aware:
  `-apple-system, BlinkMacSystemFont, "Segoe UI", system-ui, "PingFang SC", "Microsoft YaHei", sans-serif`
- Fixed rem scale, ratio ~1.2: body `1rem`/1.6, `h3` 1.125rem/600, `h2` 1.375rem/600,
  `h1` 1.75rem/650. Hierarchy comes from weight and scale, not boxes or underlines.
- Numeric columns (price, id, total, stock, qty) use `font-variant-numeric: tabular-nums`.
- Prose blocks capped ~70ch; data tables may run denser.

## 5. Components (each ships all relevant states: default, hover, focus, active, disabled)

### Top bar (`header.jspf`)
Full-width `--panel` strip: left = small book SVG + `网上书店`; right = context links,
current user name + a role pill, logout. Storefront variant (books/cart/orders) and
admin variant (dashboard sections) share the same shell. Sticky top, hairline bottom
border only.

### Tables (the "too many borders" fix)
No per-cell grid. Header row in `--panel`, medium weight. **Hairline row dividers only**
(`border-bottom: 1px solid var(--border)`), no vertical lines. Row hover = `--accent-weak`.
Numbers right-aligned, tabular. Header may be sticky on long tables. Applies to admin
book/order/user/category/audit lists.

### Storefront catalog (`book/list.jsp`)
A book **list**, not a card grid. Each row: a small **category-tinted cover chip**
(CSS only: a rounded block in the category's tint with the book title set small inside,
no images), then title (link-weight), author and publisher muted, ISBN subtle, price
right-aligned and prominent, then add-to-cart (qty stepper + primary button) or a
"登录后购买" ghost link. Out-of-stock shows a muted "缺货" pill and disables the button.

### Dashboard (`admin/dashboard.jsp`)
A small set of **nav tiles**: each is label + one-line description + chevron icon,
links to a management area. Role-aware (auditor sees only the audit tile). Not a bordered
"进入" table, not an identical icon-card grid.

### Buttons
One vocabulary used everywhere: `.btn` base plus `.btn-primary` (accent bg),
`.btn-secondary` (panel bg, hairline), `.btn-ghost` (text + hover tint), `.btn-danger`
(for disable/cancel). Focus shows `--accent-ring`. Disabled is desaturated, never
full-saturation.

### Forms (`login`, `register`, `book/form`)
Single column, max-width ~420px, label above input, consistent control height, accent
focus ring. Real error state (danger ring + message). Login/register sit on a centered
panel with `--shadow`; the book form is inline on the page (no modal).

### Status pills
`.pill` plus modifier per state, using the status token pairs:
待支付 / 待发货(pending) / 已发货(ship) / 已完成(done) / 已取消(cancel). Reused for
book ON/OFF and user ACTIVE/DISABLED and the role tag. Full-radius, small, tinted.

### Icons
Vendored inline SVG sprite at `static/img/icons.svg` (symbols) referenced via
`<svg class="icon"><use href=".../icons.svg#i-cart"/></svg>`, or inlined once in a
`common/icons.jspf`. 1.5px stroke, 20px, single consistent set: cart, user, logout,
book, box (shipment), chart, shield (audit), search, plus, check, x, chevron.

### Empty states + flash
Empty cart / no orders / no audit hits each show a centered short message that teaches
the next action, not "nothing here". Flash messages are inline banners with a tinted
background and an icon. **No side-stripe (left-border) accents anywhere.**

### Stats (`admin/stats.jsp`)
Keep the ECharts canvas; wrap it in a single panel with a heading. Series uses the accent
color. Keep the data table as an accessible fallback. A short row of quiet labeled totals
is allowed; no big-number gradient hero.

## 6. File and asset plan

New assets:
- `src/main/webapp/static/css/app.css` (tokens + component classes; the whole system)
- `src/main/webapp/static/img/icons.svg` (inline SVG symbol sprite)

New shared includes (static `<%@ include %>`, so page variables are shared):
- `src/main/webapp/WEB-INF/views/common/head.jspf` (doctype, `<head>`, meta, `<title>` from
  a `pageTitle` var, `<link>` to `app.css`, open `<body>`)
- `src/main/webapp/WEB-INF/views/common/header.jspf` (top bar; reads `currentUser`)
- `src/main/webapp/WEB-INF/views/common/footer.jspf` (closes `<main>`, optional footer
  credit line "网上书店管理系统 · Bagtyyar Kovusov 312024805043", closes `<body></html>`)

Per-view change (all 16 JSPs; `index.jsp` is a redirect and may need no change): delete the private `<style>` block, replace the
hand-rolled `<head>`/`<nav>` with `head.jspf` + `header.jspf`, wrap content in
`<main class="container">`, apply semantic classes, swap status text for pills, swap
raw action links for `.btn` where they are actions. Views:
`index.jsp`, `book/list.jsp`, `cart/list.jsp`, `order/list.jsp`, `order/detail.jsp`,
`auth/login.jsp`, `auth/register.jsp`, `admin/dashboard.jsp`, `admin/book/list.jsp`,
`admin/book/form.jsp`, `admin/category/list.jsp`, `admin/order/list.jsp`,
`admin/order/detail.jsp`, `admin/user/list.jsp`, `admin/stats.jsp`,
`admin/audit/list.jsp`.

`web.xml`: no change required. The container default servlet already serves `/static/**`
(echarts loads from `/static/js/echarts.min.js` today). Confirm `app.css` loads with a
`200` during smoke test.

## 7. Accessibility

- Visible focus ring (`--accent-ring`) on every interactive element; never remove outline
  without a replacement.
- Body text and muted text meet >= 4.5:1 on their surfaces; pill text meets >= 4.5:1 on
  its tint (verify the chosen OKLCH pairs during implementation, adjust lightness if short).
- Tables use real `<th scope>`; icons that carry meaning have `aria-label`, decorative
  icons get `aria-hidden`.
- `prefers-reduced-motion` disables transitions (token block above).

## 8. Seed data plan (`src/main/resources/seed.sql`)

Pure SQL, no crypto run needed. Keep ids 1-9 stable; append new rows.

- **Categories**: keep 1-5, add `历史`(6), `经济管理`(7), `童书`(8).
- **Books**: grow to ~50 total across all 8 categories. Real-ish CN titles/authors/publishers,
  varied price 19-199, varied stock incl. a few single-digit (low-stock) and 2-3 `OFF`.
  Keep id 9 as the OFF example. `cover_path` stays NULL (cover chip is CSS).
- **Customers**: add ~5 more `CUSTOMER` rows (ids 5-9). Each **reuses customer id 1's
  `salt` and `password_sm3`** so the login password is the same demo password
  (`Bookstore@123`); `phone_enc`/`address_enc` NULL; distinct `real_name` (masking shows
  e.g. 张三 to 张*), `gender`, `ACTIVE`. No SM3/SM4 execution required.
- **Orders**: ~18 rows attributed across customers, spread across all states with correct
  timestamp shape:
  - 待支付 PENDING_PAY: `created_at` only.
  - 待发货 PENDING_SHIP: `created_at`, `paid_at`.
  - 已发货 SHIPPED: `created_at`, `paid_at`, `shipped_at`, `tracking_no`.
  - 已完成 COMPLETED: all four timestamps, `tracking_no`.
  - 已取消 CANCELLED: `created_at` (others NULL).
  `order_no` = `O` + yyyymmddHHMMSS + 4-digit counter (matches existing format). `total`
  equals the sum of its `order_item` (`price_snapshot` * `qty`). 1-3 line items each,
  with `title_snapshot`/`price_snapshot` copied from the referenced book.
- **cart_item**: a few non-empty carts (2-3 customers) so the cart screen is populated.
  Respect the `uk_cart_user_book` unique key.
- Stock reconciliation with sold quantities is optional (order items are snapshots); keep
  stock values visually sensible.
- `audit_log`: leave to runtime (the smoke test regenerates it).

The existing seed comment header that documents the demo password stays; do not print
passwords anywhere else.

## 9. Reports (`scripts/build_final_deliverables.py`)

- Add to every report cover table: `("学生姓名", "Bagtyyar Kovusov")` and
  `("学号", "312024805043")`. Keep the existing rows.
- Add a footer credit run: `网上书店管理系统课程设计 · Bagtyyar Kovusov 312024805043`.
- Regenerate `.docx` then render `.pdf` (existing pipeline). Re-shoot the screenshot set
  below from the redesigned, fully-seeded store, then rebuild reports so they embed the
  new screenshots. Update `deliverables/README.md` and `smoke-test-report.md` counts
  (books ~50, customers ~6, orders ~18 across states) to match the new data.

## 10. Re-screenshot list (same indices, richer content)

`01` public catalog (populated, cover chips) · `02` non-empty cart · `03` customer order
detail · `03b` completed order · `04` admin dashboard (nav tiles) · `05` admin book list
(many rows) · `06` categories · `07` order shipped (tracking) · `08` users with masked
names + pills · `09` ECharts stats (real data) · `10` audit log · `11` auditor 403.
Capture against live MySQL + embedded Tomcat, as the prior smoke test did.

## 11. Out of scope (non-goals)

- No new features (reviews, refunds, multi-address, recommendations).
- No routing/DAO/service/security changes.
- No dark theme, no theme switcher.
- No JS framework, no CSS build step, no external assets.

## 12. Verification

- `mvn -q clean package` still passes (61 tests) with views + seed changed.
  (Build needs Homebrew `openjdk@17` as `JAVA_HOME`; no system JRE on this machine.)
- App boots on embedded Tomcat against MySQL seeded with schema.sql + new seed.sql.
- Every page links `app.css` (200) and renders with the shared top bar; no page keeps a
  private `<style>` block; grep finds no `border-left`/`border-right` accent stripes and
  no em dashes in UI copy.
- All 12 screenshots retaken; three reports regenerated with name + ID on covers; PDFs
  valid with expected page counts.

## 13. Execution model

This session produces this spec, an implementation plan, and a handoff only. A fresh
session executes phases in order: (1) `app.css` tokens + base, (2) shared includes +
icon sprite, (3) per-view application, (4) seed expansion, (5) boot + smoke + re-screenshot,
(6) report regeneration with name/ID. Commit per phase.
