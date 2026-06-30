<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="shop" uri="/WEB-INF/tlds/shop.tld" %>
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="用户管理"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head"><div><h1>用户管理</h1>
  <p class="muted">运营管理员只能管理普通客户；系统管理员可管理非系统管理员账号。</p></div></div>
<c:if test="${not empty message}"><div class="flash flash-ok"><svg class="icon icon-sm"><use href="#i-check"/></svg><c:out value="${message}"/></div></c:if>
<c:if test="${not empty error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${error}"/></div></c:if>
<table class="table">
  <thead><tr><th>ID</th><th>用户名</th><th>姓名</th><th>角色</th><th>状态</th><th class="right">失败次数</th><th>锁定至</th><th>操作</th></tr></thead>
  <tbody>
    <c:forEach var="u" items="${users}">
      <tr>
        <td class="num">${u.id}</td>
        <td><c:out value="${u.username}"/></td>
        <td><shop:mask type="name" value="${u.realName}"/></td>
        <td><span class="pill pill-pending"><c:out value="${u.role}"/></span></td>
        <td>
          <c:choose>
            <c:when test="${u.status == 'ACTIVE'}"><span class="pill pill-done">ACTIVE</span></c:when>
            <c:otherwise><span class="pill pill-cancel">DISABLED</span></c:otherwise>
          </c:choose>
        </td>
        <td class="right num">${u.failCount}</td>
        <td class="subtle"><c:out value="${u.lockUntil}"/></td>
        <td>
          <div class="user-actions align-start">
            <c:choose>
              <c:when test="${u.status == 'ACTIVE'}">
                <form method="post" action="${ctx}/app/admin/users/status">
                  <input type="hidden" name="id" value="${u.id}">
                  <input type="hidden" name="status" value="DISABLED">
                  <button class="btn btn-secondary btn-sm" type="submit">禁用</button>
                </form>
              </c:when>
              <c:otherwise>
                <form method="post" action="${ctx}/app/admin/users/status">
                  <input type="hidden" name="id" value="${u.id}">
                  <input type="hidden" name="status" value="ACTIVE">
                  <button class="btn btn-secondary btn-sm" type="submit">启用</button>
                </form>
              </c:otherwise>
            </c:choose>
            <form method="post" action="${ctx}/app/admin/users/reset-password" class="user-actions">
              <input type="hidden" name="id" value="${u.id}">
              <input name="temporaryPassword" value="${fn:escapeXml(tempPassword)}" required class="w-120">
              <button class="btn btn-ghost btn-sm" type="submit">重置密码</button>
            </form>
          </div>
        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
