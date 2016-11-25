<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
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
									var rowKey = grid.getGridParam("selrow");
									var request = createJSONHttpRequest();
									request.open('POST',
											'/light_web/lampInfoGridDelete.do');
									//Ajax 요청
									request
											.setRequestHeader("Content-Type",
													"application/x-www-form-urlencoded;charset=UTF-8");

									request.setRequestHeader("Cache-Control",
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

												}
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
		$("#dialog-save")
				.dialog(
						{
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

										var selectedIDs = grid
												.getGridParam("selarrrow");
										var result = "";
										var totalInfo = new Object();
										var dataArray = new Array();
										for (var i = 0; i < selectedIDs.length; i++) {
											var dataInfo = new Object();
											grid.jqGrid('saveRow',selectedIDs[i],false,'clientArray');


											dataInfo.id = grid
													.getRowData(selectedIDs[i]).id
											dataInfo.beacon_addr = grid.getRowData(selectedIDs[i]).beacon_addr;
											dataInfo.beacon_id = grid
													.getRowData(selectedIDs[i]).beacon_id;
											dataInfo.location = grid
													.getRowData(selectedIDs[i]).location;
											dataInfo.date_time = grid
													.getRowData(selectedIDs[i]).date_time;
											dataInfo.power_off = grid
													.getRowData(selectedIDs[i]).power_off;
											dataInfo.abnormal_blink = grid
													.getRowData(selectedIDs[i]).abnormal_blink;
											dataInfo.short_circuit = grid
													.getRowData(selectedIDs[i]).short_circuit;
											dataInfo.lamp_failure = grid
													.getRowData(selectedIDs[i]).lamp_failure;
											dataInfo.lamp_state = grid
													.getRowData(selectedIDs[i]).lamp_state;
											dataArray.push(dataInfo); 
										}

										totalInfo.LampVo = dataArray;
										var jsonInfo = JSON
												.stringify(totalInfo);
										//alert(jsonInfo);
										var request = createJSONHttpRequest();
										request.open('POST',
												'/light_web/lampInfoGridSave.do');
										//Ajax 요청
										request.send(jsonInfo);
										request.onreadystatechange = function() {
											if (request.readyState == 4) {
												//응답이 정상이라면
												if (request.status >= 200
														&& request.status < 300) {
												}
											}
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
				"수정" : function() {
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
</script>