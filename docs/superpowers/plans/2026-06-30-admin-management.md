# Admin Management Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Add the admin management slice for books, categories, orders, and users.

**Architecture:** Keep the existing front-controller MVC shape: `DispatcherServlet` routes to small command classes, commands call thin services, services call JDBC DAOs, and JSPs render table/form screens. Reuse the existing schema; cover upload is implemented as a simple `cover_path` field to keep the course-defense surface small.

**Tech Stack:** JDK 17, Jakarta Servlet/JSP/JSTL, Maven, plain JDBC, MySQL, JUnit 5.

---

### Task 1: Admin Order Shipping Rule

**Files:**
- Modify: `src/test/java/com/bookstore/service/OrderServiceTest.java`
- Modify: `src/main/java/com/bookstore/dao/OrderRepository.java`
- Modify: `src/main/java/com/bookstore/service/OrderService.java`
- Modify: `src/main/java/com/bookstore/dao/OrderDao.java`

- [ ] **Step 1: Write failing tests**
  Add tests that `shipOrder` trims a tracking number and calls the repository with the current time, and that a blank tracking number is rejected.

- [ ] **Step 2: Run tests to verify failure**
  Run: `mvn -q -Dtest=OrderServiceTest test`
  Expected: compilation failure for missing `shipOrder` / repository method.

- [ ] **Step 3: Implement minimal service and DAO support**
  Add `shipIfPending(long orderId, String trackingNo, LocalDateTime now)` to `OrderRepository`, implement `OrderService.shipOrder`, and update `OrderDao` with a guarded `UPDATE orders SET status='SHIPPED', tracking_no=?, shipped_at=? WHERE id=? AND status='PENDING_SHIP'`.

- [ ] **Step 4: Run tests to verify pass**
  Run: `mvn -q -Dtest=OrderServiceTest test`
  Expected: pass.

### Task 2: Admin User Policy and Password Reset

**Files:**
- Modify: `src/test/java/com/bookstore/service/UserServiceTest.java`
- Modify: `src/main/java/com/bookstore/dao/UserRepository.java`
- Modify: `src/main/java/com/bookstore/service/UserService.java`
- Modify: `src/main/java/com/bookstore/dao/UserDao.java`

- [ ] **Step 1: Write failing tests**
  Add tests proving `SYSTEM_ADMIN` can reset an operator admin password, and `OPERATOR_ADMIN` cannot reset a privileged admin account.

- [ ] **Step 2: Run tests to verify failure**
  Run: `mvn -q -Dtest=UserServiceTest test`
  Expected: compilation failure for missing admin methods.

- [ ] **Step 3: Implement minimal policy**
  Add `listUsers`, `updateUserStatus`, and `resetPassword` to `UserService`; allow `SYSTEM_ADMIN` to manage non-system accounts and `OPERATOR_ADMIN` to manage customers only. Reset passwords with a new salt, salted SM3 hash, failure count cleared, and `pwd_changed_at` updated.

- [ ] **Step 4: Run tests to verify pass**
  Run: `mvn -q -Dtest=UserServiceTest test`
  Expected: pass.

### Task 3: Book and Category Admin Data Access

**Files:**
- Create: `src/main/java/com/bookstore/model/Category.java`
- Create: `src/main/java/com/bookstore/dao/CategoryDao.java`
- Create: `src/main/java/com/bookstore/service/CategoryService.java`
- Modify: `src/main/java/com/bookstore/dao/BookDao.java`
- Modify: `src/main/java/com/bookstore/service/BookService.java`

- [ ] **Step 1: Extend book admin CRUD**
  Add list-all, find-by-id, insert, update, and status-update methods. Validate title, non-negative price, non-negative stock, and `ON`/`OFF` status in `BookService`.

- [ ] **Step 2: Add flat category CRUD**
  Add list, find, create, update, and delete-if-unused methods. Delete returns false when books still reference the category.

- [ ] **Step 3: Compile**
  Run: `mvn -q -DskipTests compile`
  Expected: compile succeeds.

### Task 4: Admin Routes and JSPs

**Files:**
- Modify: `src/main/java/com/bookstore/web/DispatcherServlet.java`
- Create: admin command classes under `src/main/java/com/bookstore/web/commands/`
- Modify: `src/main/webapp/WEB-INF/views/admin/dashboard.jsp`
- Create: admin JSPs under `src/main/webapp/WEB-INF/views/admin/`

- [ ] **Step 1: Register routes**
  Add GET/POST routes for `/admin/books`, `/admin/books/new`, `/admin/books/edit`, `/admin/books/save`, `/admin/books/status`, `/admin/categories`, `/admin/categories/save`, `/admin/categories/delete`, `/admin/orders`, `/admin/orders/detail`, `/admin/orders/ship`, `/admin/users`, `/admin/users/status`, and `/admin/users/reset-password`.

- [ ] **Step 2: Add command classes**
  Commands should parse parameters, call services, forward to JSPs for GETs, and redirect with query-message feedback for POSTs.

- [ ] **Step 3: Add JSP views**
  Render simple tables/forms with JSTL escaping. Dashboard links should point to implemented modules.

### Task 5: Verification

**Files:**
- No source edits unless verification exposes defects.

- [ ] **Step 1: Run full package**
  Run: `mvn package`
  Expected: all tests pass and WAR builds.

- [ ] **Step 2: Start embedded Tomcat**
  Run: `mvn -q compile exec:java -Dexec.mainClass=com.bookstore.Launcher`
  Expected: `http://localhost:8080/` serves the app.

- [ ] **Step 3: Manual HTTP checks**
  Verify operator can open admin books/orders/categories/users pages, auditor remains restricted by existing RBAC, and admin shipping/book status/user reset flows reach the expected routes. Stop Tomcat and confirm port 8080 is clear.
