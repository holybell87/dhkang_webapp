<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<style>
.fileDrop {
	width: 100%;
	height: 200px;
	border: 1px dotted blue;
}

small {
	margin-left: 3px;
	font-weight: bold;
	color: gray;
}
</style>

</head>
<body>

	<h3>Ajax File Upload</h3>
	<div class='fileDrop'></div>

	<div class='uploadedList'></div>

	<script src="//code.jquery.com/jquery-1.11.3.min.js"></script>
	<script>
	$(".fileDrop").on("dragenter dragover", function(event) {
		event.preventDefault();
	});

	$(".fileDrop").on("drop", function(event){
		event.preventDefault();

		var files = event.originalEvent.dataTransfer.files;
		var file = files[0];

		//console.log(file);
		//console.log(file.name);
		//console.log(file.size);
		//console.log(file.type);

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
				// data는 썸네일 파일명 또는 일반파일명
				var str ="";

				// 이미지 파일인 경우 (JPG/GIF/PNG/JPEG)
				if(checkImageType(data)) {
// 					str = "<div>"
// 						+"<img src='displayFile?fileName="+data+"'/>"
// 						+data+"</div>";

					str ="<div><a href=displayFile?fileName="+getImageLink(data)+" target='_blank'>"
						+"<img src='displayFile?fileName="+data+"'/>"
						+"</a><small data-src="+data+">X</small></div>";
				} else {
// 					str = "<div>"
// 						+ data
// 						+"</div>";

					str = "<div><a href='displayFile?fileName="+data+"'>" 
						+ getOriginalName(data)+"</a>"
						+"<small data-src="+data+">X</small></div></div>";
				}

 				$(".uploadedList").append(str);
			}
		});
	});

	function checkImageType(fileName){
		// 파일의 확장자가 존재하는지 체크 (i:대,소문자 구분 없음을 의미)
		var pattern = /jpg|gif|png|jpeg/i;

		return fileName.match(pattern);
	}

	function getOriginalName(fileName){	
		if(checkImageType(fileName)) {
			return;
		}

		var idx = fileName.indexOf("_") + 1 ;	// 원본파일명
		return fileName.substr(idx);
	}

	function getImageLink(fileName) {
		if(!checkImageType(fileName)) {
			return;
		}

		var front = fileName.substr(0, 12);	// '/년/월/일' 경로 추출
		var end = fileName.substr(14);		// 's_' 제거 (썸네일이 아닌 원본 파일명 추출)

		return front + end;
	}

	// 파일 삭제
	$(".uploadedList").on("click", "small", function(event){
		var that = $(this);

		$.ajax({
			url:"deleteFile",
			type:"post",
			data: {
				fileName: $(this).attr("data-src")
			},
			dataType:"text",
			success:function(result) {
				if(result == 'deleted'){
					that.parent("div").remove();
				}
			}
		});
	});
	</script>

</body>
</html>