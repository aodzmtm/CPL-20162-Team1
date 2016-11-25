<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
var json;

var input_id;
var input_beacon_addr;
var input_beacon_id;
var input_location;
var input_date_time;
var input_power_off;
var input_abnormal_blink;
var input_short_circuit;
var input_lamp_failure;
var input_lamp_state;
var input_illumination;
var input_x;
var input_y;

window.onload = function() {

	logInCheck();

	//inputbox
	input_id = document.getElementById("id");
	input_beacon_addr = document.getElementById("beacon_addr");
	input_beacon_id = document.getElementById("beacon_id");
	input_location = document.getElementById("location");
	input_date_time = document.getElementById("date_time");
	input_power_off = document.getElementById("power_off");
	input_abnormal_blink = document.getElementById("abnormal_blink");
	input_short_circuit = document.getElementById("short_circuit");
	input_lamp_failure = document.getElementById("lamp_failure");
	input_lamp_state = document.getElementById("lamp_state");
	input_illumination = document.getElementById("illumination");
	input_x = document.getElementById("x");
	input_y = document.getElementById("y");

	mapDisplay();
};
</script>
<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBegclj2YbPYPTIhCkA6korgBWEth5Q6v8&callback">
</script>
