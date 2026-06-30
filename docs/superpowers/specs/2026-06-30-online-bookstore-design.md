# Online Bookstore (网上书店管理系统) — Design Spec

**Course:** Web 应用开发课程设计 (final course-design project)
**Date:** 2026-06-30 · **Author:** solo · **Target grade:** 良好-中等 · **Time budget:** ~1 day
**Stack:** JDK 17 · Maven · embedded Tomcat 10 (Jakarta EE 9, `jakarta.*`) · JSP/JSTL · plain JDBC · MySQL · BouncyCastle (国密 SM3/SM4)

---

## 1. Goal

Build an MVC + DAO online bookstore that **demonstrates every technique the course grades**: Servlet, JSP, JavaBean, JDBC, custom JSP tags — plus the mandated security layer (等保三级-style controls + 国密 SM3/SM4 crypto). Built from scratch so the author understands 100% of it for the 答辩 (defense). Must run locally for a live demo and ship with the required reports.

## 2. Scope (locked)

**MUST (build):**
- Maven skeleton + embedded Tomcat launcher (+ `mvn package` → deployable WAR)
- DB schema + seed data (MySQL)
- **Security foundation:** SM3 password hashing (salted); SM4 field encryption for phone/address + masked display; login policy (complexity ≥8 mixed, 5-fail → 30-min lock, 30-min session timeout, password-age check); **RBAC** (CUSTOMER / OPERATOR_ADMIN / SYSTEM_ADMIN / AUDIT_ADMIN); **audit logging** + audit-admin viewer
- **User end:** register/login → browse books (category + keyword search) → book detail → cart (add/update/remove) → checkout (mock pay) → order list + status → cancel / confirm receipt
- **Admin end:** separate admin login; book CRUD (+ cover upload, list/unlist, stock); order management (view + mark shipped + tracking no.); user management (list, enable/disable, reset password; sys-admin manages operator admins); category management (flat); audit-log viewer
- **One custom JSP tag:** `<shop:mask>` for data masking (satisfies the JSP-tag requirement)
- **One ECharts chart:** admin sales statistics

**NICE (only if time remains):** book reviews; multiple shipping addresses; low-stock warnings; homepage carousel; HMAC-SM3 log integrity (spec marks this 选做/optional).

**CUT (out of scope):** full refund/after-sales workflow (status only); enforced 90-day password rotation (field + check only); SM2 asymmetric (spec says "SM2 **或** SM4" — SM4 satisfies it); email/SMS verification; avatar upload.

## 3. Architecture

Layered **MVC + DAO**, request flow:

```
Browser → Filters (Auth → RBAC → Audit) → DispatcherServlet (front controller)
        → Command handler → Service → DAO → JDBC → MySQL
        → forward to JSP (JSTL + <shop:mask>) → HTML
```

- **Front-controller**: one `DispatcherServlet` mapped to `/app/*` routes to small command handlers (clean place to hang the cross-cutting filters). Admin under `/app/admin/*`.
- **Cross-cutting via `jakarta.servlet.Filter`:** `AuthFilter` (session presence + 30-min inactivity timeout), `RbacFilter` (role-gates `/admin/*` and audit-only access), `AuditFilter` (writes operation rows to `audit_log`).
- **Models = JavaBeans** (serializable POJOs with getters/setters) used as DTOs in JSP via JSTL EL.
- **Embedded Tomcat** `Launcher.main()` for one-command run; same code packages to a WAR.
- **国密** isolated in a `security` package (BouncyCastle): `Sm3Util`, `Sm4Util`, `PasswordPolicy`.

## 4. Package structure

```
src/main/java/com/bookstore/
  Launcher.java              # embedded Tomcat boot
  web/DispatcherServlet.java # front controller + route table
  web/commands/...           # one handler per action (BookList, AddToCart, Checkout, admin/*)
  filter/{AuthFilter,RbacFilter,AuditFilter}.java
  service/{BookService,CartService,OrderService,UserService,AuditService,CategoryService}.java
  dao/{BookDao,CartDao,OrderDao,UserDao,AuditDao,CategoryDao}.java   # plain JDBC
  model/{User,Book,Category,CartItem,Order,OrderItem,AuditLog,Role}.java  # JavaBeans
  security/{Sm3Util,Sm4Util,PasswordPolicy,LoginGuard}.java
  tag/MaskTag.java           # custom JSP tag handler
  util/{Db,Tx,WebUtil}.java  # connection (HikariCP or DriverManager), helpers
src/main/webapp/
  WEB-INF/web.xml, WEB-INF/tags/shop.tld, WEB-INF/views/**/*.jsp
  static/{css,js(echarts)}
src/main/resources/{schema.sql, seed.sql, db.properties}
```

## 5. Data model (MySQL)

- **user**(id, username UK, password_sm3, salt, real_name, gender, phone_enc, address_enc, role, status, fail_count, lock_until, pwd_changed_at, created_at)
- **category**(id, name, parent_id NULL)
- **book**(id, title, author, publisher, isbn, price, stock, category_id FK, cover_path, intro, status[ON/OFF], created_at)
- **cart_item**(id, user_id FK, book_id FK, qty, UK(user_id,book_id))
- **orders**(id, order_no UK, user_id FK, total, status[PENDING_PAY/PENDING_SHIP/SHIPPED/COMPLETED/CANCELLED], receiver_snapshot, tracking_no, created_at, paid_at, shipped_at, completed_at)
- **order_item**(id, order_id FK, book_id FK, title_snapshot, price_snapshot, qty)
- **audit_log**(id, user_id, username, action, detail, ip, created_at, hmac NULL)
- *(NICE)* **review**(id, user_id FK, book_id FK, rating, content, created_at)

ER diagram + DDL produced in `schema.sql` (feeds the report's 数据库设计 chapter).

## 6. Security design (国密 + 等保三级)

| Control | Implementation |
|---|---|
| **身份鉴别** | SM3(salt‖password) hex stored; complexity ≥8 with upper+lower+digit+special; 5 consecutive fails → `lock_until = now+30min`; 30-min inactivity logout via `AuthFilter`; `pwd_changed_at` age check (>90d → warn) |
| **访问控制** | 4 roles; `/app/admin/*` gated by `RbacFilter`; AUDIT_ADMIN may reach **only** the log viewer; SYSTEM_ADMIN manages operator admins + roles |
| **数据完整性/保密性** | SM4 encrypt phone + address at rest; decrypt only for authorized views; **mask** on display via `<shop:mask>` (e.g. `138****1234`, name `张*三`) |
| **安全审计** | `AuditFilter` logs login, sensitive views, and CRUD to `audit_log`; AUDIT_ADMIN query/view UI; *(NICE)* per-row HMAC-SM3 integrity |

All 国密 via BouncyCastle (`org.bouncycastle:bcprov-jdk18on`).

## 7. Custom JSP tag

`<shop:mask type="phone|name|idcard" value="${u.phone}"/>` → masked string. Defined in `shop.tld`, handler `MaskTag extends jakarta.servlet.jsp.tagext.SimpleTagSupport`. Demonstrates the mandated 自定义标签 technique and centralizes 脱敏 logic.

## 8. Front-end

JSP + JSTL views with one clean CSS theme (lightweight, no framework). Admin dashboard embeds **ECharts** (vendored JS) for a sales chart. Kept deliberately simple — the grade rewards correctness + security, not visual polish.

## 9. Testing (pragmatic, 1-day)

Focus automated tests on the **security-critical, defensible units** (highest value, easy to demo in the defense):
- `Sm3Util` hash determinism + known-answer; `Sm4Util` encrypt/decrypt round-trip
- `PasswordPolicy` accept/reject cases; `LoginGuard` lockout counter
- DAO smoke test (one CRUD) against H2 (MySQL-mode) or a test schema

Web flow + RBAC verified manually and captured as screenshots for the report. (Not full TDD across every module — the deadline doesn't allow it; we test what most needs proof.)

## 10. Build & run

```bash
brew services start mysql
mysql -uroot < src/main/resources/schema.sql      # create DB + tables
mysql -uroot bookstore < src/main/resources/seed.sql
mvn -q compile exec:java -Dexec.mainClass=com.bookstore.Launcher   # embedded → http://localhost:8080/
mvn -q package                                     # → target/*.war for Tomcat deploy (deliverable)
```

## 11. Deliverables (AI-assisted, generated after the system runs)

- 技术报告 (technical report, ≥50pp, with cover + TOC, UML, ER, flowcharts, screenshots)
- 系统使用说明书 (user manual, with cover + TOC)
- 课程设计报告 (course-design report)
- SQL scripts (`schema.sql`, `seed.sql`) + `mysqldump` backup
- Complete source tree

This spec doubles as the seed for the technical report's design chapters.

## 12. Demo & defense prep

Seeded accounts (one per role) + sample catalog/orders. A short demo script that visibly hits each graded technique (Servlet routing, JSP/JSTL, JavaBean, JDBC DAO, custom tag, MVC layering) and each security control (SM3 login, SM4+mask, lockout, RBAC denial, audit log). Talking points prepared for 答辩.
