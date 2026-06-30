<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="管理后台"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head"><div><h1>管理后台</h1>
  <p class="muted">当前用户 <strong><c:out value="${sessionScope.currentUser.username}"/></strong>
  <span class="pill pill-pending"><c:out value="${sessionScope.currentUser.role}"/></span></p></div></div>
<c:choose>
  <c:when test="${sessionScope.currentUser.role == 'AUDIT_ADMIN'}">
    <div class="tiles">
      <a class="tile" href="${ctx}/app/admin/audit"><svg class="icon"><use href="#i-shield"/></svg>
        <span><span class="label">审计日志</span><br><span class="desc">查看最近操作记录</span></span>
        <svg class="icon chev"><use href="#i-chev"/></svg></a>
    </div>
  </c:when>
  <c:otherwise>
    <div class="tiles">
      <a class="tile" href="${ctx}/app/admin/books"><svg class="icon"><use href="#i-book"/></svg><span><span class="label">图书管理</span><br><span class="desc">上下架与库存</span></span><svg class="icon chev"><use href="#i-chev"/></svg></a>
      <a class="tile" href="${ctx}/app/admin/categories"><svg class="icon"><use href="#i-tag"/></svg><span><span class="label">分类管理</span><br><span class="desc">维护图书分类</span></span><svg class="icon chev"><use href="#i-chev"/></svg></a>
      <a class="tile" href="${ctx}/app/admin/orders"><svg class="icon"><use href="#i-box"/></svg><span><span class="label">订单管理</span><br><span class="desc">发货与物流</span></span><svg class="icon chev"><use href="#i-chev"/></svg></a>
      <a class="tile" href="${ctx}/app/admin/stats"><svg class="icon"><use href="#i-chart"/></svg><span><span class="label">销售统计</span><br><span class="desc">ECharts 图表</span></span><svg class="icon chev"><use href="#i-chev"/></svg></a>
      <a class="tile" href="${ctx}/app/admin/users"><svg class="icon"><use href="#i-user"/></svg><span><span class="label">用户管理</span><br><span class="desc">账号与状态</span></span><svg class="icon chev"><use href="#i-chev"/></svg></a>
    </div>
  </c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
