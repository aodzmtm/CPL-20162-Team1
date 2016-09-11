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
	<center>

		<table style="width: 100%; height: 500px; border: 1px; border-style: 1px;">
			<tr>
				<tiles:insertAttribute name="menu" />
			</tr>
			<tr>
				<td><tiles:insertAttribute name="leftSide" /></td>
				<td style="width: 70%;">
					<div>
						<tiles:insertAttribute name="main" />
					</div>
					<div>
						<tiles:insertAttribute name="state" />
					</div>
				</td>

				<td><tiles:insertAttribute name="rightSide" /></td>
			</tr>
			<tr>


			</tr>
		</table>


		<div>
			<tiles:insertAttribute name="footer" />
		</div>
	</center>
</body>


</html>
