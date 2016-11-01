<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
function getGridRequest() {
	var request = createJSONHttpRequest();
	var display = document.getElementById('grid');
	request.open('POST', '/light_web/grid/loadGrid.jsp');
	//Ajax 요청
	request.send();
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			//응답이 정상이라면
			if (request.status >= 200 && request.status < 300) {

				var str = request.responseText;
				//alert(str);
				display.innerHTML = str;
				lampGrid();
				lampHistoryGrid();
			} else
				alert("데이터를 가져오기 실패");
		}
	}
}


</script>