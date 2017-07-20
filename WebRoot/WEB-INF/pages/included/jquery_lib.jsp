<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<script type="text/javascript" src="${path }/js/jquery/jquery-2.1.4.min.js"></script>
<script type="text/javascript" src="${path }/js/jquery/jquery-form.js"></script>
<script type="text/javascript" src="${path }/js/jquery/jquery-ui.js"></script>
<script type="text/javascript" src="${path }/js/jquery/jquery-common.js"></script>
<script type="text/javascript">
	Date.prototype.Format = function(fmt){ //author:   
		var o = {   
		  "M+" : this.getMonth()+1,                 //月份   
		  "d+" : this.getDate(),                    //日   
		  "h+" : this.getHours(),                   //小时   
		  "m+" : this.getMinutes(),                 //分   
		  "s+" : this.getSeconds(),                 //秒   
		  "q+" : Math.floor((this.getMonth()+3)/3), //季度   
		  "S"  : this.getMilliseconds()             //毫秒   
		};   
		if(/(y+)/.test(fmt))   
		  fmt=fmt.replace(RegExp.$1, (this.getFullYear()+"").substr(4 - RegExp.$1.length));   
		for(var k in o)   
		  if(new RegExp("("+ k +")").test(fmt))   
		fmt = fmt.replace(RegExp.$1, (RegExp.$1.length==1) ? (o[k]) : (("00"+ o[k]).substr((""+ o[k]).length)));   
		return fmt;   
	};
</script>




