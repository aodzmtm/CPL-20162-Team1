<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
function alarmTimer() {
	var sum = 0;
	playAudio();

	var ticker = setInterval(function() {
		onAlarm();
		sum++;
		if (sum == 6)
			clearInterval(ticker);
	}, 1000);
}
function onAlarm() {

	var display = document.getElementById("alarm");
	if (display.innerHTML == "")
		display.innerHTML = "<div id=\"floating-panel\"></div>";
	else
		display.innerHTML = "";
}

function playAudio() {
    // Check for audio element support.
    var audio = new Audio('mp3/silen.mp3');
    
    if (window.HTMLAudioElement) {
        try {
                audio.play();         
        }
        catch (e) {
            // Fail silently but show in F12 developer tools console
             if(window.console && console.error("Error:" + e));
        }
    }
}
</script>