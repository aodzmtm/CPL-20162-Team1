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
.ui-dialog{
    font-size: 12px !important;
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
<script src="jqgrid/src/i18n/grid.locale-kr.js"></script>


<script type="text/javascript" src="js/util.js"></script>


<script type="text/javascript">
	window.onload = function() {
		search();
	};
	//업데이트

	function search() {
		$("#list").jqGrid({
			url : '/light_web/gridJson.do',
			mtype : "POST",
			datatype : "json",
			loadtext : '로딩중..',
			caption :"aabcd",
			rowNum : 10,
			pager : "#page",
			rownumbers : true, // row의 숫자를 표시해준다.
			  page: 1,
            colNames : [ '시퀀스', '제목','날짜','수정/삭제' ],
            colModel: [
                       {   label : "id",
   						sorttype: 'integer',
   						name: 'id', 
   						key: true, 
   						//width: 75 
   					},
                       {
   						label: "student",
                           name: 'student',
                           //width: 150,
                           editable: true,
                           // stype defines the search type control - in this case HTML select (dropdownlist)
                           stype: "select",
                           // searchoptions value - name values pairs for the dropdown - they will appear as options
                           //searchoptions: { value: ":[All];ALFKI:ALFKI;ANATR:ANATR;ANTON:ANTON;AROUT:AROUT;BERGS:BERGS;BLAUS:BLAUS;BLONP:BLONP;BOLID:BOLID;BONAP:BONAP;BOTTM:BOTTM;BSBEV:BSBEV;CACTU:CACTU;CENTC:CENTC;CHOPS:CHOPS;COMMI:COMMI;CONSH:CONSH;DRACD:DRACD;DUMON:DUMON;EASTC:EASTC;ERNSH:ERNSH;FAMIA:FAMIA;FOLIG:FOLIG;FOLKO:FOLKO;FRANK:FRANK;FRANR:FRANR;FRANS:FRANS;FURIB:FURIB;GALED:GALED;GODOS:GODOS;GOURL:GOURL;GREAL:GREAL;GROSR:GROSR;HANAR:HANAR;HILAA:HILAA;HUNGC:HUNGC;HUNGO:HUNGO;ISLAT:ISLAT;KOENE:KOENE;LACOR:LACOR;LAMAI:LAMAI;LAUGB:LAUGB;LAZYK:LAZYK;LEHMS:LEHMS;LETSS:LETSS;LILAS:LILAS;LINOD:LINOD;LONEP:LONEP;MAGAA:MAGAA;MAISD:MAISD;MEREP:MEREP;MORGK:MORGK;NORTS:NORTS;OCEAN:OCEAN;OLDWO:OLDWO;OTTIK:OTTIK;PERIC:PERIC;PICCO:PICCO;PRINI:PRINI;QUEDE:QUEDE;QUEEN:QUEEN;QUICK:QUICK;RANCH:RANCH;RATTC:RATTC;REGGC:REGGC;RICAR:RICAR;RICSU:RICSU;ROMEY:ROMEY;SANTG:SANTG;SAVEA:SAVEA;SEVES:SEVES;SIMOB:SIMOB;SPECD:SPECD;SPLIR:SPLIR;SUPRD:SUPRD;THEBI:THEBI;THECR:THECR;TOMSP:TOMSP;TORTU:TORTU;TRADH:TRADH;TRAIH:TRAIH;VAFFE:VAFFE;VICTE:VICTE;VINET:VINET;WANDK:WANDK;WARTH:WARTH;WELLI:WELLI;WHITC:WHITC;WILMK:WILMK;WOLZA:WOLZA" }
                       },
                       { 
   						label: "Order Date",
                           name: 'OrderDate',
                           //width: 150,
                           editable: true,
   						search: true,
   						sorttype:'date',
                           searchoptions: {
                               // dataInit is the client-side event that fires upon initializing the toolbar search field for a column
                               // use it to place a third party control to customize the toolbar
                               dataInit: function (element) {
                                   $(element).datepicker({
                                       id: 'orderDate_datePicker',
                                       dateFormat: 'yy-mm-dd',
                                       //minDate: new Date(2010, 0, 1),
                                       maxDate: new Date(2020, 0, 1),
                                       showOn: 'focus'
                                   });
                               }
                           }
                       },                    
                      
                       {
   						label: "Edit Actions",
                           name: "actions",
                           //width: 50,
                           formatter: "actions",
                           formatoptions: {
                               //keys: true,
                               editOptions: {},
                               addOptions: {},
                               delOptions: {}
                           }       
                       },
                   ],
			/*
			gridComplete : function() {
				$('.jqgrow').click(function(e) {
					var rowId = $(this).attr('id'); //
					var list = $('#list').getRowData(rowId);
					alert(list.id + list.student);
				});
			},*/
			loadError : function(xhr, status, error) {

				// 데이터 로드 실패시 실행되는 부분

				alert(error);

			},

			onSelectRow : function(ids) {

			//	alert('row 선택시 ids:');

			},

			ondblClickRow : function(rowid, iRow, iCol, e) {

			//	$("#list").editGridRow(rowid, updateDialog);

			},

			//그리드 수정시 submit 전                       

			beforeSubmitCell : function(rowid, cellname, value) {
			alert(rowid+cellname+value);
				return {
					"id" : rowid,
					"cellName" : cellname,
					"cellValue" : value
				}

			},

			//그리드 수정시 submit 후

			afterSubmitCell : function(res) {
				alert(res.responseText);
				var aResult = $.parseJSON(res.responseText);

				var userMsg = "수정시 오류가 발생되었습니다.";

				if ((aResult.jqResult == "1")) {

					userMsg = "수정되었습니다.";

					alert(userMsg);

				}

				return [ (aResult.jqResult == "1") ? true : false, userMsg ];

			}

		}).navGrid('#page', {
			add : false,
			del : false,
			edit :false,
			refresh : true,
			search : true,
		});

		resizeJqGridWidth('list', 'grid_container', $('#grid_container')
				.width(), $('#grid_container')
				.height(), true);
	}

	function research() {

		$("#list").jqGrid("setGridParam", {
			postData : {
				id : '2'
			}
		});

		$("#list").trigger("reloadGrid");

	}

	function resizeJqGridWidth(grid_id, div_id, width,height, tf) {
		$(window).bind('resize', function() {
			var resizeWidth = $('#grid_container').width(); 
			var resizeHeight = $('#grid_container').height(); 
			$('#' + grid_id).setGridWidth(resizeWidth, tf);
			$('#' + grid_id).setGridWidth(resizeWidth, tf);
			$('#' + grid_id).setGridHeight(resizeHeight, tf);
			$('#' + grid_id).setGridHeight(resizeHeight, tf);
		}).trigger('resize');
	}
	
	
	function startEdit() {
        var grid = $("#jqGrid");
        var ids = grid.jqGrid('getDataIDs');

        for (var i = 0; i < ids.length; i++) {
            grid.jqGrid('editRow',ids[i]);
        }
    };

    function saveRows() {
        var grid = $("#jqGrid");
        var ids = grid.jqGrid('getDataIDs');

        for (var i = 0; i < ids.length; i++) {
            grid.jqGrid('saveRow', ids[i]);
        }
    }
    
    function getSelectedRows() {
        var grid = $("#jqGrid");
        var rowKey = grid.getGridParam("selrow");

        if (!rowKey)
            alert("No rows are selected");
        else {
            var selectedIDs = grid.getGridParam("selarrrow");
            var result = "";
            for (var i = 0; i < selectedIDs.length; i++) {
                result += selectedIDs[i] + ",";
            }

            alert(result);
        }                
    }

	
	
</script>
</head>
<body>
<input onclick="getSelectedRows()" type="button" value="Get Selected Rows">
	<div>
		<div id="grid_container">
			<table id="list"></table>

			<div id="page"></div>
		</div>
	</div>
	<!-- 	<button onclick="research()">send</button> -->
</body>
</html>
