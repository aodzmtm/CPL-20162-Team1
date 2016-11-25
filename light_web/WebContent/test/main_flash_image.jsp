<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="js/imageControl.js" language="javaScript"
	type="text/javascript"></script>
<script src="js/InsertFlash.js" language="javaScript"
	type="text/javascript"></script>
<script src="js/common.js" language="javaScript" type="text/javascript"></script>

<link type="text/css" href="css/index.css" rel="stylesheet" />
<link type="text/css" herf="css/style.css" rel="stylesheet" />

<script type="text/javascript" src="js/ajax.js"></script>
<script type="text/javascript">
	var tf = 0;
	var fixText = 600;
	var textPixel = fixText;
	var text_time;
	var background;
	var imageNum = 0;
	var imageCnt = 7;
	var left;
	var right;
	var img_url = "url(img/ven_";
	var img_format = ".jpg)";
	var timer;
	var current_date;
	var browser = navigator.userAgent.toLocaleLowerCase();
	var text_array = new Array();
	text_array[1] = "<h2>WELCOME TO UTIOS</h2><br/><h3>유비쿼터스 시대의 선두주자</h3><p>생활문화를 창조하는<br/>꿈/과/미/래/가 있는 회사 </p>";
	text_array[2] = "2";
	text_array[3] = "3";
	text_array[4] = "4";
	text_array[5] = "5";
	text_array[6] = "<h2>WELCOME TO UTIOS</h2><br/><h3>유비쿼터스 시대의 선두주자</h3><p>생활문화를 창조하는<br/>꿈/과/미/래/가 있는 회사 </p>";
	text_array[7] = "7";

	var link_array = new Array();
	link_array[1] = "main.do";
	link_array[2] = "business.do";
	link_array[3] = "board.do";
	link_array[4] = "boardeditor.do";
	link_array[5] = "http://www.naver.com";
	link_array[6] = "http://www.google.com";
	link_array[7] = "http://www.nate.com";

	function setup() {
		background = document.getElementById("layer_parents");
		setImage();

		left = document.getElementById("layer_left");
		right = document.getElementById("layer_right");

		if (browser.indexOf('msie 8') != -1 || browser.indexOf('msie 7') != -1) {
			left.attachEvent("onmouseover", left_mouseOver);
			right.attachEvent("onmouseover", right_mouseOver);
			left.attachEvent("onmouseout", left_mouseOut);
			right.attachEvent("onmouseout", right_mouseOut);
		} else {
			left.addEventListener("mouseover", left_mouseOver, false);
			right.addEventListener("mouseover", right_mouseOver, false);
			left.addEventListener("mouseout", left_mouseOut, false);
			right.addEventListener("mouseout", right_mouseOut, false);
		}

	}

	function left_mouseOver() {
		document.getElementById("left").style.display = "block";
	}

	function right_mouseOver() {
		document.getElementById("right").style.display = "block";
	}

	function left_mouseOut() {
		document.getElementById("left").style.display = "none";
	}

	function right_mouseOut() {
		document.getElementById("right").style.display = "none";
	}

	function setImage() {

		if (imageNum == 0) {
			imageNum++;
			background.style.backgroundImage = img_url + imageNum + img_format;
			textChange();
			return setImage();
		} else {
			timer = setInterval("changeImage()", 5000);
		}

	}

	function changeImage() {
		current_date = new Date();
		console.log(current_date.toTimeString());
		imageNum++;
		if (imageNum != imageCnt + 1) {
			textChange();
			background.style.backgroundImage = img_url + imageNum + img_format;

		} else {

			clearInterval(timer);
			imageNum = 0;
			setImage();
		}

	}
	function leftClickImage() {
		imageNum--;

		if (imageNum == 0) {
			imageNum = imageCnt;
			background.style.backgroundImage = img_url + imageNum + img_format;
			//alert(imageNum);
			clearInterval(timer);
			textChange();
			setImage();
		} else {
			background.style.backgroundImage = img_url + imageNum + img_format;
			//alert(imageNum);
			clearInterval(timer);
			textChange();
			setImage();
		}

	}
	function rightClickImage() {

		current_date = new Date();
		console.log(current_date.toTimeString());
		imageNum++;
		if (imageNum != imageCnt + 1) {
			background.style.backgroundImage = img_url + imageNum + img_format;
			clearInterval(timer);
			textChange();
			setImage();
		} else {
			imageNum = 0;
			clearInterval(timer);
			textChange();
			setImage();
		}
	}
	function imageLink() {
		/*
		if(imageNum == 1)
		{
		
		}
		else if(imageNum == 2)
		{
		
		}
		else if(imageNum == 3)
		{
		}
		else if(imageNum == 4)
		{
		}
		 */
		location.href = link_array[imageNum];
	}
	function textChange() {
		
		var textline = document.getElementById("text_link");
		//alert(imageNum);
		textline.innerHTML = text_array[imageNum];
		//textTime = setInterval("textMove()",40);	
		moveIt();
	}
	function moveIt() {
	/*	IE만 적용
	  if (document.all) {
		
		 	document.all.text_link.style.pixelLeft-=2;   //수평 단계
		    if(text_link.style.pixelLeft == 0){
		    
		    	clearTimeout(text_time);
		    }else{
		    	text_time = setTimeout("moveIt()",1);
		    	if (text_link.style.pixelLeft<=0){text_link.style.pixelLeft=600;
		    }
	  }
	  }
	*/
	/** IE chrome 적용 **/
	    textChrome = document.getElementById("text_link");
		if(tf == 0){
		textChrome.style.left=textPixel+"px";
	    //alert("0");
	    tf =1;
		moveIt();
		}else if(tf == 1)
			{
			//alert("1");
			textPixel -= 5;

			textChrome.style.left=textPixel+"px";
			if(textChrome.style.left == 0+"px"){
			
				clearTimeout(text_time);
			
				textPixel = fixText;
			}else
				text_time = setTimeout("moveIt()",0);
			}
	}

</script>





</head>
<body onload="setup()">


	<div id="layer_parents"
		style="width: 900px; height: 400px; display: table-cell; background-image: none; display: block; position: relative;">

		<!--  <img alt="" src="img/main_app.png" width="900" height="250px"> -->
		<div id="layer_left"
			style="width: 100px; height: 100%; display: block; position: relative; float: left;">
			<div id="left"
				style="width: 100%; height: 100%; background-position: center; background-image: url(img/left_ar.png); background-repeat: no-repeat; display: none; position: relative;"
				onclick="leftClickImage()"></div>
		</div>
		<div id="link"
			style="width: 700px; height: 100%; display: block; display: table-cell; vertical-align: middle; position: relative; float: left;"
			onclick="imageLink()">
			<table style="width: 100%; height: 100%;">
				<tr>
					<td>
						<div id="text_link" style="padding: 150px 0 0 0; color: white; position: relative;">
							
						</div>
					</td>
				</tr>
			</table>
		</div>
		<div id="layer_right"
			style="width: 100px; height: 100%; display: block; position: relative; float: right;">
			<div id="right"
				style="width: 100%; height: 100%; background-position: center; background-image: url(img/right_ar.png); background-repeat: no-repeat; display: none; position: relative;"
				onclick="rightClickImage()"></div>
		</div>

	</div>



</body>
</html>