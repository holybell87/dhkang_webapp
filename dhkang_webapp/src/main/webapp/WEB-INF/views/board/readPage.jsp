<%--------------------------------------------------------------------------------
	* [dhkang_webapp] 게시글 상세화면
	* coding: utf-8
	* tab-size: 4
	* 
	* created by Kang Donghyeok
	* last modified @ 2017.05.19
--------------------------------------------------------------------------------%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<%@ include file="../include/header.jsp" %>
<script type="text/javascript" src="/resources/js/upload.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/3.0.1/handlebars.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.0/moment.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.18.0/locale/ko.js"></script>

<style type="text/css">
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
						<h3 class="box-title">READ BOARD</h3>
					</div>
					<!-- /.box-header -->

					<form role="form" method="post">
						<input type='hidden' name='idx' value="${boardVO.idx}">
						<input type='hidden' name='page' value="${cri.page}">
						<input type='hidden' name='perPageNum' value="${cri.perPageNum}">
						<input type='hidden' name='searchType' value="${cri.searchType}">
						<input type='hidden' name='keyword' value="${cri.keyword}">
					</form>

					<div class="box-body">
						<div class="form-group">
							<label for="exampleInputEmail1">Title</label>
							<input type="text" name='title' class="form-control" value="${boardVO.title}" readonly="readonly">
						</div>
						<div class="form-group">
							<label for="exampleInputPassword1">Content</label>
							<textarea class="form-control" name="content" rows="10" readonly="readonly">${boardVO.content}</textarea>
						</div>
						<div class="form-group">
							<label for="exampleInputEmail1">Writer</label>
							<input type="text" name="writer" class="form-control" value="${boardVO.writer}" readonly="readonly">
						</div>

						<ul class="mailbox-attachments clearfix uploadedList"></ul>
					</div>
					<!-- /.box-body -->

					<div class="box-footer">
						<c:if test="${login.uid == boardVO.writer}">
						<button type="submit" class="btn btn-warning" id="modifyBtn">Modify</button>
						<button type="submit" class="btn btn-danger" id="removeBtn">REMOVE</button>
						</c:if>
						<button type="submit" class="btn btn-primary" id="goListBtn">LIST ALL</button>
					</div>

				</div>
				<!-- /.box -->
			</div>
			<!--/.col (left) -->

		</div>
		<!-- /.row -->

		<!-- Replies -->
		<div class="row">
			<div class="col-md-12">
				<div class="box box-success">
					<div class="box-header">
						<h3 class="box-title">ADD NEW REPLY</h3>
					</div>

					<c:if test="${not empty login}">
					<div class="box-body">
						<div class="form-group">
							<label for="exampleInputEmail1">Writer</label>
							<input class="form-control" type="text" placeholder="USER ID" id="newReplyWriter" value="${login.uid}" readonly>
						</div>
						<label for="exampleInputEmail1">Reply Text</label>
						<input class="form-control" type="text" placeholder="REPLY TEXT" id="newReplyText">
					</div>
					<!-- /.box-body -->
					<div class="box-footer">
						<button type="button" class="btn btn-primary" id="replyAddBtn">ADD REPLY</button>
					</div>
					</c:if>

					<c:if test="${empty login}">
					<div class="box-body">
						<div>
<!-- 							<a href="javascript:goLogin();" >Login Please</a> -->
							<a href="/user/login" >Login Please</a>
						</div>
					</div>
					</c:if>
				</div>

				<!-- The time line -->
				<ul class="timeline">
					<!-- timeline time label -->
					<li class="time-label" id="repliesDiv">
						<span class="bg-green" style="cursor: pointer;"> Replies List
							<small id='replycntSmall'> [ ${boardVO.replycnt} ] </small>
						</span>
					</li>
				</ul>

				<div class='text-center'>
					<ul id="pagination" class="pagination pagination-sm no-margin ">
	
					</ul>
				</div>

			</div>
			<!-- /.col -->
		</div>
		<!-- /.Replies -->

		<!-- Modal -->
		<div id="modifyModal" class="modal modal-primary fade" role="dialog">
			<div class="modal-dialog">
				<!-- Modal content-->
				<div class="modal-content">
					<div class="modal-header">
						<button type="button" class="close" data-dismiss="modal">&times;</button>
						<h4 class="modal-title"></h4>
					</div>
					<div class="modal-body" data-idx>
						<p>
							<input type="text" id="replytext" class="form-control">
						</p>
					</div>
					<div class="modal-footer">
						<button type="button" class="btn btn-info" id="replyModBtn">Modify</button>
						<button type="button" class="btn btn-danger" id="replyDelBtn">DELETE</button>
						<button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
					</div>
				</div>
			</div>
		</div>
		<!-- /.Modal -->

	</section>
	<!-- /.content -->
</div>
<!-- /.content-wrapper -->

<script id="templateAttach" type="text/x-handlebars-template">
<li data-src='{{fullName}}'>
	<span class="mailbox-attachment-icon has-img"><img src="{{imgsrc}}" alt="Attachment"></span>
	<div class="mailbox-attachment-info overtitle">
		<a href="{{getLink}}" target="_blank" class="mailbox-attachment-name">{{fileName}}</a>
	</div>
</li>
</script>

<script id="template" type="text/x-handlebars-template">
{{#each .}}
<li class="replyLi" data-idx={{idx}}>
	<i class="fa fa-comments bg-blue"></i>
	<div class="timeline-item">
		<span class="time">
			<i class="fa fa-clock-o"></i> {{prettifyDate regdate}}
		</span>
		<h3 class="timeline-header"><strong>{{idx}}</strong> -{{replyer}}</h3>
		<div class="timeline-body">{{replytext}} </div>
		<div class="timeline-footer">
			{{#eqReplyer replyer}}
			<a class="btn btn-primary btn-xs" data-toggle="modal" data-target="#modifyModal">Modify</a>
			{{/eqReplyer}}
		</div>
	</div>
</li>
{{/each}}
</script>
<script>
Handlebars.registerHelper("eqReplyer", function(replyer, block) {
	var accum = '';
	if (replyer == '${login.uid}') {
		accum += block.fn();
	}

	return accum;
});

Handlebars.registerHelper("prettifyDate", function(timeValue) {
	var dateObj = new Date(timeValue);
//	var year = dateObj.getFullYear();
//	var month = dateObj.getMonth() + 1;
//	var date = dateObj.getDate();
//	return year + "/" + month + "/" + date;

	// https://momentjs.com/
	//console.log(moment.locale('ko'));
	//console.log(moment(dateObj).format('LLLL')); // April 28th 2017, 3:18:46 pm

	moment.locale('ko');
	//console.log(moment(dateObj).format('YYYY/MMMM/Do a h:mm:ss'));

	//return moment(dateObj).format('LLLL');
	return moment(dateObj).format('YYYY/MMMM/Do a h:mm:ss');
});

var printData = function(replyArr, target, templateObject) {
	var template = Handlebars.compile(templateObject.html());
	var html = template(replyArr);

	$(".replyLi").remove();
	target.after(html);
}

var board_idx = ${boardVO.idx};
var replyPage = 1;

function getPage(pageInfo) {
	$.getJSON(pageInfo, function(data) {
		printData(data.list, $("#repliesDiv") ,$('#template'));
		printPaging(data.pageMaker, $(".pagination"));

		$("#modifyModal").modal('hide');
		$("#replycntSmall").html("[ " + data.pageMaker.totalCount +" ]");
	});
}

var printPaging = function(pageMaker, target) {
	var str = "";

	if(pageMaker.prev) {
		str += "<li><a href='" + (pageMaker.startPage - 1)
			+ "'> << </a></li>";
	}

	for(var i = pageMaker.startPage, len = pageMaker.endPage; i <= len; i++) {
		var strClass = pageMaker.cri.page == i ? 'class=active' : '';
		str += "<li "+strClass+"><a href='"+i+"'>" + i + "</a></li>";
	}

	if(pageMaker.next) {
		str += "<li><a href='" + (pageMaker.endPage + 1)
			+ "'> >> </a></li>";
	}

	target.html(str);
};

// 댓글 목록 조회
$("#repliesDiv").on("click", function() {
	if($(".timeline li").size() > 1) {
		return;
	}

	getPage("/replies/" + board_idx + "/1");
});

$(".pagination").on("click", "li a", function(event) {
	event.preventDefault();
	replyPage = $(this).attr("href");
	getPage("/replies/"+board_idx+"/"+replyPage);
});

// 댓글 등록
$("#replyAddBtn").on("click",function() {
	var replyerObj = $("#newReplyWriter");
	var replytextObj = $("#newReplyText");
	var replyer = replyerObj.val();
	var replytext = replytextObj.val();

	if(!chkField()) {
		return;
	}

	$.ajax({
		type:'post',
		url:'/replies/',
		headers: { 
			"Content-Type": "application/json",
			"X-HTTP-Method-Override": "POST"
		},
		dataType:'text',
		data: JSON.stringify({board_idx:board_idx, replyer:replyer, replytext:replytext}),
		success:function(result) {
			//console.log("result: " + result);

			if(result == 'SUCCESS'){
				alert("등록 되었습니다.");
				replyPage = 1;
				getPage("/replies/"+board_idx+"/"+replyPage );
				//replyerObj.val("");
				replytextObj.val("");
			}
		}
	});
});

$(".timeline").on("click", ".replyLi", function(event) {
	var reply = $(this);

	$("#replytext").val(reply.find('.timeline-body').text());
	$(".modal-title").html(reply.attr("data-idx"));
});

// 댓글 수정
$("#replyModBtn").on("click", function() {
	var idx = $(".modal-title").html();
	var replytext = $("#replytext").val();

	if(replytext == "") {
		alert('댓글 내용을 입력해주세요.');
		$("#replytext").focus();
		return;
	}

	$.ajax({
		type:'put',
		url:'/replies/'+idx,
		headers: { 
			"Content-Type": "application/json",
			"X-HTTP-Method-Override": "PUT"
		},
		data:JSON.stringify({replytext:replytext}),
		dataType:'text', 
		success:function(result) {
			//console.log("result: " + result);

			if(result == 'SUCCESS') {
				alert("수정 되었습니다.");
				getPage("/replies/"+board_idx+"/"+replyPage );
			}
		}
	});
});

// 댓글 삭제
$("#replyDelBtn").on("click", function() {
	var idx = $(".modal-title").html();
	var replytext = $("#replytext").val();

	$.ajax({
		type:'delete',
		url:'/replies/'+idx,
		headers: { 
			"Content-Type": "application/json",
			"X-HTTP-Method-Override": "DELETE"
		},
		dataType:'text', 
		success:function(result) {
			//console.log("result: " + result);

			if(result == 'SUCCESS'){
				alert("삭제 되었습니다.");
				getPage("/replies/"+board_idx+"/"+replyPage );
			}
		}
	});
});
</script>

<script>
$(document).ready(function() {
	var formObj = $("form[role='form']");

	console.log(formObj);

	// 글 수정 화면으로 이동
	$("#modifyBtn").on("click", function() {
		formObj.attr("action", "/board/modifyPage");
		formObj.attr("method", "get");		
		formObj.submit();
	});

/* 	$("#removeBtn").on("click", function(){
		formObj.attr("action", "/board/removePage");
		formObj.submit();
	}); */

	// 글 삭제
	$("#removeBtn").on("click", function() {
		var replyCnt =  $("#replycntSmall").html().replace(/[^0-9]/g, "");

		if(replyCnt > 0 ) {
			alert("댓글이 달린 게시물을 삭제할 수 없습니다.");
			return;
		}

		var arr = [];
		$(".uploadedList li").each(function(index) {
			arr.push($(this).attr("data-src"));
		});

		if(arr.length > 0){
			$.post("/deleteAllFiles",{files:arr}, function() {

			});
		}

		formObj.attr("action", "/board/removePage");
		formObj.submit();
	});	

	// 리스트 화면으로 이동
	$("#goListBtn").on("click", function(){
		formObj.attr("method", "get");
		formObj.attr("action", "/board/list");
		formObj.submit();
	});

	var idx = ${boardVO.idx};
	var template = Handlebars.compile($("#templateAttach").html());

	// 첨부파일 조회
	$.getJSON("/board/getAttach/"+idx, function(list) {
		$(list).each(function() {
			var fileInfo = getFileInfo(this);
			var html = template(fileInfo);

			$(".uploadedList").append(html);
		});
	});
});

function chkField() {
	if($.trim($("#newReplyWriter").val()) == "") {
		alert('댓글 등록자를 입력해주세요.');
		$("#newReplyWriter").focus();
		return false;
	}

	if($.trim($("#newReplyText").val()) == "") {
		alert('댓글 내용을 입력해주세요.');
		$("#newReplyText").focus();
		return false;
	}

	return true;
}
</script>

<%@ include file="../include/footer.jsp" %>