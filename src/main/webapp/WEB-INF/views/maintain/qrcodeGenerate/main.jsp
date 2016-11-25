<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<es:contentHeader/>
<%@include file="/WEB-INF/views/common/import-upload-css.jspf"%>
<div class="panel" style="padding-top:40px;">
	<form id="editForm" method="post" class="form-horizontal">
		<div class="control-group">
             <label  for="content"  class="control-label">内容</label>
             <div class="controls">
                 <textarea id="content" name="content" class="validate[required]" placeholder="请输入文字内容" style="width:400px;height:100px;"></textarea>
             </div>
         </div>
         
         <div class="control-group" style="margin-bottom: 0px;<c:if test="${empty upload.src}">display: none</c:if>">
             <label for="files" class="control-label"></label>
             <div class="controls">
                 <div class="ajax-upload-view"></div>
                 <input type="hidden" name="logopath" id="logopath"/>
             </div>
         </div>
         
         <div class="control-group">
              <label for="files" class="control-label">LOGO</label>
              <div class="controls">
                 <label for="files" class="btn btn-success fileinput-button">
                      <i class="icon-plus icon-white"></i>
                      <span>添加文件...</span>
                     <input type="file" id="files" name="files" data-url="${ctx}/ajaxUploadImage" >
                 </label>

              </div>
          </div>
          
          <div class="control-group">
              <div class="controls">
                  <a class="btn btn-qrcodegenerate">
                      <i class="icon-file-alt"></i>
                     生成二维码
                  </a>
              </div>
          </div>
          
          <div class="control-group">
          	<div class="controls">
          		<img id="qrcode"/>
          	</div>
          </div>
	
	</form>
</div>	
	
	
	
<es:contentFooter/>
<%@include file="/WEB-INF/views/common/import-upload-simple-js.jspf"%>
<script type="text/javascript">
    $(function () {
        $('.fileinput-button input[type="file"]').fileupload({
            dataType : "json"
        });
        $('.fileinput-button input[type="file"]').fileupload("option", {      	
            progressall: function (e, data) {
                var view = $(".ajax-upload-view");
                view.parent().parent().show();
                var progressBar = view.find(".progress");
                if(progressBar.size() == 0) {
                    var progressBarTemplate =
                            '<div class="progress progress-striped">' +
                                    '<div class="bar"></div>' +
                                    '</div>';
                    progressBar = view.append(progressBarTemplate);
                }
                var progress = parseInt(data.loaded / data.total * 100, 10);
                progressBar.find(".bar").css("width", progress + "%");
            },
            start : function(e) {
                $(".ajax-upload-view").html("");
                var submitBtn = $(this).closest("form").find(":submit");
                submitBtn.data("value", submitBtn.val()).val("上传文件中...").prop("disabled", true);
            },
            //上传完成
            done: function (e, data) {
                $.each(data.result.files, function (index, file) {
                    if (file.error) {
                        $(".ajax-upload-view").html("<div class='alert alert-error'>" + file.error + "</div>");
                    } else {
                        $("[name=logopath]").val(file.url);
                        var msg = "<div class='alert alert-success'><strong>上传成功！</strong><br/>{preview}</div>";
                        var preview = "";
                        var url = ctx + "/" + file.url;
                        var thumbnail_url = ctx + "/" + file.thumbnail_url;
                        if($.app.isImage(file.name)) {
                            preview = "<a href='{url}' target='_blank'><img src='{thumbnail_url}' title='{name}' height='120px'/></a>"
                        } else {
                            preview = "<a href='{url}' target='_blank'>{name}</a>"
                        }
                        preview = preview.replace("{url}", url).replace("{thumbnail_url}", thumbnail_url).replace("{name}", file.name);
                        msg = msg.replace("{preview}", preview);
                        $(".ajax-upload-view").html(msg);

                    }
                });
                var submitBtn = $(this).closest("form").find(":submit");
                submitBtn.val(submitBtn.data("value")).prop("disabled", false);
            }
        });
        var validationEngine = $("#editForm").validationEngine();
        
        
        //生成二维码
        $('.btn-qrcodegenerate').click(function(){
        	var content=$('#content').val();
        	var logopath=$('#logopath').val();
        	
        	if(content==null || content==""){
        		$.app.alert({message : "内容不能为空"});
        		return;
        	}
       	
        	 $.app.waiting("正在执行...");
        	 var url = ctx + "/maintain/qrcodeGenerate/generate";
        	 
        	 $('#qrcode').attr('src',url+"?content="+content+"&logopath="+logopath);
        	 $.app.waitingOver();
        });
    });
</script>