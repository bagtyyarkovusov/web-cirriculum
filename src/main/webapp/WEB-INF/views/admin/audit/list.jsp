<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="审计日志"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head"><div><h1>审计日志</h1>
  <p class="muted">按用户名、动作、日期和关键字查看最近 200 条审计记录。</p></div></div>
<c:if test="${not empty error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${error}"/></div></c:if>
<div class="panel mb-16">
  <form method="get" action="${ctx}/app/admin/audit" class="book-actions flex-wrap">
    <div class="field m-0"><input name="username" placeholder="用户名" value="${fn:escapeXml(username)}"></div>
    <div class="field m-0"><input name="action" placeholder="动作，如 LOGIN_SUCCESS" value="${fn:escapeXml(action)}"></div>
    <div class="field m-0"><input name="keyword" placeholder="关键字" value="${fn:escapeXml(keyword)}"></div>
    <div class="field m-0"><input type="date" name="from" value="${fn:escapeXml(from)}"></div>
    <div class="field m-0"><input type="date" name="to" value="${fn:escapeXml(to)}"></div>
    <button class="btn btn-primary btn-sm" type="submit"><svg class="icon icon-sm"><use href="#i-check"/></svg>筛选</button>
    <a class="btn btn-ghost btn-sm" href="${ctx}/app/admin/audit">清空</a>
  </form>
</div>
<table class="table">
  <thead><tr><th>ID</th><th>时间</th><th>用户ID</th><th>用户名</th><th>动作</th><th>详情</th><th>IP</th></tr></thead>
  <tbody>
    <c:forEach var="log" items="${logs}">
      <tr>
        <td class="num">${log.id}</td>
        <td class="subtle"><c:out value="${log.createdAt}"/></td>
        <td class="num"><c:out value="${log.userId}"/></td>
        <td><c:out value="${log.username}"/></td>
        <td><span class="pill pill-pending"><c:out value="${log.action}"/></span></td>
        <td class="subtle max-w-420 text-break"><c:out value="${log.detail}"/></td>
        <td class="subtle num"><c:out value="${log.ip}"/></td>
      </tr>
    </c:forEach>
    <c:if test="${empty logs}">
      <tr><td colspan="7"><div class="empty p-24"><svg class="icon"><use href="#i-shield"/></svg><p>没有匹配的审计记录。</p></div></td></tr>
    </c:if>
  </tbody>
</table>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
