<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div data-table="table" class="panel">
	<form id="searchForm" class="form-inline search-form" data-change-search="true">
		<c:if test="${not empty param['search.type-eq'] }">
			<input type="hidden" name="search.type-eq" value="${param['search.type-eq'] }" />
		</c:if>
		<esform:label path="search.group_name-like">分组名称</esform:label>
	    <esform:input path="search.group_name-like" cssClass="input-small" placeholder="模糊匹配"/>
	     &nbsp;&nbsp;
	    <input type="submit" class="btn" value="查询"/>
	    
	</form>

	<%@include file="selectGroupTable.jsp"%>
</div>