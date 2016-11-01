<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
function dialogNoSelect() {
	$("#dialog-no-select").dialog({
		resizable : true,
		height : "auto",
		width : 400,

		buttons : {
			"확인" : function() {
				$(this).dialog("close");
			}
		}
	});
}

function dialogLampNull() {
	$("#dialog-lamp-null").dialog({
		resizable : true,
		height : "auto",
		width : 400,

		buttons : {
			"확인" : function() {
				$(this).dialog("close");
			}
		}
	});
}



function dialogDeleteLampNull() {
	$("#dialog-deletelamp-null").dialog({
		resizable : true,
		height : "auto",
		width : 400,

		buttons : {
			"확인" : function() {
				$(this).dialog("close");
			}
		}
	});
}


function dialogEditLampNull() {
	$("#dialog-editLamp-null").dialog({
		resizable : true,
		height : "auto",
		width : 400,

		buttons : {
			"확인" : function() {
				$(this).dialog("close");
			}
		}
	});
}


function dialogLampNullLocation() {
	$("#dialog-lamp-null-location").dialog({
		resizable : true,
		height : "auto",
		width : 400,

		buttons : {
			"확인" : function() {
				$(this).dialog("close");
			}
		}
	});
}
function dialogEditLampNullLocation() {
	$("#dialog-editLamp-null-location").dialog({
		resizable : true,
		height : "auto",
		width : 400,

		buttons : {
			"확인" : function() {
				$(this).dialog("close");
			}
		}
	});
}
function dialogLogInCheck() {
	$("#dialog-log-in-check").dialog({
		resizable : true,
		height : "auto",
		width : 400,

		buttons : {
			"확인" : function() {
				$(this).dialog("close");
			}
		}
	});
}
</script>