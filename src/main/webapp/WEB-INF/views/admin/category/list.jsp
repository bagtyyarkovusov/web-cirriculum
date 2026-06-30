<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<c:set var="section" value="admin"/>
<c:set var="pageTitle" value="分类管理"/>
<%@ include file="/WEB-INF/views/common/head.jspf" %>
<%@ include file="/WEB-INF/views/common/header.jspf" %>
<div class="page-head"><div><h1>分类管理</h1></div></div>
<c:if test="${not empty message}"><div class="flash flash-ok"><svg class="icon icon-sm"><use href="#i-check"/></svg><c:out value="${message}"/></div></c:if>
<c:if test="${not empty error}"><div class="flash flash-err"><svg class="icon icon-sm"><use href="#i-x"/></svg><c:out value="${error}"/></div></c:if>
<div class="panel mb-16">
  <form method="post" action="${ctx}/app/admin/categories/save" class="book-actions">
    <input name="name" placeholder="新分类名称" required class="w-auto">
    <button class="btn btn-primary btn-sm" type="submit"><svg class="icon icon-sm"><use href="#i-plus"/></svg>新增分类</button>
  </form>
</div>
<table class="table">
  <thead><tr><th>ID</th><th>名称</th><th>操作</th></tr></thead>
  <tbody>
    <c:forEach var="cat" items="${categories}">
      <tr>
        <td class="num">${cat.id}</td>
        <td>
          <form method="post" action="${ctx}/app/admin/categories/save" class="book-actions">
            <input type="hidden" name="id" value="${cat.id}">
            <input name="name" value="${fn:escapeXml(cat.name)}" required class="w-auto">
            <button class="btn btn-secondary btn-sm" type="submit">保存</button>
          </form>
        </td>
        <td>
          <form method="post" action="${ctx}/app/admin/categories/delete">
            <input type="hidden" name="id" value="${cat.id}">
            <button class="btn btn-danger btn-sm" type="submit"><svg class="icon icon-sm"><use href="#i-x"/></svg>删除</button>
          </form>
        </td>
      </tr>
    </c:forEach>
  </tbody>
</table>
<%@ include file="/WEB-INF/views/common/footer.jspf" %>
