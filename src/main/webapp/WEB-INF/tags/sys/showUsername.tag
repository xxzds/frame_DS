<%@ tag import="com.anjz.spring.SpringUtils" %>
<%@ tag import="com.anjz.core.model.SysUser" %>
<%@ tag import="com.anjz.core.service.intf.system.SysUserService" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="当前要展示的用户的id" %>
<%@ attribute name="needLink" type="java.lang.Boolean" required="false" description="是否需要链接" %>
<%!private SysUserService userService;%>
<%

    if(userService == null) {
        userService = SpringUtils.getBean(SysUserService.class);
    }

	SysUser user = userService.findOne(id).getData();

    if(user == null) {
        out.write("<span class='label label-important'>删除的数据，请修改</span>");
        return;
    }
    String username = user.getUserName();
    String deletedInfo = (Boolean.TRUE.equals(user.getDeleted()) ? "<span class='label label-important'>[用户已删除]</span>" : "");
    if(Boolean.FALSE.equals(needLink)) {
        out.write(username + deletedInfo);
        return;
    }
    out.write(
            String.format(
                    "<a class='btn btn-link no-padding' href='%s/system/user/%s'>%s</a>%s",
                    request.getContextPath(),
                    id,
                    username,
                    deletedInfo));
%>
