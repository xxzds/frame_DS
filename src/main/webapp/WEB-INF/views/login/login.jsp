<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="es" tagdir="/WEB-INF/tags" %>
<html>
<head>
    <title>系统登录</title>  
    
    <link href="${ctx}/static/css/login.css" rel="stylesheet" />
    <link href="${ctx}/static/css/main.css" rel="stylesheet" />
    
    <script src="${ctx}/static/js/jquery.min.js" type="text/javascript"></script>
</head>
<body>
	<!--上部背景-->
    <div id="Login">
        <div class="bg2"></div>
        <div class="bg3"></div>
        <!--上部背景 end-->
        <!--登录-->
        <div class="bg4">
            <form action="" method="post" id="loginForm">
            <ul class="login clearfloat">
                <li class="fl login-logo">
                    <img src="${ctx}/static/images/login/logo.png"  width="220px" height="120px"/></li>
                <li class="fl line">
                    <img src="${ctx}/static/images/login/line.png" /></li>
                <li class="fl denglu ml25">
                    <dl class="clearfloat">
                        <dt class="dl-title">后台管理系统</dt>
                        <dt>用户名：<input class="text-inputs" type="text" value="" name="userName" id="userName"></dt>
                        <dt>密　码：<input class="text-inputs" type="password" name="password" id="password" value=""></dt>
                        <c:if test="${not empty jcaptchaEbabled and  jcaptchaEbabled==true }">
                        	<dt class="clearfloat">
	                            <span class="fl">验证码：</span>
	                            <span class="fl"><input type="text" maxlength="5" id="jcaptchaCode" class="vcode text-inputs" style="width:94px" name="jcaptchaCode"></span>
	                            <span class="fl ml10"><img id="jcaptcha" src="jcaptcha.jpg" onclick="clickJcaptcha();"/></span>                          
                       		</dt>                        
                        </c:if>                       
                       <dt>
                       		<span class="error" id="error">${error}</span>
                       </dt>
                    </dl>
                </li>
                <li class="fl ml30">
                    <a class="login-btn" href="javascript:;" title="这里是登录按钮" id="submitForm"></a>
                </li>
            </ul>
            </form>
            
        </div>
        
        <!--登录 end-->
        <!--下部背景-->
        <div class="bg5">版权所有&nbsp;Copyright&nbsp;©&nbsp;2009-2016&nbsp;All&nbsp;Rights&nbsp;Reserved&nbsp;安徽建筑大学</div>
        <div class="bg6"></div>
        <!--下部背景 end-->
    </div>
</body>
<script type="text/javascript">
var jcaptchaEbabled =${jcaptchaEbabled};

$(function(){
	if(window != top){
		top.window.location.href='${currentUrl}';
	}
});


//验证码
function clickJcaptcha(){
	$('#jcaptcha').attr('src','jcaptcha.jpg?d='+new Date().getTime());
}


//登录
$('#submitForm').click(function(){
	var userName= $('#userName').val();
	var password= $('#password').val();
	var jcaptchaCode= $('#jcaptchaCode').val();
	
	if(userName==null || userName==''){
		$('#error').html("用户名不能为空");
		return;
	}
	if(password==null || password==''){
		$('#error').html("密码不能为空");
		return;
	}
	
	if(jcaptchaEbabled){
		if(jcaptchaCode==null || jcaptchaCode==''){
			$('#error').html("验证码不能为空");
			return;
		}	
	}
	
	$.ajax({
			url : 'login',
			data : $("#loginForm").serialize(),
			type : 'post',
			cache : false,
			dataType : 'json',
			success : function(data) {
				if(!data.success){
					$('#error').html(data.message);
					
					if(jcaptchaEbabled){
						$('#jcaptchaCode').val('');
						clickJcaptcha();
					}	
				}else{
					window.location.href=data.data;
				}
			},
			error : function() {
				alert("异常！");
			}
		});

	});
</script>
</html>