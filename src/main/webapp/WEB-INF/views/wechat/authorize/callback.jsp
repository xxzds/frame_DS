<%@ page contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
	<head>
		<title>微信授权回调页面</title>
	
	</head>
	<body>
		<c:if test="${not empty error}">
	    <div class="alert alert-error">
	        <button type="button" class="close" data-dismiss="alert">&times;</button>
	        <span class="icon-remove-sign icon-large"></span>&nbsp;${error}
	    </div>
	</c:if>
	
	${userInfo }
	
	</body>
</html>