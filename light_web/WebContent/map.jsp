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
</style>

<script type="text/javascript">
	
</script>
</head>
<body>
	<div id="map" style="width: 100%; height: 700px;"></div>


	<script async defer
		src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBegclj2YbPYPTIhCkA6korgBWEth5Q6v8&callback=initMap">
		
	</script>

</body>
<script type="text/javascript">
	//map
	/* 
	 lat : 40.714224,
	 lng : -73.997
	 lat: -25.363, lng: 131.044
	 */
	//var x, y;
	var map;
	//var marker;
	var markers = [];
	//	var image = 'images/beachflag.png';/* 
	/*var image = {
	 url: place.icon,
	 size: new google.maps.Size(71, 71),
	 origin: new google.maps.Point(0, 0),
	 anchor: new google.maps.Point(17, 34),
	 scaledSize: new google.maps.Size(25, 25)
	 }; */
	function initMap() {
		var haightAshbury = {
			lat : 35.886,
			lng : 128.610
		};

		map = new google.maps.Map(document.getElementById('map'), {
			zoom : 18,
			center : haightAshbury,
			mapTypeId : google.maps.MapTypeId.ROADMAP
		});

		// This event listener will call addMarker() when the map is clicked.
		map.addListener('click', function(event) {
			alert(event.latLng);
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
		//icon:
		});
		//마커 정보 가져오기

		//
		marker.addListener('click', function(event) {
			alert(event.latLng);
			addMarker(event.latLng);

		});

		markers.push(marker);
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

	//connect

</script>
</html>