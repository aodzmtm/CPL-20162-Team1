<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
	function lampTotalFilter() {
		//	deleteMarkers();
		input_id.value = "";
		input_beacon_addr.value = "";
		input_beacon_id.value = "";
		input_location.value = "";
		input_date_time.value = "";
		input_power_off.value = "";
		input_abnormal_blink.value = "";
		input_short_circuit.value = "";
		input_lamp_failure.value = "";
		input_lamp_state.value = "";
		input_illumination.value = "";
		input_x.value = x;
		input_y.value = y;
		addDeleteMarkers();
		initMap();
	}
	function powerOffTotalFilter() {
		input_id.value = "";
		input_beacon_addr.value = "";
		input_beacon_id.value = "";
		input_location.value = "";
		input_date_time.value = "";
		input_power_off.value = "";
		input_abnormal_blink.value = "";
		input_short_circuit.value = "";
		input_lamp_failure.value = "";
		input_lamp_state.value = "";
		input_illumination.value = "";
		input_x.value = x;
		input_y.value = y;
		addDeleteMarkers();
		deleteMarkers();
		for (var i = 0; i < json.length; i++) {

			if (json[i].power_off != 0) {

				var location = {
					lat : json[i].x,
					lng : json[i].y
				};

				var image = {
					url : 'img/light_gray.png',
					size : new google.maps.Size(462, 462),
					origin : new google.maps.Point(0, 0),
					anchor : new google.maps.Point(34, 34),
					scaledSize : new google.maps.Size(45, 45)
				};

				var marker = new google.maps.Marker({
					position : location,
					map : map,
					icon : image
				});
				//마커 정보 가져오기

				//삭제시 쓴다.

				marker.addListener('click', function(event) {
					//alert(event.latLng);
					addDeleteMarkers();
					searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
				});

				markers.push(marker);

			}
		}
	}
	function abnormalBlinkTotalFilter() {
		input_id.value = "";
		input_beacon_addr.value = "";
		input_beacon_id.value = "";
		input_location.value = "";
		input_date_time.value = "";
		input_power_off.value = "";
		input_abnormal_blink.value = "";
		input_short_circuit.value = "";
		input_lamp_failure.value = "";
		input_lamp_state.value = "";
		input_illumination.value = "";
		input_x.value = x;
		input_y.value = y;
		addDeleteMarkers();
		deleteMarkers();
		for (var i = 0; i < json.length; i++) {

			if (json[i].abnormal_blink != 0) {
				var location = {
					lat : json[i].x,
					lng : json[i].y
				};
				var image = {
					url : 'img/light_yellow.png',
					size : new google.maps.Size(462, 462),
					origin : new google.maps.Point(0, 0),
					anchor : new google.maps.Point(34, 34),
					scaledSize : new google.maps.Size(45, 45)
				};

				var marker = new google.maps.Marker({
					position : location,
					map : map,
					icon : image
				});
				//마커 정보 가져오기

				//삭제시 쓴다.

				marker.addListener('click', function(event) {
					//alert(event.latLng);
					addDeleteMarkers();
					searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
				});

				markers.push(marker);
			}
		}
	}
	function shortCircuitTotalFilter() {
		input_id.value = "";
		input_beacon_addr.value = "";
		input_beacon_id.value = "";
		input_location.value = "";
		input_date_time.value = "";
		input_power_off.value = "";
		input_abnormal_blink.value = "";
		input_short_circuit.value = "";
		input_lamp_failure.value = "";
		input_lamp_state.value = "";
		input_illumination.value = "";
		input_x.value = x;
		input_y.value = y;
		addDeleteMarkers();
		deleteMarkers();
		for (var i = 0; i < json.length; i++) {

			if (json[i].short_circuit != 0) {
				var location = {
					lat : json[i].x,
					lng : json[i].y
				};
				var image = {
					url : 'img/light_puple.png',
					size : new google.maps.Size(462, 462),
					origin : new google.maps.Point(0, 0),
					anchor : new google.maps.Point(34, 34),
					scaledSize : new google.maps.Size(45, 45)
				};

				var marker = new google.maps.Marker({
					position : location,
					map : map,
					icon : image
				});
				//마커 정보 가져오기

				//삭제시 쓴다.

				marker.addListener('click', function(event) {
					//alert(event.latLng);
					addDeleteMarkers();
					searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
				});

				markers.push(marker);
			}
		}
	}
	function lampFailureTotalFilter() {
		input_id.value = "";
		input_beacon_addr.value = "";
		input_beacon_id.value = "";
		input_location.value = "";
		input_date_time.value = "";
		input_power_off.value = "";
		input_abnormal_blink.value = "";
		input_short_circuit.value = "";
		input_lamp_failure.value = "";
		input_lamp_state.value = "";
		input_illumination.value = "";
		input_x.value = x;
		input_y.value = y;
		addDeleteMarkers();
		deleteMarkers();
		for (var i = 0; i < json.length; i++) {

			if (json[i].lamp_failure != 0) {
				var location = {
					lat : json[i].x,
					lng : json[i].y
				};
				var image = {
					url : 'img/light_alarm.png',
					size : new google.maps.Size(462, 462),
					origin : new google.maps.Point(0, 0),
					anchor : new google.maps.Point(34, 34),
					scaledSize : new google.maps.Size(45, 45)
				};

				var marker = new google.maps.Marker({
					position : location,
					map : map,
					icon : image
				});
				//마커 정보 가져오기

				//삭제시 쓴다.

				marker.addListener('click', function(event) {
					//alert(event.latLng);
					addDeleteMarkers();
					searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
				});

				markers.push(marker);
			}
		}
	}

	function lampStateTotalFilter() {
		input_id.value = "";
		input_beacon_addr.value = "";
		input_beacon_id.value = "";
		input_location.value = "";
		input_date_time.value = "";
		input_power_off.value = "";
		input_abnormal_blink.value = "";
		input_short_circuit.value = "";
		input_lamp_failure.value = "";
		input_lamp_state.value = "";
		input_illumination.value = "";
		input_x.value = x;
		input_y.value = y;
		addDeleteMarkers();
		deleteMarkers();
		for (var i = 0; i < json.length; i++) {

			if (json[i].lamp_state != 0) {
				var location = {
					lat : json[i].x,
					lng : json[i].y
				};
				var image = {
					url : 'img/light_orange.png',
					size : new google.maps.Size(462, 462),
					origin : new google.maps.Point(0, 0),
					anchor : new google.maps.Point(34, 34),
					scaledSize : new google.maps.Size(45, 45)
				};

				var marker = new google.maps.Marker({
					position : location,
					map : map,
					icon : image
				});
				//마커 정보 가져오기

				//삭제시 쓴다.

				marker.addListener('click', function(event) {
					//alert(event.latLng);
					addDeleteMarkers();
					searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
				});

				markers.push(marker);
			}
		}
	}
</script>