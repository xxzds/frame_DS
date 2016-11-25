<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<form id="searchForm" class="form-inline search-form" data-change-search="true">

    <c:if test="${type eq 'user'}">
        <esform:label path="search.username|like">用户名称</esform:label>
        <esform:input path="search.username|like" cssClass="input-small" placeholder="模糊查询"/>
    </c:if>
    <c:if test="${type eq 'user_group'}">
        <esform:label path="search.groupname|like">用户组名称</esform:label>
        <esform:input path="search.groupname|like" cssClass="input-small" placeholder="模糊查询"/>
    </c:if>
    <c:if test="${type eq 'organization_job'}">
        <esform:label path="search.organizationname|like">组织机构</esform:label>
        <esform:input path="search.organizationname|like" cssClass="input-small" placeholder="模糊查询"/>
        &nbsp;
        <esform:label path="search.jobname|like">工作职务</esform:label>
        <esform:input path="search.jobname|like" cssClass="input-small" placeholder="模糊查询"/>
    </c:if>
    <c:if test="${type eq 'organization_group'}">
        <esform:label path="search.groupname|like">组织机构组名称</esform:label>
        <esform:input path="search.groupname|like" cssClass="input-small" placeholder="模糊查询"/>
    </c:if>
    &nbsp;

    <input type="submit" class="btn " value="查询"/>
    <a class="btn btn-link btn-clear-search">清空</a>
</form>
