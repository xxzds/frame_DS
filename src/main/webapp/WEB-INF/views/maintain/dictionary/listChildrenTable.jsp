<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<table id="table" class="sort-table table table-bordered table-hover" data-async="true">
    <thead>
    <tr>
        <th style="width: 80px">
            <a class="check-all" href="javascript:;">全选</a>
            |
            <a class="reverse-all" href="javascript:;">反选</a>
        </th>
        <th style="width: 70px" sort="id">编号</th>
        <th sort="name">名称</th>
        <th>编码</th>
        <th style="width: 60px;">是否可用</th>
    </tr>
    <tbody>
    <c:forEach items="${page.content}" var="m">
        <tr>
            <td class="check"><input type="checkbox" name="ids" value="${m.id}" root="${m.root}"></td>
            <td>
                <a class="btn btn-link no-padding" href="${ctx}/maintain/dictionary/${m.id}">${m.id}</a>
            </td>
            <td>
               <maintain:showDictionaryName id="${m.id}"/>
            </td>
            <td>${m.nameCode }</td>
            <td>${m.isShow ? '是' : '否'}</td>
        </tr>
    </c:forEach>
    </tbody>
</table>
<es:page page="${page}"/>

