<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 订单管理</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        table { border-collapse: collapse; margin-top: 1rem; min-width: 860px; }
        th, td { border: 1px solid #ccc; padding: 6px 10px; text-align: left; }
        th { background: #f5f5f5; }
        .message { color: #176f2c; }
        .error { color: #b42318; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/admin">后台首页</a>
        <a href="${pageContext.request.contextPath}/app/logout">退出</a>
    </nav>

    <h1>订单管理</h1>
    <c:if test="${not empty message}"><p class="message"><c:out value="${message}"/></p></c:if>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>

    <table>
        <thead>
            <tr><th>ID</th><th>订单号</th><th>用户ID</th><th>金额</th><th>状态</th><th>快递单号</th><th>创建时间</th><th>操作</th></tr>
        </thead>
        <tbody>
            <c:forEach var="o" items="${orders}">
                <tr>
                    <td>${o.id}</td>
                    <td><c:out value="${o.orderNo}"/></td>
                    <td>${o.userId}</td>
                    <td>￥${o.total}</td>
                    <td><c:out value="${o.statusValue}"/></td>
                    <td><c:out value="${o.trackingNo}"/></td>
                    <td><c:out value="${o.createdAt}"/></td>
                    <td><a href="${pageContext.request.contextPath}/app/admin/orders/detail?id=${o.id}">详情</a></td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
