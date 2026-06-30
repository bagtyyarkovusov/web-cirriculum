# 最终烟测记录

日期：2026-06-30

## 自动化构建

- `mvn package`
- 结果：61 tests, 0 failures, 0 errors, 0 skipped
- 产物：`target/bookstore.war`

## 数据库初始化与备份

- MySQL 使用 `src/main/resources/schema.sql` 和 `src/main/resources/seed.sql` 重置。
- 最终备份：`deliverables/db/bookstore-backup-2026-06-30.sql`
- 最终状态：
  - 用户：9 个（4 个管理角色 + 5 个演示客户）
  - 订单：19 个（覆盖 5 种订单状态）
  - 图书：50 本（其中 3 本为下架状态）
  - 购物车：5 条演示记录

## 浏览器烟测

已通过真实浏览器和本地 MySQL 验证：

- 公共图书列表可访问，展示在售图书。
- 客户登录后可加入购物车、查看金额、移除购物车项。
- 客户完成结算，订单进入待发货；管理员发货后客户确认收货，订单进入 `COMPLETED`。
- 运营管理员可进入后台首页。
- 图书管理可新增图书。
- 分类管理可新增并删除未使用分类。
- 订单管理可填写物流单号并标记发货。
- 用户管理显示脱敏姓名，并完成启用/禁用状态切换。
- 销售统计页加载 ECharts canvas，并显示销售额表格。
- 审计管理员可查看审计日志。
- 审计管理员访问销售统计页返回 403。

## 截图索引

- `01-public-books.png`：用户端图书列表
- `02-customer-cart-updated.png`：购物车
- `03-customer-order-detail.png`：客户订单详情
- `03b-customer-order-completed.png`：客户确认收货后订单
- `04-admin-dashboard.png`：管理后台首页
- `05-admin-book-created.png`：图书管理
- `06-admin-categories.png`：分类管理
- `07-admin-order-shipped.png`：订单发货
- `08-admin-users-mask.png`：用户管理脱敏展示
- `09-admin-sales-stats.png`：ECharts 销售统计
- `10-audit-log.png`：审计日志
- `11-auditor-stats-denied.png`：RBAC 403

## 文档渲染验证

- 技术报告：75 页，已渲染为 DOCX/PDF。
- 系统使用说明书：15 页，已渲染为 DOCX/PDF。
- 课程设计报告：8 页，已渲染为 DOCX/PDF。
