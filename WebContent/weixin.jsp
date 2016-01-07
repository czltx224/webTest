<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Insert title here</title>
<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script language="javascript" type="text/javascript">
wx.config({
    debug: true,//这里是开启测试，如果设置为true，则打开每个步骤，都会有提示，是否成功或者失败
    appId: 'wx636a2adbdf8d9250',
    timestamp: '14999923234',//这个一定要与上面的php代码里的一样。
    nonceStr: '14999923234',//这个一定要与上面的php代码里的一样。
    signature: '<?=jssdk();?>',
    jsApiList: [
      // 所有要调用的 API 都要加到这个列表中
        'onMenuShareTimeline',
        'onMenuShareAppMessage',
        'onMenuShareQQ',
        'onMenuShareWeibo'
    ]
});
wx.ready(function () {
    wx.onMenuShareTimeline({
        title: "<?=$act['act_name']?>", // 分享标题
        link: "http://www.brandhd.com/v/events/view/<?=$act['act_id']?>", // 分享链接
        imgUrl: "http://www.brandhd.com<?=$act['act_poster_small']?>", // 分享图标
        success: function () { 
            // 用户确认分享后执行的回调函数
        },
        cancel: function () { 
            // 用户取消分享后执行的回调函数
        }
    });
    wx.onMenuShareAppMessage({
        title: "<?=$act['act_name']?>", // 分享标题
        desc: "<?=substr($act['act_stime'],0,10)?><?=$act['act_place']?>", // 分享描述
        link: "http://www.brandhd.com/v/events/view/<?=$act['act_id']?>", // 分享链接
        imgUrl: "http://www.brandhd.com<?=$act['act_poster_small']?>", // 分享图标
        type: '', // 分享类型,music、video或link，不填默认为link
        dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
        success: function () { 
            // 用户确认分享后执行的回调函数
        },
        cancel: function () { 
            // 用户取消分享后执行的回调函数
        }
    });
    wx.onMenuShareQQ({
        title: "<?=$act['act_name']?>", // 分享标题
        desc: "<?=substr($act['act_stime'],0,10)?>\n<?=$act['act_place']?>", // 分享描述
        link: "http://www.brandhd.com/v/events/view/<?=$act['act_id']?>", // 分享链接
        imgUrl: "http://www.brandhd.com<?=$act['act_poster_small']?>", // 分享图标
        success: function () { 
           // 用户确认分享后执行的回调函数
        },
        cancel: function () { 
           // 用户取消分享后执行的回调函数
        }
    });
    wx.onMenuShareWeibo({
        title: "<?=$act['act_name']?>", // 分享标题
        desc: "<?=substr($act['act_stime'],0,10)?>\n<?=$act['act_place']?>", // 分享描述
        link: "http://www.brandhd.com/v/events/view/<?=$act['act_id']?>", // 分享链接
        imgUrl: "http://www.brandhd.com<?=$act['act_poster_small']?>", // 分享图标
        success: function () { 
           // 用户确认分享后执行的回调函数
        },
        cancel: function () { 
            // 用户取消分享后执行的回调函数
        }
    });
});
</script>
</head>
<body>
	<a>微信分享</a>
</body>
</html>