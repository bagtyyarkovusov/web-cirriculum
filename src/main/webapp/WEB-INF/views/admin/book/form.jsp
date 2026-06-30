<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="UTF-8">
    <title>网上书店 · 编辑图书</title>
    <style>
        body { font-family: -apple-system, "Segoe UI", "Microsoft YaHei", sans-serif; margin: 2rem; }
        h1 { font-size: 1.4rem; }
        nav { margin-bottom: 1rem; }
        nav a { margin-right: 12px; }
        label { display: block; margin: 10px 0; }
        input, select, textarea { min-width: 360px; padding: 6px; }
        textarea { height: 90px; vertical-align: top; }
        button { margin-top: 8px; padding: 6px 14px; }
    </style>
</head>
<body>
    <nav>
        <a href="${pageContext.request.contextPath}/app/admin/books">返回图书管理</a>
        <a href="${pageContext.request.contextPath}/app/admin">后台首页</a>
    </nav>

    <h1><c:choose><c:when test="${book.id > 0}">编辑图书</c:when><c:otherwise>新增图书</c:otherwise></c:choose></h1>

    <form method="post" action="${pageContext.request.contextPath}/app/admin/books/save">
        <input type="hidden" name="id" value="${book.id}">
        <label>书名
            <input name="title" value="${fn:escapeXml(book.title)}" required>
        </label>
        <label>作者
            <input name="author" value="${fn:escapeXml(book.author)}">
        </label>
        <label>出版社
            <input name="publisher" value="${fn:escapeXml(book.publisher)}">
        </label>
        <label>ISBN
            <input name="isbn" value="${fn:escapeXml(book.isbn)}">
        </label>
        <label>价格
            <input name="price" type="number" step="0.01" min="0" value="${empty book.price ? '0.00' : book.price}" required>
        </label>
        <label>库存
            <input name="stock" type="number" min="0" value="${book.stock}" required>
        </label>
        <label>分类
            <select name="categoryId">
                <option value="">无分类</option>
                <c:forEach var="c" items="${categories}">
                    <option value="${c.id}" <c:if test="${c.id == book.categoryId}">selected</c:if>>
                        <c:out value="${c.name}"/>
                    </option>
                </c:forEach>
            </select>
        </label>
        <label>状态
            <select name="status">
                <option value="ON" <c:if test="${book.status == 'ON'}">selected</c:if>>上架</option>
                <option value="OFF" <c:if test="${book.status == 'OFF'}">selected</c:if>>下架</option>
            </select>
        </label>
        <label>封面路径
            <input name="coverPath" value="${fn:escapeXml(book.coverPath)}" placeholder="/uploads/cover.jpg">
        </label>
        <label>简介
            <textarea name="intro"><c:out value="${book.intro}"/></textarea>
        </label>
        <button type="submit">保存</button>
    </form>
</body>
</html>
