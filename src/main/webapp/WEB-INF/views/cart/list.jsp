<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 购物车</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        table { border-collapse: collapse; margin-top: 1rem; min-width: 760px; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background: #f5f5f5; }
        input[type="number"] { width: 72px; }
        button { padding: 4px 10px; }
        .price { color: #c0392b; }
        .error { color: #b00020; }
        .message { color: #1b7f3a; }
        .actions form { display: inline; margin-right: 8px; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/books">图书列表</a>
        <a href="${pageContext.request.contextPath}/app/orders">我的订单</a>
        <a href="${pageContext.request.contextPath}/app/logout">退出</a>
    </nav>

    <h1>购物车</h1>
    <p>当前用户：<strong><c:out value="${sessionScope.currentUser.username}"/></strong></p>

    <c:if test="${not empty param.error}">
        <p class="error"><c:out value="${param.error}"/></p>
    </c:if>
    <c:if test="${not empty param.message}">
        <p class="message"><c:out value="${param.message}"/></p>
    </c:if>

    <c:choose>
        <c:when test="${empty items}">
            <p>购物车为空。</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>图书</th><th>单价</th><th>库存</th><th>数量</th><th>小计</th><th>操作</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="item" items="${items}">
                        <tr>
                            <td><c:out value="${item.bookTitle}"/></td>
                            <td class="price">￥${item.bookPrice}</td>
                            <td>${item.bookStock}</td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/app/cart/update">
                                    <input type="hidden" name="bookId" value="${item.bookId}">
                                    <input type="number" name="qty" min="1" value="${item.qty}">
                                    <button type="submit">更新</button>
                                </form>
                            </td>
                            <td class="price">￥${item.lineTotal}</td>
                            <td class="actions">
                                <form method="post" action="${pageContext.request.contextPath}/app/cart/remove">
                                    <input type="hidden" name="bookId" value="${item.bookId}">
                                    <button type="submit">移除</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>

            <p>合计：<strong class="price">￥${cartTotal}</strong></p>
            <form method="post" action="${pageContext.request.contextPath}/app/checkout">
                <button type="submit">结算并模拟支付</button>
            </form>
        </c:otherwise>
    </c:choose>
</body>
</html>
