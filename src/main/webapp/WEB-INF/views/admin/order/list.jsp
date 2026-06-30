<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="订单管理"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head"><div><h1>订单管理</h1></div></div>
<c:if test="${not empty message}"><div class="flash flash-ok"><svg class="icon icon-sm"><use href="#i-check"/></svg><c:out value="${message}"/></div></c:if>
<c:if test="${not empty error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${error}"/></div></c:if>
<table class="table">
  <thead><tr><th>ID</th><th>订单号</th><th class="right">用户ID</th><th class="right">金额</th><th>状态</th><th>快递单号</th><th>创建时间</th><th>操作</th></tr></thead>
  <tbody>
    <c:forEach var="o" items="${orders}">
      <tr>
        <td class="num">${o.id}</td>
        <td class="num"><c:out value="${o.orderNo}"/></td>
        <td class="right num">${o.userId}</td>
        <td class="right price">￥${o.total}</td>
        <td><c:set var="pillStatus" value="${o.statusValue}"/><%@ include file="/WEB-INF/views/common/status-pill.jspf" %></td>
        <td class="subtle num"><c:out value="${o.trackingNo}"/></td>
        <td class="subtle"><c:out value="${o.createdAt}"/></td>
        <td><a class="btn btn-ghost btn-sm" href="${ctx}/app/admin/orders/detail?id=${o.id}">详情</a></td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
