<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    <c:set var="ctx" value="/webTest"/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script src="${ctx }/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script src="${ctx }/js/jquery.media.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function () {
	$.fn.media.defaults.flvPlayer = '<%=basepath %>/mv/mediaplayer.swf';
	$.fn.media.defaults.mp3Player = '<%=basepath %>/mv/mediaplayer.swf';
	$('a.media').media({ width: 500, height: 400, autoplay: true });
  });
</script>
</head>
<body>
	<c:choose>
		   	         <c:when test='${fn:indexOf(upFile.fileSaveName,".swf") != -1}'>
		   	         	<EMBED  src="videoFiles/${upFile.fileSaveName}" width=500 height=400 autostart=true >   	         
		   	         </c:when>
		   	         <c:otherwise>
			   	       <a  class="media" href="videoFiles/${upFile.fileSaveName}">&nbsp;</a>	   	         
		   	         </c:otherwise>	
	   	         </c:choose>   	

</body>
</html>