<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<form id="searchForm" class="form-inline search-form" data-change-search="true">

    <esform:label path="search.name-like">名称</esform:label>
    <esform:input path="search.name-like" cssClass="input-small" placeholder="模糊查询"/>
    &nbsp;&nbsp;
    <button type="submit" class="btn">查询</button>
    <a class="btn btn-link btn-clear-search">清空</a>

</form>
