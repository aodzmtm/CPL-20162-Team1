<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
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

							}
							
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

</script>