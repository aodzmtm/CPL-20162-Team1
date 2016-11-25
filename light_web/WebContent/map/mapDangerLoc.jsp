<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
	var routesCircles = [];

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
		request.open('POST', '/light_web/mobileDangerDataWeb.do');
		//Ajax 요청
		request.send(makeJson());
		request.onreadystatechange = function() {
			if (request.readyState == 4) {
				//응답이 정상이라면
				if (request.status >= 200 && request.status < 300) {
					var str = request.responseText;
					jsonNeedLocationParse(str);
					drawRoute();
				}
			}
		}
	}
	function jsonNeedLocationParse(str) {
		var subJson = JSON.parse(str);

		for (var i = 0; i < subJson.length; i++) {
			
			var location = {
				lat : subJson[i].x,
				lng : subJson[i].y
			};
			if(subJson[i].count != 0)
				{
					addNeedLocationMarker(location,subJson[i].count);
				}
		}
	}
	function addNeedLocationMarker( location, count) {
		var marker = new google.maps.Marker({
			position : location,
			total:count
		});
		
		routeMarkers.push(marker);
	}
	function deleteCirclesRoute() {
		// Sets the map on all markers in the array.
		for (var i = 0; i < routesCircles.length; i++) {
			routesCircles[i].setMap(null);

		}
		routesCircles = [];
		routeMarkers = [];
	}

	function drawRoute() {

	for ( var marker in routeMarkers){
	
				var circle = new google.maps.Circle({
					//strokeColor: '#FF0000',
					strokeOpacity : 1.0,
					strokeWeight : 0,
					fillColor : '#FF0000',
					fillOpacity : 0.35,
					map : map,
					center : routeMarkers[marker].position,
					radius : Math.sqrt(routeMarkers[marker].total) * 10
				});
				circle.addListener('click', function(event) {
					searchLamp(roundLocation(event.latLng.lat()), roundLocation(event.latLng.lng()));
					//addMarker(event.latLng); 
				});
				routesCircles.push(circle);
			}

		}

	
</script>