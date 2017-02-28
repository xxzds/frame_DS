<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form id="searchForm" class="form-inline search-form" data-change-search="true">

    <c:if test="${group.type eq 'user'}">
        <esform:label path="search.user_name-like">用户名称</esform:label>
        <esform:input path="search.user_name-like" cssClass="input-medium" placeholder="用户名称"/>
    </c:if>
    
    <c:if test="${group.type eq 'organization'}">
        <esform:label path="search.name-like">组织机构名称</esform:label>
        <esform:input path="search.name-like" cssClass="input-medium" placeholder="组织机构名称"/>
    </c:if>

    &nbsp;&nbsp;
    <input type="submit" class="btn " value="查询"/>
</form>
