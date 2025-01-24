$(function() {

	// 양도 게시글 쓰기 폼 유효성 검사
	$("#trwriteForm").on("submit", function(event) {

		if ($("#transferTitle").val().trim().length == 0) {
			alert("제목이 입력되지 않았습니다.\n제목을 입력해주세요");
			$("#transferTitle").focus();
			event.preventDefault();
			return false;
		}
		if($("#transferPrice").val().length <= 0) {
			alert("양도 가격이 입력되지 않았습니다.\n양도 가격을 입력해주세요");
			$("#transferPrice").focus();
			return false;
		}
		if($("#content").val().length <= 0) {
			alert("내용이 입력되지 않았습니다.\n내용을 입력해주세요");
			$("#content").focus();
			return false;
		}
	});

	$("#updateForm").on("submit", function() {
		if($("#transferTitle").val().length <= 0) {
			alert("제목이 입력되지 않았습니다.\n제목을 입력해주세요");
			$("#title").focus();
			return false;
		}
		if($("#content").val().length <= 0) {
			alert("내용이 입력되지 않았습니다.\n내용을 입력해주세요");
			$("#content").focus();
			return false;
		}
	});

	$("#detailDelete").on("click", function() {
		if (confirm("정말 삭제하시겠습니까?")) {
			$("#deleteForm").attr("action", "delete");
			$("#deleteForm").attr("method", "post");
			$("#deleteForm").submit();
		}
	});

});