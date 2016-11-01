<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>


<meta charset="UTF-8">

<link type="text/css" href="css/footer.css" rel="stylesheet" />
<link type="text/css" href="css/style.css" rel="stylesheet" />

<script type="text/javascript" src="js/util.js"></script>


<h3>관리자메인페이지</h3>
관리자 아이디 : ${sessionScope.admin.admin_id }<br/>
관리자 이름: ${sessionScope.admin.admin_name }
