<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<es:contentHeader/>

<c:set var="organizationId" value="${empty organizationId ? '' : organizationId}"/>
<c:set var="jobId" value="${empty jobId ? '' : jobId}"/>

<div data-table="table" class="panel">

    <ul class="nav nav-tabs">
        <li ${param['search.deleted|eq'] ne '1' and param['search.status|eq'] ne 'blocked' ? 'class="active"' : ''}>
            <a href="${ctx}/system/user/${organizationId}/${jobId}">
                <i class="icon-table"></i>
                所有用户列表
            </a>
        </li>
        <li ${param['search.deleted|eq'] eq '1' ? 'class="active"' : ''}>
            <a href="${ctx}/system/user/${organizationId}/${jobId}?search.deleted|eq=1">
                <i class="icon-table"></i>
                已删除用户列表
            </a>
        </li>
        <li ${param['search.status|eq'] eq 'blocked' ? 'class="active"' : ''}>
            <a href="${ctx}/system/user/${organizationId}/${jobId}?search.status|eq=blocked">
                <i class="icon-table"></i>
                已封禁用户列表
            </a>
        </li>
    </ul>

    <es:showMessage/>


    <div class="row-fluid tool ui-toolbar">
        <div class="span3">
            <div class="btn-group">
            
                <shiro:hasPermission name="system:user:create">
                <a class="btn no-disabled btn-create">
                    <span class="icon-file-alt"></span>
                    新增
                </a>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="system:user:update">
                <a id="update" class="btn btn-update">
                    <span class="icon-edit"></span>
                    修改
                </a>
                </shiro:hasPermission>
                
                <shiro:hasPermission name="system:user:delete">
                <a class="btn btn-delete">
                    <span class="icon-trash"></span>
                    删除
                </a>
                </shiro:hasPermission>
                
                <%-- 当拥有所有权限时才能使用 --%>
                <shiro:hasPermission name="system:user:*">
                <div class="btn-group last">
                    <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                        <i class="icon-wrench"></i>
                        更多操作
                        <span class="caret"></span>
                    </a>
                    <ul class="dropdown-menu">
                        <li>
                            <a class="btn btn-link change-password">
                                <i class="icon-key"></i>
                                改密
                            </a>
                        </li>
                        <li>
                            <a class="btn btn-link block-user">
                                <i class="icon-lock"></i>
                                封禁用户
                            </a>
                        </li>
                        <li>
                            <a class="btn btn-link unblocked-user">
                                <i class="icon-unlock"></i>
                                解封用户
                            </a>
                        </li>
                        <li>
                            <a class="btn btn-link recycle">
                                <i class="icon-ok"></i>
                                还原删除的用户
                            </a>
                        </li>
                        <li class="divider"></li>
                        <li>
                            <a class="btn btn-link status-history">
                                <i class="icon-table"></i>
                                状态变更历史
                            </a>
                        </li>
                       <!--  <li>
                            <a class="btn btn-link last-online-info">
                                <i class="icon-table"></i>
                                最后在线历史
                            </a>
                        </li> -->
                    </ul>
                </div>
                </shiro:hasPermission>
            </div>
        </div>
        <div class="span9">
            <%@include file="searchForm.jsp" %>
        </div>
    </div>
     <%@include file="listTable.jsp"%>
</div>
<es:contentFooter/>
<%@include file="/WEB-INF/views/common/admin/import-sys-js.jspf"%>
<script type="text/javascript">
    $(function() {
        $.sys.user.initUserListButton();
    });
</script>

