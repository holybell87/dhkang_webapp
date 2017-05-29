<%--------------------------------------------------------------------------------
	* [dhkang_webapp] 로그인 화면
	* coding: utf-8
	* tab-size: 4
	* 
	* created by Kang Donghyeok
	* last modified @ 2017.05.22
--------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>AdminLTE 2 | Log in</title>

<!-- Tell the browser to be responsive to screen width -->
<meta content='width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no' name='viewport'>

<!-- Bootstrap 3.3.4 -->
<link href="/resources/bootstrap/css/bootstrap.min.css" rel="stylesheet" type="text/css" />

<!-- Font Awesome Icons -->
<link href="https://maxcdn.bootstrapcdn.com/font-awesome/4.3.0/css/font-awesome.min.css" rel="stylesheet" type="text/css" />

<!-- Theme style -->
<link href="/resources/dist/css/AdminLTE.min.css" rel="stylesheet" type="text/css" />

<!-- iCheck -->
<link href="/resources/plugins/iCheck/square/blue.css" rel="stylesheet" type="text/css" />

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
</head>

<body class="login-page">
	<div class="login-box">
		<div class="login-logo">
			<a href="/resources/index2.html"><b>dhkang</b>Project</a>
		</div>
		<!-- /.login-logo -->
		<div class="login-box-body">
			<p class="login-box-msg">Sign in to start your session</p>

<!-- 			<form action="/user/loginPost" method="post"> -->
			<form role='form' method="post">
				<div class="form-group has-feedback">
					<input type="text" id="uid" name="uid" class="form-control" placeholder="USER ID"/>
					<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
				</div>
				<div class="form-group has-feedback">
					<input type="password" id="upw" name="upw" class="form-control" placeholder="Password"/>
					<span class="glyphicon glyphicon-lock form-control-feedback"></span>
				</div>
				<div class="row">
					<div class="col-xs-8">
						<div class="checkbox icheck">
							<label><input type="checkbox" name="useCookie"> Remember Me</label>
						</div>
					</div>
					<div class="col-xs-4">
						<button type="submit" class="btn btn-primary btn-block btn-flat" id="loginBtn">Sign In</button>
					</div>
				</div>
			</form>

			<a href="#">I forgot my password</a><br>
			<a href="/user/register" class="text-center">Register a new membership</a>

		</div>
		<!-- /.login-box-body -->
	</div>
	<!-- /.login-box -->

<!-- jQuery 2.1.4 -->
<script src="/resources/plugins/jQuery/jQuery-2.1.4.min.js"></script>

<!-- Bootstrap 3.3.2 JS -->
<script src="/resources/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>

<!-- iCheck -->
<script src="/resources/plugins/iCheck/icheck.min.js" type="text/javascript"></script>
<script>
$(function () {
	$('input').iCheck({
		checkboxClass: 'icheckbox_square-blue',
		radioClass: 'iradio_square-blue',
		increaseArea: '20%' // optional
	});
});

//var formObj = $("form[role='form']");

//console.log(formObj);

//$(".btn-primary").on("click", function() {
$("#loginBtn").on("click", function(){
	event.preventDefault();

	if(!chkField()) {
		return;
	}

// 	formObj.attr("method", "post");
// 	formObj.attr("action", "/user/loginPost");
// 	formObj.submit();

	$.ajax({
		type:'post',
		url:'/user/loginPost',
		headers: {
			"Content-Type": "application/json",
			"X-HTTP-Method-Override": "POST"
		},
		dataType:'JSON',	// return type
		data: JSON.stringify({
			uid: $.trim($("#uid").val()),
			upw: $.trim($("#upw").val()),
			useCookie: $('[name="useCookie"]').prop('checked')
		}),
		success:function(result) {
			//console.log(result); // {"result":"SUCCESS","dest":"/board/register"}
			var MSG = result.result;

			if(MSG == 'SUCCESS'){
				var dest = result.dest;
				self.location = dest;
			} else if(MSG == 'FAIL') {
				alert("등록된 정보가 없습니다.");
			} else if(MSG == 'UNKNOWN') {
				alert("알수없는 오류 발생.");
			} else {
				alert('unknown error');
			}
		}
	});
});

function chkField() {
	if($.trim($("#uid").val()) == "") {
		alert('아이디를 입력해주세요.');
		$("#uid").focus();
		return false;
	}

	if($.trim($("#upw").val()) == "") {
		alert('비밀번호를 입력해주세요.');
		$("#upw").focus();
		return false;
	}

	return true;
}
</script>

</body>
</html>