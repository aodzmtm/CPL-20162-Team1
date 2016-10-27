<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<meta charset="UTF-8">
<div style="margin: 5px;">
	<div
		style="width: 100%; height: 40px; text-align: center; background: linear-gradient(#bce1fa, #bce1fa, white); border-radius: 10px 10px 0px 0px; align: center; font-size: 15px; color: #2e62d9; font-weight: bold; border: 1;">
		<div style="padding-top: 10px;">로그인</div>
	</div>

	<div style="width: 265px; text-align: center;">
		<form class="form-horizontal" style="margin: 15px" action="logIn.do"
			method="post">

			<table id="log_in_state" style="width: 100%;">
			
				<tr>
					<td align="center">
						<div align="center">
							<div class="form-group">
								<div class="col-lg-10">
									<input type="text" class="form-control" name="user_id" 
										placeholder="Id" style="width: 170px; height: 30px;">
								</div>
							</div>
							<div class="form-group">
								<div class="col-lg-10">
									<input type="password" class="form-control" name="user_password" 
										placeholder="Password" style="width: 170px; height: 30px;">
								</div>
							</div>
						</div>
					</td>
					<td><input type="submit" value="로그인"
						style="width: 60px; height: 80px; margin-left: 10px; margin-bottom: 15px; font-size: 13px; color: #2e62d9; font-weight: bold; background: linear-gradient(white, #bce1fa, #bce1fa, #bce1fa, white); border-radius: 10px 10px 10px 10px;" />

					</td>
				</tr>
				<tr>
			<td colspan="2" style="font-size:10px; text-align: left;"><a href="userJoin.do">관리자 등록</a><td>
			</tr>
			</table>
		</form>
	</div>
	<ul class="nav nav-pills nav-stacked">
		<li id="leftButton1" onclick="mapDisplay()"><a href="#">실시간 관제창</a></li>
		<li id="leftButton2" onclick="gridDisplay()"><a href="#" >보안등 상태기록표</a></li>
		<li id="leftButton3" onclick="graphDisplay()"><a href="#" >보안등 그래프</a></li>
	</ul>
</div>




