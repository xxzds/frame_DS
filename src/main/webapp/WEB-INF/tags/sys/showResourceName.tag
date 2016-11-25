<%@ tag import="com.google.common.collect.Lists" %>
<%@ tag import="com.anjz.spring.SpringUtils" %>
<%@ tag import="com.anjz.core.model.SysResource" %>
<%@ tag import="com.anjz.core.service.intf.system.SysResourceService" %>
<%@ tag import="java.util.List" %>
<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="id" type="java.lang.String" required="true" description="当前要展示的资源的名字" %>
<%@ attribute name="showParents" type="java.lang.Boolean" required="false" description="是否显示父亲" %>
<%@ attribute name="includeRoot" type="java.lang.Boolean" required="false" description="是否包含根" %>
<%!private SysResourceService resourceService;%>
<%

    if(showParents == null) {
        showParents = true;
    }
    if(includeRoot == null) {
        includeRoot = true;
    }

    if(resourceService == null) {
        resourceService = SpringUtils.getBean(SysResourceService.class);
    }

    SysResource resource = resourceService.findOne(id).getData();

    if(resource == null ) {
        out.write("<span class='label label-important'>删除的数据，请修改</span>");
        return;
    }

    List<String> names = Lists.newArrayList();

    names.add(resource.getName());

    if(showParents == true) {
        List<SysResource> parents = resourceService.findAncestor(resource.getParentIds());
        for(SysResource o : parents) {
            if(includeRoot == false && o.isRoot()) {
                continue;
            }
            names.add(o.getName());
        }
    }

    StringBuilder s = new StringBuilder();
    s.append(String.format("<a class='btn btn-link no-padding' href='%s/system/resource/%s'>", request.getContextPath(), id));

    for(int l = names.size() - 1, i = l; i >= 0; i--) {
        if(i != l) {
            s.append(" &gt; ");
        }
        s.append(names.get(i));
    }

    s.append("</a>");
    out.write(s.toString());

%>
