<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-1.12.1.js"></script>
<script type="text/javascript" src="js/sockjs-1.1.1.min.js"></script>
<script type="text/javascript" src="broadcast.js"></script>
 
<script type="text/javascript">
<script>
 
    var log = function(s) {
        console.log(s);
        if (document.readyState !== "complete") {
            log.buffer.push(s);
        } else {
            document.getElementById("output").innerHTML += (s + "\n")
        }
    }
    log.buffer = [];
 
    url = "ws://localhost:8080/light/main.do";
    w = new WebSocket(url);
 
    w.onopen = function() {
        log("open");
        w.send("thank you for accepting this Web Socket request");
    }
 
    w.onmessage = function(e) {
        console.log(e.data);
        log(e.data);
    }
 
    w.onclose = function(e) {
        log("closed");
    }
 
    window.onload = function() {
        log(log.buffer.join("\n"));
 
        document.getElementById("sendButton").onclick = function() {
            console.log(document.getElementById("inputMessage").value);
            w.send(document.getElementById("inputMessage").value);
        }
              // 간지나게 엔터키 누르면 메시지 날림
        document.getElementById("inputMessage").onkeypress = function() {
            if (event.keyCode == '13') {
                value = document.getElementById("inputMessage").value
                w.send(value);
                document.getElementById("inputMessage").value = "";
            }
        }
    }
</script>
 
<input type="text" id="inputMessage">
<button id="sendButton">Send</button>
<pre id="output"></pre>
