<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 注册</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        form { max-width: 420px; }
        label { display: block; margin-top: 12px; }
        input { box-sizing: border-box; width: 100%; padding: 8px; margin-top: 4px; }
        button { margin-top: 16px; padding: 8px 16px; }
        .error { color: #b00020; }
        nav { margin-bottom: 1rem; }
    </style>
</head>
<body>
    <nav><a href="${pageContext.request.contextPath}/app/books">返回图书列表</a></nav>
    <h1>用户注册</h1>

    <c:if test="${not empty error}">
        <p class="error"><c:out value="${error}"/></p>
    </c:if>

    <form method="post" action="${pageContext.request.contextPath}/app/register">
        <label>
            用户名
            <input name="username" value="${fn:escapeXml(username)}" required>
        </label>
        <label>
            真实姓名
            <input name="realName" value="${fn:escapeXml(realName)}">
        </label>
        <label>
            密码
            <input name="password" type="password" required>
        </label>
        <button type="submit">注册</button>
    </form>

    <p>已有账号？<a href="${pageContext.request.contextPath}/app/login">去登录</a></p>
</body>
</html>
