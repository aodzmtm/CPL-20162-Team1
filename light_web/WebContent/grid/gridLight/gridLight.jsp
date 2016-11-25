<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="js/**"></script>
<!-- <script src="js/jquery-1.11.0.min.js"></script> -->
<script src="jqueryui/jquery-ui.js"></script>
<script src="jqgrid/js/jquery.jqGrid.min.js"></script>
<script src="jqgrid/src/i18n/grid.locale-kr.js"></script>
<script type="text/javascript" src="js/util.js"></script>

<script type="text/javascript">
	function lampGrid() {

		$("#jqGrid").jqGrid(
						{
							url : "/light_web/lampInfoGridJson.do",
							editurl : "/light_web/lampInfoGridEdit.do",//modify
							mtype : "POST",
							caption : "<div class=\"grid_title_font\">보안등 현황</div>",
							datatype : "json",
							loadtext : "로딩중..",
							width : 1070,
							height : 200,
							rowNum : 10,
							pager : "#jqGridPager",
							page : 1,
							colModel : [
									{
										label : "id",
										name : 'id',
										align : "center",
										sorttype : 'integer',
										key : true,
										hidden : true
									},
									{
										label : "비컨주소",
										name : "beacon_addr",
										align : "center",
										editable : true,
									},
									{
										label : "비컨id",
										name : "beacon_id",
										align : "center",
										editable : true,
									},
									{
										label : "위치",
										name : "location",
										align : "center",
										editable : true,
									// stype defines the search type control - in this case HTML select (dropdownlist)
									//stype: "select",
									// searchoptions value - name values pairs for the dropdown - they will appear as options
									//searchoptions: { value: ":[All];ALFKI:ALFKI;ANATR:ANATR;ANTON:ANTON;AROUT:AROUT;BERGS:BERGS;BLAUS:BLAUS;BLONP:BLONP;BOLID:BOLID;BONAP:BONAP;BOTTM:BOTTM;BSBEV:BSBEV;CACTU:CACTU;CENTC:CENTC;CHOPS:CHOPS;COMMI:COMMI;CONSH:CONSH;DRACD:DRACD;DUMON:DUMON;EASTC:EASTC;ERNSH:ERNSH;FAMIA:FAMIA;FOLIG:FOLIG;FOLKO:FOLKO;FRANK:FRANK;FRANR:FRANR;FRANS:FRANS;FURIB:FURIB;GALED:GALED;GODOS:GODOS;GOURL:GOURL;GREAL:GREAL;GROSR:GROSR;HANAR:HANAR;HILAA:HILAA;HUNGC:HUNGC;HUNGO:HUNGO;ISLAT:ISLAT;KOENE:KOENE;LACOR:LACOR;LAMAI:LAMAI;LAUGB:LAUGB;LAZYK:LAZYK;LEHMS:LEHMS;LETSS:LETSS;LILAS:LILAS;LINOD:LINOD;LONEP:LONEP;MAGAA:MAGAA;MAISD:MAISD;MEREP:MEREP;MORGK:MORGK;NORTS:NORTS;OCEAN:OCEAN;OLDWO:OLDWO;OTTIK:OTTIK;PERIC:PERIC;PICCO:PICCO;PRINI:PRINI;QUEDE:QUEDE;QUEEN:QUEEN;QUICK:QUICK;RANCH:RANCH;RATTC:RATTC;REGGC:REGGC;RICAR:RICAR;RICSU:RICSU;ROMEY:ROMEY;SANTG:SANTG;SAVEA:SAVEA;SEVES:SEVES;SIMOB:SIMOB;SPECD:SPECD;SPLIR:SPLIR;SUPRD:SUPRD;THEBI:THEBI;THECR:THECR;TOMSP:TOMSP;TORTU:TORTU;TRADH:TRADH;TRAIH:TRAIH;VAFFE:VAFFE;VICTE:VICTE;VINET:VINET;WANDK:WANDK;WARTH:WARTH;WELLI:WELLI;WHITC:WHITC;WILMK:WILMK;WOLZA:WOLZA" }
									},

									{
										label : "날짜",
										name : "date_time",
										align : "center",
										editable : true,
										search : true,

										sorttype : 'date',
										datatype : "local",
										editoptions : {
											dataInit : function(element) {
												$(element)
														.val(
																new Date()
																		.format("yy/MM/dd HH:mm:ss"));
											},
											dataEvents : [ {
												type : 'click',
												fn : function(e) { // 값 : this.value || e.target.val()
													this.value = new Date()
															.format("yy/MM/dd HH:mm:ss");
												},
											} ]

										},
										searchoptions : {
											dataInit : function(element) {
												$(element)
														.datepicker(
																{
																	id : 'orderDate_datePicker',
																	dateFormat : 'yy-mm-dd',
																	maxDate : new Date(
																			2020,
																			0,
																			1),
																	showOn : 'focus'
																});
											}
										}
									},
									{
										label : "정전상태",
										name : "power_off",
										align : "center",
										editable : true,
										edittype : "select",
										editoptions : {
											value : "0:점등;1:정전"
										},
										formatter : 'select',
										search : true,
										stype : "select",
										searchoptions : {
											value : "0:점등;1:정전"
										}
									},
									{
										label : "이상점등",
										name : "abnormal_blink",
										align : "center",
										editable : true,
										edittype : "select",
										editoptions : {
											value : "0:정상;1:이상점등;2:이상소등"
										},
										formatter : 'select',
										search : true,
										stype : "select",
										searchoptions : {
											value : "0:정상;1:이상점등;2:이상소등"
										}
									},
									{
										label : "누전여부",
										name : "short_circuit",
										align : "center",
										editable : true,
										edittype : "select",
										editoptions : {
											value : "0:정상;1:누전"
										},
										formatter : 'select',
										search : true,
										stype : "select",
										searchoptions : {
											value : "0:정상;1:누전"
										}
									},
									{
										label : "보안등고장",
										name : "lamp_failure",
										align : "center",
										editable : true,
										edittype : "select",
										editoptions : {
											value : "0:정상;1:램프고장;2:안정기 고장;3:램프 안정기 고장"
										},
										formatter : 'select',
										search : true,
										stype : "select",
										searchoptions : {
											value : "0:정상;1:램프고장;2:안정기 고장;3:램프 안정기 고장"
										}
									},
									{
										label : "점소등상태",
										name : "lamp_state",
										align : "center",
										editable : true,
										edittype : "select",
										editoptions : {
											value : "0:정상소등;1:정상 점등;2:강제소등;3:강제 점등"
										},
										formatter : 'select',
										search : true,
										stype : "select",
										searchoptions : {
											value : "0:정상소등;1:정상 점등;2:강제소등;3:강제 점등"
										}
									},

									{

										label : "비고",
										width : 90,
										name : "actions",
										align : "center",
										formatter : "actions",
										formatoptions : {
											editOptions : {},
											delOptions : {}
										}
									}, ],
							//gridComplete :,
							//rowList : [ 20, 30, 50 ],
							multiselect : true,
							rownumbers : true, // row의 숫자를 표시해준다.
							loadonce : true,
							viewrecords : true
						});
		$('#jqGrid').navGrid("#jqGridPager", {
			search : true, // show search button on the toolbar
			add : false,
			edit : false,
			del : false,
			refresh : true
		}, {}, // edit options
		{}, // add options
		{
			errorTextFormat : function(data) {
				return 'Error: ' + data.responseText
			}
		}, // delete options
		{
			multipleSearch : true,
			uniqueSearchFields : true,
			multipleGroup : true
		});

	}

	/* function resizeJqGridWidth(grid_id, div_id, width, tf) {
	 $(window).bind('resize', function() {

	 var resizeWidth = 1050;
	 $('#' + grid_id).setGridWidth(resizeWidth, tf);
	 $('#' + grid_id).setGridWidth(resizeWidth, tf);
	 }).trigger('resize');
	 }
	 function startEdit() {
	 var grid = $("#jqGrid");
	 var ids = grid.jqGrid('getDataIDs');
	 for (var i = 0; i < ids.length; i++) {
	 grid.jqGrid('editRow', ids[i]);
	 }
	 };

	 function saveRows() {
	 var grid = $("#jqGrid");
	 var ids = grid.jqGrid('getDataIDs');

	 for (var i = 0; i < ids.length; i++) {
	 grid.jqGrid('delRowData', ids[i]);
	 }
	 } */
</script>