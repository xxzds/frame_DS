<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="esform" uri="http://www.anjz.com/tags/es-form" %>

<form id="searchForm" class="form-inline search-form" data-change-search="true">

    <esform:label path="search.group_name-like">分组名称</esform:label>
    <esform:input path="search.group_name-like" cssClass="input-medium" placeholder="分组名称"/>
    &nbsp;&nbsp;
    <%-- <esform:label path="search.status_eq">默认</esform:label>
    <esform:select path="search.defaultGroup_eq" cssClass="input-small">
        <esform:option label="所有" value=""/>
        <esform:options items="${booleanList}" itemLabel="info"/>
    </esform:select> --%>
    &nbsp;&nbsp;
    <esform:label path="search.is_show-eq">有效</esform:label>
    <esform:select path="search.is_show-eq" cssClass="input-small">
        <esform:option label="所有" value=""/>
        <option value="0">否</option>
        <option value="1">是</option>
    </esform:select>

    &nbsp;&nbsp;
    <input type="submit" class="btn " value="查询"/>
    <a class="btn btn-link btn-clear-search">清空</a>
</form>
