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

#jqGrid {
	margin: 0;
	padding: 0;
	font-size: 90%
}

#sjqGrid {
	margin: 0;
	padding: 0;
	font-size: 90%
}

.ui-dialog {
	font-size: 12px !important;
}
</style>

<link rel="stylesheet" type="text/css" media="screen" href="jqueryui/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen" href="jqgrid/css/ui.jqgrid.css" />
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
	<div id="grid" style="width: 100%;" align="center"></div>
	<div style="display: none;">
		<div id="dialog-confirm" style="font-size: 12px;" title="경고">
			<p>삭제하시겠습니까?</p>
		</div>

	</div>
	<script type="text/javascript">
	function getGridRequest() {
		var request = createJSONHttpRequest();
		var display = document.getElementById('grid');
		request.open('POST', '/light_web/grid/loadGrid.jsp');
		//Ajax 요청
		request.send();
		request.onreadystatechange = function() {
			if (request.readyState == 4) {
				//응답이 정상이라면
				if (request.status >= 200 && request.status < 300) {

					var str = request.responseText;
					//alert(str);
					display.innerHTML = str;
					lampGrid();
					lampHistoryGrid();
				} else
					alert("데이터를 가져오기 실패");
			}
		}
	}
		window.onload = function() {
			getGridRequest();

		};

	
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
								//rowList : [ 20, 30, 50 ],
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
			$("#dialog-delete")
					.dialog(
							{
								resizable : true,
								height : "auto",
								width : 400,

								buttons : {
									"삭제" : function() {
										$(this).dialog("close");
										var grid = $("#jqGrid");
										var rowKey = grid
												.getGridParam("selrow");
										var request = createJSONHttpRequest();
										request
												.open('POST',
														'/light_web/lampInfoGridDelete.do');
										//Ajax 요청
										request
												.setRequestHeader(
														"Content-Type",
														"application/x-www-form-urlencoded;charset=UTF-8");

										request.setRequestHeader(
												"Cache-Control",
												"no-cache, must-revalidate");

										request.setRequestHeader("Pragma",
												"no-cache");
										if (!rowKey)
											dialogNoSelect();
										else {
											var selectedIDs = grid
													.getGridParam("selarrrow");
											var result = "";
											for (var i = 0; i < selectedIDs.length - 1; i++) {
												result += selectedIDs[i] + "/";
											}
											result += selectedIDs[i];
											request.send("idstr=" + result);

											request.onreadystatechange = function() {
												if (request.readyState == 4) {
													//응답이 정상이라면
													if (request.status >= 200
															&& request.status < 300) {
														getGridRequest();
													} else
														alert("데이터를 가져오기 실패");
												}
											}
											// grid.trigger("reloadGrid");
										}
									},
									"취소" : function() {
										$(this).dialog("close");

									}
								}
							});

		}
	
		
		
		function batchSave() {
			$("#dialog-save").dialog({
				resizable : true,
				height : "auto",
				width : 400,

				buttons : {
					"저장" : function() {
						$(this).dialog("close");
						var grid = $("#jqGrid");
						var rowKey = grid.getGridParam("selrow");

						if (!rowKey)
							dialogNoSelect();
						else {
							var selectedIDs = grid.getGridParam("selarrrow");
							var result = "";
							for (var i = 0; i < selectedIDs.length; i++) {
								grid.jqGrid('saveRow', selectedIDs[i]);
							}
							grid.trigger("reloadGrid");
						}
					},
					"취소" : function() {
						$(this).dialog("close");
					}
				}
			});
		}
		

		
		
	

		
		function batchEdit() {
			$("#dialog-edit").dialog({
				resizable : true,
				height : "auto",
				width : 400,

				buttons : {
					"삭제" : function() {
						$(this).dialog("close");
						var grid = $("#jqGrid");
						var rowKey = grid.getGridParam("selrow");

						if (!rowKey)
							dialogNoSelect();
						else {
							var selectedIDs = grid.getGridParam("selarrrow");
							var result = "";
							for (var i = 0; i < selectedIDs.length; i++) {
								grid.jqGrid('editRow', selectedIDs[i], true);
							}
						}
					},
					"취소" : function() {
						$(this).dialog("close");

					}
				}
			});

		}

		
		
		
		
		function batchCancelEdit() {
			var grid = $("#jqGrid");
			var rowKey = grid.getGridParam("selrow");

			if (!rowKey)
				dialogNoSelect();
			else {
				var selectedIDs = grid.getGridParam("selarrrow");
				var result = "";
				for (var i = 0; i < selectedIDs.length; i++) {
					grid.jqGrid('restoreRow', selectedIDs[i], true);
				}
				grid.jqGrid('resetSelection');
			}
		}
		////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		//history grid
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
											label : "처리날짜",
											name : "repair_date_time",
											align : "center",
											editable : false,
											search : true,
											sorttype : 'date',
											datatype : "local",
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
											align : "center"
										}, ],
								//rowList : [ 20, 30, 50 ],
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
		
		
		

		function dialog() {
			$("#dialog-edit").dialog({
				resizable : true,
				height : "auto",
				width : 400,

				buttons : {
					"삭제" : function() {
						$(this).dialog("close");
					},
					"취소" : function() {
						$(this).dialog("close");

					}
				}
			});

		}
		
		function historyBatchDelete() {
			$("#dialog-delete").dialog({
				resizable : true,
				height : "auto",
				width : 400,

				buttons : {
					"삭제" : function() {
						$(this).dialog("close");
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
							dialogNoSelect();
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
					},
					"취소" : function() {
						$(this).dialog("close");

					}
				}
			});
		}
		function historyBatchSave() {
			$("#dialog-save").dialog({
				resizable : true,
				height : "auto",
				width : 400,

				buttons : {
					"삭제" : function() {
						$(this).dialog("close");
						var grid = $("#sjqGrid");
						var rowKey = grid.getGridParam("selrow");

						if (!rowKey)
							dialogNoSelect();
						else {
							var selectedIDs = grid.getGridParam("selarrrow");
							var result = "";
							for (var i = 0; i < selectedIDs.length; i++) {
								grid.jqGrid('saveRow', selectedIDs[i]);
							}
							grid.trigger("reloadGrid");
						}
					},
					"취소" : function() {
						$(this).dialog("close");

					}
				}
			});

		}
		
		
		function dialogNoSelect() {
			$("#dialog-no-select").dialog({
				resizable : true,
				height : "auto",
				width : 400,

				buttons : {
					"확인" : function() {
						$(this).dialog("close");
					}
				}
			});

		}
		
		function historyBatchEdit() {
			$("#dialog-edit").dialog({
				resizable : true,
				height : "auto",
				width : 400,

				buttons : {
					"삭제" : function() {
						$(this).dialog("close");
						var grid = $("#sjqGrid");
						var rowKey = grid.getGridParam("selrow");

						if (!rowKey)
							dialogNoSelect();
						else {
							var selectedIDs = grid.getGridParam("selarrrow");
							var result = "";
							for (var i = 0; i < selectedIDs.length; i++) {
								grid.jqGrid('editRow', selectedIDs[i], true);
							}
						}
					},
					"취소" : function() {
						$(this).dialog("close");

					}
				}
			});

		}

		function historyBatchCancelEdit() {
			var grid = $("#sjqGrid");
			var rowKey = grid.getGridParam("selrow");
			if (!rowKey)
				dialogNoSelect();
			grid.jqGrid('resetSelection');
		}
		
		
		/* function dialogNoSelect() {
		$("#dialog-no-select").dialog({
			resizable : true,
			height : "auto",
			width : 400,

			buttons : {
				"삭제" : function() {
					$(this).dialog("close");
				},
				"취소" : function() {
					$(this).dialog("close");

				}
			}
		});

	} */
	</script>
</body>
</html>
