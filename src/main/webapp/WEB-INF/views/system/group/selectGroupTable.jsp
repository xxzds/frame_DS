<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<table  id="table" class="sort-table table table-bordered table-hover"  data-url="${ctx }/system/group/selectGroupTable" data-async="true">
	    <thead>
	    <tr>
	        <th style="width: 80px;">
	            <a class="check-all" href="javascript:;">全选</a>
	            |
	            <a class="reverse-all" href="javascript:;">反选</a>
	        </th>
	        <th sort="group_name">分组名称</th>
	        <th style="width: 80px">类型</th>
	        <th style="width: 70px">是否有效</th>
	    </tr>
	    <tbody>
	    <c:forEach items="${page.content}" var="m">
        <tr>
            <td class="check"><input type="checkbox" name="ids" value="${m.id}"></td>
            <td>${m.groupName}</td>
            <td>${m.type.info}</td>
            <td>${m.isShow ? '是' : '否'}</td>
        </tr>
    </c:forEach>
	    </tbody>
	</table>
	<es:page page="${page}"/>