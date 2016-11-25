<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<input id="user_id" name="user_id" type="hidden"
		value="${sessionScope.admin.admin_id}" autocomplete="off"></input>
	<%-- 	<canvas id="myCanvas" width="120" height="120"></canvas>
	<button onclick="saveScene();">Upload Canvas Image</button> --%>

	<canvas id="createUserCanvas"
		style="width: 100px; height: 100px;  display: none; "></canvas>
<tr>
	<td style="font-size: 10px; text-align: right;"><a
		href="userInfo.do">내 정보</a>&nbsp&nbsp&nbsp<a href="logOut.do">로그아웃</a>
	<td>
</tr>
<tr style="height: 150px;">
	<td align="center">
		<div id='previewId' onclick="getUserImageClick();" style="width:130px; height:130px;">
					<!-- 		사용자 이미지가 뿌려지는 곳 -->

				</div> <input id="file" type="file" size="30"
				style="display:none;"
				onchange="previewImage(this,'previewId')" />
	</td>
</tr>
<tr style="height: 30px;">
	<td>관리자 아이디 : ${sessionScope.admin.admin_id}</td>
</tr>
<tr style="height: 30px;">
	<td>관리자 이름 : ${sessionScope.admin.admin_name}</td>
</tr>