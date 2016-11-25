<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>


<meta charset="UTF-8">

<link type="text/css" href="css/footer.css" rel="stylesheet" />
<link type="text/css" href="css/style.css" rel="stylesheet" />

<script type="text/javascript" src="js/util.js"></script>


<script type="text/javascript">
	
	


window.onload = function() {
	exampleReJson();
};

function exampleReJson() {
	var request = createJSONHttpRequest();

	request.open('POST', '/light_web/json.do');
	//Ajax 요청
	request.send(makeJson());
	request.onreadystatechange = function() {
		if (request.readyState == 4) {
			//응답이 정상이라면
			if (request.status >= 200 && request.status < 300) {

				var str = request.responseText;
				//alert(str);
				//JSON 문자열 파싱
				var json = JSON.parse(str);
				var display = document.getElementById("content");
				for (var i = 0; i < json.length; i++) {
					display.innerHTML += json[i].id + ":" + json[i].student;
				}
			} else
				alert("데이터를 가져오기 실패");
		}
	}
}

function makeJson() {
	var totalInfo = new Object();
	var dataArray = new Array();
	var dataInfo = new Object();
	var dataInfo2 = new Object();
	dataInfo.id = "3";
	dataInfo.student = "송광호";
	dataArray.push(dataInfo);
	//추가
	dataInfo2.id = "4";
	dataInfo2.student = "오광호";
	dataArray.push(dataInfo2);
	totalInfo.TestVo = dataArray;
	var jsonInfo = JSON.stringify(totalInfo);
	//  alert(jsonInfo);
	return jsonInfo;
}
</script>
	
	<div id="content"></div>
	
	
	
	


footer