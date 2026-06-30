<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 在售图书</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        table { border-collapse: collapse; margin-top: 1rem; }
        th, td { border: 1px solid #ccc; padding: 6px 12px; text-align: left; }
        th { background: #f5f5f5; }
        .price { color: #c0392b; }
    </style>
</head>
<body>
    <nav>
        <c:choose>
            <c:when test="${not empty sessionScope.currentUser}">
                当前用户：<c:out value="${sessionScope.currentUser.username}"/>
                （<c:out value="${sessionScope.currentUser.role}"/>）
                <a href="${pageContext.request.contextPath}/app/cart">购物车</a>
                <a href="${pageContext.request.contextPath}/app/orders">我的订单</a>
                <a href="${pageContext.request.contextPath}/app/logout">退出</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/app/login">登录</a>
                <a href="${pageContext.request.contextPath}/app/register">注册</a>
            </c:otherwise>
        </c:choose>
    </nav>
    <h1>网上书店 — 在售图书</h1>
    <p>共 <strong>${books.size()}</strong> 本图书</p>
    <table>
        <thead>
            <tr>
                <th>ID</th><th>书名</th><th>作者</th><th>出版社</th>
                <th>ISBN</th><th>价格</th><th>库存</th><th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="b" items="${books}">
                <tr>
                    <td>${b.id}</td>
                    <td><c:out value="${b.title}"/></td>
                    <td><c:out value="${b.author}"/></td>
                    <td><c:out value="${b.publisher}"/></td>
                    <td><c:out value="${b.isbn}"/></td>
                    <td class="price">￥${b.price}</td>
                    <td>${b.stock}</td>
                    <td>
                        <c:choose>
                            <c:when test="${not empty sessionScope.currentUser}">
                                <form method="post" action="${pageContext.request.contextPath}/app/cart/add">
                                    <input type="hidden" name="bookId" value="${b.id}">
                                    <input type="number" name="qty" min="1" value="1" style="width: 64px;">
                                    <button type="submit">加入购物车</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/app/login">登录后购买</a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
