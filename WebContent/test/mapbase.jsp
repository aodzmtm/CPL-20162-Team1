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

		//requestJson();
	};

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
		var temp=0;
		var display = document.getElementById("content");
		for (var i = 0; i < json.length; i++) {
			display.innerHTML += json[i].id + ":" + json[i].beacon_addr + ":"
					+ json[i].beacon_id + ":" + json[i].location + ":"
					+ json[i].date_time + ":" + json[i].power_off + ":"
					+ json[i].abnormal_blink + ":" + json[i].short_circuit
					+ ":" + json[i].lamp_failure + ":" + json[i].lamp_state
					+ ":" + json[i].illumination + ":" + json[i].x + ":"
					+ json[i].y + "\n"
			var location = {
				lat : json[i].x,
				lng : json[i].y
			};
			
			
			if(json[i].lamp_failure == 1)	
				addLightAlarmMarker(location);
			else 
			{ 
				if(json[i].power_off == '0')		
					addLightOnMarker(location);
				else if(json[i].power_off == '1')
					addLightOffMarker(location);
			}
		}
		
	//	addLightRecentAlarmMarker(location);
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

			alert("추가하시게씁니까");

			input_x.value = x;
			input_y.value = y;
			/*
			input_x.value = json[i].x;
			input_y.value = json[i].y;
			 */
		}
	}

	function makeJson() {
		var totalInfo = new Object();
		var dataArray = new Array();
		var dataInfo = new Object();
		var dataInfo2 = new Object();
		dataInfo.id = "3";
		dataInfo.student = "송광호";
		dataArray.push(dataInfo);
		//추가
		dataInfo2.id = "4";
		dataInfo2.student = "오광호";
		dataArray.push(dataInfo2);
		totalInfo.TestVo = dataArray;
		var jsonInfo = JSON.stringify(totalInfo);
		//  alert(jsonInfo);
		return jsonInfo;
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	//지도
	
	var map;
	//var marker;
	var markers = [];

	
	//var image = 'img/캡쳐.png';

	//center change
	//map.panTo(haightAshbury);
	var haightAshbury = {
		lat : 35.84914581115387,
		lng : 128.60822982620448
	};

	function initMap() {
		var initLocation = {
			lat : 35.886503063295,
			lng : 128.60822339047
		};

		map = new google.maps.Map(document.getElementById('map'), {
			zoom : 12,
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

	// Adds a marker to the map and push to the array.
	function addLightOnMarker(location) {
		
		var image = {
				 url: 'img/light_on.png',
				 size: new google.maps.Size(462, 462),
				 origin: new google.maps.Point(0, 0),
				 anchor: new google.maps.Point(34, 34),
				 scaledSize: new google.maps.Size(45,45)
				 };
				
		var marker = new google.maps.Marker({
			position : location,
			map : map,
		    icon: image
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
				 url: 'img/light_off.png',
				 size: new google.maps.Size(462, 462),
				 origin: new google.maps.Point(0, 0),
				 anchor: new google.maps.Point(34, 34),
				 scaledSize: new google.maps.Size(45,45)
				 };
				
		var marker = new google.maps.Marker({
			position : location,
			map : map,
		    icon: image
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
				 url: 'img/light_alarm.png',
				 size: new google.maps.Size(462, 462),
				 origin: new google.maps.Point(0, 0),
				 anchor: new google.maps.Point(34, 34),
				 scaledSize: new google.maps.Size(45,45)
				 };
				
		var marker = new google.maps.Marker({
			position : location,
			map : map,
		    icon: image
		});
		//마커 정보 가져오기

				marker.addListener('click', function(event) {
			//alert(event.latLng);
			searchLamp(event.latLng.lat(), event.latLng.lng());
		});
		
		
		markers.push(marker);
	}
	
	function addLightRecentAlarmMarker(location) {
		
		var image = {
				 url: 'img/light_alarm.png',
				 size: new google.maps.Size(462, 462),
				 origin: new google.maps.Point(0, 0),
				 anchor: new google.maps.Point(34, 34),
				 scaledSize: new google.maps.Size(45,45)
				 };
				
		var marker = new google.maps.Marker({
			position : location,
			draggable: true,
		    animation: google.maps.Animation.DROP,
			map : map,
		    icon: image
		});
		//마커 정보 가져오기

				marker.addListener('click', function(event) {
			//alert(event.latLng);
			searchLamp(event.latLng.lat(), event.latLng.lng());
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

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
	function search() {
		
		
		var display = document.getElementById('map');
		display.innerHTML =	"<div><div id=\"grid_container\"><table id=\"list\"></table><div id=\"page\"></div></div></div>";
		
		
		$("#list").jqGrid({
			url : '/light_web/gridJson.do',
			mtype : "POST",
			datatype : "json",
			loadtext : '로딩중..',

			rowNum : 10,
			pager : "#page",
			rownumbers : true, // row의 숫자를 표시해준다.
			page : 1,
			colNames : [ '시퀀스', '제목', '날짜', '수정/삭제' ],
			colModel : [ {
				label : "id",
				sorttype : 'integer',
				name : 'id',
				key : true,
			//width: 75 
			}, {
				label : "student",
				name : 'student',
				//width: 150,
				editable : true,
				// stype defines the search type control - in this case HTML select (dropdownlist)
				stype : "select",
			// searchoptions value - name values pairs for the dropdown - they will appear as options
			//searchoptions: { value: ":[All];ALFKI:ALFKI;ANATR:ANATR;ANTON:ANTON;AROUT:AROUT;BERGS:BERGS;BLAUS:BLAUS;BLONP:BLONP;BOLID:BOLID;BONAP:BONAP;BOTTM:BOTTM;BSBEV:BSBEV;CACTU:CACTU;CENTC:CENTC;CHOPS:CHOPS;COMMI:COMMI;CONSH:CONSH;DRACD:DRACD;DUMON:DUMON;EASTC:EASTC;ERNSH:ERNSH;FAMIA:FAMIA;FOLIG:FOLIG;FOLKO:FOLKO;FRANK:FRANK;FRANR:FRANR;FRANS:FRANS;FURIB:FURIB;GALED:GALED;GODOS:GODOS;GOURL:GOURL;GREAL:GREAL;GROSR:GROSR;HANAR:HANAR;HILAA:HILAA;HUNGC:HUNGC;HUNGO:HUNGO;ISLAT:ISLAT;KOENE:KOENE;LACOR:LACOR;LAMAI:LAMAI;LAUGB:LAUGB;LAZYK:LAZYK;LEHMS:LEHMS;LETSS:LETSS;LILAS:LILAS;LINOD:LINOD;LONEP:LONEP;MAGAA:MAGAA;MAISD:MAISD;MEREP:MEREP;MORGK:MORGK;NORTS:NORTS;OCEAN:OCEAN;OLDWO:OLDWO;OTTIK:OTTIK;PERIC:PERIC;PICCO:PICCO;PRINI:PRINI;QUEDE:QUEDE;QUEEN:QUEEN;QUICK:QUICK;RANCH:RANCH;RATTC:RATTC;REGGC:REGGC;RICAR:RICAR;RICSU:RICSU;ROMEY:ROMEY;SANTG:SANTG;SAVEA:SAVEA;SEVES:SEVES;SIMOB:SIMOB;SPECD:SPECD;SPLIR:SPLIR;SUPRD:SUPRD;THEBI:THEBI;THECR:THECR;TOMSP:TOMSP;TORTU:TORTU;TRADH:TRADH;TRAIH:TRAIH;VAFFE:VAFFE;VICTE:VICTE;VINET:VINET;WANDK:WANDK;WARTH:WARTH;WELLI:WELLI;WHITC:WHITC;WILMK:WILMK;WOLZA:WOLZA" }
			}, {
				label : "Order Date",
				name : 'OrderDate',
				//width: 150,
				editable : true,
				search : true,
				sorttype : 'date',
				searchoptions : {
					// dataInit is the client-side event that fires upon initializing the toolbar search field for a column
					// use it to place a third party control to customize the toolbar
					dataInit : function(element) {
						$(element).datepicker({
							id : 'orderDate_datePicker',
							dateFormat : 'yy-mm-dd',
							//minDate: new Date(2010, 0, 1),
							maxDate : new Date(2020, 0, 1),
							showOn : 'focus'
						});
					}
				}
			},

			{
				label : "Edit Actions",
				name : "actions",
				//width: 50,
				formatter : "actions",
				formatoptions : {
					//keys: true,
					editOptions : {},
					addOptions : {},
					delOptions : {}
				}
			}, ],
			/*
			gridComplete : function() {
				$('.jqgrow').click(function(e) {
					var rowId = $(this).attr('id'); //
					var list = $('#list').getRowData(rowId);
					alert(list.id + list.student);
				});
			},*/
			loadError : function(xhr, status, error) {

				// 데이터 로드 실패시 실행되는 부분

				alert(error);

			},

			onSelectRow : function(ids) {

				//	alert('row 선택시 ids:');

			},

			ondblClickRow : function(rowid, iRow, iCol, e) {

				//	$("#list").editGridRow(rowid, updateDialog);

			},

			//그리드 수정시 submit 전                       

			beforeSubmitCell : function(rowid, cellname, value) {
				alert(rowid + cellname + value);
				return {
					"id" : rowid,
					"cellName" : cellname,
					"cellValue" : value
				}

			},

			//그리드 수정시 submit 후

			afterSubmitCell : function(res) {
				alert(res.responseText);
				var aResult = $.parseJSON(res.responseText);

				var userMsg = "수정시 오류가 발생되었습니다.";

				if ((aResult.jqResult == "1")) {

					userMsg = "수정되었습니다.";

					alert(userMsg);

				}

				return [ (aResult.jqResult == "1") ? true : false, userMsg ];

			}

		}).navGrid('#page', {
			add : false,
			del : false,
			edit : false,
			refresh : true,
			search : true,
		});

		resizeJqGridWidth('list', 'grid_container', $('#grid_container')
				.width(), $('#grid_container').height(), true);
	}

	function research() {

		$("#list").jqGrid("setGridParam", {
			postData : {
				id : '2'
			}
		});

		$("#list").trigger("reloadGrid");

	}

	function resizeJqGridWidth(grid_id, div_id, width, height, tf) {
		$(window).bind('resize', function() {
			var resizeWidth = $('#grid_container').width();
			var resizeHeight = $('#grid_container').height();
			$('#' + grid_id).setGridWidth(resizeWidth, tf);
			$('#' + grid_id).setGridWidth(resizeWidth, tf);
			$('#' + grid_id).setGridHeight(resizeHeight, tf);
			$('#' + grid_id).setGridHeight(resizeHeight, tf);
		}).trigger('resize');
	}

	//////////////////////////////////////////////////////////////////////////////////////////////////////////////
</script>


<script async defer
	src="https://maps.googleapis.com/maps/api/js?key=AIzaSyBegclj2YbPYPTIhCkA6korgBWEth5Q6v8&callback">
	
</script>
</head>
<body>

	<div id="map" style="width: 100%; height: 700px;">
	
	</div>


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
		value="send" onclick="initMap()" /> <input type="submit" value="send"
		onclick="search()" />
</fieldset>
</html>