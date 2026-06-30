<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="购物车"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head"><div><h1>购物车</h1></div></div>
<c:if test="${not empty param.message}"><div class="flash flash-ok"><svg class="icon icon-sm"><use href="#i-check"/></svg><c:out value="${param.message}"/></div></c:if>
<c:if test="${not empty param.error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${param.error}"/></div></c:if>
<c:choose>
  <c:when test="${empty items}">
    <div class="empty"><svg class="icon"><use href="#i-cart"/></svg><p>购物车是空的。</p><a class="btn btn-primary" href="${ctx}/app/books">去挑选图书</a></div>
  </c:when>
  <c:otherwise>
    <table class="table">
      <thead><tr><th>图书</th><th class="right">单价</th><th class="right">库存</th><th>数量</th><th class="right">小计</th><th>操作</th></tr></thead>
      <tbody>
        <c:forEach var="item" items="${items}">
          <tr>
            <td><c:out value="${item.bookTitle}"/></td>
            <td class="right price">￥${item.bookPrice}</td>
            <td class="right num">${item.bookStock}</td>
            <td>
              <form method="post" action="${ctx}/app/cart/update" class="book-actions">
                <input type="hidden" name="bookId" value="${item.bookId}">
                <input class="qty num" type="number" name="qty" min="1" value="${item.qty}">
                <button class="btn btn-secondary btn-sm" type="submit">更新</button>
              </form>
            </td>
            <td class="right price">￥${item.lineTotal}</td>
            <td>
              <form method="post" action="${ctx}/app/cart/remove">
                <input type="hidden" name="bookId" value="${item.bookId}">
                <button class="btn btn-danger btn-sm" type="submit"><svg class="icon icon-sm"><use href="#i-x"/></svg>移除</button>
              </form>
            </td>
          </tr>
        </c:forEach>
      </tbody>
    </table>
    <div class="page-head" style="margin-top:20px">
      <div class="muted">合计 <strong class="price">￥${cartTotal}</strong></div>
      <form method="post" action="${ctx}/app/checkout">
        <button class="btn btn-primary" type="submit"><svg class="icon icon-sm"><use href="#i-check"/></svg>结算并模拟支付</button>
      </form>
    </div>
  </c:otherwise>
</c:choose>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
