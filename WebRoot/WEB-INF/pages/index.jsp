<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="included/path_lib.jsp"%>
<%@ include file="included/jquery_lib.jsp"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
  
  <body>
    	<input id="id" type="text">
    	<button onclick="text_exception();">测试异常</button>
    	<button onclick="text_exception2();">测试异常2</button>
    	<script type="text/javascript">
    		function text_exception(){
    			var url="${path}/exception.action";
    			$.ajax({
    				url: url,
    				data: {"id":$("#id").val()},
    				type: "get",
    				dataType: "json",
    				success: function(data){
    					if(data.success){
    						alert(data.message);
    					}else{
    						alert("失败");
    						if(data.herf!=null){
    							window.location.href=data.herf;
    						}
    					}
    				},
    				error: function(XMLHttpRequest,textstatus){
    					var message="message:"+XMLHttpRequest.responseText+
    						"\n"+"status:"+XMLHttpRequest.status+
    						"\n"+"readyState:"+XMLHttpRequest.readyState+
    						"\n"+"textstatus:"+textstatus;
   						alert(message);
    				}
    			});
    		}
    		function text_exception2(){
    			window.location.href="${path}/exception.action?id="+$("#id").val();
    		}
    	</script>
  </body>
</html>
