<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="pageTitle" value="注册"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="form-panel">
  <h1>用户注册</h1>
  <c:if test="${not empty error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${error}"/></div></c:if>
  <form method="post" action="${ctx}/app/register">
    <div class="field"><label>用户名</label><input name="username" value="${fn:escapeXml(username)}" required></div>
    <div class="field"><label>真实姓名</label><input name="realName" value="${fn:escapeXml(realName)}"></div>
    <div class="field"><label>密码</label><input name="password" type="password" required></div>
    <button class="btn btn-primary" type="submit">注册</button>
  </form>
  <p class="subtle" style="margin-top:16px">已有账号？<a href="${ctx}/app/login">去登录</a></p>
</div>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
