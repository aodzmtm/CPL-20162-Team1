<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<meta charset="UTF-8">
<div style="margin: 5px;">
	<div class="panel panel-primary" style="width: 265px;">
		<div
			style="width: 100%; height: 40px; background: linear-gradient(#bce1fa, #bce1fa, white); border-radius: 10px 10px 0px 0px; align: center; font-size: 15px; color: #2e62d9; font-weight: bold; border: 1; margin-bottom: 10px;">
			<div style="padding-top: 10px;">보안등 상태정보</div>
		</div>
		<table
			style="border-color: #999999; width: 100%; border-top: 0px; border-bottom: 0px; border-left: 0px; border-right: 0px;">
			<tbody>
				<!-- 	<tr height="30px">
  			<td width="50%"><span style="margin:5px">보안등 id</span></td><td align="right"><input type="text" class="stateElement" id="id" value="" size="15" readonly></td>
  		</tr> -->
				<tr height="40px">
					<td><span class="light_state_font" style="margin-left: 10px;">비콘
							id</span></td>
					<td align="center"><input
						class="editable inline-edit-cell ui-widget-content ui-corner-all"
						type="text" style="height: 25px; width: 140px;" id="beacon_addr"
						value=""></td>
				</tr>
				<tr height="40px">
					<td><span class="light_state_font" style="margin-left: 10px;">비콘
							주소</span></td>
					<td align="center"><input
						class="editable inline-edit-cell ui-widget-content ui-corner-all"
						type="text" style="height: 25px; width: 140px;" id="beacon_id"
						value=""></td>
				</tr>
				<tr height="40px">
					<td><span class="light_state_font" style="margin-left: 10px;">비콘
							설치 장소</span></td>
					<td align="center"><input
						class="editable inline-edit-cell ui-widget-content ui-corner-all"
						type="text" style="height: 25px; width: 140px;" id="location"
						value=""></td>
				</tr>
				<tr height="40px">
					<td><span class="light_state_font" style="margin-left: 10px;">날짜
							시간</span></td>
					<td align="center"><input
						class="editable inline-edit-cell ui-widget-content ui-corner-all"
						readonly="readonly" type="text"
						style="height: 25px; width: 140px;" id="date_time" value=""
						onclick="rightSideEditDateTime()"></td>
				</tr>
				<tr height="40px">
					<td><span class="light_state_font" style="margin-left: 10px;">정전
							여부</span></td>
					<td align="center"><select id="power_off"
						class="editable inline-edit-cell ui-widget-content ui-corner-all"
						style="height: 25px; width: 140px;">
							<option value="0">정상</option>
							<option value="1">정전</option>
					</select></td>
				</tr>
				<tr height="40px">
					<td><span class="light_state_font" style="margin-left: 10px;">이상
							점소등</span></td>
					<td align="center"><select id="abnormal_blink"
						class="editable inline-edit-cell ui-widget-content ui-corner-all"
						style="height: 25px; width: 140px;">
							<option value="0">정상</option>
							<option role="option" value="1">이상점등</option>
							<option role="option" value="2">이상소등</option>
					</select></td>
				</tr>
				<tr height="40px">
					<td><span class="light_state_font" style="margin-left: 10px;">누전
							여부</span></td>
					<td align="center"><select id="short_circuit"
						class="editable inline-edit-cell ui-widget-content ui-corner-all"
						style="height: 25px; width: 140px;">
							<option value="0">정상</option>
							<option role="option" value="1">누전</option>
					</select></td>
				</tr>
				<tr height="40px">
					<td><span class="light_state_font" style="margin-left: 10px;">보안등
							고장</span></td>
					<td align="center"><select id="lamp_failure"
						class="editable inline-edit-cell ui-widget-content ui-corner-all"
						style="height: 25px; width: 140px;">
							<option value="0">정상</option>
							<option role="option" value="1">램프고장</option>
							<option role="option" value="2">안정기고장</option>
							<option role="option" value="3">램프안정기고장</option>
					</select></td>
				</tr>
				<tr height="40px">
					<td><span class="light_state_font" style="margin-left: 10px;">점소등
							상태</span></td>
					<td align="center"><select id="lamp_state"
						class="editable inline-edit-cell ui-widget-content ui-corner-all"
						style="height: 25px; width: 140px;">
							<option role="option" value="0">정상소등</option>
							<option role="option" value="1">정상점등</option>
							<option role="option" value="2">강제소등</option>
							<option role="option" value="3">강제점등</option>
					</select></td>
				</tr>
			</tbody>
		</table>
		<table style="margin-top: 10px;">

			<tr>
				<td>
					<div class="btn-group btn-group-justified">
						<a href="#" class="btn btn-default" onclick="updateLamp()">수정</a>
						<a href="#" class="btn btn-default" onclick="insertLamp()">추가</a>
						<a href="#" class="btn btn-default" onclick="deleteLamp()">삭제</a>
					</div>
				</td>
			</tr>
			<tr>
				<td colspan="3">
					<div class="btn-group btn-group-justified">
						<a href="#" class="btn btn-default" onclick="routeDensity()">위험지역</a>

					</div>
				</td>
			</tr>
		</table>

	</div>
	<div class="panel panel-primary">
		<div
			style="width: 100%; height: 40px; text-align: center; background: linear-gradient(#bce1fa, #bce1fa, white); border-radius: 10px 10px 0px 0px; align: center; font-size: 15px; color: #2e62d9; font-weight: bold; border: 1; margin-bottom: 10px;">
			<div style="padding-top: 10px;">보안등 현황</div>
		</div>
		<div
			style="width: 265px; margin: auto; text-align: center; margin-bottom: 15px; border: 0px;">
	
			<div align="center">
				<table style="width: 80%">
					<tbody>
						<tr height="30px">
							<td align="left"><span style="color: #8a9294">● 정전 여부</span></td>
							<td align="right"><input class="stateElement" size="5"
								value="" id="power_off_total" onclick="powerOffTotalFilter()"
								readonly="readonly"></input> <span class="numberOfLight">
									대 </span></td>
						</tr>
						<tr height="30px">
							<td align="left"><span style="color: #12b806">● 이상 점소등</span></td>
							<td align="right"><input class="stateElement" size="5"
								id="abnormal_blink_total" value=""
								onclick="abnormalBlinkTotalFilter()" readonly="readonly"></input>
								<span class="numberOfLight"> 대 </span></td>
						</tr>
						<tr height="30px">
							<td align="left"><span style="color: #ce3df2">● 누전 여부</span></td>
							<td align="right"><input class="stateElement" size="5"
								id="short_circuit_total" value=""
								onclick="shortCircuitTotalFilter()" readonly="readonly"></input>
								<span class="numberOfLight"> 대 </span></td>
						</tr>
						<tr height="30px">
							<td align="left"><span style="color: #f51e13">● 보안등 고장</span></td>
							<td align="right"><input class="stateElement" size="5"
								id="lamp_failure_total" value=""
								onclick="lampFailureTotalFilter()" readonly="readonly"></input> <span
								class="numberOfLight"> 대 </span></td>
						</tr>
						<tr height="30px">
							<td align="left"><span style="color: #f57128">● 점소등 상태</span></td>
							<td align="right"><input class="stateElement" size="5"
								id="lamp_state_total" value="" onclick="lampStateTotalFilter()"
								readonly="readonly"> <span class="numberOfLight">
									대 </span></td>
						</tr>
						<tr height="30px">
							<td colspan="2" align="right">총<input class="stateElement"
								size="3" id="lamp_total" value="" onclick="lampTotalFilter()"
								readonly="readonly"></input>대
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>
<!-- <input id="id" value=""></input>
<input id="illumination" value=""></input>
<input id="x" value=""></input>
<input id="y" value=""></input> -->

<input type="hidden" id="id" value=""></input>
<input type="hidden" id="illumination" value=""></input>
<input type="hidden" id="x" value=""></input>
<input type="hidden" id="y" value=""></input> 