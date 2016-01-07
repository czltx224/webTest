<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="ctx" value="/webTest" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<style type="text/css">
body, div {
	margin: 0;
	padding: 0;
}
</style>
<script src="${ctx }/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script type="text/javascript">
	function loadVideo() {
		var width = $(document).width();
		var height = $(document).height();

		var html = '<video width="'+width+'px" height="'+height+'px" autoplay="autoplay" loop="loop" >'
				+ '<source src="/webTest/mv/mv.mp4" type="video/mp4">'
				+ 'Your browser does not support the video tag.' + '</video>';

		$('#mainDiv').append(html);
		fullScreen();
		// window.open(document.location, 'big', 'fullscreen=yes');
	}
	function fullScreen() {
		var el = document.documentElement;
		var rfs = el.requestFullScreen || el.webkitRequestFullScreen
				|| el.mozRequestFullScreen || el.msRequestFullScreen;
		if (typeof rfs != "undefined" && rfs) {
			rfs.call(el);
		} else if (typeof window.ActiveXObject != "undefined") {
			//for IE，这里其实就是模拟了按下键盘的F11，使浏览器全屏
			var wscript = new ActiveXObject("WScript.Shell");
			if (wscript != null) {
				wscript.SendKeys("{F11}");
			}
		} else {
			console.log("no");
		}
	}
	function exitFullScreen() {
		var el = document;
		var cfs = el.cancelFullScreen || el.webkitCancelFullScreen
				|| el.mozCancelFullScreen || el.exitFullScreen;
		if (typeof cfs != "undefined" && cfs) {
			cfs.call(el);
		} else if (typeof window.ActiveXObject != "undefined") {
			//for IE，这里和fullScreen相同，模拟按下F11键退出全屏
			var wscript = new ActiveXObject("WScript.Shell");
			if (wscript != null) {
				wscript.SendKeys("{F11}");
			}
		}
	}
</script>
</head>
<body onload="loadVideo();">
	<div style="width: 100%; height: 100%;" id="mainDiv"></div>
</body>
</html>