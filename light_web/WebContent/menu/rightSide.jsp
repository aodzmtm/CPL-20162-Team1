<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<div class="panel panel-primary" style="width:280px">
  <div class="panel-heading">
    <h3 class="panel-title">가로등 상태정보</h3>
  </div>
  <div class="panel-body">
    <table border=1 width="100%" style="border-color : #999999;
	border-top: 0px; border-bottom: 0px; border-left: 0px; border-right:0px">
  	<tbody>
  		<tr height="30px">
  			<td width="50%"><span style="margin:5px">가로등 id</span></td><td align="right"><input type="text" class="stateElement" id="id" value="" size="15" readonly></td>
  		</tr>
  		<tr height="30px">
  			<td><span style="margin:5px">비콘 주소</span></td><td align="right"><input type="text" class="stateElement" id="beacon_addr" value="" size="15" readonly></td>
  		</tr>
  		<tr height="30px">
  			<td><span style="margin:5px">비콘 id</span></td><td align="right"><input type="text" class="stateElement" id="beacon_id" value="" size="15" readonly></td>
  		</tr>
  		<tr height="30px">
  			<td><span style="margin:5px">가로등 위치</span></td><td align="right"><input type="text" class="stateElement" id="location" value="" size="15" readonly></td>
  		</tr>
  		<tr height="30px">
  			<td><span style="margin:5px">날짜</span></td><td align="right"><input type="text" class="stateElement" id="date_time" value="" size="15" readonly></td>
  		</tr>
  		<tr height="30px">
  			<td><span style="margin:5px">전원</span></td><td align="right"><input type="text" class="stateElement" id="lamp_state" value="" size="15" readonly></td>
  		</tr>
  		<tr height="30px">
  			<td><span style="margin:5px">상태</span></td><td align="right"><input type="text" class="stateElement" id="lamp_failure" value="" size="15" readonly></td>
  		</tr>
  	</tbody>
  </table>
  </div>
</div>
	
<div class="well bs-component" style ="width:280px; margin:auto; text-align:center; margin-bottom: 15px; border: 0px; background-color:#eef6ff">
	<legend>보안등 현황판</legend>
	<center>
		<table width="80%">
			<tbody>
				<tr height="30px">
					<td align="left"><span style="color:#88affb">● 정상</span></td>
					<td align="right"><span class="numberOfLight">10대</span></td>
				</tr>
				<tr height="30px">
					<td align="left"><span style="color:#f2cb61">● 수리중</span></td>
					<td align="right"><span class="numberOfLight">1대</span></td>
				</tr>
				<tr height="30px">
					<td align="left"><span style="color:#ff8b8b">● 고장</span></td>
					<td align="right"><span class="numberOfLight">2대</span></td>
				</tr>
				<tr height="30px">
				<td colspan="2" align="right">총 13대</td>
				</tr>
			</tbody>
		</table>
	</center>
	<div class="btn-group btn-group-justified">
	  <a href="#" class="btn btn-default">수정</a>
	  <a href="#" class="btn btn-default">추가</a>
	  <a href="#" class="btn btn-default">삭제</a>
	</div>
</div>
