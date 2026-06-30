<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="编辑图书"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head">
  <div><h1><c:choose><c:when test="${book.id > 0}">编辑图书</c:when><c:otherwise>新增图书</c:otherwise></c:choose></h1></div>
  <a class="btn btn-ghost btn-sm" href="${ctx}/app/admin/books">返回图书管理</a>
</div>
<div class="form-panel" style="max-width:560px;margin-top:0">
  <form method="post" action="${ctx}/app/admin/books/save">
    <input type="hidden" name="id" value="${book.id}">
    <div class="field"><label>书名</label><input name="title" value="${fn:escapeXml(book.title)}" required></div>
    <div class="field"><label>作者</label><input name="author" value="${fn:escapeXml(book.author)}"></div>
    <div class="field"><label>出版社</label><input name="publisher" value="${fn:escapeXml(book.publisher)}"></div>
    <div class="field"><label>ISBN</label><input name="isbn" value="${fn:escapeXml(book.isbn)}"></div>
    <div class="field"><label>价格</label><input name="price" type="number" step="0.01" min="0" value="${empty book.price ? '0.00' : book.price}" required></div>
    <div class="field"><label>库存</label><input name="stock" type="number" min="0" value="${book.stock}" required></div>
    <div class="field"><label>分类</label>
      <select name="categoryId">
        <option value="">无分类</option>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat.id}" <c:if test="${cat.id == book.categoryId}">selected</c:if>><c:out value="${cat.name}"/></option>
        </c:forEach>
      </select>
    </div>
    <div class="field"><label>状态</label>
      <select name="status">
        <option value="ON" <c:if test="${book.status == 'ON'}">selected</c:if>>上架</option>
        <option value="OFF" <c:if test="${book.status == 'OFF'}">selected</c:if>>下架</option>
      </select>
    </div>
    <div class="field"><label>封面路径</label><input name="coverPath" value="${fn:escapeXml(book.coverPath)}" placeholder="/uploads/cover.jpg"></div>
    <div class="field"><label>简介</label><textarea name="intro" rows="4"><c:out value="${book.intro}"/></textarea></div>
    <button class="btn btn-primary" type="submit"><svg class="icon icon-sm"><use href="#i-check"/></svg>保存</button>
  </form>
</div>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
