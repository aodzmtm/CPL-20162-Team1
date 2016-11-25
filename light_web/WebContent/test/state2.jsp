<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Testing websockets</title>

</head>
<body>
<!-- 

	  <div style=" width: 100%; height: 30px; text-align: center;
    background: linear-gradient(#bce1fa,  #bce1fa,white);  
    border-radius: 10px 10px 0px 0px; align:center;
		          font-size:15px; color:#2e62d9;  font-weight:bold; 
		        "><div style="padding-top: 10px;">보안등 현황</div></div>

 -->


	<div>
		<div
			style="width: 100%; height: 30px; background: linear-gradient(#bce1fa,  #bce1fa,white);
			 margin-top: 10px; margin-bottom: 10px; border-radius: 10px 10px 0px 0px;
			   font-size:15px; color:#2e62d9;  font-weight:bold; ">
			<div style="padding-left: 10px; padding-top: 5px;">보안등 업데이트 정보창d</div>
		</div>
		<div style="width: 100%; /* border-style: solid; border-color: #bce1fa;  border-top-color: white; */">
			<table style="width: 100%;">
				<tr style="vertical-align: center; border-top-color: white;">
					
					<td align="center">
					<textarea id="messageWindow" rows="10"cols="50"
							style="width: 90%; margin: 10px 10px 10px 10px; border-color: #bce1fa; border-style: solid; border: 0;"></textarea>
					</td>
				</tr>
			</table>
		</div>
	</div>

	<input id="inputMessage" type="text" /> <input type="submit"
			value="send" onclick="send()" /> 

	<script type="text/javascript">
		var textarea = document.getElementById("messageWindow");
		var webSocket = new WebSocket('ws://localhost:8080/light_web/echo.do');
		var inputMessage = document.getElementById('inputMessage');
		webSocket.onerror = function(event) {
			onError(event)
		};

		webSocket.onopen = function(event) {
			onOpen(event)
		};

		webSocket.onmessage = function(event) {
			onMessage(event)
		};

		function onMessage(event) {
			textarea.value += "상대 : " + event.data + "\n";
			var x = 40.714224;
			var y = -73.997;
			var addparam = {
				lat : x,
				lng : y
			};
			addMarker(addparam);

		}

		function onOpen(event) {
			textarea.value += "연결 성공\n";
		}

		function onError(event) {
			alert(event.data);
		}

		function send() {
			textarea.value += "나 : " + inputMessage.value + "\n";
			webSocket.send(inputMessage.value);
			inputMessage.value = "";
		}
	</script>
</body>

</html>
