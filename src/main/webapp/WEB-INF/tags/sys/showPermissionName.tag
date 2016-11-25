<%@ tag import="com.anjz.spring.SpringUtils" %>
<%@ tag import="com.anjz.core.model.SysPermission" %>
<%@ tag import="com.anjz.core.service.intf.system.SysPermissionService" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="当前要展示的组织机构的名字" %>
<%!private SysPermissionService permissionService;%>
<%

    if(permissionService == null) {
        permissionService = SpringUtils.getBean(SysPermissionService.class);
    }

	SysPermission permission = permissionService.findOne(id).getData();

    if(permission == null) {
        out.write("<span class='label label-important'>删除的数据，请修改</span>");
        return;
    }

    StringBuilder s = new StringBuilder();
    s.append(String.format("<a class='btn btn-link no-padding' href='%s/system/permission/permission/%s'>", request.getContextPath(), id));
    s.append(String.format("<span title='%s'>%s[%s]</span>", permission.getDescription(), permission.getName(), permission.getPermission()));
    s.append("</a>");
    out.write(s.toString());

%>
