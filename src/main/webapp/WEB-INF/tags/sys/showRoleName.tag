<%@ tag import="com.anjz.spring.SpringUtils" %>
<%@ tag import="com.anjz.core.model.SysRole" %>
<%@ tag import="com.anjz.core.service.intf.system.SysRoleService" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="当前要展示的角色id" %>
<%!private SysRoleService roleService;%>
<%

    if(roleService == null) {
        roleService = SpringUtils.getBean(SysRoleService.class);
    }

	SysRole role = roleService.findOne(id).getData();
    if(role == null) {
        out.write("<span class='label label-important'>删除的数据，请修改</span>");
        return;
    }

    StringBuilder s = new StringBuilder();
    s.append(String.format("<a class='btn btn-link no-padding' href='%s/system/permission/role/%s'>", request.getContextPath(), id));
    s.append(String.format("<span title='%s'>%s[%s]</span>", role.getDescription(), role.getName(), role.getRole()));
    s.append("</a>");
    out.write(s.toString());

%>
