<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<div data-table="table" class="panel">
	<form id="searchForm" class="form-inline search-form" data-change-search="true">
		<esform:label path="search.user_name-like">用户名</esform:label>
	    <esform:input path="search.user_name-like" cssClass="input-small" placeholder="模糊匹配"/>
	     &nbsp;&nbsp;
	    <input type="submit" class="btn" value="查询"/>
	    <a class="btn btn-link btn-clear-search">清空</a>
	    
	</form>

	<%@include file="selectUserTable.jsp"%>
</div>
<!-- <script>
$(function(){
	$.table.initTable($('.table'));
});

</script>
 -->