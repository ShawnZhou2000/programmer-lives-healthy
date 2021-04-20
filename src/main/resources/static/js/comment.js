/*
    提交回复
 */
function post() {
	var groupId = $("#groupId").val();
	var userId = $("#userId").val();
	var content = $("#content").val();
	var img = $("#img")[0].files[0];
	document.getElementById("send").style.display = "none";
	document.getElementById("loading").style.display = "block";
	document.getElementById("postbtn").disabled = "true";
	setTimeout(function() {
		comment2target(groupId, userId, content, img);
	},1000);
	
}

function comment2target(groupId, userId, content, img) {
	if (!content) {
		alert("回复内容不能为空！");
		return;
	}
	if (img === undefined) {
		alert("请上传图片！");
		return;
	}
	var formData = new FormData();
	formData.append("img", img);
	formData.append("userId", userId);
	formData.append("groupId", groupId);
	formData.append("content", content);
	$.ajax({
		url: '/comment',
		dataType: 'json',
		type: 'POST',
		async: false,
		processData: false,
		data: formData,
		contentType: false,
		success: function(data) {
			if (data.code === 200) {
				window.location.reload();
				alert(data.message)
			} else {
				alert(data.message);
			}
			document.getElementById("send").style.display = "block";
			document.getElementById("loading").style.display = "none";
			document.getElementById("postbtn").disabled = "";
		}
	});
}

function preloadComment(event) {
	let val = event.target.value;
	console.log(event.target.value);
	if (val == "no") {
		return;
	}
	else if (val == "rec7") {
		loadComment(7);
	} else if (val == "rec30") {
		loadComment(30);
	} else if(val == "rec14"){
		loadComment(14);
	} else if (val == "recall") {
		loadComment(-1);
	} else if(val == "now"){
		location.reload();
	}
	
}

function loadComment(arg) {
	let groupId = document.getElementById("groupId").value;
	let u = "/loadComment?id=" + groupId + "&dayNumber=" + arg;
	$('#pcomment').load(u);
}

