<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="在售图书"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head">
  <div><h1>在售图书</h1><p class="muted">共 <strong class="num">${books.size()}</strong> 本图书</p></div>
</div>
<div class="book-list">
  <c:forEach var="b" items="${books}">
    <div class="book-row">
      <div class="cover-chip cover-${(b.categoryId == null ? 1 : ((b.categoryId - 1) % 8) + 1)}"><c:out value="${b.title}"/></div>
      <div class="book-meta">
        <div class="title"><c:out value="${b.title}"/></div>
        <div class="by"><c:out value="${b.author}"/> · <c:out value="${b.publisher}"/></div>
        <div class="subtle">ISBN <c:out value="${b.isbn}"/></div>
      </div>
      <div class="book-actions">
        <span class="price">￥${b.price}</span>
        <c:choose>
          <c:when test="${b.stock <= 0}"><span class="pill pill-cancel">缺货</span></c:when>
          <c:when test="${not empty sessionScope.currentUser}">
            <form method="post" action="${ctx}/app/cart/add" class="book-actions">
              <input type="hidden" name="bookId" value="${b.id}">
              <input class="qty num" type="number" name="qty" min="1" value="1">
              <button class="btn btn-primary btn-sm" type="submit"><svg class="icon icon-sm"><use href="#i-cart"/></svg>加入购物车</button>
            </form>
          </c:when>
          <c:otherwise><a class="btn btn-secondary btn-sm" href="${ctx}/app/login">登录后购买</a></c:otherwise>
        </c:choose>
      </div>
    </div>
  </c:forEach>
</div>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
