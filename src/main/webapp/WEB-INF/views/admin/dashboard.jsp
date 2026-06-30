<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 管理后台</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        table { border-collapse: collapse; margin-top: 1rem; min-width: 520px; }
        th, td { border: 1px solid #ccc; padding: 8px 12px; text-align: left; }
        th { background: #f5f5f5; }
        .muted { color: #666; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/books">图书列表</a>
        <a href="${pageContext.request.contextPath}/app/logout">退出</a>
    </nav>

    <h1>管理后台</h1>
    <p>
        当前用户：<strong><c:out value="${sessionScope.currentUser.username}"/></strong>
        （<c:out value="${sessionScope.currentUser.role}"/>）
    </p>

    <c:choose>
        <c:when test="${sessionScope.currentUser.role == 'AUDIT_ADMIN'}">
            <p class="muted">审计管理员可查看审计日志，不能进入业务管理模块。</p>
            <table>
                <thead>
                    <tr><th>入口</th><th>状态</th></tr>
                </thead>
                <tbody>
                    <tr>
                        <td>审计日志</td>
                        <td><a href="${pageContext.request.contextPath}/app/admin/audit">进入</a></td>
                    </tr>
                </tbody>
            </table>
        </c:when>
        <c:otherwise>
            <p class="muted">运营和系统管理员可进入业务管理入口。</p>
            <table>
                <thead>
                    <tr><th>模块</th><th>入口</th></tr>
                </thead>
                <tbody>
                    <tr>
                        <td>图书管理</td>
                        <td><a href="${pageContext.request.contextPath}/app/admin/books">进入</a></td>
                    </tr>
                    <tr>
                        <td>分类管理</td>
                        <td><a href="${pageContext.request.contextPath}/app/admin/categories">进入</a></td>
                    </tr>
                    <tr>
                        <td>订单管理</td>
                        <td><a href="${pageContext.request.contextPath}/app/admin/orders">进入</a></td>
                    </tr>
                    <tr>
                        <td>销售统计</td>
                        <td><a href="${pageContext.request.contextPath}/app/admin/stats">进入</a></td>
                    </tr>
                    <tr>
                        <td>用户管理</td>
                        <td><a href="${pageContext.request.contextPath}/app/admin/users">进入</a></td>
                    </tr>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
</body>
</html>
