<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<style type="text/css">
html, body {
	height: 100%;
	margin: 0;
	padding: 0;
}

#floating-panel {
	position: absolute;
	z-index: 5;
	background-image: url('img/alarm.png');
	width: 1090px;
	height: 700px;
}
</style>
<link rel="stylesheet" type="text/css" media="screen"
	href="jqueryui/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="jqgrid/css/ui.jqgrid.css" />

<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="js/jquery-1.11.0.min.js"></script>
<script src="jqueryui/jquery-ui.js"></script>
<script src="jqgrid/js/jquery.jqGrid.min.js"></script>
<script src="jqgrid/src/i18n/grid.locale-kr.js"></script>
<script type="text/javascript" src="js/util.js"></script>
<script type="text/javascript">
	//window.onload
	//Lampdata
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
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	function mapDisplay()
	{	
		var display = document.getElementById("map");
		gridNoneDisplay();
		display.style.display = "";
		initMap();
	}
	
	function gridDisplay()
	{
		var display = document.getElementById("grid");
		mapNoneDisplay();
		display.style.display = "";
		getGridRequest();
	}
	
	
	
	
	
	function mapNoneDisplay()
	{	
		var display = document.getElementById("map");
		if(display.style.display != "none")
				display.style.display = "none";
	}
	
	function gridNoneDisplay()
	{
		var display = document.getElementById("grid");
		if(display.style.display != "none")
				display.style.display = "none";
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////	
	//lamp data

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
					alert("데이터를 가져오기 실패");
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
			if (json[i].lamp_failure != '0')
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

			if (json[i].lamp_failure != '0') {
				if (i == recentNum) {
					if (json[i].lamp_failure == '1')
						addLightRecentAlarmMarker(location);
					else if (json[i].lamp_failure == '2')
						addLightRecentBallAlarmMarker(location);
					else if (json[i].lamp_failure == '3')
						addLightRecentLampAlarmMarker(location);
				} else {
					if (json[i].lamp_failure == '1')
						addLightAlarmMarker(location);
					else if (json[i].lamp_failure == '2')
						addLightBallAlarmMarker(location);
					else if (json[i].lamp_failure == '3')
						addLightLampAlarmMarker(location);
				}
			} else {
				if (json[i].power_off == '0')
					addLightOnMarker(location);
				else if (json[i].power_off == '1')
					addLightOffMarker(location);
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
		dataInfo.date_time = new Date().format("yy/MM/dd HH:mm:ss");
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

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//lamp Data insert / update /delete
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/* function getDayToTime() {
		alert(new Date().format("yyyy-MM-dd"));
		alert(new Date().format("yyMMddhhmmss"));
		return
	} */

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
		var request = createJSONHttpRequest();

		request.open('POST', '/light_web/insertLamp.do');

		request.send(makeJson());
	}
	function updateLamp() {
		var request = createJSONHttpRequest();
		request.open('POST', '/light_web/updateLamp.do');
		request.send(makeJson());
	}
	function deleteLamp() {
		var request = createJSONHttpRequest();
		request.open('POST', '/light_web/deleteLamp.do');
		request.send(makeJson());
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//지도
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	var map;

	var markers = [];

	var haightAshbury = {
		//공대 9호관
		lat : 35.886503063295,
		lng : 128.60822339047
	};

	function initMap() {
		var initLocation = {
			lat : 35.886503063295,
			lng : 128.60822339047
		};

		map = new google.maps.Map(document.getElementById('map'), {
			zoom : 17,
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

	function searchLamp(x, y) {
		var flag = 0;
		var i;
		for (var j = 0; j < json.length; j++) {

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

			input_x.value = x;
			input_y.value = y;
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
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//지도 route circle 추가 위험지역
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////

	var routesCircles = [];
	var routesMarkers = [];
	var routeMarkers = [];
	var routeFlag = 0;

	function routeDensity() {
		if (routeFlag == 0) {
			requestNeedLocationJson();
			routeFlag = 1;
		} else {
			deleteCirclesRoute();
			routeFlag = 0;
		}

	}
	function requestNeedLocationJson() {
		var request = createJSONHttpRequest();
		request.open('POST', '/light_web/selectNeedLocation.do');
		//Ajax 요청
		request.send(makeJson());
		request.onreadystatechange = function() {
			if (request.readyState == 4) {
				//응답이 정상이라면
				if (request.status >= 200 && request.status < 300) {
					var str = request.responseText;
					jsonNeedLocationParse(str);
					drawRoute();
				} else
					alert("데이터를 가져오기 실패");
			}
		}
	}
	function jsonNeedLocationParse(str) {
		var subJson = JSON.parse(str);
		var tempDate = 0;
		var recentNum;
		var display = document.getElementById("content");
		for (var i = 0; i < subJson.length; i++) {
			var location = {
				lat : subJson[i].x,
				lng : subJson[i].y
			};
			addNeedLocationMarker(location);
		}
	}
	function addNeedLocationMarker(location) {
		var marker = new google.maps.Marker({
			position : location,
			flag : 0
		});
		routeMarkers.push(marker);
	}
	function deleteCirclesRoute() {
		// Sets the map on all markers in the array.
		for (var i = 0; i < routesCircles.length; i++) {
			routesCircles[i].setMap(null);

		}
		routesCircles = [];
		routesMarkers = [];
		routeMarkers = [];
	}

	function drawRoute() {
		var r = 0.0005;

		for ( var i in routeMarkers) {
			if (routeMarkers[i].flag != '1') {
				routeMarkers[i].flag = '1';
				routesMarkers[i] = routeMarkers[i];
				var x = routesMarkers[i].position.lat();
				var y = routesMarkers[i].position.lng();
				var sum = 1;

				for ( var j in routeMarkers) {
					var flag = 0;
					if (routeMarkers[j].flag != '1') {
						var sx = routeMarkers[j].position.lat();
						var sy = routeMarkers[j].position.lng();
						/* 
							for(var angle=0; angle<=360; angle++)
									{
										var x1=x+(Math.cos(angle)*r);
										var y1=y+(Math.sin(angle)*r);
									} */
						var d = Math.pow(sx - x, 2) + Math.pow(sy - y, 2);
						if (Math.sqrt(d) <= r) {
							flag = 1;
						}

						if (flag == 1) {
							routeMarkers[j].flag = '1';
							sum++;
						}
					}

				}

				routesMarkers[i].total = sum;
			}
		}
		for ( var marker in routesMarkers) {
			var flag = 1;
			for ( var i in markers) {
				var rx = routesMarkers[marker].position.lat()
						- markers[i].position.lat();
				var ry = routesMarkers[marker].position.lng()
						- markers[i].position.lng();
				var rd = Math.pow(rx, 2) + Math.pow(ry, 2);

				if (Math.sqrt(rd) < r) {
					flag = 0;
					break;
				}

			}
			// Add the circle for this city to the map.
			// routeMarkers[marker].populations =10;
			if (flag) {
				var circle = new google.maps.Circle({
					//strokeColor: '#FF0000',
					strokeOpacity : 1.0,
					strokeWeight : 0,
					fillColor : '#FF0000',
					fillOpacity : 0.35,
					map : map,
					center : routesMarkers[marker].position,
					radius : Math.sqrt(routesMarkers[marker].total) * 30
				});
				circle.addListener('click', function(event) {
					searchLamp(event.latLng.lat(), event.latLng.lng());
					//addMarker(event.latLng); 
				});
				routesCircles.push(circle);
			}

		}

	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//지도 Lamp 마커 추가
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	// Adds a marker to the map and push to the array.

	function lampTotalFilter() {
	//	deleteMarkers();
	initMap();
	}
	function powerOffTotalFilter() {
		deleteMarkers();
		for (var i = 0; i < json.length; i++) {

			if (json[i].power_off != 0) {

				var location = {
					lat : json[i].x,
					lng : json[i].y
				};

				var image = {
					url : 'img/light_on.png',
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
					searchLamp(event.latLng.lat(), event.latLng.lng());
				});

				markers.push(marker);

			}
		}
	}
	function abnormalBlinkTotalFilter() {
		deleteMarkers();
		for (var i = 0; i < json.length; i++) {

			if (json[i].abnormal_blink != 0) {
				var location = {
					lat : json[i].x,
					lng : json[i].y
				};
				var image = {
					url : 'img/light_on.png',
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
					searchLamp(event.latLng.lat(), event.latLng.lng());
				});

				markers.push(marker);
			}
		}
	}
	function shortCircuitTotalFilter() {
		deleteMarkers();
		for (var i = 0; i < json.length; i++) {

			if (json[i].short_circuit != 0) {
				var location = {
					lat : json[i].x,
					lng : json[i].y
				};
				var image = {
					url : 'img/light_on.png',
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
					searchLamp(event.latLng.lat(), event.latLng.lng());
				});

				markers.push(marker);
			}
		}
	}
	function lampFailureTotalFilter() {
		deleteMarkers();
		for (var i = 0; i < json.length; i++) {

			if (json[i].lamp_failure != 0) {
				var location = {
					lat : json[i].x,
					lng : json[i].y
				};
				var image = {
					url : 'img/light_on.png',
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
					searchLamp(event.latLng.lat(), event.latLng.lng());
				});

				markers.push(marker);
			}
		}
	}

	
	function lampStateTotalFilter() {
		deleteMarkers();
		for (var i = 0; i < json.length; i++) {

			if (json[i].lamp_state != 0) {
				var location = {
					lat : json[i].x,
					lng : json[i].y
				};
				var image = {
					url : 'img/light_on.png',
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
					searchLamp(event.latLng.lat(), event.latLng.lng());
				});

				markers.push(marker);
			}
		}
	}

	function addLightOnMarker(location) {

		var image = {
			url : 'img/light_on.png',
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
			searchLamp(event.latLng.lat(), event.latLng.lng());
		});

		markers.push(marker);
	}

	function addLightOffMarker(location) {

		var image = {
			url : 'img/light_off.png',
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

		marker.addListener('click', function(event) {
			//alert(event.latLng);
			searchLamp(event.latLng.lat(), event.latLng.lng());
		});

		markers.push(marker);
	}

	function addLightAlarmMarker(location) {

		var image = {
			url : 'img/light_alarm.png',
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

		marker.addListener('click', function(event) {
			//alert(event.latLng);
			searchLamp(event.latLng.lat(), event.latLng.lng());
		});

		markers.push(marker);
	}
	function addLightBallAlarmMarker(location) {

		var image = {
			url : 'img/light_ball.png',
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

		marker.addListener('click', function(event) {
			//alert(event.latLng);
			searchLamp(event.latLng.lat(), event.latLng.lng());
		});

		markers.push(marker);
	}
	function addLightLampAlarmMarker(location) {

		var image = {
			url : 'img/light_lamp.png',
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

		marker.addListener('click', function(event) {
			//alert(event.latLng);
			searchLamp(event.latLng.lat(), event.latLng.lng());
		});

		markers.push(marker);
	}
	function addLightRecentBallAlarmMarker(location) {

		var image = {
			url : 'img/light_ball.png',
			origin : new google.maps.Point(0, 0),
			anchor : new google.maps.Point(34, 34),
			scaledSize : new google.maps.Size(45, 45)
		};

		var marker = new google.maps.Marker({
			position : location,
			//draggable : true,
			animation : google.maps.Animation.BOUNCE,
			map : map,
			icon : image
		});
		//마커 정보 가져오기

		marker.addListener('click', function(event) {
			//alert(event.latLng);
			searchLamp(event.latLng.lat(), event.latLng.lng());
		});

		markers.push(marker);
		map.panTo(location);
		alarmTimer();
	}
	function addLightRecentLampAlarmMarker(location) {

		var image = {
			url : 'img/light_lamp.png',
			origin : new google.maps.Point(0, 0),
			anchor : new google.maps.Point(34, 34),
			scaledSize : new google.maps.Size(45, 45)
		};

		var marker = new google.maps.Marker({
			position : location,
			//draggable : true,
			animation : google.maps.Animation.BOUNCE,
			map : map,
			icon : image
		});
		//마커 정보 가져오기

		marker.addListener('click', function(event) {
			//alert(event.latLng);
			searchLamp(event.latLng.lat(), event.latLng.lng());
		});

		markers.push(marker);
		map.panTo(location);
		alarmTimer();
	}

	function addLightRecentAlarmMarker(location) {

		var image = {
			url : 'img/light_alarm.png',
			origin : new google.maps.Point(0, 0),
			anchor : new google.maps.Point(34, 34),
			scaledSize : new google.maps.Size(45, 45)
		};

		var marker = new google.maps.Marker({
			position : location,
			//draggable : true,
			animation : google.maps.Animation.BOUNCE,
			map : map,
			icon : image
		});
		//마커 정보 가져오기

		marker.addListener('click', function(event) {
			//alert(event.latLng);
			searchLamp(event.latLng.lat(), event.latLng.lng());
		});

		markers.push(marker);
		map.panTo(location);
		alarmTimer();
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*alaram timer*/
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	function alarmTimer() {
		var sum = 0;
		var ticker = setInterval(function() {
			onAlarm();
			sum++;
			if (sum == 6)
				clearInterval(ticker);
		}, 1000);
	}
	function onAlarm() {

		var display = document.getElementById("alarm");
		if (display.innerHTML == "")
			display.innerHTML = "<div id=\"floating-panel\"></div>";
		else
			display.innerHTML = "";
	}
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
		function getGridRequest() {
			var request = createJSONHttpRequest();
			var display = document.getElementById('grid');
			request.open('POST', '/light_web/loadGrid.jsp');
			//Ajax 요청
			request.send();
			request.onreadystatechange = function() {
				if (request.readyState == 4) {
					//응답이 정상이라면
					if (request.status >= 200 && request.status < 300) {

						var str = request.responseText;
						alert(str);
						map=null;
						display.innerHTML = str;
						lampGrid();
						lampHistoryGrid();
					} else
						alert("데이터를 가져오기 실패");
				}
			}

		}

		function lampGrid() {

			$("#jqGrid")
					.jqGrid(
							{
								url : "/light_web/lampInfoGridJson.do",
								editurl : "/light_web/lampInfoGridEdit.do",//modify
								mtype : "POST",
								caption : "ppap",
								datatype : "json",
								loadtext : "로딩중..",
								width : 1070,
								height : 200,
								rowNum : 10,
								pager : "#jqGridPager",
								page : 1,
								colModel : [
										{
											label : "id",
											name : 'id',
											align : "center",
											sorttype : 'integer',
											key : true,
											hidden : true
										},
										{
											label : "비컨주소",
											name : "beacon_addr",
											align : "center",
											editable : true,
										},
										{
											label : "비컨id",
											name : "beacon_id",
											align : "center",
											editable : true,
										},
										{
											label : "위치",
											name : "location",
											align : "center",
											editable : true,
										// stype defines the search type control - in this case HTML select (dropdownlist)
										//stype: "select",
										// searchoptions value - name values pairs for the dropdown - they will appear as options
										//searchoptions: { value: ":[All];ALFKI:ALFKI;ANATR:ANATR;ANTON:ANTON;AROUT:AROUT;BERGS:BERGS;BLAUS:BLAUS;BLONP:BLONP;BOLID:BOLID;BONAP:BONAP;BOTTM:BOTTM;BSBEV:BSBEV;CACTU:CACTU;CENTC:CENTC;CHOPS:CHOPS;COMMI:COMMI;CONSH:CONSH;DRACD:DRACD;DUMON:DUMON;EASTC:EASTC;ERNSH:ERNSH;FAMIA:FAMIA;FOLIG:FOLIG;FOLKO:FOLKO;FRANK:FRANK;FRANR:FRANR;FRANS:FRANS;FURIB:FURIB;GALED:GALED;GODOS:GODOS;GOURL:GOURL;GREAL:GREAL;GROSR:GROSR;HANAR:HANAR;HILAA:HILAA;HUNGC:HUNGC;HUNGO:HUNGO;ISLAT:ISLAT;KOENE:KOENE;LACOR:LACOR;LAMAI:LAMAI;LAUGB:LAUGB;LAZYK:LAZYK;LEHMS:LEHMS;LETSS:LETSS;LILAS:LILAS;LINOD:LINOD;LONEP:LONEP;MAGAA:MAGAA;MAISD:MAISD;MEREP:MEREP;MORGK:MORGK;NORTS:NORTS;OCEAN:OCEAN;OLDWO:OLDWO;OTTIK:OTTIK;PERIC:PERIC;PICCO:PICCO;PRINI:PRINI;QUEDE:QUEDE;QUEEN:QUEEN;QUICK:QUICK;RANCH:RANCH;RATTC:RATTC;REGGC:REGGC;RICAR:RICAR;RICSU:RICSU;ROMEY:ROMEY;SANTG:SANTG;SAVEA:SAVEA;SEVES:SEVES;SIMOB:SIMOB;SPECD:SPECD;SPLIR:SPLIR;SUPRD:SUPRD;THEBI:THEBI;THECR:THECR;TOMSP:TOMSP;TORTU:TORTU;TRADH:TRADH;TRAIH:TRAIH;VAFFE:VAFFE;VICTE:VICTE;VINET:VINET;WANDK:WANDK;WARTH:WARTH;WELLI:WELLI;WHITC:WHITC;WILMK:WILMK;WOLZA:WOLZA" }
										},

										{
											label : "날짜",
											name : "date_time",
											align : "center",
											editable : true,
											search : true,

											sorttype : 'date',
											datatype : "local",
											editoptions : {
												dataInit : function(element) {
													$(element)
															.val(
																	new Date()
																			.format("yy/MM/dd HH:mm:ss"));
												},
												dataEvents : [ {
													type : 'click',
													fn : function(e) { // 값 : this.value || e.target.val()
														this.value = new Date().format("yy/MM/dd HH:mm:ss");
													},
												} ]

											},
											searchoptions : {
												dataInit : function(element) {
													$(element)
															.datepicker(
																	{
																		id : 'orderDate_datePicker',
																		dateFormat : 'yy-mm-dd',
																		maxDate : new Date(
																				2020,
																				0,
																				1),
																		showOn : 'focus'
																	});
												}
											}
										},
										{
											label : "정전상태",
											name : "power_off",
											align : "center",
											editable : true,
											edittype : "select",
											editoptions : {
												value : "0:점등;1:정전"
											},
											formatter : 'select',
											search : true,
											stype : "select",
											searchoptions : {
												value : "0:점등;1:정전"
											}
										},
										{
											label : "이상점등",
											name : "abnormal_blink",
											align : "center",
											editable : true,
											edittype : "select",
											editoptions : {
												value : "0:정상;1:이상점등;2:이상소등"
											},
											formatter : 'select',
											search : true,
											stype : "select",
											searchoptions : {
												value : "0:정상;1:이상점등;2:이상소등"
											}
										},
										{
											label : "누전여부",
											name : "short_circuit",
											align : "center",
											editable : true,
											edittype : "select",
											editoptions : {
												value : "0:정상;1:누전"
											},
											formatter : 'select',
											search : true,
											stype : "select",
											searchoptions : {
												value : "0:정상;1:누전"
											}
										},
										{
											label : "보안등고장",
											name : "lamp_failure",
											align : "center",
											editable : true,
											edittype : "select",
											editoptions : {
												value : "0:정상;1:램프고장;2:안정기 고장;3:램프 안정기 고장"
											},
											formatter : 'select',
											search : true,
											stype : "select",
											searchoptions : {
												value : "0:정상;1:램프고장;2:안정기 고장;3:램프 안정기 고장"
											}
										},
										{
											label : "점소등상태",
											name : "lamp_state",
											align : "center",
											editable : true,
											edittype : "select",
											editoptions : {
												value : "0:정상소등;1:정상 점등;2:강제소등;3:강제 점등"
											},
											formatter : 'select',
											search : true,
											stype : "select",
											searchoptions : {
												value : "0:정상소등;1:정상 점등;2:강제소등;3:강제 점등"
											}
										},

										{

											label : "비고",
											width : 90,
											name : "actions",
											align : "center",
											formatter : "actions",
											formatoptions : {
												editOptions : {},
												delOptions : {}
											}
										}, ],
								//gridComplete :,
								rowList : [ 20, 30, 50 ],
								multiselect : true,
								rownumbers : true, // row의 숫자를 표시해준다.
								loadonce : true,
								viewrecords : true
							});
			$('#jqGrid').navGrid("#jqGridPager", {
				search : true, // show search button on the toolbar
				add : false,
				edit : false,
				del : false,
				refresh : true
			}, {}, // edit options
			{}, // add options
			{
				errorTextFormat : function(data) {
					return 'Error: ' + data.responseText
				}
			}, // delete options
			{
				multipleSearch : true,
				uniqueSearchFields : true,
				multipleGroup : true
			});
			/* resizeJqGridWidth('jqGrid', 'grid_container', $('#grid_container')
					.width(), true);  */
			//jQuery("#jqGrid").jqGrid('gridResize',{minWidth:350,maxWidth:850,minHeight:80, maxHeight:350});
		}

		function resizeJqGridWidth(grid_id, div_id, width, tf) {
			$(window).bind('resize', function() {

				var resizeWidth = 1050;
				$('#' + grid_id).setGridWidth(resizeWidth, tf);
				$('#' + grid_id).setGridWidth(resizeWidth, tf);
			}).trigger('resize');
		}
		function startEdit() {
			var grid = $("#jqGrid");
			var ids = grid.jqGrid('getDataIDs');
			for (var i = 0; i < ids.length; i++) {
				grid.jqGrid('editRow', ids[i]);
			}
		};

		function saveRows() {
			var grid = $("#jqGrid");
			var ids = grid.jqGrid('getDataIDs');

			for (var i = 0; i < ids.length; i++) {
				grid.jqGrid('delRowData', ids[i]);
			}
		}
		function batchDelete() {
			var grid = $("#jqGrid");
			var rowKey = grid.getGridParam("selrow");
			var request = createJSONHttpRequest();
			request.open('POST', '/light_web/lampInfoGridDelete.do');
			//Ajax 요청
			request.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");

			request.setRequestHeader("Cache-Control",
					"no-cache, must-revalidate");

			request.setRequestHeader("Pragma", "no-cache");
			if (!rowKey)
				alert("No rows are selected");
			else {
				var selectedIDs = grid.getGridParam("selarrrow");
				var result = "";
				for (var i = 0; i < selectedIDs.length - 1; i++) {
					result += selectedIDs[i] + "/";
				}
				result += selectedIDs[i];
				request.send("idstr=" + result);

				request.onreadystatechange = function() {
					if (request.readyState == 4) {
						//응답이 정상이라면
						if (request.status >= 200 && request.status < 300) {
							alert("cc");
							grid.trigger("reloadGrid");
						} else
							alert("데이터를 가져오기 실패");
					}
				}
				// grid.trigger("reloadGrid");
			}
		}
		function batchSave() {
			var grid = $("#jqGrid");
			var rowKey = grid.getGridParam("selrow");

			if (!rowKey)
				alert("No rows are selected");
			else {
				var selectedIDs = grid.getGridParam("selarrrow");
				var result = "";
				for (var i = 0; i < selectedIDs.length; i++) {
					grid.jqGrid('saveRow', selectedIDs[i]);
				}
				grid.trigger("reloadGrid");
			}
		}
		function batchEdit() {
			var grid = $("#jqGrid");
			var rowKey = grid.getGridParam("selrow");

			if (!rowKey)
				alert("No rows are selected");
			else {
				var selectedIDs = grid.getGridParam("selarrrow");
				var result = "";
				for (var i = 0; i < selectedIDs.length; i++) {
					grid.jqGrid('editRow', selectedIDs[i], true);
				}
			}
		}

		function batchCancelEdit() {
			var grid = $("#jqGrid");
			var grid = $("#jqGrid");
			var rowKey = grid.getGridParam("selrow");

			if (!rowKey)
				alert("No rows are selected");
			else {
				var selectedIDs = grid.getGridParam("selarrrow");
				var result = "";
				for (var i = 0; i < selectedIDs.length; i++) {
					grid.jqGrid('restoreRow', selectedIDs[i], true);
				}
			}
		}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//grid
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		function lampHistoryGrid() {

			$("#sjqGrid")
					.jqGrid(
							{
								url : "/light_web/historyInfoGridJson.do",
								editurl : "/light_web/historyInfoGridEdit.do",//modify
								mtype : "POST",
								caption : "history",
								datatype : "json",
								loadtext : "로딩중..",
								width : 1070,
								height : 200,
								rowNum : 10,
								pager : "#sjqGridPager",

								// postData : {date_time:"abc"},
								page : 1,
								//colNames : [ '시퀀스', '제목','날짜','수정/삭제' ],
								colModel : [
										{
											label : "id",
											name : 'id',
											align : "center",
											sorttype : 'integer',
											key : true,
											hidden : true
										},
										{
											label : "비컨주소",
											name : "beacon_addr",
											align : "center",
											editable : false,
										},
										{
											label : "비컨id",
											name : "beacon_id",
											align : "center",
											editable : false,
										},
										{
											label : "위치",
											name : "location",
											align : "center",
											editable : false,
										},

										{
											label : "날짜",
											name : "date_time",
											align : "center",
											editable : false,
											search : true,

											sorttype : 'date',
											datatype : "local",
											editoptions : {
												dataInit : function(element) {
													$(element)
															.val(
																	new Date()
																			.format("yy/MM/dd HH:mm:ss"));
												},
												dataEvents : [ {
													type : 'click',
													fn : function(e) { // 값 : this.value || e.target.val()
														this.value = new Date()
																.format("yy/MM/dd HH:mm:ss");
													},
												} ]

											},
											searchoptions : {
												dataInit : function(element) {
													$(element)
															.datepicker(
																	{
																		id : 'orderDate_datePicker',
																		dateFormat : 'yy-mm-dd',
																		maxDate : new Date(
																				2020,
																				0,
																				1),
																		showOn : 'focus'
																	});
												}
											}
										},
										{
											label : "장애원인",
											name : "failure_reason_id",
											align : "center",
											editable : false,
											edittype : "select",
											editoptions : {
												value : "0:정상;1:정전;2:이상점등;3:이상소등;4:누전;5:램프고장;6:안정기고장;7:램프안정기고장;8:강제소등;9:강제점등"
											},
											formatter : 'select',
											search : true,
											stype : "select",
											searchoptions : {
												value : "0:정상;1:정전;2:이상점등;3:이상소등;4:누전;5:램프고장;6:안정기고장;7:램프안정기고장;8:강제소등;9:강제점등"
											}
										}, {
											label : "장애처리",
											name : "repair",
											align : "center",
											editable : true,
											edittype : "select",
											editoptions : {
												value : "0:수리;1:고장"
											},
											formatter : 'select',
											search : true,
											stype : "select",
											searchoptions : {
												value : "0:수리;1:고장"
											}
										}, {

											label : "비고",
											width : 70,
											name : "actions",
											align : "center",
											formatter : "actions",
											formatoptions : {
												editOptions : {},
												delOptions : {}
											}
										}, ],
								rowList : [ 20, 30, 50 ],
								multiselect : true,
								rownumbers : true, // row의 숫자를 표시해준다.
								loadonce : true,
								viewrecords : true
							});
			$("#sjqGrid").navGrid("#sjqGridPager", {
				search : true, // show search button on the toolbar
				add : false,
				edit : false,
				del : false,
				refresh : true
			}, {}, // edit options
			{}, // add options
			{
				errorTextFormat : function(data) {
					return 'Error: ' + data.responseText
				}
			}, // delete options
			{
				multipleSearch : true,
				uniqueSearchFields : true,
				multipleGroup : true
			});
		}

		function historyBatchDelete() {
			var grid = $("#sjqGrid");
			var rowKey = grid.getGridParam("selrow");
			var request = createJSONHttpRequest();
			request.open('POST', '/light_web/historyInfoGridDelete.do');
			//Ajax 요청
			request.setRequestHeader("Content-Type",
					"application/x-www-form-urlencoded;charset=UTF-8");

			request.setRequestHeader("Cache-Control",
					"no-cache, must-revalidate");

			request.setRequestHeader("Pragma", "no-cache");
			if (!rowKey)
				alert("No rows are selected");
			else {
				var selectedIDs = grid.getGridParam("selarrrow");
				var result = "";
				for (var i = 0; i < selectedIDs.length - 1; i++) {
					result += selectedIDs[i] + "/";
				}
				result += selectedIDs[i];
				request.send("idstr=" + result);
				request.onreadystatechange = function() {
					if (request.readyState == 4) {
						//응답이 정상이라면
						if (request.status >= 200 && request.status < 300) {
				
							getGridRequest();
							//grid.trigger("reloadGrid");
						} else
							alert("데이터를 가져오기 실패");
					}
				}
				// grid.trigger("reloadGrid");
				//alert(result);
			}
		}
		function historyBatchSave() {
			var grid = $("#sjqGrid");
			var rowKey = grid.getGridParam("selrow");

			if (!rowKey)
				alert("No rows are selected");
			else {
				var selectedIDs = grid.getGridParam("selarrrow");
				var result = "";
				for (var i = 0; i < selectedIDs.length; i++) {
					grid.jqGrid('saveRow', selectedIDs[i]);
				}
				grid.trigger("reloadGrid");
			}
		}
		function historyBatchEdit() {
			var grid = $("#sjqGrid");
			var rowKey = grid.getGridParam("selrow");

			if (!rowKey)
				alert("No rows are selected");
			else {
				var selectedIDs = grid.getGridParam("selarrrow");
				var result = "";
				for (var i = 0; i < selectedIDs.length; i++) {
					grid.jqGrid('editRow', selectedIDs[i], true);
				}
			}
		}

		function historyBatchCancelEdit() {
			var grid = $("#sjqGrid");
			var grid = $("#sjqGrid");
			var rowKey = grid.getGridParam("selrow");

			if (!rowKey)
				alert("No rows are selected");
			else {
				var selectedIDs = grid.getGridParam("selarrrow");
				var result = "";
				for (var i = 0; i < selectedIDs.length; i++) {
					grid.jqGrid('restoreRow', selectedIDs[i], true);
				}
			}
		}
	
	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
</script>


<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBegclj2YbPYPTIhCkA6korgBWEth5Q6v8&callback">
	
</script>
</head>
<body>
	<div id="alarm"></div>
	<div id="map" style="width: 100%; height: 700px;"></div>
<div id="grid" style="width: 100%; height: 700px;"></div>

</body>
<script type="text/javascript">
	//map
	/* 
	 lat : 40.714224,
	 lng : -73.997
	 lat: -25.363, lng: 131.044
	 */
	//var x, y;
	//ajax 요청
</script>

<div id="content"></div>
<div id="geo"></div>
<!-- id, 
	beacon_addr, 
	beacon_id, 
	location, 
	date_time, 
	power_off, 
	abnormal_blink, 
	short_circuit, 
	lamp_failure, 
	lamp_state, 
	illumination, 
	x, 
	y -->

<input id="id" value=""></input>
<input id="beacon_addr" value=""></input>
<input id="beacon_id" value=""></input>
<input id="location" value=""></input>
<input id="date_time" value=""></input>
<input id="power_off" value=""></input>
<input id="abnormal_blink" value=""></input>
<input id="short_circuit" value=""></input>
<input id="lamp_failure" value=""></input>
<input id="lamp_state" value=""></input>
<input id="illumination" value=""></input>
<input id="x" value=""></input>
<input id="y" value=""></input>

<fieldset>
	<textarea id="messageWindow" rows="10" cols="50" readonly="true"
		style="width: 100%;"></textarea>
	<br /> <input id="inputMessage" type="text" /> <input type="submit"
		value="mapreload" onclick="mapDisplay()" /> <input type="submit" value="send"
		onclick="gridDisplay()" /> <input type="submit" value="insert"
		onclick="insertLamp()" /> <input type="submit" value="edit"
		onclick="updateLamp()" /> <input type="submit" value="delete"
		onclick="deleteLamp()" /> <input type="submit" value="deleteMarkers"
		onclick="deleteMarkers()" /> <input type="submit" value="alarm"
		onclick="alarmTimer()" /><input type="submit" value="getDate"
		onclick="getDayToTime()" /> <input type="submit" value="routeDensity"
		onclick="routeDensity()" /> 
		
<!-- 			lampTotalFilter()
	powerOffTotalFilter()
	lampStateTotalFilter()
	lampFailureTotalFilter()
	shortCircuitTotalFilter()
	abnormalBlinkTotalFilter() -->
	<input id="lamp_total" value="" onclick="lampTotalFilter()"></input>
	<input id="power_off_total" value=""onclick="powerOffTotalFilter()"></input> <input
		id="abnormal_blink_total" value=""onclick="abnormalBlinkTotalFilter()"></input> <input
		id="short_circuit_total" value=""onclick="shortCircuitTotalFilter()"></input> <input
		id="lamp_failure_total" value=""onclick="lampFailureTotalFilter()"></input> <input id="lamp_state_total"
		value="" onclick="lampStateTotalFilter()"></input>
</fieldset>
</html>