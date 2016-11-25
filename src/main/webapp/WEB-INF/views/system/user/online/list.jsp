<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="panel" id="panel">
	<div class="row-fluid tool ui-toolbar">
        <div class="span4">
            <div class="btn-group">
                <shiro:hasPermission name="system:userOnline:forcedout">
                <a class="btn btn-force-logout">
                    <span class="icon-lightbulb"></span>
                    强制退出
                </a>
                </shiro:hasPermission>
            </div>

        </div>
        <div class="span8">
            <%@include file="searchForm.jsp" %>
        </div>
    </div>
    <%@include file="listTable.jsp"%>
</div>
<es:contentFooter/>
<%@include file="/WEB-INF/views/common/admin/import-sys-js.jspf"%>
<script type="text/javascript">
	$(function(){
		$.sys.user.initOnlineListButton();
	});
</script>

