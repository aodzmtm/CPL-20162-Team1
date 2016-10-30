<!doctype html>

<html lang="en">

<head>

  <meta charset="utf-8">

  <meta name="viewport" content="width=device-width, initial-scale=1">

  <title>jQuery UI Selectable - Default functionality</title><link rel="stylesheet" type="text/css" media="screen"
	href="jqueryui/jquery-ui.css" />
<link rel="stylesheet" type="text/css" media="screen"
	href="jqgrid/css/ui.jqgrid.css" />
<script src="http://code.jquery.com/jquery-latest.js"></script>
<script src="js/**"></script>
<!-- <script src="js/jquery-1.11.0.min.js"></script> -->
<script src="jqueryui/jquery-ui.js"></script>
<script src="jqgrid/js/jquery.jqGrid.min.js"></script>
<script src="jqgrid/src/i18n/grid.locale-kr.js"></script>


<script type="text/javascript" src="js/util.js"></script>



  <style>

  #feedback { font-size: 1.4em; }

  #selectable .ui-selecting { background: #FECA40; }

  #selectable .ui-selected { background: #F39814; color: white; }

  #selectable { list-style-type: none; margin: 0; padding: 0; width: 60%; }

  #selectable li { margin: 3px; padding: 0.4em; font-size: 1.4em; height: 18px; }

  </style>


  <script>

  $( function() {

    $( "#selectable" ).selectable();

  } );

  </script>

</head>

<body>

 

<ol id="selectable">

  <li class="ui-widget-content">Item 1</li>

  <li class="ui-widget-content">Item 2</li>

  <li class="ui-widget-content">Item 3</li>

  <li class="ui-widget-content">Item 4</li>

  <li class="ui-widget-content">Item 5</li>

  <li class="ui-widget-content">Item 6</li>

  <li class="ui-widget-content">Item 7</li>

</ol>

 

 

</body>

</html>

