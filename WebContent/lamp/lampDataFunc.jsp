<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
	function insertLamp() {
		// 동기 방법		
		/* post_to_url('insertLamp.do',{
			'beacon_addr':input_beacon_addr.value,
			'beacon_id':input_beacon_id.value,
			'location':input_location.value,
			'date_time':input_date_time.value,
			'power_off':input_power_off.value,
			'abnormal_blink':input_abnormal_blink.value,
			'short_circuit':input_short_circuit.value,
			'lamp_failure':input_lamp_failure.value,
			'lamp_state':input_lamp_state.value,
			'illumination':input_illumination.value,
			'x':input_x.value,
			'y':input_y.value
		
			}); */
		//비동기 방법
		$("#dialog-add").dialog({
			resizable : true,
			height : "auto",
			width : 400,

			buttons : {
				"추가" : function() {
					$(this).dialog("close");

					if (input_beacon_addr.value == "") {
						dialogLampNull();
						return;
					}
					if (input_beacon_id.value == "") {
						dialogLampNull();
						return;
					}
					if (input_location.value == "") {
						dialogLampNull();
						return;
					}
					if (input_date_time.value == "") {
						dialogLampNull();
						return;
					}
					if (input_power_off.value == "") {
						dialogLampNull();
						return;
					}
					if (input_abnormal_blink.value == "") {
						dialogLampNull();
						return;
					}
					if (input_short_circuit.value == "") {
						dialogLampNull();
						return;
					}
					if (input_lamp_failure.value == "") {
						dialogLampNull();
						return;
					}
					if (input_lamp_state.value == "") {
						dialogLampNull();
						return;
					}
					if (input_x.value == ""){
						dialogLampNullLocation();
						return;
					}
						var request = createJSONHttpRequest();

					request.open('POST', '/light_web/insertLamp.do');

					request.send(makeJson());

				},
				"취소" : function() {
					$(this).dialog("close");

				}
			}
		});
	}
	function updateLamp() {
		$("#dialog-edit").dialog({
			resizable : true,
			height : "auto",
			width : 400,

			buttons : {
				"수정" : function() {
					$(this).dialog("close");
					if (input_id.value == "") {
						dialogEditLampNull();
						return;
					}
					if (input_beacon_addr.value == "") {
						dialogEditLampNull();
						return;
					}
					if (input_beacon_id.value == "") {
						dialogEditLampNull();
						return;
					}
					if (input_location.value == "") {
						dialogEditLampNull();
						return;
					}
					if (input_date_time.value == "") {
						dialogEditLampNull();
						return;
					}
					if (input_power_off.value == "") {
						dialogEditLampNull();
						return;
					}
					if (input_abnormal_blink.value == "") {
						dialogEditLampNull();
						return;
					}
					if (input_short_circuit.value == "") {
						dialogEditLampNull();
						return;
					}
					if (input_lamp_failure.value == "") {
						dialogEditLampNull();
						return;
					}
					if (input_lamp_state.value == "") {
						dialogEditLampNull();
						return;
					}
					if (input_x.value == ""){
						dialogEditLampNullLocation();
						return;
					}
					var request = createJSONHttpRequest();
					request.open('POST', '/light_web/updateLamp.do');
					request.send(makeJson());

				},
				"취소" : function() {
					$(this).dialog("close");

				}
			}
		});

	}
	function deleteLamp() {
		$("#dialog-delete").dialog({
			resizable : true,
			height : "auto",
			width : 400,

			buttons : {
				"삭제" : function() {
					$(this).dialog("close");
					if (input_id.value == "") {
						dialogDeleteLampNull();
						return;
					}
				
					var request = createJSONHttpRequest();
					request.open('POST', '/light_web/deleteLamp.do');
					request.send(makeJson());

				},
				"취소" : function() {
					$(this).dialog("close");

				}
			}
		});
	}
</script>