<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="我的订单"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head"><div><h1>我的订单</h1></div></div>
<c:if test="${not empty param.message}"><div class="flash flash-ok"><svg class="icon icon-sm"><use href="#i-check"/></svg><c:out value="${param.message}"/></div></c:if>
<c:if test="${not empty param.error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${param.error}"/></div></c:if>
<c:choose>
  <c:when test="${empty orders}">
    <div class="empty"><svg class="icon"><use href="#i-box"/></svg><p>还没有订单。</p><a class="btn btn-primary" href="${ctx}/app/books">去挑选图书</a></div>
  </c:when>
  <c:otherwise>
    <table class="table">
      <thead><tr><th>订单号</th><th>下单时间</th><th class="right">金额</th><th>状态</th><th>操作</th></tr></thead>
      <tbody>
        <c:forEach var="order" items="${orders}">
          <tr>
            <td class="num"><c:out value="${order.orderNo}"/></td>
            <td class="subtle"><c:out value="${order.createdAt}"/></td>
            <td class="right price">￥${order.total}</td>
            <td><c:set var="pillStatus" value="${order.statusValue}"/><%@ include file="/WEB-INF/views/common/status-pill.jspf" %></td>
            <td>
              <span class="book-actions">
                <a class="btn btn-ghost btn-sm" href="${ctx}/app/orders/detail?id=${order.id}">详情</a>
                <c:if test="${order.cancellable}">
                  <form method="post" action="${ctx}/app/orders/cancel">
                    <input type="hidden" name="id" value="${order.id}">
                    <button class="btn btn-danger btn-sm" type="submit">取消</button>
                  </form>
                </c:if>
              </span>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
  </c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
