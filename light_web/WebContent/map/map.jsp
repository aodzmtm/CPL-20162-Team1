<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
	var map;

	var markers = [];
	var addMarkers = [];

	/* var haightAshbury = {
	 //공대 9호관
	 lat : 35.886503063295,
	 lng : 128.60822339047
	 };
	 */
	function initMap() {
		var initLocation = {
			lat : 35.886503063295,
			lng : 128.60822339047
		};

		map = new google.maps.Map(document.getElementById('map'), {
			zoom : 18,
			center : initLocation,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		});

		// This event listener will call addMarker() when the map is clicked.
		map.addListener('click', function(event) {
			//alert(event.latLng.lat());
			searchLamp(event.latLng.lat(), event.latLng.lng());

			//addMarker(event.latLng);
		});

		// Adds a marker at the center of the map.
		//addMarker(initLocation);
		requestJson();

	}
	function roundLocation(x) {
		return Math.round(x * 1000000) / 1000000;
		//return x; <- 나중에  좌표값이 열몇자리가 필요해 질 때 이걸 사용함.
	}
	function searchLamp(x, y) {

		var flag = 0;
		var i;
		try {
			json.length;
		} catch (e) {
			return;
		}
		if (x != "" && y != "") {
			x = roundLocation(x);
			y = roundLocation(y);
		}
		for (var j = 0; j < json.length; j++) {
			/* 		var u = Math.round(x*100000) / 100000;
			 var v = Math.round(y*100000)/ 100000;
			 var x = Math.round(json[j].x * 100000)/ 100000;
			 var y = Math.round(json[j].y * 100000)/ 100000;
			
			 //if(u == x && v == y)*/
			if (json[j].x == x && json[j].y == y) {

				flag = 1;
				i = j;
				break;
			}
		}

		if (flag == 1) {
			input_id.value = json[i].id;
			input_beacon_addr.value = json[i].beacon_addr;
			input_beacon_id.value = json[i].beacon_id;
			input_location.value = json[i].location;
			input_date_time.value = json[i].date_time;
			input_power_off.value = json[i].power_off;
			input_abnormal_blink.value = json[i].abnormal_blink;
			input_short_circuit.value = json[i].short_circuit;
			input_lamp_failure.value = json[i].lamp_failure;
			input_lamp_state.value = json[i].lamp_state;
			input_illumination.value = json[i].illumination;
			input_x.value = json[i].x;
			input_y.value = json[i].y;
			return;
		} else {
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
			var location = {
				lat : x,
				lng : y
			};
			addMarker(location);
		}
	}

	//toggleBounce
	function toggleBounce() {
		if (marker.getAnimation() !== null) {
			marker.setAnimation(null);
		} else {
			marker.setAnimation(google.maps.Animation.BOUNCE);
		}
	}

	// Sets the map on all markers in the array.
	function setMapOnAll(map) {
		for (var i = 0; i < markers.length; i++) {
			markers[i].setMap(map);
		}
	}
	function addSetMapOnAll(map) {
		for (var i = 0; i < addMarkers.length; i++) {
			addMarkers[i].setMap(map);
		}
	}
	// Removes the markers from the map, but keeps them in the array.
	function clearMarkers() {
		setMapOnAll(null);
	}

	// Shows any markers currently in the array.
	function showMarkers() {
		setMapOnAll(map);
	}

	// Deletes all markers in the array by removing references to them.
	function deleteMarkers() {
		clearMarkers();
		markers = [];
	}
	//Deletes all markers in the array by removing references to them.
	function addDeleteMarkers() {
		addSetMapOnAll(null);
		addMarkers = [];
	}
</script>