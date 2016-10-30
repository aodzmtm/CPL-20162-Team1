<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

 <script type="text/javascript">
     var randomScalingFactor = function(){ return Math.round(Math.random()*100)};
     var months = ["January","February","March","April","May","June","July", "August", "September", "October", "November", "December"];
     var barChart = null;
     var barChartData = {
         labels : ["January","February","March","April","May","June","July"],
         datasets : [
             {
                 fillColor : "rgba(220,220,220,0.5)",
                 strokeColor : "rgba(220,220,220,0.8)",
                 highlightFill: "rgba(220,220,220,0.75)",
                 highlightStroke: "rgba(220,220,220,1)",
                 data : [randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor()]
             },
             {
                 fillColor : "rgba(151,187,205,0.5)",
                 strokeColor : "rgba(151,187,205,0.8)",
                 highlightFill : "rgba(151,187,205,0.75)",
                 highlightStroke : "rgba(151,187,205,1)",
                 data : [randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor()]
             },
             {
                 fillColor : "rgba(240,73,73,0.5)",
                 strokeColor : "rgba(240,73,73,0.8)",
                 highlightFill : "rgba(240,73,73,0.75)",
                 highlightStroke : "rgba(240,73,73,1)",
                 data : [randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor(),randomScalingFactor()]
             }
         ]

     };

     $(function() {
    	 function drawGraph(){
	    	  var graphOptions = "<td width=\"180px\"><div style=\"font-size: 15px; color: #2e62d9; font-weight: bold;\">고장분류</div></td><td width=\"260px\" align=\"left\">" +
	    		  "<select id=\"failureType\" class=\"editable inline-edit-cell ui-widget-content ui-corner-all\" style=\"height: 25px; width: 140px;\">"
	    		+ "	<option value=\"0\">전체</option>"
				+ "	<option value=\"1\">정전</option>"
				+ "	<option value=\"2\">이상점등</option>"
				+ "	<option value=\"3\">이상소등</option>"
				+ "	<option value=\"4\">누전</option>"
				+ "	<option value=\"5\">램프고장</option>"
				+ "	<option value=\"6\">안정기 고장</option>"
				+ "	<option value=\"7\">램프 안정기 고장</option>"
				+ "	<option value=\"8\">강제소등</option>"
				+ "	<option value=\"9\">강제점등</option>"
				+ "</select></td>";
			 
			var Term = "<td width=\"120px\"><div style=\"font-size: 15px; color: #2e62d9; font-weight: bold;\">기간</div></td><td width=\"300px\" align=\"right\">"
			 + "<input class=\"editable inline-edit-cell ui-widget-content ui-corner-all\" type=\"text\" style=\"height: 25px; width: 140px;\" id=\"startDate\" value=\"\">"
			 + " ~ "
			 + "<input class=\"editable inline-edit-cell ui-widget-content ui-corner-all\" type=\"text\" style=\"height: 25px; width: 140px;\" id=\"endDate\" value=\"\">"
			 + "</td><td align=\"left\"><input class=\"btn btn-default\" type=\"button\" id=\"dateSearch\" value=\"조회\" style=\"height: 25px; padding: 3px 12px; margin-left:10px\" onclick=\"getGraphRequest();\"></td>";
			 
			 var chart = "<canvas id=\"canvas\" height=\"530\" width=\"950\"></canvas>";
	    	 
	    	 document.getElementById("graph").innerHTML = "<table style=\"margin:auto; margin-top: 5%; margin-bottom: 5%;  text-align:center\">" +
	    	 "<tr style=\"height:60px\">" + graphOptions + "" +  Term + "</tr>" + "<tr><td colspan=\"6\">" + chart + "</td></tr>";
	         var ctx = document.getElementById("canvas").getContext("2d");
	         barChart = new Chart(ctx).StackedBar(barChartData, {
	             //Boolean - Whether the scale should start at zero, or an order of magnitude down from the lowest value
	             scaleBeginAtZero : false,
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
	             onAnimationProgress: function() {
	                 console.log("onAnimationProgress");
	             },
	             onAnimationComplete: function() {
	                 console.log("onAnimationComplete");
	             }
	         });
    	 }
    	 getGraphRequest = drawGraph;
     })

     $("input#btnAdd").on("click", function() {
         barChart.addData(
             [randomScalingFactor(),randomScalingFactor(),randomScalingFactor()], 
             months[(barChart.datasets[0].bars.length)%12]
         );
     });

     $("canvas").on("click", function(e) {
         var activeBars = barChart.getBarsAtEvent(e);
         console.log(activeBars);

         for(var i in activeBars) {
             console.log(activeBars[i].value);
         }
     });

 </script>