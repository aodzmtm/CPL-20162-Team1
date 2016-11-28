<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<meta name="viewport" content="width=device-width, initial-scale=1">

<title><tiles:insertAttribute name="title" /></title>
<link rel="stylesheet" type="text/css" media="screen"
	href="jqueryui/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="jqgrid/css/ui.jqgrid.css" />
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="js/**"></script>
<script src="jqueryui/jquery-ui.js"></script>
<script src="jqgrid/js/jquery.jqGrid.min.js"></script>
<script src="jqgrid/src/i18n/grid.locale-kr.js"></script>
<script type="text/javascript" src="js/util.js"></script>

<tiles:insertAttribute name="main" />