<!DOCTYPE html>
<html>
<head>
<title>Remove Markers</title>
<style>
html, body {
	height: 100%;
	margin: 0;
	padding: 0;
}

#map {
	height: 100%;
}

#floating-panel {
	position: absolute;
	z-index: 5;
	background-image: url('img/alarm.png');
	width: 100%;
	height: 100%;
}
</style>
</head>
<body>
	<div id="alarm"></div>
	<div id="map"></div>
	<p>Click on the map to add markers.</p>
	<script>
		// In the following example, markers appear when the user clicks on the map.
		// The markers are stored in an array.
		// The user can then click an option to hide, show or delete the markers.
		var map;
		var markers = [];

		function initMap() {
			var haightAshbury = {
				lat : 37.769,
				lng : -122.446
			};

			map = new google.maps.Map(document.getElementById('map'), {
				zoom : 12,
				center : haightAshbury,
				mapTypeId : google.maps.MapTypeId.TERRAIN
			});

			// This event listener will call addMarker() when the map is clicked.
			map.addListener('click', function(event) {
				addMarker(event.latLng);
			});

			// Adds a marker at the center of the map.
			addMarker(haightAshbury);
		}

		// Adds a marker to the map and push to the array.
		function addMarker(location) {
			var marker = new google.maps.Marker({
				position : location,
				map : map
			});
			markers.push(marker);
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

		function alarmTimer() {
			var sum=0;
			var ticker = setInterval(function() {
				onAlarm();
				sum++;
				if(sum == 6)
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

		function offAlarm() {

			var display = document.getElementById("alarm");

			display.innerHTML = "";
		}

		function sleep(milliSeconds) { // 1/1000초
			var startTime = new Date().getTime();
			while (new Date().getTime() < startTime + milliSeconds)
				;
		}
		
	</script>
	
		<script>
		$(function() {
			$("#datepicker").datepicker();
		});
	</script>
<p>

		<!-- 	<p>Date: <input type="text" id="datepicker" value=""></p> -->
	<script async defer
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBegclj2YbPYPTIhCkA6korgBWEth5Q6v8&callback=initMap"></script>
	<!-- <div style="width:100%; height:100%;background-image: url('img/alarm.png');">
   </div> -->
	<input type="submit" value="alarm" onclick="alarmTimer()" />
	<input type="submit" value="send" onclick="alarmTimer2()" />
</body>
</html>
<%-- 
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<meta charset="UTF-8">

<link type="text/css" href="css/footer.css" rel="stylesheet" />
<link type="text/css" href="css/style.css" rel="stylesheet" />

<script type="text/javascript" src="js/util.js"></script>
 --%>



<!-- <div>

    <div style="position: absolute;">

        <div style="position: relative;"> <img src="img/alarm.png"></img></div>

    </div>

    <div style="position: absolute;">

        <div style="position: relative; top: 80px; left: 120px;"><img src="img/light_base.png"></img></div>

    </div>
<img src="img/light_on.png"></img>
   

</div>
 -->