<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
function addLightOnMarker(location) {

	var image = {
		url : 'img/light_green.png',
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
		//alert(event.latLng+"1");
		addDeleteMarkers();
		searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
	});

	markers.push(marker);
}

function addMarker(location) {

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
		searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
	});
	addDeleteMarkers();
	addMarkers.push(marker);
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
		addDeleteMarkers();
		searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
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
		addDeleteMarkers();
		searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
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
		addDeleteMarkers();
		searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
	});

	markers.push(marker);
}
function addLightLampAlarmMarker(location) {

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
		addDeleteMarkers();
		searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
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
		addDeleteMarkers();
		searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
	});

	markers.push(marker);
	map.panTo(location);
	alarmTimer();
}
function addLightRecentLampAlarmMarker(location) {

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
		addDeleteMarkers();
		searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
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
		addDeleteMarkers();
		searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
	});

	markers.push(marker);
	map.panTo(location);
	alarmTimer();
}

</script>