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
window.onload = function() {

	selectContact();
};
function selectContact() {
	var request = createJSONHttpRequest();
	request.open('POST', '/light_web/userContactData.do');
	//Ajax 요청
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=UTF-8");

	request.setRequestHeader("Cache-Control",
			"no-cache, must-revalidate");

	request.setRequestHeader("Pragma", "no-cache");

	//Ajax 요청
	request.send("user_id=" + document.getElementById("user_id").value);
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			//응답이 정상이라면
			if (request.status >= 200 && request.status < 300) {

				var str = request.responseText;
				document.getElementById("user_contact").value = str;

			
				}
			}
		}
	}

function sendContact() {
	var request = createJSONHttpRequest();
	request.open('POST', '/light_web/updateUserContact.do');
	//Ajax 요청
	request.setRequestHeader("Content-Type",
			"application/x-www-form-urlencoded;charset=UTF-8");

	request.setRequestHeader("Cache-Control",
			"no-cache, must-revalidate");

	request.setRequestHeader("Pragma", "no-cache");

	//Ajax 요청

	request.send("parameter="+ document.getElementById("user_id").value+"::" + document.getElementById("user_contact").value);
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			//응답이 정상이라면
			if (request.status >= 200 && request.status < 300) {

				var str = request.responseText;
				document.getElementById("user_contact").value = str;
			
				}
			}
		}
	}
</script>

</head>
<!-- #e5f7ff -->

<body bgcolor="#f0f5f7">
		<input id="user_id" name="user_id" type="hidden" value="${sessionScope.admin.admin_id}"
			autocomplete="off"></input>
		<div align="center" style="vertical-align: middle;">
			<table style="margin-top: 50px;">
				<tr>
					<td style="width: 1300px; border-bottom: 1px solid #bce1fa;">
						<div style="font-size: 25pt; margin-bottom: 10px; color: #34718a;">
							관리자 정보 <a href="main.do"
								style="color: #34718a; font-size: 10pt; float: right;">이전
								페이지로 돌아가기</a>
						</div>
					</td>
				</tr>
			</table>
			<div style="margin-top: 150px;">
				<table>
					<tr>
						<td align="center"
							style="width: 500px; height: 250px; border: 1px solid #bce1fa; color: #34718a;background: white; border-radius: 10px;">
							<input type="button" value="비밀번호 수정"  onclick="location.href='userPassChange.do'"
							style="color: #34718a; font-size: 15pt; border: 1px solid #bce1fa; background: white; width: 200px; height: 35px;"/>

						</td>
						<td style="width: 20px;"></td>
						<td
							style="width: 500px; height: 250px; border: 1px solid #bce1fa; background: white; border-radius: 10px;" 
							align="center">
							<table
								style="width: 400px; height: 200px; font-weight: bold; color: #34718a;">
								<tr>
									<td style="font-size: 15pt; font-weight: bold; width: 150px;">
										연락처</td>
								</tr>
								<tr>
									<td style="font-size: 10pt;">휴대전화 및 이메일</td>
									<td><input style="border: 0px;" id="user_contact"
										value=""></td>
								</tr>
								<tr style="font-size: 10pt;">
									<td><input
										style="border: 1px solid #bce1fa; font-weight: bold; color: #34718a; width: 70px; height: 32px; text-align: center; background: white;"
										value="수정" type="button" onclick="sendContact()"></td>
								</tr>
							</table>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="height: 30px;"></td>
					</tr>
					<tr>
						<td colspan="2">
							<table>
								<tr>
									<td>
										<div style="font-size: 8pt; color: #9a9d9e; margin-right: 10px;">보안등 시스템
											서비스를 더 이상 이용하지 않는다면</div>
									</td>
									<td><a href="userAway.do"
										style="font-size: 10pt; color: #34718a; font-weight: bold;">
											관리자 탈퇴 바로가기 ▶ </a></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</div>
		</div>
</body>

</html>
