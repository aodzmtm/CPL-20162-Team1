<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!doctype html>
<html>
<head>
<style>
.join_table_complete {
	width: 430px;
	color: #34718a;
}

.join_td {
	font-weight: bold;
	width: 100%;
}

.join_sub_td {
	width: 100%;
	border-bottom: 1px solid #bce1fa;
}

.join_sub_font {
	margin-left: 13px;
	font-size: 12px;
	color: red;
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
	margin-top: 100px;
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

</head>

<body bgcolor="#f0f5f7">
	<div align="center">
		<table style="margin-top: 50px;">
			<tr>
				<td style="width: 1300px; border-bottom: 1px solid #bce1fa;">
					<div style="font-size: 25pt; margin-bottom: 10px; color: #34718a;">
						비밀번호 변경 완료 <a href="main.do"
							style="color: #34718a; font-size: 10pt; float: right;">이전
							페이지로 돌아가기</a>
					</div>
				</td>
			</tr>
		</table>
		<div style="margin-top: 100px;">
			<%
				String id = request.getParameter("user_id");
				String pass1 = request.getParameter("user_password");

				/* out.println("id : " + id + "<br>");
				out.println("pass1 : " + pass1 + "<br>"); */
			%>
			<table class="join_table_complete" style="height: 400px;">
				<tr>
					<td align="center">
						<div style="font-size: 40pt">
							비밀번호 변경 <br>되셨습니다!
						</div>
						<div>관리자님 시스템에 로그인해서 확인 해보세요.</div>
					</td>
				</tr>
			</table>
			<input class="join_button" type="submit" value="시작하기" onclick="location.href='main.do'"></input>

		</div>
	</div>
</body>

</html>

