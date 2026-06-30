# 网上书店管理系统交付包

生成日期：2026-06-30

## 交付内容

- `reports/网上书店-技术报告.docx` / `.pdf`：技术报告，已渲染验证，共 75 页。
- `reports/网上书店-系统使用说明书.docx` / `.pdf`：系统使用说明书，已渲染验证，共 15 页。
- `reports/网上书店-课程设计报告.docx` / `.pdf`：课程设计报告，已渲染验证，共 8 页。
- `screenshots/`：最终 DB-backed 浏览器烟测截图。
- `db/bookstore-backup-2026-06-30.sql`：最终演示数据库备份。
- `smoke-test-report.md`：最终烟测记录。
- `defense-demo-script.md`：答辩演示脚本和讲解要点。

源码、SQL 初始化脚本和 WAR 包位于项目根目录：

- `src/main/resources/schema.sql`
- `src/main/resources/seed.sql`
- `target/bookstore.war`

## 本地运行

```bash
mysql -uroot < src/main/resources/schema.sql
mysql -uroot < src/main/resources/seed.sql
mvn -q compile exec:java -Dexec.mainClass=com.bookstore.Launcher
```

访问：`http://localhost:8080/app/books`

演示账号以 `src/main/resources/seed.sql` 为准；交付文档正文不重复列出演示口令。

