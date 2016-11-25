<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%-- <jsp:forward page="/login_toLogin" /> --%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>上传</title>
<body>
	<form action="file/upload" method="post" enctype="multipart/form-data">
		<input type="file" name="file"> 
		<input type="file" name="file"> 
		<input type="file" name="file"> 
		<!-- <input type="hidden" name="callback" value="dispaly"/> -->
		<input type="submit" value="Submit" />
	</form>
</body>
</head>
</html>