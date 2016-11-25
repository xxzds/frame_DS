<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<es:contentHeader/>

<div class="panel">
    <ul class="nav nav-tabs">
        <shiro:hasPermission name="system:user:create">
        <c:if test="${op eq '新增'}">
        <li ${op eq '新增' ? 'class="active"' : ''}>
            <a href="${ctx}/system/user/create?BackURL=<es:BackURL/>">
                <i class="icon-file-alt"></i>
                新增
            </a>
        </li>
        </c:if>
        </shiro:hasPermission>

        <c:if test="${not empty m.id}">
        <li ${op eq '查看' ? 'class="active"' : ''}>
            <a href="${ctx}/system/user/${m.id}?BackURL=<es:BackURL/>">
                <i class="icon-eye-open"></i>
                查看
            </a>
        </li>
        <shiro:hasPermission name="system:user:update">
        <li ${op eq '修改' ? 'class="active"' : ''}>
            <a href="${ctx}/system/user/${m.id}/update?BackURL=<es:BackURL/>">
                <i class="icon-edit"></i>
                修改
            </a>
        </li>
        </shiro:hasPermission>
        <shiro:hasPermission name="system:user:delete">
        <li ${op eq '删除' ? 'class="active"' : ''}>
            <a href="${ctx}/system/user/${m.id}/delete?BackURL=<es:BackURL/>">
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

    <form:form id="editForm" method="post" commandName="m" cssClass="form-inline form-horizontal form-small">
        <es:showGlobalError commandName="m"/>

        <form:hidden path="id"/>
        <form:hidden path="deleted"/>
        <form:hidden path="userSalt"/>
        <div id="baseinfo">
            <h4 class="hr">用户基本信息</h4>
            <div class="control-group span4">
                <form:label path="userName" cssClass="control-label">用户名</form:label>
                <div class="controls">
                    <form:input path="userName" cssClass="validate[required,custom[username],ajax[ajaxCall]]"
                                placeholder="5到20个汉字、字母、数字或下划线"/>
                </div>
            </div>

            <div class="control-group span4">
                <form:label path="userEmail" cssClass="control-label">邮箱</form:label>
                <div class="controls">
                    <form:input path="userEmail" cssClass="validate[required,custom[email],ajax[ajaxCall]]"
                                placeholder="如abc@163.com"/>
                </div>
            </div>
            <div class="control-group span4">
                <form:label path="userPhone" cssClass="control-label">手机号</form:label>
                <div class="controls">
                    <form:input path="userPhone"
                                cssClass="validate[required,custom[mobilePhoneNumber],ajax[ajaxCall]]"
                                placeholder="如13512345678"/>
                </div>
            </div>

            <div class="control-group span4">
                <form:label path="userCreateTime" cssClass="control-label">创建时间</form:label>
                <div class="controls input-append date">
                    <form:input path="userCreateTime"
                                  data-format="yyyy-MM-dd hh:mm:ss"
                                  data-position="bottom-left"
                                  placeholder="默认当前时间"/>
                    <span class="add-on"><i data-time-icon="icon-time" data-date-icon="icon-calendar"></i></span>
                </div>
            </div>

            <c:choose>
                <c:when test="${op eq '新增'}">
                    <div class="control-group span4">
                        <form:label path="userPassword" cssClass="control-label">初始密码</form:label>
                        <div class="controls">
                            <form:password path="userPassword" cssClass="validate[required,minSize[5],maxSize[100]]"
                                             placeholder="请输入至少5位的初始密码"/>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <form:hidden path="userPassword"/>
                </c:otherwise>
            </c:choose>

            <div class="clearfix"></div>

            <%-- <div class="control-group span4">
                <form:label path="admin" cssClass="control-label">是否管理员</form:label>
                <div class="controls inline-radio">
                    <form:radiobuttons path="admin" items="${booleanList}" itemLabel="info" itemValue="value" cssClass="validate[required]"/>
                </div>
            </div> --%>

            <div class="control-group span4">
                <form:label path="status" cssClass="control-label">状态</form:label>
                <div class="controls inline-radio">
                    <form:radiobuttons path="status" items="${statusList}" itemLabel="info" cssClass="validate[required]"/>
                </div>
            </div>
        </div>

        <c:set var="displayOrganizationJobs" value="${organizationJobsMap}"/>
        <%@include file="/WEB-INF/views/system/selectOrganizationAndJob.jspf"%>

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
                $.sys.organization.removeOrganizationBtn();
            </c:when>
            <c:when test="${op eq '查看'}">
                $.app.readonlyForm($("#editForm"), true);
                $.sys.organization.removeOrganizationBtn();
            </c:when>
            <c:otherwise>
                $.sys.user.initValidator($("#editForm"));
                <es:showFieldError commandName="m"/>
            </c:otherwise>
        </c:choose>

        $.sys.organization.initSelectForm("organizationId", "jobId");

    });

</script>