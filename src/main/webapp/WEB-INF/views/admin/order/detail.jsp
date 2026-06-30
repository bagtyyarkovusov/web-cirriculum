<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 订单详情</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        table { border-collapse: collapse; margin-top: 1rem; min-width: 760px; }
        th, td { border: 1px solid #ccc; padding: 6px 10px; text-align: left; }
        th { background: #f5f5f5; }
        input { padding: 5px; }
        .message { color: #176f2c; }
        .error { color: #b42318; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/admin/orders">返回订单管理</a>
        <a href="${pageContext.request.contextPath}/app/admin">后台首页</a>
    </nav>

    <h1>订单详情</h1>
    <c:if test="${not empty message}"><p class="message"><c:out value="${message}"/></p></c:if>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>

    <table>
        <tbody>
            <tr><th>ID</th><td>${order.id}</td></tr>
            <tr><th>订单号</th><td><c:out value="${order.orderNo}"/></td></tr>
            <tr><th>用户ID</th><td>${order.userId}</td></tr>
            <tr><th>金额</th><td>￥${order.total}</td></tr>
            <tr><th>状态</th><td><c:out value="${order.statusValue}"/></td></tr>
            <tr><th>快递单号</th><td><c:out value="${order.trackingNo}"/></td></tr>
            <tr><th>创建时间</th><td><c:out value="${order.createdAt}"/></td></tr>
            <tr><th>付款时间</th><td><c:out value="${order.paidAt}"/></td></tr>
            <tr><th>发货时间</th><td><c:out value="${order.shippedAt}"/></td></tr>
            <tr><th>完成时间</th><td><c:out value="${order.completedAt}"/></td></tr>
        </tbody>
    </table>

    <c:if test="${order.statusValue == 'PENDING_SHIP'}">
        <h2>发货</h2>
        <form method="post" action="${pageContext.request.contextPath}/app/admin/orders/ship">
            <input type="hidden" name="id" value="${order.id}">
            <input name="trackingNo" placeholder="快递单号" required>
            <button type="submit">标记发货</button>
        </form>
    </c:if>

    <h2>商品明细</h2>
    <table>
        <thead>
            <tr><th>图书ID</th><th>书名</th><th>单价</th><th>数量</th><th>小计</th></tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td>${item.bookId}</td>
                    <td><c:out value="${item.titleSnapshot}"/></td>
                    <td>￥${item.priceSnapshot}</td>
                    <td>${item.qty}</td>
                    <td>￥${item.lineTotal}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
