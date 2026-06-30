<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="图书管理"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head">
  <div><h1>图书管理</h1></div>
  <a class="btn btn-primary" href="${ctx}/app/admin/books/new"><svg class="icon icon-sm"><use href="#i-plus"/></svg>新增图书</a>
</div>
<c:if test="${not empty message}"><div class="flash flash-ok"><svg class="icon icon-sm"><use href="#i-check"/></svg><c:out value="${message}"/></div></c:if>
<c:if test="${not empty error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${error}"/></div></c:if>
<table class="table">
  <thead><tr><th>ID</th><th>书名</th><th>作者</th><th>ISBN</th><th class="right">价格</th><th class="right">库存</th><th>状态</th><th>操作</th></tr></thead>
  <tbody>
    <c:forEach var="b" items="${books}">
      <tr>
        <td class="num">${b.id}</td>
        <td><c:out value="${b.title}"/></td>
        <td class="muted"><c:out value="${b.author}"/></td>
        <td class="subtle num"><c:out value="${b.isbn}"/></td>
        <td class="right price">￥${b.price}</td>
        <td class="right num">${b.stock}</td>
        <td><c:choose><c:when test="${b.status == 'ON'}"><span class="pill pill-done">上架</span></c:when><c:otherwise><span class="pill pill-cancel">下架</span></c:otherwise></c:choose></td>
        <td>
          <span class="book-actions">
            <a class="btn btn-ghost btn-sm" href="${ctx}/app/admin/books/edit?id=${b.id}">编辑</a>
            <c:choose>
              <c:when test="${b.status == 'ON'}">
                <form method="post" action="${ctx}/app/admin/books/status">
                  <input type="hidden" name="id" value="${b.id}"><input type="hidden" name="status" value="OFF">
                  <button class="btn btn-secondary btn-sm" type="submit">下架</button>
                </form>
              </c:when>
              <c:otherwise>
                <form method="post" action="${ctx}/app/admin/books/status">
                  <input type="hidden" name="id" value="${b.id}"><input type="hidden" name="status" value="ON">
                  <button class="btn btn-secondary btn-sm" type="submit">上架</button>
                </form>
              </c:otherwise>
            </c:choose>
          </span>
        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
