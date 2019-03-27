<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Frameset//EN" "http://www.w3c.org/TR/1999/REC-html401-19991224/frameset.dtd">
<HTML xmlns="http://www.w3.org/1999/xhtml">
<HEAD>
<script type="text/javascript">
	window.onload=function(){
		
		if(window.parent != window){// 如果是在框架中
			//就让框架页面跳转到登陆页面
			window.parent.location.href = "${pageContext.request.contextPath}/error.jsp";
		}
		
	};
</script>
</HEAD>


<BODY>

    异常信息：<s:property value="exception.message"/>
</BODY>
</HTML>
