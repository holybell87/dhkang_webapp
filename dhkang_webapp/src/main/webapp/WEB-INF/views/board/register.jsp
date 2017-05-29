<%--------------------------------------------------------------------------------
	* [dhkang_webapp] 게시글 등록화면
	* coding: utf-8
	* tab-size: 4
	* 
	* created by Kang Donghyeok
	* last modified @ 2017.05.19
--------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%@ include file="../include/header.jsp" %>

<style>
.fileDrop {
  width: 80%;
  height: 100px;
  border: 1px dotted gray;
  background-color: #EAEAEA;
  margin: auto;
  text-align: center;
  line-height: 100px;
}

.overtitle {
  max-width:200px;
  overflow:hidden;
  white-space:nowrap;
  text-overflow:ellipsis;
}
</style>

	<!-- Main content -->
	<section class="content">
		<div class="row">
			<!-- left column -->
			<div class="col-md-12">
				<!-- general form elements -->
				<div class="box box-primary">
					<div class="box-header">
						<h3 class="box-title">REGISTER BOARD</h3>
					</div>
					<!-- /.box-header -->

<!-- 					<form id="registerForm" action="register" role="form" method="post"> -->
					<form role="form" method="post">
						<div class="box-body">
							<div class="form-group">
								<label for="exampleInputEmail1">Title</label> 
								<input type="text" id="title" name='title' class="form-control" placeholder="Enter Title">
							</div>
							<div class="form-group">
								<label for="exampleInputPassword1">Content</label>
								<textarea class="form-control" id="content" name="content" rows="10" placeholder="Enter ..."></textarea>
							</div>
							<div class="form-group">
								<label for="exampleInputEmail1">Writer</label> 
								<input type="text" id="writer" name="writer" class="form-control" placeholder="Enter Writer" value="${login.uid}" readonly>
							</div>
							<div class="form-group">
								<label for="exampleInputEmail1">File DROP Here</label>
								<div class="fileDrop">마우스로 드래그해서 파일을 첨부해주세요. (파일크기 최대 10MB)</div>
							</div>
						</div>
						<!-- /.box-body -->

						<div class="box-footer">
							<div>
								<hr>
							</div>

							<ul class="mailbox-attachments clearfix uploadedList">
							</ul>

							<button type="submit" class="btn btn-primary" id="regBtn">Submit</button>
							<button type="submit" class="btn btn-primary" id="goListBtn">LIST ALL</button>
						</div>
					</form>

				</div>
				<!-- /.box -->
			</div>
			<!--/.col (left) -->

		</div>
		<!-- /.row -->
	</section>
	<!-- /.content -->

</div>
<!-- /.content-wrapper -->

<script type="text/javascript" src="/resources/js/upload.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>

<script id="template" type="text/x-handlebars-template">
<li>
	<span class="mailbox-attachment-icon has-img"><img src="{{imgsrc}}" alt="Attachment"></span>
	<div class="mailbox-attachment-info overtitle">
		<a href="{{getLink}}" target="_blank" class="mailbox-attachment-name">{{fileName}}</a>
		<a href="{{fullName}}" class="btn btn-default btn-xs pull-right delbtn">
			<i class="fa fa-fw fa-remove"></i>
		</a>
	</div>
</li>
</script>

<script>
var template = Handlebars.compile($("#template").html());

$(".fileDrop").on("dragenter dragover", function(event){
	event.preventDefault();
});

$(".fileDrop").on("drop", function(event) {
	event.preventDefault();

	var files = event.originalEvent.dataTransfer.files;
	var file = files[0];

	// 이미지 파일
	if(checkImageType(file.name)) {
		console.log('이미지 파일');
	} else {
		console.log('일반 파일');
	}

	// 첨부파일 용량 체크
	if(!checkImgSize(file, 10)) {
		return;
	}

	var formData = new FormData();
	formData.append("file", file);

	$.ajax({
		url: '/uploadAjax',
		data: formData,
		dataType:'text',
		processData: false,
		contentType: false,
		type: 'POST',
		success: function(data) {
			var fileInfo = getFileInfo(data);
			var html = template(fileInfo);
			//console.log(fileInfo);

			$(".uploadedList").append(html);
		}
	});	
});

// 첨부파일 X 선택
$(".uploadedList").on("click", ".delbtn", function(event) {
	event.preventDefault();

	if(confirm('첨부파일을 삭제하시겠습니까?')) {
		var arr = [];
		$(".uploadedList li").each(function(index){
			arr.push($(this).attr("data-src"));
		});

		if(arr.length > 0){
			$.post("/deleteAllFiles",{files:arr}, function(){
	
			});
		}

		$(".uploadedList").empty();
	}
});

// 글 등록
// $("#registerForm").submit(function(event) {
// 	event.preventDefault();

// 	var that = $(this);
// 	var str ="";

// 	// 첨부파일 파일명
// 	$(".uploadedList .delbtn").each(function(index) {
// 		str += "<input type='hidden' name='files["+index+"]' value='"+$(this).attr("href") +"'> ";
// 	});

// 	that.append(str);
// 	that.get(0).submit();
// });

var formObj = $("form[role='form']");

// 등록
$("#regBtn").on("click", function(){
	event.preventDefault();

	if(!chkField()) {
		return;
	}

	//var that = $(this);
	var that = formObj;
	var str ="";

	// 첨부파일 파일명
	$(".uploadedList .delbtn").each(function(index) {
		str += "<input type='hidden' name='files["+index+"]' value='"+$(this).attr("href") +"'> ";
	});

	that.append(str);
	that.get(0).submit();
});

// 리스트 화면으로 이동
$("#goListBtn").on("click", function(){
	formObj.attr("method", "get");
	formObj.attr("action", "/board/list");
	formObj.submit();
});

// 첨부파일 용량 확인
function checkImgSize(file, size){
	var check = false;

	var sizeinbytes = file.size;	// 파일의 사이즈
	var fSExt = new Array('Bytes', 'KB', 'MB', 'GB');
	var i=0;
	var checkSize = size*1024*1024;	// 바이트
	while(checkSize>900) {
		checkSize/=1024;
		i++;
	}

	checkSize = (Math.round(checkSize*100)/100)+' '+fSExt[i]; 

	var fSize = sizeinbytes;
	if(fSize > (size*1024*1024)) {
		alert("첨부파일은 "+ checkSize + " 이하로 등록가능합니다.");
		check = false;
	} else {
		check = true;
	}

	return check;
}

function chkField() {
	if($.trim($("#title").val()) == "") {
		alert('제목을 입력해주세요.');
		$("#title").focus();
		return false;
	}

	if($.trim($("#content").val()) == "") {
		alert('내용을 입력해주세요.');
		$("#content").focus();
		return false;
	}

	if($.trim($("#writer").val()) == "") {
		alert('등록자를 입력해주세요.');
		$("#writer").focus();
		return false;
	}

	return true;
}
</script>

<%@ include file="../include/footer.jsp" %>