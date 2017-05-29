<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Ajax Test</title>

<style>
#modDiv {
	width: 300px;
	height: 100px;
	background-color: skyblue;
	position: absolute;
	top: 50%;
	left: 50%;
	margin-top: -50px;
	margin-left: -150px;
	padding: 10px;
	z-index: 1000;
}

.pagination {
  width: 100%;
}

.pagination li{
  list-style: none;
  float: left; 
  padding: 3px; 
  border: 1px solid blue;
  margin:3px;  
}

.pagination li a{
  margin: 3px;
  text-decoration: none;  
}

</style>
</head>
<body>
	<!-- 댓글 수정화면 -->
	<div id='modDiv' style="display: none;">
		<div class='modal-title'></div>
		<div>
			<input type='text' id='replytext'>
		</div>
		<div>
			<button type="button" id="replyModBtn">Modify</button>
			<button type="button" id="replyDelBtn">DELETE</button>
			<button type="button" id='closeBtn'>Close</button>
		</div>
	</div>

	<h2>Ajax Test Page</h2>

	<div>
		<div>
			REPLYER <input type="text" name="replyer" id="newReplyWriter">
		</div>
		<div>
			REPLY TEXT <input type="text" name="replytext" id="newReplyText">
		</div>
		<button id="replyAddBtn">등록</button>
	</div>

	<ul id="replies"></ul>

	<ul class='pagination'></ul>

<!-- jQuery 2.1.4 -->
<script src="/resources/plugins/jQuery/jQuery-2.1.4.min.js"></script>

<script>
var board_idx = 59;

getPageList(1);

// $.getJSON("/replies/all/"+ board_idx, function(data) {
// 	var str = '';
// 	console.log(data.length);

// 	$(data).each(function() {
// 		str += "<li data-idx='"+this.idx+"' class=replyLi'>"
// 			+ this.idx + " : " + this.replytext
// 			+ "</li>";
// 	});

// 	$("#replies").html(str);
// });

// 전체댓글 조회
function getAllList() {
	$.getJSON("/replies/all/" + board_idx, function(data) {
		//console.log(data.length);
		var str = "";
		$(data).each(function() {
			str += "<li data-idx='"+this.idx+"' class='replyLi'>"
					+ this.idx + " : " + this.replytext
					+ "<button>수정</button></li>";
		});

		$("#replies").html(str);
	});
}

// 댓글 등록
$("#replyAddBtn").on("click", function() {
	var replyer = $("#newReplyWriter").val();
	var replytext = $("#newReplyText").val();

	$.ajax({
		type : 'post',
		url : '/replies',
		headers : {
			"Content-Type" : "application/json",
			"X-HTTP-Method-Override" : "POST"
		},
		dataType : 'text',
		data : JSON.stringify({
			board_idx : board_idx,
			replyer : replyer,
			replytext : replytext
		}),
		success : function(result) {
			if (result == 'SUCCESS') {
				alert("등록 되었습니다.");
				getAllList();
			}
		}
	});
});

// 댓글 수정 화면 열기
$("#replies").on("click", ".replyLi button", function() {
	var reply = $(this).parent();
	var idx = reply.attr("data-idx");
	var replytext = reply.text();
	console.log(replytext);
	$(".modal-title").html(idx);
	$("#replytext").val(replytext);
	$("#modDiv").show("slow");
});

// 댓글 삭제
$("#replyDelBtn").on("click", function() {
	var idx = $(".modal-title").html();
	var replytext = $("#replytext").val();

	$.ajax({
		type : 'delete',
		url : '/replies/' + idx,
		headers : {
			"Content-Type" : "application/json",
			"X-HTTP-Method-Override" : "DELETE"
		},
		dataType : 'text',
		success : function(result) {
			console.log("result: " + result);

			if (result == 'SUCCESS') {
				alert("삭제 되었습니다.");

				$("#modDiv").hide("slow");
				getAllList();
			}
		}
	});
});

// 댓글 수정 처리
$("#replyModBtn").on("click",function(){
	var idx = $(".modal-title").html();
	var replytext = $("#replytext").val();

	$.ajax({
		type:'put',
		url:'/replies/'+idx,
		headers: { 
			"Content-Type": "application/json",
			"X-HTTP-Method-Override": "PUT"
		},
		data:JSON.stringify({
			replytext:replytext
		}), 
		dataType:'text', 
		success:function(result){
			console.log("result: " + result);

			if(result == 'SUCCESS'){
				alert("수정 되었습니다.");

				$("#modDiv").hide("slow");
				//getAllList();
				getPageList(replyPage);
			}
		}
	});
});

// 댓글 수정 화면 닫기
$("#closeBtn").on("click",function(){
	$("#modDiv").hide("slow");
});

function getPageList(page){
	$.getJSON("/replies/" + board_idx + "/" + page, function(data) {
		console.log(data.list.length);

		var str ="";

		$(data.list).each(function() {
			str+= "<li data-idx='" + this.idx + "' class='replyLi'>" 
				+ this.idx + " : " + this.replytext
				+ "<button>수정</button></li>";
		});

		$("#replies").html(str);

		printPaging(data.pageMaker);
	});
}

function printPaging(pageMaker) {
	var str = "";

	if(pageMaker.prev) {
		str += "<li><a href='"+(pageMaker.startPage-1)+"'> << </a></li>";
	}

	for(var i=pageMaker.startPage, len = pageMaker.endPage; i <= len; i++) {
		var strClass= pageMaker.cri.page == i?'class=active' : '';
		str += "<li "+strClass+"><a href='"+i+"'>" + i + "</a></li>";
	}

	if(pageMaker.next){
		str += "<li><a href='" + (pageMaker.endPage + 1) + "'> >> </a></li>";
	}

	$('.pagination').html(str);
}

var replyPage = 1;

$(".pagination").on("click", "li a", function(event) {
	event.preventDefault();

	replyPage = $(this).attr("href");

	getPageList(replyPage);
});
</script>
</body>
</html>