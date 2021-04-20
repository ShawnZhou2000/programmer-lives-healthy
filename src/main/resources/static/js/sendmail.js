function send() {
	document.getElementById("btnsend").disabled = true;
	document.getElementById("btnsend").innerHTML = "60秒";
	let a = setInterval(function(){
		let s = document.getElementById("btnsend").innerHTML;
		let s2 = s.split("秒");
		let nowtime = parseInt(s2[0]);
		nowtime--;
		document.getElementById("btnsend").innerHTML = nowtime.toString() + "秒";
		if (nowtime == 0) {
			document.getElementById("btnsend").disabled = false;
			document.getElementById("btnsend").innerHTML = "发送";
			clearInterval(a);
		}
	},1000);
	var email = $("#email").val();
	var type = $("#type").val();
	console.log(type);
	sendEmail(email, type);
}

function sendEmail(email, type) {
	if (!email) {
		alert("邮箱不能为空!"); //这里可以进化成对邮箱格式正确性的判断
		return;
	}

	$.ajax({
		url: '/getMailCode?email=' + email + "&type=" + type,
		dataType: 'json',
		type: 'GET',
		success: function(response) {
			if (response.code === 200) {
				alert("邮件发送成功,请查收。")
			} else {
				alert(response.message);
			}
		}
	});
}
