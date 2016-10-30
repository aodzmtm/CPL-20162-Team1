<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">

</script>


<input type="submit" value="send"
		onclick="" />
<a href="grid.do">abc</a>
<script type="text/javascript" src="js/util.js"></script>


<form action="logIn.do" method="post">
	<input type="text" name="id"/>
	<input type="password" name="password"/>
	<input type="submit" value="전송" />
</form>

<h3>관리자메인페이지</h3>
관리자 아이디 : ${sessionScope.admin.admin_id }<br/>
관리자 이름: ${sessionScope.admin.admin_name }
