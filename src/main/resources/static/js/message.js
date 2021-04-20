/*
    发送消息
 */
function sendMessage() {
	var fromId = $("#fromId").val();
	var toId = $("#toId").val();
	var content = $("#content").val();
	var img = $("#img")[0].files[0];
	// document.getElementById("send").style.display = "none";
	// document.getElementById("loading").style.display = "block";
	// document.getElementById("postbtn").disabled = "true";
	// setTimeout(function() {
		send2target(fromId, toId, content, img);
	// },1000);
}

function send2target(fromId, toId, content, img) {
	if (!content) {
		alert("回复内容不能为空！");
		return;
	}
	var formData = new FormData();
	formData.append("img", img);
	formData.append("toId", toId);
	formData.append("fromId", fromId);
	formData.append("content", content);
	$.ajax({
		url: '/sendMessage',
		dataType: 'json',
		type: 'POST',
		async: false,
		processData: false,
		data: formData,
		contentType: false,
		success: function(response) {
			if (response.code == 200) {
				window.location.reload();
				alert("发送成功!")
				// $("#comment_section").hide();
			} else {
				alert(response.message);
			}
      if (document.getElementById("send") != undefined)
        document.getElementById("send").style.display = "block";
      if (document.getElementById("loading") != undefined)
        document.getElementById("loading").style.display = "none";
      if (document.getElementById("postbtn") != undefined)
        document.getElementById("postbtn").disabled = "";
		}
	});
}

function getOneMessage(e) {
	var messageId = e.getAttribute("data-id");
	var u = "/loadMessage?messageId=" + messageId;
	$('#reply2').load(u);

}
