<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>


<meta charset="UTF-8">


<html>

<head>
<style type="text/css">
html, body {
	height: 100%;
	margin: 0;
	padding: 0;
}
</style>



<link rel="stylesheet" type="text/css" media="screen"
	href="jqueryui/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="jqgrid/css/ui.jqgrid.css" />
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="js/jquery-1.11.0.min.js"></script>
<script src="jqueryui/jquery-ui.js"></script>
<script src="jqgrid/js/jquery.jqGrid.min.js"></script>


<script type="text/javascript" src="js/util.js"></script>


<script type="text/javascript">
	/* window.onload = function() {
		//가상의 local json data
		var gridData = [ {
			seq : "1",
			create_date : "2007-10-01",
			create_name : "test",
			title : "note",
			hitnum : "11"
		}, {
			seq : "2",
			create_date : "2007-10-02",
			create_name : "test2",
			title : "note2",
			hitnum : "22"
		} ];

		//jqGrid껍데기 생성
		$("#list").jqGrid({
			//로컬그리드이용
			datatype : "local",
			//그리드 높이
			height : 250,
			//컬럼명들
			colNames : [ '시퀀스', '제목', '등록일', '등록자명', '조회수' ],
			//컬럼모델
			rowNum : 2,
			//페이징UI적용을 위한 속성
			pager : 5,
			colModel : [ {
				name : 'seq'
			}, {
				name : 'title'
			}, {
				name : 'create_date'
			}, {
				name : 'create_name'
			}, {
				name : 'hitnum'
			} ],
			//그리드타이틀
			caption : "그리드 목록"
		});

		// 스크립트 변수에 담겨있는 json데이터의 길이만큼 
		for (var i = 0; i <= gridData.length; i++) {
			//jqgrid의 addRowData를 이용하여 각각의 row에 gridData변수의 데이터를 add한다
			$("#list").jqGrid('addRowData', i + 1, gridData[i]);
		}
	}; */

	$(function() {
		$("#list").jqGrid({
			url : '/light_web/gridJson.do',
			mtype:"POST",
			datatype : "json",
			loadtext : '로딩중..',
			height : '670px',
			//한페이지에 출력할 데이터 갯수
			//rowNum : ,
			//페이징UI적용을 위한 속성
			regional :  "kr" ,
			 pgbuttons:true,
			 viewrecords: true,
			 rowNum: 20,
	            rowTotal : null,    
	            regional :  "kr",

			//pager : "#page",
			colNames : [ '시퀀스', '제목', '등록일', '등록자명', '조회수' ],
			colModel : [ {
				name : 'id'
			}, {
				name : 'student'
			}, {
				name : 'create_date'
			}, {
				name : 'create_name'
			}, {
				name : 'hitnum'
			} ],
	
		});
		resizeJqGridWidth('list', 'grid_container', $('#grid_container').width(), true);
	})

	function resizeJqGridWidth(grid_id, div_id, width, tf) {

		// window에 resize 이벤트를 바인딩 한다. 

		$(window).bind('resize', function() {

			var resizeWidth = $('#grid_container').width(); //jQuery-ui의 padding 설정 및 border-width값때문에 넘치는 걸 빼줌.

			// 그리드의 width 초기화

			$('#' + grid_id).setGridWidth(resizeWidth, tf);

			// 그리드의 width를 div 에 맞춰서 적용

			$('#' + grid_id).setGridWidth(resizeWidth, tf); //Resized to new width as per window. 

		}).trigger('resize');

	}
</script>
</head>
<body>
<div style="width:100%">
<div id="grid_container">
	<table id="list"></table>

	<div id="page"></div>
	</div>
</div>
</body>
</html>
