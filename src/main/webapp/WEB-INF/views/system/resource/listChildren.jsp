<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<es:contentHeader/>

<div data-table="table" class="panel">

    <ul class="nav nav-tabs">
        <li ${empty param['search.is_show-eq'] ? 'class="active"' : ''}>
            <a href="${ctx}/system/resource/${parent.id}/children">
                <i class="icon-table"></i>
                [${parent.name}]子节点列表
            </a>
        </li>
        <li ${param['search.is_show-eq'] eq '1' ? 'class="active"' : ''}>
            <a href="${ctx}/system/resource/${parent.id}/children?search.is_show-eq=1">
                <i class="icon-table"></i>
                显示的
            </a>
        </li>
        <li ${param['search.is_show-eq'] eq '0' ? 'class="active"' : ''}>
            <a href="${ctx}/system/resource/${parent.id}/children?search.is_show-eq=0">
                <i class="icon-table"></i>
                隐藏的
            </a>
        </li>
        <li>
            <a href="${ctx}/system/resource/${parent.id}/update">
                <i class="icon-reply"></i>
                返回父节点
            </a>
        </li>
    </ul>

    <%@include file="refreshTreeMessage.jsp"%>


    <div class="row-fluid tool ui-toolbar">
        <div class="span4">
            <div class="btn-group">
                <shiro:hasPermission name="system:resource:create">
                    <a class="btn btn-custom no-disabled" href="${ctx}/system/resource/${parent.id}/appendChild?BackURL=<es:BackURL/>">
                    <i class="icon-file-alt"></i>
                    添加子节点
                </a>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="system:resource:update">
                <a id="updateTree" class="btn btn-custom">
                    <i class="icon-edit"></i>
                    修改
                </a>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="system:resource:delete">
                <a id="deleteTree" class="btn btn-custom">
                    <i class="icon-trash"></i>
                    删除
                </a>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="system:resource:update">
                <div class="btn-group">
                    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="icon-pencil"></i>
                        更多操作
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a id="moveTree" class="btn">
                                <i class="icon-move"></i>
                                移动节点
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a class="btn btn-link status-show">
                                <i class="icon-pencil"></i>
                                选中显示
                            </a>
                        </li>
                        <li>
                            <a class="btn btn-link status-hide">
                                <i class="icon-pencil"></i>
                                选中隐藏
                            </a>
                        </li>
                    </ul>
                </div>
                 </shiro:hasPermission>
                 
            </div>
           
        </div>
        <div class="span8">
            <%@include file="searchForm.jsp"%>
        </div>
    </div>
    
    <%@include file="listChildrenTable.jsp"%>

</div>

<es:contentFooter/>
<%@include file="/WEB-INF/views/common/import-zTree-js.jspf"%>
<script type="text/javascript">
    $(function() {
        var tableId = "table";
        var urlPrefix = "${ctx}/system/resource";
        $.btn.initChangeShowStatus(urlPrefix + "/changeStatus", tableId);
        $.zTree.initMaintainBtn(urlPrefix, tableId, ${not empty param.async and param.async});
    });

</script>