<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">

/**/	

function startDatePicker() {

   	 $( "#startDate" ).datepicker();
     $( "#startDate" ).datepicker( "option", "dateFormat", "yy/mm/dd" );

  }

function endDatePicker() {

   	 $( "#endDate" ).datepicker();
     $( "#endDate" ).datepicker( "option", "dateFormat", "yy/mm/dd" );

  }
function replaceDate(str)
{
	var dateTemp = null;
	var dateTemp = str.split("/");
	var date = dateTemp[0]+dateTemp[1]+dateTemp[2];
	return date;
}
/**/
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
			
			document.getElementById("startDateTemp").value = replaceDate(document.getElementById("startDate").value);
		}
		startDateArg = document.getElementById("startDateTemp").value - 20000000;
		if (document.getElementById("endDateTemp") != null)
		{
			document.getElementById("endDateTemp").value = replaceDate(document.getElementById("endDate").value);
		}
		endDateArg = document.getElementById("endDateTemp").value - 20000000;
		document.getElementById("pageNumTemp").value = 1;
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
	
	function pageMove(direction){
		if(direction == 1)
		{
			if(document.getElementById("pageNumField").innerHTML > 1)
			{
				document.getElementById("pageNumTemp").value--;
				document.getElementById("pageNumField").innerHTML = document.getElementById("pageNumTemp").value;
				getGraphRequest(document.getElementById("failureTypeNumTemp").value, document.getElementById("startDateTemp").value - 20000000, document.getElementById("endDateTemp").value - 20000000, document.getElementById("pageNumTemp").value);
			}
		}
		else if(direction == 2)
		{
			if(document.getElementById("pageNumField").innerHTML < document.getElementById("maxPageNumTemp").value)
			{
				document.getElementById("pageNumTemp").value++;
				document.getElementById("pageNumField").innerHTML = document.getElementById("pageNumTemp").value;
				getGraphRequest(document.getElementById("failureTypeNumTemp").value, document.getElementById("startDateTemp").value - 20000000, document.getElementById("endDateTemp").value - 20000000, document.getElementById("pageNumTemp").value);
			}
		}
	}
	
	$(function() {
		function drawGraph(failureTypeNum, startDate, endDatem, pageNum) {
			var lampName = [];
			var maxCount = 0;
			var i = 0, j = 0;
			var obj;
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
						
						if(pageNum == null)
							pageNum = 1;
						else
							pageNum = document.getElementById("pageNumTemp").value;
						
						if(document.getElementById("startDateTemp").value == 999999 && document.getElementById("endDateTemp").value == 0)
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
						else
						{
							startDate = document.getElementById("startDateTemp").value - 20000000;
							endDate = document.getElementById("endDateTemp").value - 20000000;
						}
						
						// 0번째 lampName 찾기
						if (failureTypeNum == 0 && checkDate(obj['rows'][0]['date_time'], startDate, endDate)) {
							lampName[0] = obj['rows'][0]['location'];
						}
						else
						{
							for (var i = 0; i < obj['rows'].length; i++)
								if (failureTypeNum == obj['rows'][i]['failure_reason_id'] && checkDate(obj['rows'][i]['date_time'], startDate, endDate))
								{
									lampName[0] = obj['rows'][i]['location'];
									break;
								}
						}
						//0번째 lampName 찾기 완료
						
						//1번째부터 끝까지 lampName 찾기 시작
						for (var i = 1; i < obj['rows'].length; i++) {
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
						//1번째부터 끝까지 lampName 찾기 완료
						//maxCount는 lampName의 갯수와 동일, ex) 1개의 경우 0
						
						if(document.getElementById("maxPageNumTemp") != null)
							document.getElementById("maxPageNumTemp").value = (maxCount - (maxCount%12))/ 12 + 1;
						//페이지 갯수 계산
						
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
						
						//실제 그래프 이름배열에 lampName 매핑 시작
						for(var tempCount = 0; tempCount < 12; tempCount++)
						{
							if(lampName[tempCount + ((pageNum - 1) * 12)] == null)
								barChartData['labels'][tempCount] = " ";
							else
								barChartData['labels'][tempCount] = lampName[tempCount + ((pageNum - 1) * 12)];
							barChartData['datasets'][0]['data'][tempCount] = getFailureCount(lampName[tempCount + ((pageNum - 1) * 12)], obj, failureTypeNum);
						}
						//매핑 완료
						
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
						
						var chart = "<canvas id=\"canvas\" height=\"500\" width=\"950\"></canvas>";
						
						var pagingButtons = "<span class =\"leftout\" onMouseOut=\"this.className='leftout' \"onMouseOver=\"this.className='leftover'\" onclick=\"pageMove(1);\"></span>"
										+ "<span id=\"pageNumField\" style=\"font-size: 30px; padding: 10px 10px;\">"+ pageNum +"</span>"
										+ "<span class =\"rightout\" onMouseOut=\"this.className='rightout' \"onMouseOver=\"this.className='rightover'\" onclick=\"pageMove(2);\"></span>";
						
						document.getElementById("graph").innerHTML = "<table style=\"margin:auto; margin-top: 5%; margin-bottom: 5%;  text-align:center\">"
								+ "<tr style=\"height:60px\">"
								+ graphOptions
								+ ""
								+ Term
								+ "</tr>"
								+ "<tr><td colspan=\"6\">"
								+ chart
								+ "</td></tr>"
								+ "<tr style=\"vertical-align: bottom;\" height=\"50\"><td colspan=\"6\" align=\"center\" height=\"50px\">"
								+ pagingButtons
								+ "</td></tr>";

						startDatePicker();
						endDatePicker();
			
						document.getElementById("startDate").value ="20" +startDate.toString().substring(0, 2)+"/"+startDate.toString().substring(2,4)+"/"+startDate.toString().substring(4, 6) ;
						document.getElementById("endDate").value ="20" +endDate.toString().substring(0, 2)+"/"+endDate.toString().substring(2,4)+"/"+endDate.toString().substring(4, 6) ;
						
						var ctx = document.getElementById("canvas").getContext(
								"2d");

						barChart = new Chart(ctx).StackedBar(barChartData, {
							scaleBeginAtZero : true,
							scaleShowGridLines : true,
							scaleGridLineColor : "rgba(0,0,0,0.05)",
							scaleGridLineWidth : 1,
							barShowStroke : false,
							barStrokeWidth : 2,
							barValueSpacing : 5,
							barDatasetSpacing : 1,
							onAnimationProgress : function() {
								console.log("onAnimationProgress");
							},
							onAnimationComplete : function() {
								console.log("onAnimationComplete");
							}
						})
					}
					else
					{
						dialogLogInCheck();
					}
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

	$("canvas").on("click", function(e) {
		var activeBars = barChart.getBarsAtEvent(e);
		console.log(activeBars);

		for ( var i in activeBars) {
			console.log(activeBars[i].value);
		}
	});
</script>
<input type="hidden" id="failureTypeNumTemp" value="0">
<input type="hidden" id="startDateTemp" value="999999">
<input type="hidden" id="endDateTemp" value="0">
<input type="hidden" id="pageNumTemp" value="1">
<input type="hidden" id="maxPageNumTemp" value="1">