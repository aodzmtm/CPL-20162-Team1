<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>


<meta charset="UTF-8">

<link type="text/css" href="css/footer.css" rel="stylesheet" />
<link type="text/css" href="css/style.css" rel="stylesheet" />

<script type="text/javascript" src="js/util.js">


function dialog()
{
	$( "#dialog-confirm" ).dialog({

	      resizable: false,

	      height: "auto",

	      width: 400,

	      modal: true,

	      buttons: {

	        "Delete all items": function() {

	          $( this ).dialog( "close" );

	        },

	        Cancel: function() {

	          $( this ).dialog( "close" );

	        }

	      }

	    });


	
}

</script>


<div id="dialog-confirm" title="Empty the recycle bin?">

  <p><span class="ui-icon ui-icon-alert" style="float:left; margin:12px 12px 20px 0;"></span>These items will be permanently deleted and cannot be recovered. Are you sure?</p>

</div>


