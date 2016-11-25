<%@ tag import="com.anjz.spring.SpringUtils" %>
<%@ tag import="com.anjz.core.model.SysGroup" %>
<%@ tag import="com.anjz.core.service.intf.system.SysGroupService" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="当前要展示的组的id" %>
<%!private SysGroupService groupService;%>
<%

    if(groupService == null) {
        groupService = SpringUtils.getBean(SysGroupService.class);
    }

	SysGroup group = groupService.findOne(id).getData();

    if(group == null) {
        out.write("<span class='label label-important'>删除的数据，请修改</span>");
        return;
    }

    StringBuilder s = new StringBuilder();
    s.append(String.format("<a class='btn btn-link no-padding' href='%s/system/group/%s'>", request.getContextPath(), id));
    s.append(group.getGroupName());
    s.append("</a>");
    out.write(s.toString());
%>
