<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>


<meta charset="UTF-8">


<html>

<head>
<style type="text/css">
html, body {
	height: 100%;
	margin: 0;
	padding: 0;
}

div {
	margin: 0;
	padding: 0;
}

#sjqGrid {
	margin: 0;
	padding: 0;
	font-size: 90%
}
</style>



<link rel="stylesheet" type="text/css" media="screen"
	href="jqueryui/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="jqgrid/css/ui.jqgrid.css" />
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="js/**"></script>
<!-- <script src="js/jquery-1.11.0.min.js"></script> -->
<script src="jqueryui/jquery-ui.js"></script>
<script src="jqgrid/js/jquery.jqGrid.min.js"></script>
<script src="jqgrid/src/i18n/grid.locale-kr.js"></script>


<script type="text/javascript" src="js/util.js"></script>


<script type="text/javascript">
	
</script>
</head>
<body>

	<script>
		$(function() {
			$("#datepicker").datepicker();
		});
	</script>

	<div id="map" style="width: 100%;" align="center"></div>
	<p>

		<!-- 	<p>Date: <input type="text" id="datepicker" value=""></p> -->
	
		<div style="float: right;">

				<input class="ui-button ui-corner-all ui-widget" id="button"
					style="font-size: 12px; font-weight: none;"
					onclick="historyBatchEdit()" type="button" value="일괄 수정"> <input
					class="ui-button ui-corner-all ui-widget" id="button"
					style="font-size: 12px; font-weight: none;"
					onclick="historyBatchCancelEdit()" type="button" value="일괄 취소">
				<input class="ui-button ui-corner-all ui-widget" id="button"
					style="font-size: 12px; font-weight: none;"
					onclick="historyBatchSave()" type="button" value="일괄 저장"> <input
					class="ui-button ui-corner-all ui-widget" id="button"
					style="font-size: 12px; font-weight: none;"
					onclick="historyBatchDelete()" type="button" value="일괄 삭제">
			</div>


	<script type="text/javascript">
		window.onload = function() {
			search();

		};

		function search() {

			var display = document.getElementById('map');
			display.innerHTML = "<div id=\"history_grid_container\"><table id=\"sjqGrid\"></table><div id=\"sjqGridPager\"></div></div>";

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

							grid.trigger("reloadGrid");
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
	</script>


</body>
</html>
