<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>


<meta charset="UTF-8">

<link type="text/css" href="css/footer.css" rel="stylesheet" />
<link type="text/css" href="css/style.css" rel="stylesheet" />

<script type="text/javascript" src="js/util.js"></script>
<form action="login.do" method="post">
	<input type="text" name="id"/>
	<input type="password" name="password"/>
	<input type="submit" value="전송" />
</form>