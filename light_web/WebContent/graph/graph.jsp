<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">

	
	function modifyElementValue(){
		var failureTypeNum = 0;
		var startDateArg = 0;
		var endDateArg = 0;
		if (document.getElementById("failureType") != null)
		{
			var tempSelectObj = document.getElementById("failureType");
			failureTypeNum = tempSelectObj.options[tempSelectObj.selectedIndex].value;
		}
		if (document.getElementById("failureTypeNumTemp") != null)
			document.getElementById("failureTypeNumTemp").value = failureTypeNum;
		
		if (document.getElementById("startDateTemp") != null)
		{
			document.getElementById("startDateTemp").value = document.getElementById("startDate").value;
			startDateArg = document.getElementById("startDateTemp").value - 20000000;
		}
		if (document.getElementById("endDateTemp") != null)
		{
			document.getElementById("endDateTemp").value = document.getElementById("endDate").value;
			endDateArg = document.getElementById("endDateTemp").value - 20000000;
		}
		document.getElementById("dateSearch").onclick = function() {
			getGraphRequest(failureTypeNum, startDateArg, endDateArg);
		};
	}
	
	function checkDate(date, startDate, endDate){
		//alert(Math.floor(date.replace(/[^0-9]/g,'')/1000000) + " >= " + startDate + " && " + Math.floor(date.replace(/[^0-9]/g,'')/1000000) + " <= " + endDate);
		if(Math.floor(date.replace(/[^0-9]/g,'')/1000000) >= startDate && Math.floor(date.replace(/[^0-9]/g,'')/1000000) <= endDate)
		{
			return 1;
		}
		else
		{
			return 0;
		}
	}
	
	function transformToYMD(rowDate){
		return Math.floor(rowDate.replace(/[^0-9]/g,'')/1000000)
	}
	
	$(function() {
		function drawGraph(failureTypeNum, startDate, endDate) {
			var lampName = [];
			var maxCount = 0;
			var i = 0, j = 0;
			var obj;
			var lampNameCount;
			var request = createJSONHttpRequest();
			request.open('POST', '/light_web/lampInfoGraphJson.do');
			//Ajax 요청
			request.send();
			request.onreadystatechange = function() {
				if (request.readyState == 4) {
					//응답이 정상이라면
					if (request.status >= 200 && request.status < 300) {
						obj = JSON.parse(request.responseText);
						//obj 시작		
						
						if(startDate == null && endDate == null)
						{
							startDate = 999999;
							endDate = 0;
							for (var i = 0; i < obj['rows'].length; i++)
							{
								if(startDate >= transformToYMD(obj['rows'][i]['date_time']))
									startDate = transformToYMD(obj['rows'][i]['date_time']);
								if(endDate <= transformToYMD(obj['rows'][i]['date_time']))
									endDate = transformToYMD(obj['rows'][i]['date_time']);
							}
						}
						
						if (failureTypeNum == 0 && checkDate(obj['rows'][0]['date_time'], startDate, endDate)) {
							lampName[0] = obj['rows'][0]['location'];
						}
						else
						{
							for (var i = 0; !(failureTypeNum == obj['rows'][i]['failure_reason_id'] && checkDate(obj['rows'][i]['date_time'], startDate, endDate)) && i < obj['rows'].length; i++);
							lampName[0] = obj['rows'][i]['location'];
						}
						
						for (var i = 0; i < obj['rows'].length; i++) {
							if (lampName[maxCount] != obj['rows'][i]['location'] && checkDate(obj['rows'][i]['date_time'], startDate, endDate)) {
								if (failureTypeNum == 0) {
									maxCount++;
									lampName[maxCount] = obj['rows'][i]['location'];
								} else if (failureTypeNum == obj['rows'][i]['failure_reason_id']) {
									maxCount++;
									lampName[maxCount] = obj['rows'][i]['location'];
								}
							}
						}
						
						var barChart = null;
						var barChartData = { 
							labels : [],
							datasets : [ {
								fillColor : "rgba(151,187,205,0.5)",
								strokeColor : "rgba(151,187,205,0.8)",
								highlightFill : "rgba(151,187,205,0.75)",
								highlightStroke : "rgba(151,187,205,1)",
								data : []
							} ]
						}
						
						if(obj['rows'].length < 12)
						{
							lampNameCount = obj['rows'].length;
						}
						else
						{
							lampNameCount = 12;
						}
						
						for(var tempCount = 0; tempCount < lampNameCount; tempCount++)
						{
							barChartData['labels'][tempCount] = lampName[tempCount];
							barChartData['datasets'][0]['data'][tempCount] = getFailureCount(lampName[tempCount], obj, failureTypeNum);
						}
						
						var graphOptions = "<td width=\"180px\"><div style=\"font-size: 15px; color: #2e62d9; font-weight: bold;\">고장분류</div></td><td width=\"260px\" align=\"left\">"
								+ "<select id=\"failureType\" class=\"editable inline-edit-cell ui-widget-content ui-corner-all\" style=\"height: 25px; width: 140px;\" onChange=\"modifyElementValue();\">";
								if (failureTypeNum == 0)
									graphOptions += "	<option value=\"0\" selected>전체</option>";
								else
									graphOptions += "	<option value=\"0\">전체</option>";
								if (failureTypeNum == 1)
									graphOptions += "	<option value=\"1\" selected>정전</option>"
								else
									graphOptions += "	<option value=\"1\">정전</option>"
								if (failureTypeNum == 2)
									graphOptions += "	<option value=\"2\" selected>이상점등</option>"
								else
									graphOptions += "	<option value=\"2\">이상점등</option>"
								if (failureTypeNum == 3)
									graphOptions += "	<option value=\"3\" selected>이상소등</option>"
								else
									graphOptions += "	<option value=\"3\">이상소등</option>"
								if (failureTypeNum == 4)
									graphOptions += "	<option value=\"4\" selected>누전</option>"
								else
									graphOptions += "	<option value=\"4\">누전</option>"
								if (failureTypeNum == 5)
									graphOptions += "	<option value=\"5\" selected>램프고장</option>"
								else
									graphOptions += "	<option value=\"5\">램프고장</option>"
								if (failureTypeNum == 6)
									graphOptions += "	<option value=\"6\" selected>안정기 고장</option>"
								else
									graphOptions += "	<option value=\"6\">안정기 고장</option>"
								if (failureTypeNum == 7)
									graphOptions += "	<option value=\"7\" selected>램프 안정기 고장</option>"
								else
									graphOptions += "	<option value=\"7\">램프 안정기 고장</option>"
								if (failureTypeNum == 8)
									graphOptions += "	<option value=\"8\" selected>강제소등</option>"
								else
									graphOptions += "	<option value=\"8\">강제소등</option>"
								if (failureTypeNum == 9)
									graphOptions += "	<option value=\"9\" selected>강제점등</option>"
								else
									graphOptions += "	<option value=\"9\">강제점등</option>"
								graphOptions += "</select></td>";
								
						var Term = "<td width=\"120px\"><div style=\"font-size: 15px; color: #2e62d9; font-weight: bold;\">조회 기간</div></td><td width=\"300px\" align=\"right\">"
								+ "<input class=\"editable inline-edit-cell ui-widget-content ui-corner-all\" type=\"text\" style=\"height: 25px; width: 140px;\" id=\"startDate\" value=\"20" + startDate + "\" onChange=\"modifyElementValue();\">"
								+ " ~ "
								+ "<input class=\"editable inline-edit-cell ui-widget-content ui-corner-all\" type=\"text\" style=\"height: 25px; width: 140px;\" id=\"endDate\" value=\"20" + endDate + "\" onChange=\"modifyElementValue();\">"
								+ "</td><td align=\"left\"><input class=\"btn btn-default\" type=\"button\" id=\"dateSearch\" value=\"조회\" style=\"height: 25px; padding: 3px 12px; margin-left:10px\""
								+ " onclick=\"getGraphRequest(" + failureTypeNum + ");\"></td>";
						
						var chart = "<canvas id=\"canvas\" height=\"530\" width=\"950\"></canvas>";
						
						document.getElementById("graph").innerHTML = "<table style=\"margin:auto; margin-top: 5%; margin-bottom: 5%;  text-align:center\">"
								+ "<tr style=\"height:60px\">"
								+ graphOptions
								+ ""
								+ Term
								+ "</tr>"
								+ "<tr><td colspan=\"6\">"
								+ chart
								+ "</td></tr>";

						var ctx = document.getElementById("canvas").getContext(
								"2d");

						barChart = new Chart(ctx).StackedBar(barChartData, {
							//Boolean - Whether the scale should start at zero, or an order of magnitude down from the lowest value
							scaleBeginAtZero : true,
							//Boolean - Whether grid lines are shown across the chart
							scaleShowGridLines : true,
							//String - Colour of the grid lines
							scaleGridLineColor : "rgba(0,0,0,0.05)",
							//Number - Width of the grid lines
							scaleGridLineWidth : 1,
							//Boolean - If there is a stroke on each bar
							barShowStroke : false,
							//Number - Pixel width of the bar stroke
							barStrokeWidth : 2,
							//Number - Spacing between each of the X value sets
							barValueSpacing : 5,
							//Number - Spacing between data sets within X values
							barDatasetSpacing : 1,
							onAnimationProgress : function() {
								console.log("onAnimationProgress");
							},
							onAnimationComplete : function() {
								console.log("onAnimationComplete");
							}
						})
					} else
						dialogLogInCheck();
				}
			}
		}
		getGraphRequest = drawGraph;
	})

	var getFailureCount = function(name, query, failnum) {
		var index;
		var num = 0;
		for (index = 0; index < query['rows'].length; index++) {
			if (name == query['rows'][index]['location']) {
				if (failnum == 0) {
					num += 1;
				} else if (failnum == query['rows'][index]['failure_reason_id']) {
					num += 1;
				}
			}
		}
		return num;
	}
	
	$("input#btnAdd").on(
			"click",
			function() {
				barChart.addData([ randomScalingFactor(),
						randomScalingFactor(), randomScalingFactor() ],
						lampName[(barChart.datasets[0].bars.length) % 12]);
			});

	$("canvas").on("click", function(e) {
		var activeBars = barChart.getBarsAtEvent(e);
		console.log(activeBars);

		for ( var i in activeBars) {
			console.log(activeBars[i].value);
		}
	});
</script>
<input type="hidden" id="failureTypeNumTemp" value="0">
<input type="hidden" id="startDateTemp" value="0">
<input type="hidden" id="endDateTemp" value="0">