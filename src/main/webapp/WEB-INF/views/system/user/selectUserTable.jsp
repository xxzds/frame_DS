<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<table  id="table" class="sort-table table table-bordered table-hover"  data-url="${ctx }/system/user/selectUserTable" data-async="true">
	    <thead>
	    <tr>
	        <th style="width: 80px;">
	            <a class="check-all" href="javascript:;">全选</a>
	            |
	            <a class="reverse-all" href="javascript:;">反选</a>
	        </th>
	        <th sort="user_name">用户名</th>
	        <th sort="user_email">邮箱</th>
	        <th sort="user_phone">手机号</th>
	        <th>创建时间</th>
	        <th>帐户状态</th>
	    </tr>
	    <tbody>
	    <c:forEach items="${page.content}" var="m">
	        <tr>
	            <td class="check">
	                <input type="checkbox" name="ids" value="${m.id}" data-status="${m.status}"/>
	            </td>
	            <td>${m.userName}</td>
	            <td>${m.userEmail}</td>
	            <td>${m.userPhone}</td>
	            <td><fmt:formatDate value="${m.userCreateTime}" pattern="yyyy-MM-dd hh:mm:ss" type="date" dateStyle="long" /> </td>
	            <td>${m.status.info}</td>
	        </tr>
	    </c:forEach>
	    </tbody>
	</table>
	<es:page page="${page}"/>