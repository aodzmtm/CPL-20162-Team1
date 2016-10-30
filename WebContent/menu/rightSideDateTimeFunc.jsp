<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
function rightSideEditDateTime()
{
	
	var dateTime = new Date().format("yy/MM/dd HH:mm:ss");
	
	input_date_time.value = dateTime;
	
}
</script>