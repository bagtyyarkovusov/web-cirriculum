<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 用户管理</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        table { border-collapse: collapse; margin-top: 1rem; min-width: 960px; }
        th, td { border: 1px solid #ccc; padding: 6px 10px; text-align: left; vertical-align: top; }
        th { background: #f5f5f5; }
        input { padding: 5px; width: 130px; }
        form { display: inline; }
        .message { color: #176f2c; }
        .error { color: #b42318; }
        .muted { color: #666; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/admin">后台首页</a>
        <a href="${pageContext.request.contextPath}/app/logout">退出</a>
    </nav>

    <h1>用户管理</h1>
    <p class="muted">运营管理员只能管理普通客户；系统管理员可管理非系统管理员账号。</p>
    <c:if test="${not empty message}"><p class="message"><c:out value="${message}"/></p></c:if>
    <c:if test="${not empty error}"><p class="error"><c:out value="${error}"/></p></c:if>

    <table>
        <thead>
            <tr>
                <th>ID</th><th>用户名</th><th>姓名</th><th>角色</th><th>状态</th>
                <th>失败次数</th><th>锁定至</th><th>创建时间</th><th>操作</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach var="u" items="${users}">
                <tr>
                    <td>${u.id}</td>
                    <td><c:out value="${u.username}"/></td>
                    <td><c:out value="${u.realName}"/></td>
                    <td><c:out value="${u.role}"/></td>
                    <td><c:out value="${u.status}"/></td>
                    <td>${u.failCount}</td>
                    <td><c:out value="${u.lockUntil}"/></td>
                    <td><c:out value="${u.createdAt}"/></td>
                    <td>
                        <c:choose>
                            <c:when test="${u.status == 'ACTIVE'}">
                                <form method="post" action="${pageContext.request.contextPath}/app/admin/users/status">
                                    <input type="hidden" name="id" value="${u.id}">
                                    <input type="hidden" name="status" value="DISABLED">
                                    <button type="submit">禁用</button>
                                </form>
                            </c:when>
                            <c:otherwise>
                                <form method="post" action="${pageContext.request.contextPath}/app/admin/users/status">
                                    <input type="hidden" name="id" value="${u.id}">
                                    <input type="hidden" name="status" value="ACTIVE">
                                    <button type="submit">启用</button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                        <form method="post" action="${pageContext.request.contextPath}/app/admin/users/reset-password">
                            <input type="hidden" name="id" value="${u.id}">
                            <input name="temporaryPassword" value="${fn:escapeXml(tempPassword)}" required>
                            <button type="submit">重置密码</button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
