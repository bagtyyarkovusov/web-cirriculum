<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 我的订单</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        table { border-collapse: collapse; margin-top: 1rem; min-width: 760px; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background: #f5f5f5; }
        button { padding: 4px 10px; }
        .price { color: #c0392b; }
        .error { color: #b00020; }
        .message { color: #1b7f3a; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/books">图书列表</a>
        <a href="${pageContext.request.contextPath}/app/cart">购物车</a>
        <a href="${pageContext.request.contextPath}/app/logout">退出</a>
    </nav>

    <h1>我的订单</h1>

    <c:if test="${not empty param.error}">
        <p class="error"><c:out value="${param.error}"/></p>
    </c:if>
    <c:if test="${not empty param.message}">
        <p class="message"><c:out value="${param.message}"/></p>
    </c:if>

    <c:choose>
        <c:when test="${empty orders}">
            <p>暂无订单。</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>订单号</th><th>金额</th><th>状态</th><th>创建时间</th><th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="order" items="${orders}">
                        <tr>
                            <td><c:out value="${order.orderNo}"/></td>
                            <td class="price">￥${order.total}</td>
                            <td><c:out value="${order.statusValue}"/></td>
                            <td><c:out value="${order.createdAt}"/></td>
                            <td>
                                <a href="${pageContext.request.contextPath}/app/orders/detail?id=${order.id}">详情</a>
                                <c:if test="${order.cancellable}">
                                    <form method="post" action="${pageContext.request.contextPath}/app/orders/cancel" style="display:inline; margin-left:8px;">
                                        <input type="hidden" name="id" value="${order.id}">
                                        <button type="submit">取消</button>
                                    </form>
                                </c:if>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>
