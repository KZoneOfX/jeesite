<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="org.apache.shiro.web.filter.authc.FormAuthenticationFilter"%>
<%@ include file="/WEB-INF/views/include/taglib.jsp"%>
<html>
<head>
	<title>${fns:getConfig('productName')} 登录</title>
	<meta name="decorator" content="blank"/>
	<style type="text/css">
      html,body,table{background-color:#f5f5f5;width:100%;text-align:center;}.form-signin-heading{font-family:Helvetica, Georgia, Arial, sans-serif, 黑体;font-size:36px;margin-bottom:20px;color:#0663a2;}
      .form-signin{position:relative;text-align:left;width:300px;padding:25px 29px 29px;margin:0 auto 20px;background-color:#fff;border:1px solid #e5e5e5;
        	-webkit-border-radius:5px;-moz-border-radius:5px;border-radius:5px;-webkit-box-shadow:0 1px 2px rgba(0,0,0,.05);-moz-box-shadow:0 1px 2px rgba(0,0,0,.05);box-shadow:0 1px 2px rgba(0,0,0,.05);}
      .form-signin .checkbox{margin-bottom:10px;color:#0663a2;} .form-signin .input-label{font-size:16px;line-height:23px;color:#999;}
      .form-signin .input-block-level{font-size:16px;height:auto;margin-bottom:15px;padding:7px;*width:283px;*padding-bottom:0;_padding:7px 7px 9px 7px;}
      .form-signin .btn.btn-large{font-size:16px;} .form-signin #themeSwitch{position:absolute;right:15px;bottom:10px;}
      .form-signin div.validateCode {padding-bottom:15px;} .mid{vertical-align:middle;}
      .header{height:80px;padding-top:20px;} .alert{position:relative;width:300px;margin:0 auto;*padding-bottom:0px;}
      label.error{background:none;width:270px;font-weight:normal;color:inherit;margin:0;}
	  .hidden{ display:none}
	  /*评论浮动层*/
	  #smallLay{width:498px; height:600px;padding:4px 10px 10px;background-color:#FFFFFF;border:1px solid #05549d;color:#333333;line-height:24px;text-align:left;-webkit-box-shadow:5px 2px 6px #000;-moz-box-shadow:3px 3px 6px #555;}

	</style>
	<script type="text/javascript">
		//判断浏览器ie6创建的div的样式和非ie6创建的div的样式
		//创建div
		function showid() {
			var isIE = (document.all) ? true : false;
			var isIE6 = isIE && ([/MSIE (\d)\.0/i.exec(navigator.userAgent)][0][1] == 6);

            var newbox = document.getElementById("smallLay");
			newbox.style.zIndex = "9999";
			newbox.style.display = "block"
			newbox.style.position = !isIE6 ? "fixed" : "absolute";
			newbox.style.top = newbox.style.left = "50%";
			newbox.style.marginTop = -newbox.offsetHeight / 2 + "px";
			newbox.style.marginLeft = -newbox.offsetWidth / 2 + "px";
			var jihualayer = document.createElement("div");
			jihualayer.id = "jihualayer";
			jihualayer.style.width = jihualayer.style.height = "100%";
			jihualayer.style.position = !isIE6 ? "fixed" : "absolute";
			jihualayer.style.top = jihualayer.style.left = 0;
			jihualayer.style.backgroundColor = "#000";
			jihualayer.style.zIndex = "9998";
			jihualayer.style.opacity = "0.6";
			document.body.appendChild(jihualayer);
			var sel = document.getElementsByTagName("select");
			for (var i = 0; i < sel.length; i++) {
				sel[i].style.visibility = "hidden";
			}
			function jihualayer_iestyle() {
				jihualayer.style.width = Math.max(document.documentElement.scrollWidth, document.documentElement.clientWidth)
						+ "px";
				jihualayer.style.height = Math.max(document.documentElement.scrollHeight, document.documentElement.clientHeight) +
						"px";
			}
			function newbox_iestyle() {
				newbox.style.marginTop = document.documentElement.scrollTop - newbox.offsetHeight / 2 + "px";
				newbox.style.marginLeft = document.documentElement.scrollLeft - newbox.offsetWidth / 2 + "px";
			}
			if (isIE) { jihualayer.style.filter = "alpha(opacity=60)"; }
			if (isIE6) {
				jihualayer_iestyle()
				newbox_iestyle();
				window.attachEvent("onscroll", function () {
					newbox_iestyle();
				})
				window.attachEvent("onresize", jihualayer_iestyle)
			}
			jihualayer.onclick = function () {
				newbox.style.display = "none"; jihualayer.style.display = "none"; for (var i = 0; i < sel.length; i++) {
					sel[i].style.visibility = "visible";
				}
			}
		}

		$(document).ready(function() {
            $("#loginForm").validate({
                rules: {
                    validateCode: {remote: "${pageContext.request.contextPath}/servlet/validateCodeServlet"}
                },
                messages: {
                    username: {required: "请填写用户名."},password: {required: "请填写密码."},
                    validateCode: {remote: "验证码不正确.", required: "请填写验证码."}
                },
                errorLabelContainer: "#messageBox",
                errorPlacement: function(error, element) {
                    error.appendTo($("#loginError").parent());
                }
            });

			$("#register_btn").bind("click",function(){
				showid();
			});

			$("#btnSubmitRegister").bind("click",function(){
				var name = $("#name").val();
                var class_no = $("#class_no").val();
                var university_name = $("#university_name").val();
                var major_name = $("#major_name").val();
                var email = $("#email").val();
                var qq = $("#qq").val();
                var wechat = $("#weChat").val();
                var mobile = $("#mobile").val();
                var classTeacherName = $("#classTeacherName").val();
				if (name.length == 0||
				class_no.length == 0||
				university_name.length == 0||
				major_name.length ==0 ||
				classTeacherName.length ==0 ||
				email.length == 0){
					alert("注册信息不得为空");
					return;
				}
				var url = "${ctx}/register";
//				TODO 完成注册
				$.ajax({
					type: "POST",
					url: url,
					data: {
						"name":name,
						"class_no":class_no,
						"university_name":university_name,
						"major_name":major_name,
						"email":email,
						"qq":qq,
						"wechat":wechat,
						"classTeacherName":classTeacherName,
						"mobile":mobile

					},
					async: false,
					dataType: "json",
					success: function (data) {
						console.log(data);
						if (data.code == "1000") {
							alert(data.message);
//							if (data.isEdit) {
//								alert("订单更新成功，1秒后跳转订单列表页。");
//
////                        setTimeout("javascript:location.href=serverDomain+'/views/Node/order/orderInquiry.html'", 500);
////								setTimeout("javascript:history.go(-1);", 500);
//							} else {
//								alert("订单更新失败，请稍候再试。");
//							}
						} else {
							alert("订单更新失败，请稍候再试。");
						}
					},
					error: function () {
						alert("发生网络错误，请稍候再试。");
					}
				});
			});
		});
		// 如果在框架或在对话框中，则弹出提示并跳转到首页
		if(self.frameElement && self.frameElement.tagName == "IFRAME" || $('#left').length > 0 || $('.jbox').length > 0){
			alert('未登录或登录超时。请重新登录，谢谢！');
			top.location = "${ctx}";
		}
	</script>
</head>
<body>
	<!--[if lte IE 6]><br/><div class='alert alert-block' style="text-align:left;padding-bottom:10px;"><a class="close" data-dismiss="alert">x</a><h4>温馨提示：</h4><p>你使用的浏览器版本过低。为了获得更好的浏览体验，我们强烈建议您 <a href="http://browsehappy.com" target="_blank">升级</a> 到最新版本的IE浏览器，或者使用较新版本的 Chrome、Firefox、Safari 等。</p></div><![endif]-->
	<div class="header">
		<div id="messageBox" class="alert alert-error ${empty message ? 'hide' : ''}"><button data-dismiss="alert" class="close">×</button>
			<label id="loginError" class="error">${message}</label>
		</div>
	</div>
	<h1 class="form-signin-heading">${fns:getConfig('productName')}</h1>
	<form id="loginForm" class="form-signin" action="${ctx}/login" method="post">
		<label class="input-label" for="username">登录名</label>
		<input type="text" id="username" name="username" class="input-block-level required" value="${username}">
		<label class="input-label" for="password">密码</label>
		<input type="password" id="password" name="password" class="input-block-level required">
		<c:if test="${isValidateCodeLogin}"><div class="validateCode">
			<label class="input-label mid" for="validateCode">验证码</label>
			<sys:validateCode name="validateCode" inputCssStyle="margin-bottom:0;"/>
		</div></c:if><%--
		<label for="mobile" title="手机登录"><input type="checkbox" id="mobileLogin" name="mobileLogin" ${mobileLogin ? 'checked' : ''}/></label> --%>
		<input  id="login_btn" class="btn btn-large btn-primary" type="submit" value="登 录"/>&nbsp;&nbsp;
		<input type="button" id="register_btn" style="background: #00B83F;position:absolute;right:15px;" class="btn btn-large btn-primary" value="注册"/></br>
		<label for="rememberMe" title="下次不需要再登录"><input type="checkbox" id="rememberMe" name="rememberMe" ${rememberMe ? 'checked' : ''}/> 记住我（公共场所慎用）</label>
		<div id="themeSwitch" class="dropdown">
			<a class="dropdown-toggle" data-toggle="dropdown" href="#">${fns:getDictLabel(cookie.theme.value,'theme','默认主题')}<b class="caret"></b></a>
			<ul class="dropdown-menu">
			  <c:forEach items="${fns:getDictList('theme')}" var="dict"><li><a href="#" onclick="location='${pageContext.request.contextPath}/theme/${dict.value}?url='+location.href">${dict.label}</a></li></c:forEach>
			</ul>
			<!--[if lte IE 6]><script type="text/javascript">$('#themeSwitch').hide();</script><![endif]-->
		</div>
	</form>
	<div class="footer">
		Copyright &copy; 2012-${fns:getConfig('copyrightYear')} <a href="${pageContext.request.contextPath}${fns:getFrontPath()}">${fns:getConfig('productName')}</a> - Powered By <a href="http://jeesite.com" target="_blank">JeeSite</a> ${fns:getConfig('version')} 
	</div>

    <!--收藏浮层开始-->
	<div id="smallLay" style="display:none" >
        <%--<form:form id="inputForm" modelAttribute="user" action="${ctx}/sys/user/info" method="post" class="form-horizontal">--%>
            <%--<sys:message content="${message}"/>--%>

            <div class="control-group">
                <label class="control-label">姓名:</label>
                <div class="controls">
                    <input id="name" name="name"  maxlength="50" class="required" value="test" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">辛中毕业班级:</label>
                <div class="controls">
                    <input id="class_no" name="class_no"  maxlength="50" class="required" value="test" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">大学就读学校:</label>
                <div class="controls">
                    <input id="university_name" name="university_name"  maxlength="50" class="required" value="test" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">大学就读专业:</label>
                <div class="controls">
                    <input id="major_name" name="major_name"  maxlength="50" class="required" value="test" />
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">邮箱:</label>
                <div class="controls">
                    <input id="email" name="email" maxlength="50" class="email" value="zhangxk@outlook.com"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">QQ:</label>
                <div class="controls">
                    <input id="qq" name="qq"  maxlength="50" value="604547487"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">微信:</label>
                <div class="controls">
                    <input id="weChat" name="wechat"  maxlength="50" value="hello"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">手机:</label>
                <div class="controls">
                    <input id="mobile" name="mobile"  maxlength="50" value="18202426984"/>
                </div>
            </div>
            <div class="control-group">
                <label class="control-label">辛中班主任姓名:</label>
                <div class="controls">
                    <input id="classTeacherName" name="classTeacherName"  maxlength="50" value="test"/>
                </div>
            </div>
            <div class="form-actions">
                <input id="btnSubmitRegister" class="btn btn-primary" type="submit" value="提 交"/>
            </div>
        <%--</form:form>--%>
	</div>
    <!--收藏浮层结束-->
	<script src="${ctxStatic}/flash/zoom.min.js" type="text/javascript"></script>
</body>
</html>