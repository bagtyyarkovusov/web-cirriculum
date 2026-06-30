<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 分类管理</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        table { border-collapse: collapse; margin-top: 1rem; min-width: 640px; }
        th, td { border: 1px solid #ccc; padding: 6px 10px; text-align: left; }
        th { background: #f5f5f5; }
        input { padding: 5px; }
        form { display: inline; }
        .create { margin: 1rem 0; }
        .message { color: #176f2c; }
        .error { color: #b42318; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/admin">后台首页</a>
        <a href="${pageContext.request.contextPath}/app/admin/books">图书管理</a>
        <a href="${pageContext.request.contextPath}/app/logout">退出</a>
    </nav>

    <h1>分类管理</h1>
    <c:if test="${not empty message}"><p class="message"><c:out value="${message}"/></p></c:if>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>

    <form class="create" method="post" action="${pageContext.request.contextPath}/app/admin/categories/save">
        <input name="name" placeholder="新分类名称" required>
        <button type="submit">新增分类</button>
    </form>

    <table>
        <thead>
            <tr><th>ID</th><th>名称</th><th>操作</th></tr>
        </thead>
        <tbody>
            <c:forEach var="c" items="${categories}">
                <tr>
                    <td>${c.id}</td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/app/admin/categories/save">
                            <input type="hidden" name="id" value="${c.id}">
                            <input name="name" value="${fn:escapeXml(c.name)}" required>
                            <button type="submit">保存</button>
                        </form>
                    </td>
                    <td>
                        <form method="post" action="${pageContext.request.contextPath}/app/admin/categories/delete">
                            <input type="hidden" name="id" value="${c.id}">
                            <button type="submit">删除</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
