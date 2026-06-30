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
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background: #f5f5f5; }
        button { padding: 6px 12px; margin-right: 8px; }
        .price { color: #c0392b; }
        .error { color: #b00020; }
        .message { color: #1b7f3a; }
        .meta p { margin: 4px 0; }
        .actions form { display: inline; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/books">图书列表</a>
        <a href="${pageContext.request.contextPath}/app/cart">购物车</a>
        <a href="${pageContext.request.contextPath}/app/orders">我的订单</a>
        <a href="${pageContext.request.contextPath}/app/logout">退出</a>
    </nav>

    <h1>订单详情</h1>

    <c:if test="${not empty param.error}">
        <p class="error"><c:out value="${param.error}"/></p>
    </c:if>
    <c:if test="${not empty param.message}">
        <p class="message"><c:out value="${param.message}"/></p>
    </c:if>

    <div class="meta">
        <p>订单号：<strong><c:out value="${order.orderNo}"/></strong></p>
        <p>状态：<strong><c:out value="${order.statusValue}"/></strong></p>
        <p>金额：<strong class="price">￥${order.total}</strong></p>
        <p>创建时间：<c:out value="${order.createdAt}"/></p>
        <p>支付时间：<c:out value="${order.paidAt}"/></p>
        <c:if test="${not empty order.trackingNo}">
            <p>物流单号：<c:out value="${order.trackingNo}"/></p>
        </c:if>
    </div>

    <table>
        <thead>
            <tr>
                <th>图书</th><th>单价快照</th><th>数量</th><th>小计</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="item" items="${items}">
                <tr>
                    <td><c:out value="${item.titleSnapshot}"/></td>
                    <td class="price">￥${item.priceSnapshot}</td>
                    <td>${item.qty}</td>
                    <td class="price">￥${item.lineTotal}</td>
                </tr>
            </c:forEach>
        </tbody>
    </table>

    <div class="actions" style="margin-top: 16px;">
        <c:if test="${order.cancellable}">
            <form method="post" action="${pageContext.request.contextPath}/app/orders/cancel">
                <input type="hidden" name="id" value="${order.id}">
                <button type="submit">取消订单</button>
            </form>
        </c:if>
        <c:if test="${order.confirmable}">
            <form method="post" action="${pageContext.request.contextPath}/app/orders/confirm">
                <input type="hidden" name="id" value="${order.id}">
                <button type="submit">确认收货</button>
            </form>
        </c:if>
    </div>
</body>
</html>
