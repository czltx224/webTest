<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
     <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <c:set var="ctx" value="/webTest"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<style type="text/css">
body,div{ margin:0; padding:0;}
</style>
<script src="${ctx }/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script type="text/javascript">
	function loadVideo(){
		var num = 16
		var width = $(document).width();
		var divWidth = parseInt(width/4 - 10);
		
		for(var i=0;i<num;i++){
			var html = '<video width="'+divWidth+'px" height="auto" autoplay="autoplay">'
			+'<source src="/webTest/mv/mv.mp4" type="video/mp4">'
			+'Your browser does not support the video tag.'
			+'</video>';
			
			$('#mainDiv').append(html);
		}
		// window.open(document.location, 'big', 'fullscreen=yes');
	}
</script>
</head>
<body onload="loadVideo();">
<div style="width: 100%;height: 100%;" id="mainDiv">
</div>
</body>
</html>