<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
	function requestJson() {
		var request = createJSONHttpRequest();

		request.open('POST', '/light_web/lampData.do');
		//Ajax 요청
		request.send(makeJson());
		request.onreadystatechange = function() {
			if (request.readyState == 4) {
				//응답이 정상이라면
				if (request.status >= 200 && request.status < 300) {

					var str = request.responseText;

					//JSON 문자열 파싱
					jsonParse(str);

				} else
					dialogLogInCheck();
			}
		}
	}

	//JSON 문자열 파싱

	function jsonParse(str) {
		json = JSON.parse(str);
		
		var lamp_total = 0;
		var power_off_total = 0;
		var abnormal_blink_total = 0;
		var short_circuit_total = 0;
		var lamp_failure_total = 0;
		var lamp_state_total = 0;

		var tempDate = 0;
		var recentNum;
		var display = document.getElementById("content");
		for (var i = 0; i < json.length; i++) {
		
			if (json[i].lamp_failure != '0' 
				|| json[i].power_off != '0'
				|| json[i].abnormal_blink != '0'
				|| json[i].short_circuit != '0'
				|| json[i].lamp_state != '0')
				if (tempDate <= exceptStrDateTime(json[i].date_time)) {
					tempDate = exceptStrDateTime(json[i].date_time);
					recentNum = i;
				}
		}
		for (var i = 0; i < json.length; i++) {
			/* 	
				display.innerHTML += json[i].id + ":" + json[i].beacon_addr + ":"
						+ json[i].beacon_id + ":" + json[i].location + ":"
						+ json[i].date_time + ":" + json[i].power_off + ":"
						+ json[i].abnormal_blink + ":" + json[i].short_circuit
						+ ":" + json[i].lamp_failure + ":" + json[i].lamp_state
						+ ":" + json[i].illumination + ":" + json[i].x + ":"
						+ json[i].y + "\n"
			 */var location = {
				lat : json[i].x,
				lng : json[i].y
			};

			if (json[i].lamp_failure != '0' 
					|| json[i].power_off != '0'
					|| json[i].abnormal_blink != '0'
					|| json[i].short_circuit != '0'
					|| json[i].lamp_state != '0') {
				if (i == recentNum) {
					/* if (json[i].lamp_failure == '1')
						addLightRecentAlarmMarker(location);
					else if (json[i].lamp_failure == '2')
						addLightRecentBallAlarmMarker(location);
					else if (json[i].lamp_failure == '3')
						addLightRecentLampAlarmMarker(location); */

					addLightRecentLampAlarmMarker(location);

				} else {
					/* if (json[i].lamp_failure == '1')
						addLightAlarmMarker(location);
					else if (json[i].lamp_failure == '2')
						addLightBallAlarmMarker(location);
					else if (json[i].lamp_failure == '3')
						addLightLampAlarmMarker(location); */

					addLightLampAlarmMarker(location);

				}
			} else {
				addLightOnMarker(location);
			
			}

			if (json[i].power_off != 0) {
				power_off_total += 1;
			}
			if (json[i].abnormal_blink != 0) {
				abnormal_blink_total += 1;
			}
			if (json[i].short_circuit != 0) {
				short_circuit_total += 1;
			}
			if (json[i].lamp_failure != 0) {
				lamp_failure_total += 1;
			}
			if (json[i].lamp_state != 0) {
				lamp_state_total += 1;
			}

		}
		lamp_total = json.length;
		document.getElementById("lamp_total").value = lamp_total;
		document.getElementById("power_off_total").value = power_off_total;
		document.getElementById("abnormal_blink_total").value = abnormal_blink_total;
		document.getElementById("short_circuit_total").value = short_circuit_total;
		document.getElementById("lamp_failure_total").value = lamp_failure_total;
		document.getElementById("lamp_state_total").value = lamp_state_total;

		//	addLightRecentAlarmMarker(location);
	}

	function makeJson() {
		var totalInfo = new Object();
		var dataArray = new Array();
		var dataInfo = new Object();
		dataInfo.id = input_id.value;
		dataInfo.beacon_addr = input_beacon_addr.value;
		dataInfo.beacon_id = input_beacon_id.value;
		dataInfo.location = input_location.value;
		dataInfo.date_time = input_date_time.value;
		dataInfo.power_off = input_power_off.value;
		dataInfo.abnormal_blink = input_abnormal_blink.value;
		dataInfo.short_circuit = input_short_circuit.value;
		dataInfo.lamp_failure = input_lamp_failure.value;
		dataInfo.lamp_state = input_lamp_state.value;
		dataInfo.illumination = input_illumination.value;
		dataInfo.x = input_x.value;
		dataInfo.y = input_y.value;
		dataArray.push(dataInfo);
		totalInfo.LampVo = dataArray;
		var jsonInfo = JSON.stringify(totalInfo);
		//  alert(jsonInfo);
		return jsonInfo;
	}
</script>