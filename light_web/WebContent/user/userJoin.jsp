<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<style>
.join_table {
	width: 430px;
	border: 1px solid;
	color: #bce1fa;
	background-color: white;
	border-radius: 10px;
}

.join_td {
	font-weight: bold;
	width: 100%;
}

.join_sub_td {
	width: 100%;
	border-bottom: 1px solid #bce1fa;
}

.join_sub_red_font {
	margin-left: 13px;
	font-size: 12px;
	color: red;
	width: 390px;
	height: 30px;
}

.join_sub_green_font {
	margin-left: 13px;
	font-size: 12px;
	color: green;
	width: 390px;
	height: 30px;
}

.join_td_input {
	border: 0;
	float: inherit;
	margin-top: 15px;
	margin-left: 10px;
	width: 390px;
	font-weight: bold;
}

.join_button {
	margin-top: 120px;
	height: 50px;
	width: 430px;
	font-size: 20px;
	color: white;
	/* background: linear-gradient( white,#bce1fa, #bce1fa, white); border: 0; */
	background: #bce1fa;
	border: 0;
}

#feedback {
	font-size: 1.4em;
}

#selectable .ui-selecting {
	background: #FECA40;
}

#selectable .ui-selected {
	background: #F39814;
	color: white;
}

#selectable {
	list-style-type: none;
	margin: 0;
	padding: 0;
	width: 60%;
}

#selectable li {
	margin: 3px;
	padding: 0.4em;
	font-size: 1.4em;
	height: 18px;
}
</style>


<script>
var requestCheckId=true;
function checkId(user_id) {
	var request = createJSONHttpRequest();
	request.open('POST', '/light_web/userIdCheck.do');
	//Ajax 요청
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=UTF-8");

	request.setRequestHeader("Cache-Control",
			"no-cache, must-revalidate");

	request.setRequestHeader("Pragma", "no-cache");

	//Ajax 요청
	request.send("user_id=" + user_id);
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			//응답이 정상이라면
			if (request.status >= 200 && request.status < 300) {

				var str = request.responseText;
				
				if(str == "true")
					{
					document.getElementById("user_id_state").innerHTML = "<div class=\"join_sub_green_font\">OK</div>";
					requestCheckId =true;	
					}
					else if(str == "false")
					{
					document.getElementById("user_id_state").innerHTML = "<div class=\"join_sub_red_font\">기존에 아이디가 존재합니다.</div>";
					requestCheckId =false;	
					}
				//JSON 문자열 파싱
			
				}
			}
		}
	}

	function formCheck() {

		nameValidate();
		contactValidate();
		passwordCheck();

		passwordValidate();
		idValidate();
		if (nameValidate() == false || contactValidate() == false
				|| passwordCheck() == false || idValidate() == false
				|| passwordValidate() == false||requestCheckId == false) {
			return;
		}
	document.joinForm.submit();

	}

	function nameValidate() {
		var name = document.getElementById("user_name").value;
		if (name != "") {
			document.getElementById("user_name_state").innerHTML = "<div class=\"join_sub_green_font\">OK</div>";
		} else {
			document.getElementById("user_name_state").innerHTML = "<div class=\"join_sub_red_font\">이름을 입력하세요.</div>";
			return false;
		}
		return true;
	}

	function contactValidate() {
		var contact = document.getElementById("user_contact").value;
		if (contact != "") {
			document.getElementById("user_contact_state").innerHTML = "<div class=\"join_sub_green_font\">OK</div>";
		} else {
			document.getElementById("user_contact_state").innerHTML = "<div class=\"join_sub_red_font\">연락처 또는 이메일을 입력하세요.</div>";
			return false;
		}
		return true;
	}

	function passwordCheck() {
		var password = document.getElementById("user_password").value;
		var passwordCheck = document.getElementById("user_password_check").value;

		if (passwordCheck == "") {
			passwordCheck.innerHTML = ""
			return false;
		}

		else if (password != passwordCheck) {
			document.getElementById("user_password_check_state").innerHTML = "<div class=\"join_sub_red_font\">비밀번호가 일치하지 않습니다.</div>";
			return false;
		}

		else {
			document.getElementById("user_password_check_state").innerHTML = "<div class=\"join_sub_green_font\">OK</div>";
		}
		return true;

	}

	function idValidate() {

		var user_id = document.getElementById("user_id");

		if (user_id.value.length < 4) {
			document.getElementById("user_id_state").innerHTML = "<div class=\"join_sub_red_font\">문자의 조합으로 4자리 이상 입력해주세요.</div>";
			return false;
		}
		
		checkId(user_id.value);
		return true;
	}

	function passwordValidate() {

		var user_id = document.getElementById("user_id");
		var user_password = document.getElementById("user_password");

		if (user_password.value.length < 6) {
			document.getElementById("user_password_state").innerHTML = "<div class=\"join_sub_red_font\">비밀번호는 문자, 숫자, 특수문자의 조합으로 6~16자리로 입력해주세요.</div>";
			return false;
		}

		if (!user_password.value
				.match(/([a-zA-Z0-9].*[!,@,#,$,%,^,&,*,?,_,~])|([!,@,#,$,%,^,&,*,?,_,~].*[a-zA-Z0-9])/)) {
			document.getElementById("user_password_state").innerHTML = "<div class=\"join_sub_red_font\">비밀번호는 문자, 숫자, 특수문자의 조합으로 6~16자리로 입력해주세요.</div>";
			return false;
		}

		if (user_id.value == user_password.value) {
			document.getElementById("user_password_state").innerHTML = "<div class=\"join_sub_red_font\">비밀번호에 아이디를 사용할 수 없습니다.</div>";
			return false;
		}

		var SamePass_0 = 0; //동일문자 카운트
		var SamePass_1 = 0; //연속성(+) 카운드
		var SamePass_2 = 0; //연속성(-) 카운드

		var chr_pass_0;
		var chr_pass_1;

		var chr_pass_2;

		for (var i = 0; i < user_password.value.length; i++) {
			chr_pass_0 = user_password.value.charAt(i);
			chr_pass_1 = user_password.value.charAt(i + 1);

			//동일문자 카운트
			if (chr_pass_0 == chr_pass_1) {
				SamePass_0 = SamePass_0 + 1
			}

			chr_pass_2 = user_password.value.charAt(i + 2);
			//연속성(+) 카운드

			if (chr_pass_0.charCodeAt(0) - chr_pass_1.charCodeAt(0) == 1
					&& chr_pass_1.charCodeAt(0) - chr_pass_2.charCodeAt(0) == 1) {
				SamePass_1 = SamePass_1 + 1
			}

			//연속성(-) 카운드
			if (chr_pass_0.charCodeAt(0) - chr_pass_1.charCodeAt(0) == -1
					&& chr_pass_1.charCodeAt(0) - chr_pass_2.charCodeAt(0) == -1) {
				SamePass_2 = SamePass_2 + 1
			}
		}
		if (SamePass_0 > 1) {
			alert("동일문자를 3번 이상 사용할 수 없습니다.");
			return false;
		}

		if (SamePass_1 > 1 || SamePass_2 > 1) {
			alert("연속된 문자열(123 또는 321, abc, cba 등)을\n 3자 이상 사용 할 수 없습니다.");
			return false;
		}

		document.getElementById("user_password_state").innerHTML = "<div class=\"join_sub_green_font\">OK</div>";
		return true;
	}
</script>

</head>
<!-- #e5f7ff -->

<body bgcolor="#f0f5f7">
	<form name="joinForm" action="userJoinConf.do" method="post">
		<div align="center" style="vertical-align: middle;">
			<table style="margin-top: 50px;">
				<tr>
					<td style="width: 1300px; border-bottom: 1px solid #bce1fa;">
						<div style="font-size: 25pt; margin-bottom: 10px; color: #34718a;">관리자 등록
						<a href="main.do" style="color: #34718a; font-size: 10pt;float: right;">이전 페이지로 돌아가기</a></div>
					</td>
				</tr>
			</table>
			<div style="margin-top: 120px;">
				<table class="join_table">
					<tr>
						<td class="join_td"><input class="join_td_input" id="user_id"
							name="user_id" type="text" onblur="idValidate()" placeholder="아이디"
							value="" autocomplete="off"></input></td>
					</tr>
					<tr>
						<td class="join_sub_td" id="user_id_state">
							<div class="join_sub_red_font">필수 정보입니다.</div>
						</td>
					</tr>
					<tr>
						<td class="join_td"><input class="join_td_input"
							name="user_password" id="user_password" type="password"
							onblur="passwordValidate()" placeholder="비밀번호" value=""
							autocomplete="off"></input></td>
					</tr>
					<tr>
						<td class="join_sub_td" id="user_password_state">
							<div class="join_sub_red_font">필수 정보입니다.</div>
						</td>
					</tr>
					<tr>
						<td class="join_td"><input class="join_td_input"
							id="user_password_check" type="password"
							onkeyup="passwordCheck()" placeholder="비밀번호 재확인" value=""
							autocomplete="off"></input></td>
					</tr>
					<tr>
						<td class="join_sub_td" id="user_password_check_state">
							<div class="join_sub_red_font">필수 정보입니다.</div>
						</td>
					</tr>
					<tr>
						<td class="join_td"><input class="join_td_input"
							name="user_name" id="user_name" type="text" placeholder="이름"
							value="" autocomplete="off" onblur="nameValidate()"></input></td>
					</tr>
					<tr>
						<td class="join_sub_td" id="user_name_state">
							<div class="join_sub_red_font">필수 정보입니다.</div>
						</td>
					</tr>
					<tr>
						<td class="join_td"><input class="join_td_input"
							name="user_contact" id="user_contact" type="text"
							placeholder="연락처 또는 이메일" value="" autocomplete="off"
							onblur="contactValidate()"></input></td>
					</tr>
					<tr>
						<td id="user_contact_state">
							<div class="join_sub_red_font">필수 정보입니다.</div>
						</td>
					</tr>
				</table>
				<input class="join_button" type="button" value="가입하기" onclick="formCheck()"></input>
			</div>
		</div>
	</form>
	



</body>

</html>
