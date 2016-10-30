<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
function mapDisplay()
{		
/* 	if (location.pathname == "/light_web/join.do")
		return;
	if ("${sessionScope.admin.admin_id}" == "") 
		return; */
	var display = document.getElementById("map");
	gridNoneDisplay();
	graphNoneDisplay();
	display.style.display = "";
	input_id.value = "";
	initMap();
}

function gridDisplay()
{

	var display = document.getElementById("grid");
	mapNoneDisplay();
	graphNoneDisplay();
	display.style.display = "";
	input_id.value = "";
	getGridRequest();
}

function graphDisplay()
{
	
	var display = document.getElementById("graph");
	mapNoneDisplay();
	gridNoneDisplay();
	display.style.display = "";
	input_id.value = "";
	$(getGraphRequest());
}


function mapNoneDisplay()
{	
	
	var display = document.getElementById("map");
	if(display.style.display != "none")
			display.style.display = "none";
}

function gridNoneDisplay()
{

	var display = document.getElementById("grid");
	if(display.style.display != "none")
			display.style.display = "none";
}
function graphNoneDisplay()
{

	var display = document.getElementById("graph");
	if(display.style.display != "none")
			display.style.display = "none";
}
</script>