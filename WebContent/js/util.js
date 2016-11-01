	Date.prototype.format = function(f) {
		if (!this.valueOf()) return " ";

		var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];
		var d = this;
		
		return f.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
			switch ($1) {
				case "yyyy": return d.getFullYear();
				case "yy": return (d.getFullYear() % 1000).zf(2);
				case "MM": return (d.getMonth() + 1).zf(2);
				case "dd": return d.getDate().zf(2);
				case "E": return weekName[d.getDay()];
				case "HH": return d.getHours().zf(2);
				case "hh": return ((h = d.getHours() % 12) ? h : 12).zf(2);
				case "mm": return d.getMinutes().zf(2);
				case "ss": return d.getSeconds().zf(2);
				case "a/p": return d.getHours() < 12 ? "오전" : "오후";
				default: return $1;
			}
		});
	};



String.prototype.string = function(len){var s = '', i = 0; while (i++ < len) { s += this; } return s;};
	String.prototype.zf = function(len){return "0".string(len - this.length) + this;};
	Number.prototype.zf = function(len){return this.toString().zf(len);};

	function exceptStrDateTime(str)
	{
		var dateTime = null;
			var dateWithTime= str.split(" ");
			var date = dateWithTime[0].split("/");
			var time = dateWithTime[1].split(":");
			var dateTime = date[0]+date[1]+date[2]+time[0]+time[1]+time[2];
		return dateTime;
	}


function dataURItoBlob( dataURI ) {
	var byteString =  dataURI.split( ',' )[ 1 ] ;
	// alert("encode Data:::"+dataURI);
	return  byteString;
}
/*
 * function dataURItoBlob( dataURI ) { var byteString = atob( dataURI.split( ',' )[
 * 1 ] ) ; var mimeString = dataURI.split( ',' )[ 0 ].split( ':' )[ 1 ].split(
 * ';' )[ 0 ] ; var arrayBuffer = new ArrayBuffer( byteString.length ) ; var ia =
 * new Uint8Array( arrayBuffer ) ; for( var i = 0 ; i < byteString.length ; i++ ) {
 * ia[ i ] = byteString.charCodeAt( i ) ; } return new Blob( [arrayBuffer], {
 * "type": mimeString } ) ; }
 */

function createJSONHttpRequest() {
	var request;
	// IE8.0 이하 브라우저가 아닐 때
	if (window.XMLHttpRequest) {
		request = new XMLHttpRequest();
		// alert("Ajax 요청 객체 생성");
	}

	// IE8.0 이하 브라우저일 때
	else if (window.ActiveXObject) {

		request = new ActiveXObject("Microsoft.XMLHTTP");

		// alert("IE에서 객체 생성");

	} else {

		alert("객체 생성 실패");

	}
    return request ;
}

function post_to_url( params) {

   // method = method || "post"; // 전송 방식 기본값을 POST로

    var form = document.createElement("form");
  // form.setAttribute("method", method);
   // form.setAttribute("action", path);

 
     for(var key in params) {
        var hiddenField = document.createElement("input");
        hiddenField.setAttribute("type", "hidden");
        hiddenField.setAttribute("name", key);
        hiddenField.setAttribute("value", params[key]);
        form.appendChild(hiddenField);
    }
    document.body.appendChild(form);
    return form;
    // form.submit();
}
function imgReSizing( which, maxWidth ) {
	var width = eval( "document." + which + ".width" ) ;
	var height = eval( "document." + which + ".height" ) ;
	// alert( "width1[" + width + "] height1[" + height + "]" ) ;
	
	if( width > maxWidth ) { // 이미지가 maxWidth보다 크다면 너비를 maxWidth으로 맞추고 비율에 맞춰
								// 세로값을 변경한다.
		height = height / ( width / maxWidth ) ;
		eval( "document." + which + ".width = maxWidth" ) ;
		eval( "document." + which + ".height = height" ) ;
	}
}

function uploadImage( w, h, idx ) {
	var width = eval( "document.image" + idx + ".width" ) ;
	var height = eval( "document.image" + idx + ".height" ) ;
	// alert( "width2[" + width + "] height2[" + height + "]" ) ;

	if( width > w && width >= height ) {
		eval( "document.image" + idx + ".height = w / width * height ;" ) ;
		eval( "document.image" + idx + ".width = w ;" ) ;
	}
	if( height > h && height > width ) {
		eval( "document.image" + idx + ".width = h / height * width ;" ) ;
		eval( "document.image" + idx + ".height = h ;" ) ;
	}
	eval( "document.image" + idx + ".style.visibility = 'visible' ;" ) ;
}

function currentDatatimeMilli() {
	var date = new Date() ;
	return date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds() + ":" + date.getMilliseconds() ;
}

window.onerror = function ( msg, url, line ) {
    alert( "Message : " + msg + "\nUrl : " + url + "\nLine number : " + line ) ;
}

function checkBrowser() {
	// 1. Internet Explorer 11
	// Mozilla/5.0 (Windows NT 6.3; WOW64; Trident/7.0; rv:11.0) like Gecko
	 
	// 2. Internet Explorer 10
	// Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.1; WOW64; Trident/6.0)
	 
	// 3. Safari
	// Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/534.57.2 (KHTML, like
	// Gecko) Version/5.1.7 Safari/534.57.2

	// 4. Chrome
	// Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like
	// Gecko) Chrome/30.0.1599.101 Safari/537.36
	 
	// 5. Opera
	// Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like
	// Gecko) Chrome/30.0.1599.101 Safari/537.36 OPR/17.0.1241.53

	// 6. Opera 구버전
	// Opera/9.80 (Windows NT 6.1; WOW64; U; ko) Presto/2.10.229 Version/11.62
	 
	// 7. Firefox
	// Mozilla/5.0 (Windows NT 6.3; WOW64; rv:24.0) Gecko/20100101 Firefox/24.0
	 
	// 여기서 각각 특징있는 단어를 뽑아서 브라우저를 구별하면 됩니다.
	// 한가지 주의사항이 있는데, IE10까지는 MSIE라는 단어로 구별해왔습니다만 IE11에는 자세히 보면 MSIE라는 단어가 없습니다.
	// 그래서 IE11은 Trident 라는 단어로 구별해야합니다.
	// Trident라는 단어는 IE6~IE7 에는 없고 IE8~IE11까지 있습니다.
	 
	// 오페라도 Opera와 OPR 두 가지로 구별해야겠습니다.
	
	// document.write( window.navigator.userAgent ) ;
	
	var agent = window.navigator.userAgent ;
	
	if( agent.indexOf( "Trident" ) != -1 ) { return 1 ; }
	else if( agent.indexOf( "MSIE" ) != -1 ) { return 2 ; }
	else if( agent.indexOf( "OPR" ) != -1 ) { return 5 ; }
	else if( agent.indexOf( "Opera" ) != -1 ) { return 6 ; }
	else if( agent.indexOf( "Safari" ) != -1 ) {
		if( agent.indexOf( "Chrome" ) != -1 ) { return 4 ; }
		else { return 3 ; }
	}
	else if( agent.indexOf( "Firefox" ) != -1 ) { return 7 ; }
	else { return -1 ; }
}

// import="javax.servlet.http.HttpServletRequest"
// String ua = request.getHeader( "User-Agent" ) ;
// out.print( ua ) ;
