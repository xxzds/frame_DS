<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<table id="historyTable" class="sort-table table table-bordered table-hover" data-async="true">
    <thead>
    <tr>
        <th>序号</th>
        <th sort="user_id">用户</th>
        <th>状态</th>
        <th>原因</th>
        <th sort="op_user_id">操作人</th>
        <th sort="op_date">操作时间</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${page.content}" var="m" varStatus="status">
        <tr>
            <td>${status.index +1}</td>
            <td><a href="${ctx}/system/user/${m.userId}">${esfn:findUserNameById(m.userId)}</a></td>
            <td>${m.status.info}</td>
            <td>${m.reason}</td>
            <td><a href="${ctx}/system/user/${m.opUserId}">${esfn:findUserNameById(m.opUserId)}</a></td>
            <td><fmt:formatDate value="${m.opDate}" pattern="yyyy-MM-dd hh:mm:ss" type="date" dateStyle="long" /></td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<es:page page="${page}"/>

