<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<es:contentHeader/>
<div class="panel">

    <ul class="nav nav-tabs">
        <shiro:hasPermission name="system:auth:create">
        <c:if test="${op eq '新增'}">
            <c:forEach items="${types}" var="t">
                <li ${t eq type ? 'class="active"' : ''}>
                    <a href="${ctx}/system/permission/auth/${t}/create?BackURL=<es:BackURL/>">
                        <i class="icon-file-alt"></i>
                        新增${t.info}授权
                    </a>
                </li>
            </c:forEach>
        </c:if>
        </shiro:hasPermission>

        <c:if test="${not empty m.id}">
        	 <shiro:hasPermission name="system:auth:view">
            <li ${op eq '查看' ? 'class="active"' : ''}>
                <a href="${ctx}/system/permission/auth/${m.id}?BackURL=<es:BackURL/>">
                    <i class="icon-eye-open"></i>
                    查看
                </a>
            </li>
            </shiro:hasPermission>
            
            <shiro:hasPermission name="system:auth:update">
            <li ${op eq '修改' ? 'class="active"' : ''}>
                <a href="${ctx}/system/permission/auth/${m.id}/update?BackURL=<es:BackURL/>">
                    <i class="icon-edit"></i>
                    修改
                </a>
            </li>
            </shiro:hasPermission>
            
            <shiro:hasPermission name="system:auth:delete">
            <li ${op eq '删除' ? 'class="active"' : ''}>
                <a href="${ctx}/system/permission/auth/${m.id}/delete?BackURL=<es:BackURL/>">
                    <i class="icon-trash"></i>
                    删除
                </a>
            </li>
            </shiro:hasPermission>
        </c:if>
        <li>
            <a href="<es:BackURL/>" class="btn btn-link">
                <i class="icon-reply"></i>
                返回
            </a>
        </li>
    </ul>

    <form:form id="editForm" method="post" commandName="m" cssClass="form-inline form-horizontal form-medium">

        <es:showGlobalError commandName="m"/>
        <form:hidden path="id"/>
        <form:hidden path="type"/>

        <c:set var="type" value="${not empty type ? type : m.type}"/>

        <h4 class="hr">${type.info}信息</h4>

        <c:if test="${op ne '新增'}">
            <c:if test="${type eq 'user'}">
                <div class="control-group">
                    <label class="control-label">
                        用户
                    </label>
                    <div class="controls" style="padding-top: 3px;">
                        <sys:showUsername id="${m.userId}"/>
                        <form:hidden path="userId"/>
                    </div>
                </div>
            </c:if>
            <c:if test="${type eq 'user_group'}">
                <div class="control-group">
                    <label class="control-label">
                        用户分组
                    </label>
                    <div class="controls" style="padding-top: 3px;">
                        <sys:showGroupName id="${m.groupId}"/>
                        <form:hidden path="groupId"/>
                    </div>
                </div>
            </c:if>
            <c:if test="${type eq 'organization_group'}">
                <div class="control-group">
                    <label class="control-label">
                        组织机构分组
                    </label>
                    <div class="controls" style="padding-top: 3px;">
                        <sys:showGroupName id="${m.groupId}"/>
                    </div>
                </div>
            </c:if>
            <c:if test="${type eq 'organization_job'}">
                <div class="control-group">
                    <label class="control-label">
                        组织机构
                    </label>
                    <div class="controls" style="padding-top: 3px;">
                        <sys:showOrganizationName id="${m.organizationId}"/>
                        <form:hidden path="organizationId"/>
                    </div>
                </div>
                <div class="control-group">
                    <label class="control-label">
                        工作职务
                    </label>
                    <div class="controls" style="padding-top: 3px;">
                        <sys:showJobName id="${m.jobId}"/>
                        <form:hidden path="jobId"/>
                    </div>
                </div>
            </c:if>
        </c:if>


        <c:if test="${op eq '新增'}">
            <c:if test="${type eq 'user'}">
                <div class="control-group">
                    <label for="userIds" class="control-label">
                        用户名称(批量)<br/>
                        <span class="muted">多个逗号分隔</span>
                    </label>
                    <div class="controls" >
		                <div style="float: left;" id="userIds_msg" data-prompt-position="topLeft">
		                	<input type="hidden" id="userIds" name="userIds" class="validate[required]"/>
		                	
		                    <textarea id="userNames" name="userNames" rows="3" cols="20" placeholder="请选择用户" readonly></textarea>
		                </div>
		                
		                <a class="btn btn-link btn-search_user"  href="#search-username">
	                        <i class="icon-search"></i>
	                    </a>
		            </div>
                </div>
            </c:if>

            <c:if test="${type eq 'user_group' or type eq 'organization_group'}">
                <div class="control-group">
                    <label for="groupIds" class="control-label">分组名称(批量)</label>
                    <div class="controls">
		                <div style="float: left;" id="groupIds_msg" data-prompt-position="topLeft">
		                	<input type="hidden" id="groupIds" name="groupIds" class="validate[required]"/>
		                    <textarea id="groupNames" name="groupNames" rows="3" cols="20" placeholder="请选择分组名称" readonly></textarea>
		                </div>
		                
		                <a class="btn btn-link btn-search_group"  href="#search-groupName">
	                        <i class="icon-search"></i>
	                    </a>
		            </div>
                </div>
            </c:if>

            <c:if test="${type eq 'organization_job'}">
                <c:set var="noOrganizationAndJobTitle" value="true"/>
                <%@include file="/WEB-INF/views/system/selectOrganizationAndJob.jspf"%>
            </c:if>
        </c:if>

        <br/>

        <h4 class="hr">角色信息</h4>
        <div class="control-group">
            <label class="control-label">角色列表</label>
            <div class="controls">
                <div class="auth">
                    <div class="left">
                        <div class="title muted">未选择的角色列表</div>
                        <div class="list">
                            <ul>
                                <c:forEach items="${roles}" var="r">
                                    <c:if test="${not esfn:in(m.roleIdsSet, r.id)}">
                                        <li class="ui-state-default" data-value="${r.id}" title="${r.description}">
                                            ${r.name}[${r.role}]
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                    <div class="btns">
                        <a class="btn btn-link btn-move-all-right" data-toggle="tooltip" data-placement="bottom" title="全部右移">
                            <i class="icon-double-angle-right"></i>
                        </a>
                        <a class="btn btn-link btn-move-all-left" data-toggle="tooltip" data-placement="bottom" title="全部左移">
                            <i class="icon-double-angle-left"></i>
                        </a>
                    </div>
                    <div class="right" id="roleIds_msg" data-prompt-position="topLeft">
                        <div class="title muted">已选择的角色列表</div>
                        <div class="list">
                            <ul data-input-id="roleIds">
                                <c:forEach items="${roles}" var="r">
                                    <c:if test="${esfn:in(m.roleIdsSet, r.id)}">
                                        <li class="ui-state-default" data-value="${r.id}" title="${r.description}">
                                            ${r.name}[${r.role}]
                                        </li>
                                    </c:if>
                                </c:forEach>
                            </ul>
                        </div>
                        <form:hidden path="roleIds" cssClass="validate[required]"/>
                    </div>

                </div>
            </div>
        </div>

        <c:if test="${op eq '新增'}">
            <c:set var="icon" value="icon-file-alt"/>
        </c:if>
        <c:if test="${op eq '修改'}">
            <c:set var="icon" value="icon-edit"/>
        </c:if>
        <c:if test="${op eq '删除'}">
            <c:set var="icon" value="icon-trash"/>
        </c:if>

        <div class="control-group left-group">
            <div>
                <button type="submit" class="btn btn-primary">
                    <i class="${icon}"></i>
                    ${op}
                </button>
                <a href="<es:BackURL/>" class="btn">
                    <i class="icon-reply"></i>
                    返回
                </a>
            </div>
        </div>


    </form:form>
</div>
<es:contentFooter/>
<%@include file="/WEB-INF/views/common/import-zTree-css.jspf"%>
<%@include file="/WEB-INF/views/common/import-zTree-js.jspf"%>
<%@include file="/WEB-INF/views/common/admin/import-sys-js.jspf"%>
<script type="text/javascript">
    $(function () {
        <c:choose>
            <c:when test="${op eq '删除'}">
                //删除时不验证 并把表单readonly
                $.app.readonlyForm($("#editForm"), false);
                $(".auth > .left,.auth > .btns").remove();
            </c:when>
            <c:when test="${op eq '查看'}">
                $.app.readonlyForm($("#editForm"), true);
                $(".auth > .left,.auth > .btns").remove();
            </c:when>
            <c:otherwise>
                var validationEngine = $("#editForm").validationEngine({prettySelect:true, useSuffix : "_msg"});
                <es:showFieldError commandName="m"/>

                $.sys.auth.initSelectRoleForm();

            </c:otherwise>
        </c:choose>

        <c:if test="${op eq '新增'}">


            <c:if test="${type eq 'user'}">
            $('.btn-search_user').click(function(){
        		$.app.modalDialog("用户信息", "${ctx}/system/user/selectUser",
        				 {
        			 		 ok:true,
    	    				 height:400,
    	    	             width:800,
    	    	             buttons:{
    	    	                 '确定': function() {	    	                	 
    	    	                     var checkbox = $.table.getAllSelectedCheckbox($(".table"));
    	    	                     $.each(checkbox, function(i,element){	    
    	    	                    	 
    	    	                         var $ids = $("#userIds");
    	    	                         var ids = $ids.val();
    	    	                         ids = (ids ? ids : "") + (ids ? "," : "") + $(element).val();
    	    	                         $ids.val(ids);
    	    	                         
    	    	                         
    	    	                         var userNames=$('#userNames').val();
    	    	                         userNames=(userNames ? userNames : "") + (userNames ? "," : "") +  $(element).parent().parent().children().eq(1).html();
    	    	                         $('#userNames').val(userNames);
    	    	                         
    	    	                         //关闭
    	    	                         $(this).closest(".ui-dialog").remove();
    	    	                     });   
    	    	                 }
    	    	             }
        			 
        				 });
        		
        	});
            </c:if>


            <c:if test="${type eq 'user_group' or type eq 'organization_group'}">
                
                var groupType = '${type eq 'user_group' ? 'user' : 'organization'}';
                $('.btn-search_group').click(function(){
            		$.app.modalDialog("分组信息", "${ctx}/system/group/selectGroup?search.type-eq="+groupType,
            				 {
            			 		 ok:true,
        	    				 height:300,
        	    	             width:500,
        	    	             buttons:{
        	    	                 '确定': function() {	    	                	 
        	    	                     var checkbox = $.table.getAllSelectedCheckbox($(".table"));
        	    	                     $.each(checkbox, function(i,element){	    
        	    	                    	 
        	    	                         var $ids = $("#groupIds");
        	    	                         var ids = $ids.val();
        	    	                         ids = (ids ? ids : "") + (ids ? "," : "") + $(element).val();
        	    	                         $ids.val(ids);
        	    	                         
        	    	                         
        	    	                         var groupNames=$('#groupNames').val();
        	    	                         groupNames=(groupNames ? groupNames : "") + (groupNames ? "," : "") +  $(element).parent().parent().children().eq(1).html();
        	    	                         $('#groupNames').val(groupNames);
        	    	                         
        	    	                         //关闭
        	    	                         $(this).closest(".ui-dialog").remove();
        	    	                     });   
        	    	                 }
        	    	             }
            			 
            				 });
            		
            	});
            </c:if>

            <c:if test="${type eq 'organization_job'}">
                $.sys.organization.initSelectForm("organizationIds", "jobIds", true);

                $("#editForm").submit(function() {
                    var selectedOrganizationLength = $("#selectedOrganization > table > tbody > tr").length;
                    if(!selectedOrganizationLength) {
                        $("#organizationName").validationEngine('showPrompt', '必须选择一个组织机构', null, null, true);
                        return false;
                    } else {
                        $("#organizationName").validationEngine("hide");
                        return true;
                    }
                });
            </c:if>
        </c:if>

    });
</script>