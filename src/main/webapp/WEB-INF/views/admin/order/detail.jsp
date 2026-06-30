<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="订单详情"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head">
  <div>
    <h1>订单 <c:out value="${order.orderNo}"/></h1>
    <p class="muted">创建于 <c:out value="${order.createdAt}"/></p>
  </div>
  <c:set var="pillStatus" value="${order.statusValue}"/>
  <%@ include file="/WEB-INF/views/common/status-pill.jspf" %>
</div>
<c:if test="${not empty message}"><div class="flash flash-ok"><svg class="icon icon-sm"><use href="#i-check"/></svg><c:out value="${message}"/></div></c:if>
<c:if test="${not empty error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${error}"/></div></c:if>

<div class="panel" style="margin-bottom:16px">
  <table class="table" style="border:0">
    <tbody>
      <tr><th>ID</th><td class="num">${order.id}</td></tr>
      <tr><th>用户ID</th><td class="num">${order.userId}</td></tr>
      <tr><th>金额</th><td class="price">￥${order.total}</td></tr>
      <tr><th>收货人</th><td><c:out value="${order.receiverSnapshot}"/></td></tr>
      <tr><th>快递单号</th><td class="subtle num"><c:out value="${order.trackingNo}"/></td></tr>
      <tr><th>付款时间</th><td class="subtle"><c:out value="${order.paidAt}"/></td></tr>
      <tr><th>发货时间</th><td class="subtle"><c:out value="${order.shippedAt}"/></td></tr>
      <tr><th>完成时间</th><td class="subtle"><c:out value="${order.completedAt}"/></td></tr>
    </tbody>
  </table>
</div>

<c:if test="${order.statusValue == 'PENDING_SHIP'}">
  <div class="panel" style="margin-bottom:16px">
    <h3>发货</h3>
    <form method="post" action="${ctx}/app/admin/orders/ship" class="book-actions">
      <input type="hidden" name="id" value="${order.id}">
      <div class="field" style="margin:0;flex:1;min-width:220px">
        <input name="trackingNo" placeholder="快递单号" required>
      </div>
      <button class="btn btn-primary" type="submit"><svg class="icon icon-sm"><use href="#i-box"/></svg>标记发货</button>
    </form>
  </div>
</c:if>

<h2>商品明细</h2>
<table class="table">
  <thead><tr><th>图书ID</th><th>书名</th><th class="right">单价</th><th class="right">数量</th><th class="right">小计</th></tr></thead>
  <tbody>
    <c:forEach var="item" items="${items}">
      <tr>
        <td class="num">${item.bookId}</td>
        <td><c:out value="${item.titleSnapshot}"/></td>
        <td class="right price">￥${item.priceSnapshot}</td>
        <td class="right num">${item.qty}</td>
        <td class="right price">￥${item.lineTotal}</td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
