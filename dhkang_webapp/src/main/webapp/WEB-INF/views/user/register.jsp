<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<title>AdminLTE 2 | Registration Page</title>

<!-- Tell the browser to be responsive to screen width -->
<meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">

<!-- Bootstrap 3.3.6 -->
<link rel="stylesheet" href="/resources/bootstrap/css/bootstrap.min.css">

<!-- Font Awesome -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.5.0/css/font-awesome.min.css">

<!-- Ionicons -->
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/ionicons/2.0.1/css/ionicons.min.css">

<!-- Theme style -->
<link rel="stylesheet" href="/resources/dist/css/AdminLTE.min.css">

<!-- iCheck -->
<link rel="stylesheet" href="/resources/plugins/iCheck/square/blue.css">

<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
<script src="https://oss.maxcdn.com/html5shiv/3.7.3/html5shiv.min.js"></script>
<script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
<![endif]-->
</head>
<body class="hold-transition register-page">
<div class="register-box">
	<div class="register-logo">
		<a href="/"><b>dhkang</b>Project</a>
	</div>

	<div class="register-box-body">
		<p class="login-box-msg">Register a new membership</p>

		<form role='form' method="post">
			<div class="form-group has-feedback">
				<input type="text" class="form-control" id="uname" name="uname" placeholder="Full name">
				<span class="glyphicon glyphicon-user form-control-feedback"></span>
			</div>
			<div class="form-group has-feedback">
				<input type="email" class="form-control" id="uid" name="uid" placeholder="Email">
				<span class="glyphicon glyphicon-envelope form-control-feedback"></span>
			</div>
			<div class="form-group has-feedback">
				<input type="password" class="form-control" id="upw" name="upw" placeholder="Password">
				<span class="glyphicon glyphicon-lock form-control-feedback"></span>
			</div>
			<div class="form-group has-feedback">
				<input type="password" class="form-control" id="reupw" name="reupw" placeholder="Retype password">
				<span class="glyphicon glyphicon-log-in form-control-feedback"></span>
			</div>
			<div class="row">
				<div class="col-xs-8">
					<div class="checkbox icheck">
						<label>
							<input type="checkbox" name="chkterms"> I agree to the <a href="#">terms</a>
						</label>
					</div>
				</div>
				<!-- /.col -->
				<div class="col-xs-4">
					<button type="submit" class="btn btn-primary btn-block btn-flat" id="regBtn">Register</button>
				</div>
				<!-- /.col -->
			</div>
		</form>

		<div class="social-auth-links text-center">
			<p>- OR -</p>
			<a href="javascript:alert('No service!');" class="btn btn-block btn-social btn-facebook btn-flat"><i class="fa fa-facebook"></i> Sign up using
				Facebook</a>
			<a href="javascript:alert('No service!');" class="btn btn-block btn-social btn-google btn-flat"><i class="fa fa-google-plus"></i> Sign up using
				Google+</a>
		</div>

		<a href="/user/login" class="text-center">I already have a membership</a>
	</div>
	<!-- /.form-box -->
</div>
<!-- /.register-box -->

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

var formObj = $("form[role='form']");

//console.log(formObj);

$("#regBtn").on("click", function(){
	event.preventDefault();

	if(!chkField()) {
		return;
	}

// 	formObj.attr("method", "post");
// 	formObj.attr("action", "/user/registerPost");
// 	formObj.submit();

	$.ajax({
		type:'post',
		url:'/user/registerPost',
		headers: { 
			"Content-Type": "application/json",
			"X-HTTP-Method-Override": "POST"
		},
		dataType:'text',
		processData: false,
		contentType: false,
		data: JSON.stringify({
			uname: $.trim($("#uname").val()),
			uid: $.trim($("#uid").val()),
			upw: $.trim($("#upw").val())
		}),
		success:function(result) {
			console.log("result: " + result);

			if(result == 'SUCCESS'){
				alert("회원가입 되었습니다.");

				formObj.attr("method", "post");
				formObj.attr("action", "/user/loginPost");
				formObj.submit();
			} else if(result == 'FAIL') {
				alert("이미 등록된 회원정보가 있습니다.");
			}
		}
	});
});

function chkField() {
	if($.trim($("#uname").val()) == "") {
		alert('이름을 입력해주세요.');
		$("#uname").focus();
		return false;
	}

	if($.trim($("#uid").val()) == "") {
		alert('이메일(아이디)을 입력해주세요.');
		$("#uid").focus();
		return false;
	}

	var exptext = /^[A-Za-z0-9_\.\-]+@[A-Za-z0-9\-]+\.[A-Za-z0-9\-]+/;
	if(exptext.test($.trim($("#uid").val())) == false) {
		// 이메일 형식이 알파벳+숫자@알파벳+숫자.알파벳+숫자 형식이 아닐경우
		alert("이메일 형식이 올바르지 않습니다.");
		$("#uid").focus();
		return false;
	}

	if($.trim($("#upw").val()) == "") {
		alert('비밀번호를 입력해주세요.');
		$("#upw").focus();
		return false;
	}

	var pattern1 = /[0-9]/;						// 숫자
	var pattern2 = /[a-zA-Z]/;					// 문자
	var pattern3 = /[~!@#$%^&*()_+|<>?:{}]/;	// 특수문자
	var IDPASS = $.trim($("#upw").val());

	if(!pattern1.test(IDPASS) || !pattern2.test(IDPASS) || !pattern3.test(IDPASS) || IDPASS.length < 8){
		alert("비밀번호는 8자리 이상 문자, 숫자, 특수문자로 구성하여야 합니다.");
		$("#upw").focus();
		return false;
	}

	if($.trim($("#reupw").val()) == "") {
		alert('비밀번호 확인란을 입력해주세요.');
		$("#reupw").focus();
		return false;
	}

	if($.trim($("#upw").val()) != $.trim($("#reupw").val())) {
		alert('비밀번호가 일치하지 않습니다.');
		$("#reupw").focus();
		return false;
	}

	var obj = document.getElementsByName("chkterms");

	for(var i=0; i<obj.length; i++) {
		if(obj[i].checked == false) {
			alert('약관에 동의해주세요.');
			obj[i].focus();
			return false;
		}
	}

	return true;
}
</script>
</body>
</html>
