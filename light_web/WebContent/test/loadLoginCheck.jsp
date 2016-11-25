<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<meta charset="UTF-8">

<div align="center">
<table style="height:350px;">
	<tr>
		<th>
			<div id="grid_container">
				<table id="jqGrid"></table>
				<div id="jqGridPager"></div>
			</div>
		</th>

	</tr>

<tr>
		<th>

			<div style="float: right;">
				<input class="ui-button ui-corner-all ui-widget" id="button"
					style="font-size: 12px; font-weight: none;" onclick="batchEdit()"
					type="button" value="일괄 수정"> <input
					class="ui-button ui-corner-all ui-widget" id="button"
					style="font-size: 12px; font-weight: none;"
					onclick="batchCancelEdit()" type="button" value="일괄 취소"> <input
					class="ui-button ui-corner-all ui-widget" id="button"
					style="font-size: 12px; font-weight: none;" onclick="batchSave()"
					type="button" value="일괄 저장"> <input
					class="ui-button ui-corner-all ui-widget" id="button"
					style="font-size: 12px; font-weight: none;" onclick="batchDelete()"
					type="button" value="일괄 삭제">
			</div>

		</th>
		</tr>

	

</table>
<table style="height:350px;">
<tr>
		<th>
			<div id="history_grid_container">
				<table id="sjqGrid"></table>
				<div id="sjqGridPager"></div>
			</div>
		</th>

		

	</tr>
	
	<tr>
	<th>
			<div style="float: right;">

			<input
					class="ui-button ui-corner-all ui-widget" id="button"
					style="font-size: 12px; font-weight: none;"
					onclick="historyBatchCancelEdit()" type="button" value="일괄 취소">
			 <input
					class="ui-button ui-corner-all ui-widget" id="button"
					style="font-size: 12px; font-weight: none;"
					onclick="historyBatchDelete()" type="button" value="일괄 삭제">
			</div>




		</th>
	</tr>
</table>
</div>