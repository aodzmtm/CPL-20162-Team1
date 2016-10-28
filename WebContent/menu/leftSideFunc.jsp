<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
function logInCheck() {
	var log_in_state;
	if ("${sessionScope.admin.admin_id}" != "") {
		/* alert("${sessionScope.admin.admin_id}"); */
		log_in_state = document.getElementById("log_in_state");
		log_in_state.innerHTML =
			"<tr style=\"height: 30px;\"><td>관리자 아이디 : ${sessionScope.admin.admin_id}</td></tr>"
			+ "<tr style=\"height: 30px;\"><td>관리자 이름 : ${sessionScope.admin.admin_name }</td></tr>"
			+ "<tr><td style=\"font-size:10px; height: 30px\"><a href=\"userInfo.do\">내 정보</a>&nbsp&nbsp&nbsp<a href=\"logOut.do\">로그아웃</a><td></tr>";
		lampInfoStateConnect();	
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