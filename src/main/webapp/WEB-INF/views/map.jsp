<!doctype html>
<html>
  <head>
    <!-- <meta name="viewport" content="initial-scale=1.0, user-scalable=no"> -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta charset="UTF-8">

    <title>Spatial Cloud Demo</title>

    <!-- Latest compiled and minified CSS -->
    <link rel="stylesheet" href="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/css/bootstrap.min.css">

    <!-- jQuery library -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

    <!-- Latest compiled JavaScript -->
    <script src="http://maxcdn.bootstrapcdn.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>

    <!-- Google Map API -->
    <script type="text/javascript"
            src="http://maps.google.com/maps/api/js?sensor=false&libraries=drawing"></script>

    <script type="text/javascript" src="/resources/map.js"></script>

    <style type="text/css">
     @media screen and (max-width: 767px) {
       #panel {
         display: none
       }
     }
     #map, html, body {
       padding: 0;
       margin: 0;
       height: 100%;
     }
     body {
       padding-top: 50px;
     }
     #panel {
       width: 150px;
       height: 100%;
       font-family: Arial, sans-serif;
       font-size: 13px;
       float: left;
       margin: 10px;
     }
     #color-palette {
       clear: both;
     }
     .color-button {
       width: 14px;
       height: 14px;
       font-size: 0;
       margin: 2px;
       float: left;
       cursor: pointer;
     }
     #search-form {
       margin-top: 10px;
     }
     #search-form > label {
       margin-top: 5px;
     }
     #delete {
       margin-top: 10px;
     }
     </style>
  </head>
  <body>
    <nav class="navbar navbar-inverse navbar-fixed-top">
      <div class="container-fluid">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#myNavbar">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="/">Mosaic</a>
        </div>
        <div class="collapse navbar-collapse" id="myNavbar">
          <ul class="nav navbar-nav">
            <li class="active"><a href="/">Search</a></li>
            <li><a href="/autocomplete.html">Auto Complete</a></li>
          </ul>
        </div>
      </div>
    </nav>

    <div id="panel">
      <div id="color-palette"></div><br>
      <hr />

      <div class="form-group" id="search-form">
        <label for="nlimit">Nearest Count</label>
        <input type="text" class="form-control input-sm" value="10" id="nlimit"/>
        <label for="lradnius">Line Radius (meter)</label>
        <input type="text" class="form-control input-sm" value="100" id="lradius"/>
      </div>
      <hr />

      <div class="btn-group-vertical" id="delete">
        <button type="button" class="btn btn-default btn-sm" id="delete-button">
          Delete Selected Shape
        </button>
        <button type="button" class="btn btn-default btn-sm" id="delete-all-button">
          Delete All Shape
        </button>
      </div>
    </div>

    <div id="map"></div>
  </body>
</html>
