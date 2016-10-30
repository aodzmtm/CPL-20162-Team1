<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><tiles:insertAttribute name="title" /></title>
<style type="text/css">
html, body {
	height: 100%;
	margin: 0;
	padding: 0;
}
.profile_box {
	width: 100%;
	height: 100%;
 	/* background-size:100%; */
}
div {
	margin: 0;
	padding: 0;
}

#jqGrid {
	margin: 0;
	padding: 0;
	/* font-size: 90% */
}

#sjqGrid {
	margin: 0;
	padding: 0;
	/* font-size: 90% */
}

.ui-dialog {
	font-size: 12px !important;
}

#floating-panel {
	position: absolute;
	z-index: 5;
	background-image: url('img/alarm.png');
	width: 1095px;
	height: 730px;
}

.light_state_font {
	font-size: 14px;
	color: #6f7374;;
}

.log_in_font {
	font-size: 14px;
	color: #6f7374;;
}

.grid_title_font {
	font-size: 11px;
	color: #2e62d9;
}
</style>
<link type="text/css" href="css/layout.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" media="screen"
	href="jqueryui/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="jqgrid/css/ui.jqgrid.css" />
<script src="js/imageControl.js" language="javaScript"
	type="text/javascript"></script>
<script src="js/common.js" language="javaScript" type="text/javascript"></script>
<script src="jqueryui/jquery-ui.js"></script>
<script src="jqgrid/js/jquery.jqGrid.min.js"></script>
<script src="jqgrid/src/i18n/grid.locale-kr.js"></script>
<script type="text/javascript" src="js/util.js"></script>
<script src="js/jquery-1.11.0.min.js"></script>
<script src="jqgrid/src/i18n/grid.locale-kr.js"></script>

<script src="js/imageControl.js" language="javaScript"
	type="text/javascript"></script>
<script src="js/common.js" language="javaScript" type="text/javascript">
	
</script>


<tiles:insertAttribute name="leftSideDisplayFunc" />
<tiles:insertAttribute name="leftSideFunc" />
<tiles:insertAttribute name="rightSideDateTimeFunc" />

<tiles:insertAttribute name="loadMap" />
<tiles:insertAttribute name="map" />
<tiles:insertAttribute name="lampData" />
<tiles:insertAttribute name="lampDataFunc" />
<tiles:insertAttribute name="mapAlarm" />
<tiles:insertAttribute name="mapAddMarker" />
<tiles:insertAttribute name="mapDangerLoc" />
<tiles:insertAttribute name="mapMarkerFilter" />

<tiles:insertAttribute name="grid" />
<tiles:insertAttribute name="gridLight" />
<tiles:insertAttribute name="gridLightFunc" />
<tiles:insertAttribute name="gridHistory" />
<tiles:insertAttribute name="gridHistoryFunc" />

<tiles:insertAttribute name="dialogFunc" />

</head>


<body>
	<tiles:insertAttribute name="dialog" />
	<div align="center" style="height: 100%;">
		<table style="display: inline-block; align: center; height: 100%;">

			<tr>
				<td width="250px" style="vertical-align: top;"><tiles:insertAttribute
						name="leftSide" /></td>
				<td height="100%" align=center>
					<table style="width: 1100px;">
						<tr
							style="height: 730px; vertical-align: top; border-style: solid; border-color: #bce1fa;">
							<td><tiles:insertAttribute name="main" /></td>
						</tr>
						<tr>
							<td><tiles:insertAttribute name="state" /></td>
						</tr>

					</table>

				</td>

				<td width="250px" align=center style="vertical-align: top"><tiles:insertAttribute
						name="rightSide" /></td>
			</tr>

		</table>
	</div>
</body>

</html>
