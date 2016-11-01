<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
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
</script>