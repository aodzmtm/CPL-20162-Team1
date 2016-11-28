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
<script src="jqgrid/src/i18n/grid.locale-kr.js"></script>


<script type="text/javascript" src="js/util.js"></script>


<script type="text/javascript">

</script>
</head>
<body>
	<div id="map" style="width: 100%">

	</div>
    <script type="text/javascript"> 
	window.onload = function() {search();};
    
    
    
    function search() {
    	
    	
    	var display = document.getElementById('map');
		display.innerHTML =	"<div id=\"grid_container\"><table id=\"jqGrid\"></table><div id=\"jqGridPager\"></div></div>";
		
    	
            $("#jqGrid").jqGrid({
        		url : "/light_web/gridJson.do",
               	editurl: "/light_web/edit.do",//modify
    			mtype : "POST",
                datatype: "json",
                loadtext : "로딩중..",
                //width: 780,
				height : '642px',
                rowNum: 10,
                pager: "#jqGridPager",
                page: 1,
                //colNames : [ '시퀀스', '제목','날짜','수정/삭제' ],
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
                        //stype: "select",
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
    			rownumbers : true, // row의 숫자를 표시해준다.
				loadonce: true,
				viewrecords: true
            });
			// activate the build in search with multiple option
            $('#jqGrid').navGrid("#jqGridPager", {                
                search: true, // show search button on the toolbar
                add: false,
                edit: false,
                del: false,
                refresh: true
            },
            {}, // edit options
            {}, // add options
            {
                errorTextFormat: function (data) {
                    return 'Error: ' + data.responseText}
            }, // delete options
            { multipleSearch: true, uniqueSearchFields : true, multipleGroup : true}
			);
    		resizeJqGridWidth('jqGrid', 'grid_container', $('#grid_container')
    				.width(), true);
        }
        
    	function resizeJqGridWidth(grid_id, div_id, width, tf) {
    		$(window).bind('resize', function() {
    			var resizeWidth = $('#grid_container').width(); 
    			$('#' + grid_id).setGridWidth(resizeWidth, tf);
    			$('#' + grid_id).setGridWidth(resizeWidth, tf);
    		}).trigger('resize');
    	}
    </script>

    
</body>
</html>
