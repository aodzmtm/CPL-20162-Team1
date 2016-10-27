<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<script type="text/javascript">
function lampHistoryGrid() {

	$("#sjqGrid")
			.jqGrid(
					{
						url : "/light_web/historyInfoGridJson.do",
						editurl : "/light_web/historyInfoGridEdit.do",//modify
						mtype : "POST",
						caption : "<div class=\"grid_title_font\">보안등 고장 및 수리 내역</div>",
						datatype : "json",
						loadtext : "로딩중..",
						width : 1070,
						height : 200,
						rowNum : 10,
						pager : "#sjqGridPager",

						// postData : {date_time:"abc"},
						page : 1,
						//colNames : [ '시퀀스', '제목','날짜','수정/삭제' ],
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
									editable : false,
								},
								{
									label : "비컨id",
									name : "beacon_id",
									align : "center",
									editable : false,
								},
								{
									label : "위치",
									name : "location",
									align : "center",
									editable : false,
								},
								{
									label : "날짜",
									name : "date_time",
									align : "center",
									editable : false,
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
									label : "처리날짜",
									name : "repair_date_time",
									align : "center",
									editable : false,
									search : true,
									sorttype : 'date',
									datatype : "local",
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
									label : "장애원인",
									name : "failure_reason_id",
									align : "center",
									editable : false,
									edittype : "select",
									editoptions : {
										value : "0:정상;1:정전;2:이상점등;3:이상소등;4:누전;5:램프고장;6:안정기고장;7:램프안정기고장;8:강제소등;9:강제점등"
									},
									formatter : 'select',
									search : true,
									stype : "select",
									searchoptions : {
										value : "0:정상;1:정전;2:이상점등;3:이상소등;4:누전;5:램프고장;6:안정기고장;7:램프안정기고장;8:강제소등;9:강제점등"
									}
								}, {
									label : "장애처리",
									name : "repair",
									align : "center",
									editable : true,
									edittype : "select",
									editoptions : {
										value : "0:수리;1:고장"
									},
									formatter : 'select',
									search : true,
									stype : "select",
									searchoptions : {
										value : "0:수리;1:고장"
									}
								}, {

									label : "비고",
									width : 70,
									name : "actions",
									align : "center"
								}, ],
						//rowList : [ 20, 30, 50 ],
						multiselect : true,
						rownumbers : true, // row의 숫자를 표시해준다.
						loadonce : true,
						viewrecords : true
					});
	$("#sjqGrid").navGrid("#sjqGridPager", {
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
</script>