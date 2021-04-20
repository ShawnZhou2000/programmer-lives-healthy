let uname,uid,uqq,uemail,uavatar,ubio;

window.onload = function() {
	uname = document.getElementById("usrname").value;
	uid = document.getElementById("usrid").value;
	uqq = document.getElementById("usrqq").value;
	uemail = document.getElementById("usremail").value;
	uavatar = document.getElementById("useravatar").src;
	ubio = document.getElementById("usrbio").value;
}

function updateInfo() {
	let arr = document.getElementsByClassName("dis-plain");
	for (let index in arr) {
		if (index == 1 || index == 3 || index == 4 || index == 5)
			continue;
		arr[index].disabled = false;
	}
	document.getElementById("submitpanel").style.display = "flex";
	document.getElementById("av-ps-panel").style.display = "block";
}

function cancelUpd() {
	document.getElementById("usrname").value = uname;
	document.getElementById("usrid").value = uid;
	document.getElementById("usrqq").value = uqq;
	document.getElementById("usremail").value = uemail;
	document.getElementById("useravatar").src = uavatar;
	let arr = document.getElementsByClassName("dis-plain");
	for (let index in arr) {
		if (index == 1 || index == 3 || index == 4 || index == 5)
			continue;
		arr[index].disabled = true;
	}
	document.getElementById("submitpanel").style.display = "none";
	document.getElementById("av-ps-panel").style.display = "none";
}