<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="esform" uri="http://www.anjz.com/tags/es-form" %>

<form id="searchForm" class="form-inline search-form" data-change-search="true">

    <esform:label path="search.name|like">名称</esform:label>
    <esform:input path="search.name|like" cssClass="input-small" placeholder="模糊查询"/>
    &nbsp;&nbsp;
    <input type="submit" class="btn " value="查询"/>
    <a class="btn btn-link btn-clear-search">清空</a>

</form>
