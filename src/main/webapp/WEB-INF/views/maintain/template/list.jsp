<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<es:contentHeader/>
<div data-table="table" class="panel">

    <ul class="nav nav-tabs">
        <li ${param['search.deleted-eq'] ne '1' ? 'class="active"' : ''}>
            <a href="${ctx}/maintain/template?search.deleted-eq=0">
                <i class="icon-table"></i>
                未删除的模板列表
            </a>
        </li>
        <li ${param['search.deleted-eq'] eq '1' ? 'class="active"' : ''}>
            <a href="${ctx}/maintain/template?search.deleted-eq=1">
                <i class="icon-table"></i>
                已删除的模板列表
            </a>
        </li>
    </ul>

    <es:showMessage/>

    <div class="row-fluid tool ui-toolbar">
        <div class="span4">
            <div class="btn-group">
                <shiro:hasPermission name="maintain:template:create">
                <a class="btn btn-create">
                    <i class="icon-file-alt"></i>
                    新增
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="maintain:template:update">
                <a id="update" class="btn btn-update">
                    <i class="icon-edit"></i>
                    修改
                </a>
                </shiro:hasPermission>
                <shiro:hasPermission name="maintain:template:delete">
                <c:if test="${param['search.deleted-eq'] ne '1'}">
					<a class="btn btn-logicDelete"> 
						<i class="icon-trash"></i>
						 逻辑删除
					</a>
				</c:if>
				 <c:if test="${param['search.deleted-eq'] eq '1'}">
					<a class="btn btn-reduction"> 
						<i class="icon-ok"></i>
						 还原
					</a>
				</c:if>
                
                </shiro:hasPermission>
            </div>
        </div>
        <div class="span8">
            <%@include file="searchForm.jsp"%>
        </div>
    </div>
    <%@include file="listTable.jsp"%>

</div>
<es:contentFooter/>
<script type="text/javascript">
	$('.btn-logicDelete,.btn-reduction').click(function(){
		 var checkbox = $.table.getAllSelectedCheckbox($(".table"));
         if(!checkbox.length) return;
         var deleted=$(this).is(".btn-logicDelete")? "1":"0";
         var title = deleted == "1" ? "逻辑删除":"还原";
         var message = deleted == "1" ? "确定逻辑删除选择的数据吗？":"确定还原选择的数据吗？";
         
         $.app.confirm({
        	 title:title,
             message: message,
             ok : function() {
                 window.location.href =ctx+"/maintain/template/change/"+deleted+"?"+checkbox.serialize();
             }
         });
	});


</script>