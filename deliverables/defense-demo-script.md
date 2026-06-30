# 答辩演示脚本

## 1. 开场说明

本系统是网上书店管理系统，采用 Servlet + JSP + JavaBean + JDBC + MySQL，实现 MVC + DAO 分层结构。系统同时覆盖课程要求的自定义 JSP 标签、ECharts 图表、SM3/SM4 国密算法、RBAC 和安全审计。

建议先打开：

- `docs/superpowers/specs/2026-06-30-online-bookstore-design.md`
- `src/main/java/com/bookstore/web/DispatcherServlet.java`
- `src/main/resources/schema.sql`

## 2. 用户端演示

1. 打开 `http://localhost:8080/app/books`。
2. 说明图书列表来自 `BookListCommand -> BookService -> BookDao -> MySQL`。
3. 登录客户账号（口令以 `seed.sql` 为准）。
4. 加入购物车，修改数量，查看合计金额。
5. 结算生成订单，说明订单主表 `orders` 和明细表 `order_item`。
6. 管理员发货后，客户确认收货，订单状态变为 `COMPLETED`。

讲解点：

- JSP 通过 JSTL 输出列表。
- JavaBean 提供 EL 可读取的 getter。
- 结算是典型跨表业务：订单、明细、库存、购物车。

## 3. 管理端演示

1. 登录运营管理员账号。
2. 进入管理后台。
3. 展示图书管理、分类管理、订单管理、用户管理和销售统计。
4. 打开订单详情，说明发货动作写入物流单号和审计日志。
5. 打开销售统计，说明 ECharts 数据来自 `StatsDao` 聚合 SQL。

讲解点：

- `DispatcherServlet` 中管理端路由集中注册。
- 管理端操作由 `CommandSupport.audit` 写入日志。
- 销售统计同时有图表和表格降级展示。

## 4. 安全演示

1. 说明密码存储：`Sm3Util.hashPassword(salt + password)`。
2. 说明敏感字段：手机号和地址用 SM4 加密保存。
3. 打开用户管理，展示姓名脱敏来自 `<shop:mask>` 自定义 JSP 标签。
4. 登录审计管理员，打开审计日志。
5. 用审计管理员访问销售统计，展示 403。

讲解点：

- `AuthFilter`：登录态和 30 分钟空闲超时。
- `RbacFilter`：管理端角色访问控制。
- `AuditFilter`：管理端查看日志。
- `LoginGuard`：5 次失败锁定 30 分钟。

## 5. 代码答辩索引

- Servlet：`src/main/java/com/bookstore/web/DispatcherServlet.java`
- Filter：`src/main/java/com/bookstore/filter/`
- Command：`src/main/java/com/bookstore/web/commands/`
- Service：`src/main/java/com/bookstore/service/`
- DAO：`src/main/java/com/bookstore/dao/`
- JavaBean：`src/main/java/com/bookstore/model/`
- 国密：`src/main/java/com/bookstore/security/`
- 自定义标签：`src/main/java/com/bookstore/tag/MaskTag.java`
- JSP TLD：`src/main/webapp/WEB-INF/tlds/shop.tld`
- 销售统计页面：`src/main/webapp/WEB-INF/views/admin/stats.jsp`

## 6. 可能问题

- 为什么不用 Spring Boot：课程要求是 Servlet、JSP、JavaBean、JDBC、JSP 标签，系统刻意保持课程栈。
- 为什么使用 SM4 而不是 SM2：题目允许 SM2 或 SM4，SM4 更适合本项目的字段加密场景。
- 为什么评价/退款未完成：它们属于扩展功能；本次优先保证课程评分核心技术点完整可演示。
- 如何恢复数据库：使用 `schema.sql` + `seed.sql`，或恢复 `deliverables/db/bookstore-backup-2026-06-30.sql`。

