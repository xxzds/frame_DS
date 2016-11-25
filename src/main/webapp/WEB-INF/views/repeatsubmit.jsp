<%@ page contentType="text/html; charset=UTF-8"%>
<%@include file="/WEB-INF/views/common/taglibs.jspf"%>
<es:contentHeader />
<div style="margin:0 auto;width: 200px;height:200px;
     line-height:200px;vertical-align: middle;font-size:22px;">
    <span>
		${invalidtoken }
		<a href="<es:BackURL/>" class="btn btn-link"> <i class="icon-reply"></i>
			返回
		</a>
	</span>
</div>
<es:contentFooter />