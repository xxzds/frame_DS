<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<es:contentHeader/>
<div class="panel">

    <ul class="nav nav-tabs">
        <li class="active">
            <a href="${ctx}/system/group/${group.id}/batch/append?BackURL=<es:BackURL/>">
                <i class="icon-file-alt"></i>
                ${group.type.info}-批量新增
            </a>
        </li>

        <li>
            <a href="<es:BackURL/>" class="btn btn-link">
                <i class="icon-reply"></i>
                返回
            </a>
        </li>
    </ul>

    <form id="editForm" method="post" class="form-horizontal">

        <es:BackURL hiddenInput="true"/>

        <div class="control-group">
            <label for="ids" class="control-label">
                用户名称(批量)<br/>
                <span class="muted">多个逗号分隔</span>
            </label>
            <div class="controls">
                <div style="float: left;">
                	<input type="hidden" id="ids" name="ids"/>
                    <textarea id="userNames" name="userNames" rows="3" cols="20" placeholder="请选择用户" readonly class="validate[required]"></textarea>
                </div>
                
                <a class="btn btn-link btn-search"  href="#search-username">
                        <i class="icon-search"></i>
                    </a>
            </div>
        </div>


        <div class="control-group">
            <label class="control-label"></label>
            <div class="controls">
                <button type="submit" class="btn btn-primary">
                    <i class="icon-file-alt"></i>
                    批量新增
                </button>
                <a href="<es:BackURL/>" class="btn">
                    <i class="icon-reply"></i>
                    返回
                </a>
            </div>
        </div>


    </form>
</div>
<es:contentFooter/>

<script type="text/javascript">
    $(function() {
    	
    	//开启校验
    	$('#editForm').validationEngine();
    	
    	$('.btn-search').click(function(){
    		$.app.modalDialog("用户信息", "${ctx}/system/user/selectUser",
    				 {
    			 		 ok:true,
	    				 height:400,
	    	             width:800,
	    	             buttons:{
	    	                 '确定': function() {	    	                	 
	    	                     var checkbox = $.table.getAllSelectedCheckbox($(".table"));
	    	                     $.each(checkbox, function(i,element){	    
	    	                    	 
	    	                         var $ids = $("#ids");
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
    	
    	
        /* var $username = $("#username");
        $.app.initAutocomplete({
        	renderItem:'',
            input : $username,
            source : "${ctx}/system/user/ajax/autocomplete",
            select : function(event, ui) {
            	debugger;
                var $ids = $("#ids");
                var ids = $ids.val();
                ids = (ids ? ids : "") + (ids ? "," : "") + ui.item.value;
                $ids.val(ids);
                $username.val("");
                return false;
            }
        }); */
    });


</script>
