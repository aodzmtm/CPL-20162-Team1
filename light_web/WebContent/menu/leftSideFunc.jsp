<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
	function logInCheck() {
		if ("${sessionScope.admin.admin_id}" != "") {
			var request = createJSONHttpRequest();
			var display = document.getElementById("log_in_state");
			request.open('POST', '/light_web/menu/loadProfile.jsp');
			//Ajax 요청
			request.send();
			request.onreadystatechange = function() {
				if (request.readyState == 4) {
					//응답이 정상이라면
					if (request.status >= 200 && request.status < 300) {

						var str = request.responseText;
						//alert(str);
						display.innerHTML = str;
						profileCheck();
						lampInfoStateConnect();
					} //else alert("데이터를 가져오기 실패");
				}
			}
		}
	}
	/* profile check */
	function profileCheck() {

		var request = createJSONHttpRequest();

		request.open('POST', '/light_web/selectUserProfile.do');
		//Ajax 요청
		//Ajax 요청
		request.setRequestHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=UTF-8");

		request.setRequestHeader("Cache-Control", "no-cache, must-revalidate");

		request.setRequestHeader("Pragma", "no-cache");

		//Ajax 요청
		request.send("user_id=" + "${sessionScope.admin.admin_id}");
		request.onreadystatechange = function() {
			if (request.readyState == 4) {
				//응답이 정상이라면
				if (request.status >= 200 && request.status < 300) {

					var str = request.responseText;
					//alert(str);
					if (str == "")
						var userImage = "img/log_in.PNG;";
					else
						var userImage = str;

					getUserImage(userImage);

				} else
					getUserImage("img/log_in.PNG;");
			}
		}
	}

	function makeUserAlbumJson(blob) {
		var totalInfo = new Object();
		var dataArray = new Array();
		var dataInfo = new Object();
		dataInfo.user_id = "${sessionScope.admin.admin_id}";
		dataInfo.user_album = blob;
		dataArray.push(dataInfo);
		totalInfo.UserVo = dataArray;
		var jsonInfo = JSON.stringify(totalInfo);
		//  alert(jsonInfo);
		return jsonInfo;
	}

	function getUserImageClick() {

		document.getElementById('file').click();
	}

	function getUserImage(userImage) {
		previewId = 'previewId';
		var preview = document.getElementById(previewId); //div id   
		var canvas = document.getElementById("createUserCanvas");
		var context = canvas.getContext("2d");
		var img = new Image();
		img.src = userImage;
		img.onload = function() {
			context.drawImage(img, 0, 0, canvas.width, canvas.height);
			var dataUrl = canvas.toDataURL('image/jpeg');
			//alert(dataUrl);
			var setImg = document.createElement("img");
			setImg.id = "prev_" + previewId;
			setImg.classList.add("profile_box");
			setImg.src = dataUrl;
			preview.appendChild(setImg);
		};
	}

	function previewImage(targetObj, previewId) {

		var request = createJSONHttpRequest();
		var preview = document.getElementById(previewId); //div id   
		var ua = window.navigator.userAgent;

		var files = targetObj.files;
		for (var i = 0; i < files.length; i++) {

			var file = files[i];

			var imageType = /image.*/;
			if (!file.type.match(imageType))
				continue;

			var prevImg = document.getElementById("prev_" + previewId);
			if (prevImg) {
				preview.removeChild(prevImg);
			}

			var canvas = document.getElementById("createUserCanvas");
			var context = canvas.getContext("2d");
			var img = new Image();
			var imgTemp = new Image();
			var div = document.createElement("div");
			/*preview.appendChild(div);*/

			if (window.FileReader) { // FireFox, Chrome, Opera 확인.
				var reader = new FileReader();
				reader.onloadend = (function(aImg) {
					return function(e) {
						aImg.src = e.target.result;
						imgTemp.src = aImg.src.toString();
						/*
						img.style.width = '100px'; 
						img.style.height = '100px';
						 */
						imgTemp.onload = function() {
							context.drawImage(img, 0, 0, canvas.width,
									canvas.height);
							var dataUrl = canvas.toDataURL('image/jpeg');
							var blob = dataURItoBlob(dataUrl);
							//alert(blob);
							var url = '/light_web/saveUserProfile.do';
							request.open("POST", url, true);
							/* request.setRequestHeader("Content-Type",
							"application/x-www-form-urlencoded;");
							 */
							request.send(makeUserAlbumJson(blob));
							request.onload = function() {
								if (request.readyState == 4
										&& request.status == 200) {
									/* var parser = new DOMParser();
									var xmlDoc = request.responseXML; */
									var setImg = document.createElement("img");
									setImg.id = "prev_" + previewId;
									setImg.classList.add("profile_box");
									setImg.src = dataUrl;
									preview.appendChild(setImg);
								}
							}

						};
					};
				})(img);
				reader.readAsDataURL(file);
			}
		}
	}
	function leftButton1() {
		if ("${sessionScope.admin.admin_id}" != "") {
			document.getElementById("leftButton1").className = "active";
			leftNoneButton2();
			leftNoneButton3();
		}
	}
	function leftButton2() {
		if ("${sessionScope.admin.admin_id}" != "") {
			document.getElementById("leftButton2").className = "active";
			leftNoneButton1();
			leftNoneButton3();
		}
	}
	function leftButton3() {
		if ("${sessionScope.admin.admin_id}" != "") {
			document.getElementById("leftButton3").className = "active";
			leftNoneButton1();
			leftNoneButton2();
		}
	}

	function leftNoneButton1() {
		if ("${sessionScope.admin.admin_id}" != "") {
			document.getElementById("leftButton1").className = "";
		}
	}
	function leftNoneButton2() {
		if ("${sessionScope.admin.admin_id}" != "") {
			document.getElementById("leftButton2").className = "";
		}
	}
	function leftNoneButton3() {
		if ("${sessionScope.admin.admin_id}" != "") {
			document.getElementById("leftButton3").className = "";
		}
	}
</script>