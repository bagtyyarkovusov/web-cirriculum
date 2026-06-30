<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="订单详情"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head">
  <div><h1>订单详情</h1><p class="muted num"><c:out value="${order.orderNo}"/></p></div>
  <c:set var="pillStatus" value="${order.statusValue}"/><%@ include file="/WEB-INF/views/common/status-pill.jspf" %>
</div>
<c:if test="${not empty param.message}"><div class="flash flash-ok"><svg class="icon icon-sm"><use href="#i-check"/></svg><c:out value="${param.message}"/></div></c:if>
<c:if test="${not empty param.error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${param.error}"/></div></c:if>
<div class="panel" style="margin-bottom:16px">
  <div class="stat-row">
    <div class="stat"><div class="cap">金额</div><div class="figure price">￥${order.total}</div></div>
    <div class="stat"><div class="cap">收货信息</div><div><c:out value="${order.receiverSnapshot}"/></div></div>
    <c:if test="${not empty order.trackingNo}"><div class="stat"><div class="cap">物流单号</div><div class="num"><c:out value="${order.trackingNo}"/></div></div></c:if>
  </div>
  <p class="subtle">创建 <c:out value="${order.createdAt}"/><c:if test="${not empty order.paidAt}"> · 支付 <c:out value="${order.paidAt}"/></c:if><c:if test="${not empty order.shippedAt}"> · 发货 <c:out value="${order.shippedAt}"/></c:if><c:if test="${not empty order.completedAt}"> · 完成 <c:out value="${order.completedAt}"/></c:if></p>
</div>
<table class="table">
  <thead><tr><th>图书</th><th class="right">单价快照</th><th class="right">数量</th><th class="right">小计</th></tr></thead>
  <tbody>
    <c:forEach var="item" items="${items}">
      <tr>
        <td><c:out value="${item.titleSnapshot}"/></td>
        <td class="right price">￥${item.priceSnapshot}</td>
        <td class="right num">${item.qty}</td>
        <td class="right price">￥${item.lineTotal}</td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<div class="book-actions" style="margin-top:16px">
  <c:if test="${order.cancellable}">
    <form method="post" action="${ctx}/app/orders/cancel">
      <input type="hidden" name="id" value="${order.id}">
      <button class="btn btn-danger" type="submit">取消订单</button>
    </form>
  </c:if>
  <c:if test="${order.confirmable}">
    <form method="post" action="${ctx}/app/orders/confirm">
      <input type="hidden" name="id" value="${order.id}">
      <button class="btn btn-primary" type="submit"><svg class="icon icon-sm"><use href="#i-check"/></svg>确认收货</button>
    </form>
  </c:if>
</div>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
