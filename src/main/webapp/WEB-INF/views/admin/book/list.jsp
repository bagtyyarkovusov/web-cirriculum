<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 图书管理</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        table { border-collapse: collapse; margin-top: 1rem; min-width: 960px; }
        th, td { border: 1px solid #ccc; padding: 6px 10px; text-align: left; vertical-align: top; }
        th { background: #f5f5f5; }
        form { display: inline; }
        .message { color: #176f2c; }
        .error { color: #b42318; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/admin">后台首页</a>
        <a href="${pageContext.request.contextPath}/app/admin/books/new">新增图书</a>
        <a href="${pageContext.request.contextPath}/app/logout">退出</a>
    </nav>

    <h1>图书管理</h1>
    <c:if test="${not empty message}"><p class="message"><c:out value="${message}"/></p></c:if>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>

    <table>
        <thead>
            <tr>
                <th>ID</th><th>书名</th><th>作者</th><th>ISBN</th><th>价格</th>
                <th>库存</th><th>分类ID</th><th>状态</th><th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="b" items="${books}">
                <tr>
                    <td>${b.id}</td>
                    <td><c:out value="${b.title}"/></td>
                    <td><c:out value="${b.author}"/></td>
                    <td><c:out value="${b.isbn}"/></td>
                    <td>￥${b.price}</td>
                    <td>${b.stock}</td>
                    <td><c:out value="${b.categoryId}"/></td>
                    <td><c:out value="${b.status}"/></td>
                    <td>
                        <a href="${pageContext.request.contextPath}/app/admin/books/edit?id=${b.id}">编辑</a>
                        <c:choose>
                            <c:when test="${b.status == 'ON'}">
                                <form method="post" action="${pageContext.request.contextPath}/app/admin/books/status">
                                    <input type="hidden" name="id" value="${b.id}">
                                    <input type="hidden" name="status" value="OFF">
                                    <button type="submit">下架</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" action="${pageContext.request.contextPath}/app/admin/books/status">
                                    <input type="hidden" name="id" value="${b.id}">
                                    <input type="hidden" name="status" value="ON">
                                    <button type="submit">上架</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
