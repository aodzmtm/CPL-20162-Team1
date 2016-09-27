<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="js/imageControl.js" language="javaScript"
	type="text/javascript"></script>
<script src="js/common.js" language="javaScript" type="text/javascript">
	
</script>
<link type="text/css" href="css/layout.css" rel="stylesheet" />


</head>
<body>

	<table width="100%">
		<tr>
			<td align=center colspan=3><tiles:insertAttribute name="menu" /></td>
		</tr>
		<tr>
			<td width="300px" style="vertical-align: top"><tiles:insertAttribute
					name="leftSide" /></td>
			<td width=70% height="100%" align=center><tiles:insertAttribute
					name="main" />
				<div>
						<tiles:insertAttribute name="state" /> 
				</div></td>

			<td width="300px" align=center  style="vertical-align: top"><tiles:insertAttribute
					name="rightSide" /></td>
		</tr>
		<tr>
			<td align=center colspan=3><tiles:insertAttribute name="footer" /></td>
		</tr>
	</table>
</body>
<%-- <body>

	<table
		style="width: 100%; height: 1000px; border: 1px; border-style: 1px; border-color: black">


		<tr style="height: 100%;">
			<tiles:insertAttribute name="menu" />
		</tr>


		<tr>
			<td><tiles:insertAttribute name="leftSide" /></td>




			<td style="width: 70%; height: 100%;"><tiles:insertAttribute
					name="main" />
				<div>
					<tiles:insertAttribute name="state" />
				</div></td>




			<td><tiles:insertAttribute name="rightSide" /></td>
		</tr>
		<tr style="height: 100%;">
			<tiles:insertAttribute name="footer" />
		</tr>
	</table>



</body> --%>


</html>
