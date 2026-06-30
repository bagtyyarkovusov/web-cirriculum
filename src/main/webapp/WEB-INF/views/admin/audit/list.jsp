<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 审计日志</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        table { border-collapse: collapse; margin-top: 1rem; min-width: 980px; }
        th, td { border: 1px solid #ccc; padding: 6px 10px; text-align: left; vertical-align: top; }
        th { background: #f5f5f5; }
        form { margin: 1rem 0; }
        input { padding: 5px; margin-right: 8px; }
        button { padding: 5px 10px; }
        .error { color: #b42318; }
        .muted { color: #666; }
        .detail { max-width: 420px; word-break: break-word; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/books">图书列表</a>
        <a href="${pageContext.request.contextPath}/app/logout">退出</a>
    </nav>

    <h1>审计日志</h1>
    <p class="muted">按用户名、动作、日期和关键字查看最近200条审计记录。</p>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>

    <form method="get" action="${pageContext.request.contextPath}/app/admin/audit">
        <input name="username" placeholder="用户名" value="${fn:escapeXml(username)}">
        <input name="action" placeholder="动作，如 LOGIN_SUCCESS" value="${fn:escapeXml(action)}">
        <input name="keyword" placeholder="关键字" value="${fn:escapeXml(keyword)}">
        <input type="date" name="from" value="${fn:escapeXml(from)}">
        <input type="date" name="to" value="${fn:escapeXml(to)}">
        <button type="submit">筛选</button>
        <a href="${pageContext.request.contextPath}/app/admin/audit">清空</a>
    </form>

    <table>
        <thead>
            <tr>
                <th>ID</th><th>时间</th><th>用户ID</th><th>用户名</th>
                <th>动作</th><th>详情</th><th>IP</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="log" items="${logs}">
                <tr>
                    <td>${log.id}</td>
                    <td><c:out value="${log.createdAt}"/></td>
                    <td><c:out value="${log.userId}"/></td>
                    <td><c:out value="${log.username}"/></td>
                    <td><c:out value="${log.action}"/></td>
                    <td class="detail"><c:out value="${log.detail}"/></td>
                    <td><c:out value="${log.ip}"/></td>
                </tr>
            </c:forEach>
            <c:if test="${empty logs}">
                <tr><td colspan="7" class="muted">暂无审计记录。</td></tr>
            </c:if>
        </tbody>
    </table>
</body>
</html>
