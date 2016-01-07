var drawingManager;
var selectedShape;
var colors = ['#1E90FF', '#FF1493', '#32CD32', '#FF8C00', '#4B0082'];
var selectedColor;
var colorButtons = {};
var markers = [];
var shapes = [];
var global_map;

function clearSelection() {
    if (selectedShape) {
        selectedShape.setEditable(false);
        selectedShape = null;
    }
}

function setSelection(shape) {
    clearSelection();
    selectedShape = shape;
    shape.setEditable(true);
    selectColor(shape.get('fillColor') || shape.get('strokeColor'));
}

function deleteSelectedShape() {
    if (selectedShape) {
        selectedShape.setMap(null);
    }
}

function deleteAllShape() {
    var i;
    for (i = 0; i < shapes.length; i++) {
        shapes[i].setMap(null);
    }
    for (i = 0; i < markers.length; i++) {
        markers[i].setMap(null);
    }
}

function drawGeojson() {
    var geoobj = JSON.parse($('#geojson').val());
    var shape;
    var i;

    switch (geoobj.type) {
    case 'Point':
        {
            var latlng = {lat: geoobj.coordinates[1],
                          lng: geoobj.coordinates[0]};
            shape = new google.maps.Marker({
                position: latlng,
                map: global_map
            });
        }
        break;
    case 'Polygon':
        {
            var ring = geoobj.coordinates[0];
            var ringCoords = [];
            for (i = 0; i < ring.length; i++) {
                ringCoords.push({
                    lat: ring[i][1],
                    lng: ring[i][0]
                });
            }
            shape = new google.maps.Polygon({
                paths: ringCoords,
                strokeColor: selectedColor,
                strokeOpacity: 0.8,
                strokeWeight: 2,
                fillColor: selectedColor,
                fillOpacity: 0.35,
                map: global_map
            });
        }
        break;
    case 'LineString':
        {
            var line = geoobj.coordinates;
            var lineCoords = [];
            for (i = 0; i < line.length; i++) {
                lineCoords.push({
                    lat: line[i][1],
                    lng: line[i][0]
                });
            }
            shape = new google.maps.Polyline({
                path: lineCoords,
                geodesic: true,
                strokeColor: selectedColor,
                strokeOpacity: 1.0,
                strokeWeight: 2,
                map: global_map
            });
        }
        break;
    } // switch ()
    shapes.push(shape);
}

function selectColor(color) {
    selectedColor = color;
    for (var i = 0; i < colors.length; ++i) {
        var currColor = colors[i];
        colorButtons[currColor].style.border = currColor == color ? '2px solid #789' : '2px solid #fff';
    }
    // Retrieves the current options from the drawing manager and replaces the
    // stroke or fill color as appropriate.
    var polylineOptions = drawingManager.get('polylineOptions');
    polylineOptions.strokeColor = color;
    drawingManager.set('polylineOptions', polylineOptions);
    var rectangleOptions = drawingManager.get('rectangleOptions');
    rectangleOptions.fillColor = color;
    drawingManager.set('rectangleOptions', rectangleOptions);
    var circleOptions = drawingManager.get('circleOptions');
    circleOptions.fillColor = color;
    drawingManager.set('circleOptions', circleOptions);
    var polygonOptions = drawingManager.get('polygonOptions');
    polygonOptions.fillColor = color;
    drawingManager.set('polygonOptions', polygonOptions);
}

function setSelectedShapeColor(color) {
    if (selectedShape) {
        if (selectedShape.type == google.maps.drawing.OverlayType.POLYLINE) {
            selectedShape.set('strokeColor', color);
        } else {
            selectedShape.set('fillColor', color);
        }
    }
}

function makeColorButton(color) {
    var button = document.createElement('span');
    button.className = 'color-button';
    button.style.backgroundColor = color;
    google.maps.event.addDomListener(button, 'click', function() {
        selectColor(color);
        setSelectedShapeColor(color);
    });
    return button;
}

function buildColorPalette() {
    var colorPalette = document.getElementById('color-palette');
    for (var i = 0; i < colors.length; ++i) {
        var currColor = colors[i];
        var colorButton = makeColorButton(currColor);
        colorPalette.appendChild(colorButton);
        colorButtons[currColor] = colorButton;
    }
    selectColor(colors[0]);
}

function createMarker(map, value) {
    var myLatLng = {lat: value.lat, lng: value.lon};
    var marker = new google.maps.Marker({
        position: myLatLng,
        map: map,
        title: value.name
    });
    var infowindow = new google.maps.InfoWindow({
        content: value.name
    });
    marker.addListener('click', function() {
        infowindow.open(map, marker);
    });
    return marker;
}

function searchWithMarker(map, shape) {
    var radius;
    var count = 10;

    if ($('#nnearest').length) {
        count = $('#nnearest').val();
    }

    radius = {
        lat: shape.getPosition().lat(),
        lon: shape.getPosition().lng(),
        count: count
    };

    $.getJSON("/nnearest?" + $.param(radius), function (data) {
        $.each(data, function(key, value) {
            marker = createMarker(map, value);
            markers.push(marker);
            if ($('#search-list').length) {
                $('#search-list').append(value.name + '<hr/>');
            }
        });
    });
    shapes.push(shape);
}

function searchWithRadius(map, shape) {
    var radius = {
        lat: shape.getCenter().lat(),
        lon: shape.getCenter().lng(),
        radius: shape.getRadius()
    };
    $.getJSON("/radius?" + $.param(radius), function (data) {
        $.each(data, function(key, value) {
            marker = createMarker(map, value);
            markers.push(marker);
            if ($('#search-list').length) {
                $('#search-list').append(value.name + '<hr/>');
            }
        });
    });
    shapes.push(shape);
}

function searchWithRectangle(map, shape) {
    sw = shape.getBounds().getSouthWest();
    ne = shape.getBounds().getNorthEast();
    latlon = sw.lat() + ',' + sw.lng() + ',' +
        ne.lat() + ',' + sw.lng() + ',' +
        ne.lat() + ',' + ne.lng() + ',' +
        sw.lat() + ',' + ne.lng() + ',' +
        sw.lat() + ',' + sw.lng();

    $.getJSON("/polygon?latlon=" + latlon, function (data) {
        $.each(data, function(key, value) {
            marker = createMarker(map, value);
            markers.push(marker);
            if ($('#search-list').length) {
                $('#search-list').append(value.name + '<hr/>');
            }
        });
    });
    shapes.push(shape);
}

function searchWithPolygon(map, shape) {
    var verticles = shape.getPath();
    var latlon = '';
    var xy;
    for (var i = 0; i < verticles.getLength(); i++) {
        xy = verticles.getAt(i);
        latlon += xy.lat() + ',' + xy.lng() + ',';
    }
    xy = verticles.getAt(0);
    latlon += xy.lat() + ',' + xy.lng();
    $.getJSON("/polygon?latlon=" + latlon, function (data) {
        $.each(data, function(key, value) {
            marker = createMarker(map, value);
            markers.push(marker);
            if ($('#search-list').length) {
                $('#search-list').append(value.name + '<hr/>');
            }
        });
    });
    shapes.push(shape);
}

function searchWithPolyline(map, shape) {
    var verticles = shape.getPath();
    var latlon = '';
    var radius = 100;
    var line;

    for (var i = 0; i < verticles.getLength(); i++) {
        var xy = verticles.getAt(i);
        if (i !== 0) {
            latlon += ',';
        }
        latlon += xy.lat() + ',' + xy.lng();
    }

    if ($('#dwithin').length) {
        radius = $('#dwithin').val();
    }

    line = {
        latlon: latlon,
        radius: radius
    };

    $.getJSON("/polyline?" + $.param(line), function (data) {
        $.each(data, function(key, value) {
            marker = createMarker(map, value);
            markers.push(marker);
            if ($('#search-list').length) {
                $('#search-list').append(value.name + '<hr/>');
            }
        });
    });
    shapes.push(shape);
}

function initialize() {
    var map = new google.maps.Map(document.getElementById('map'), {
        zoom: 15,
        center: new google.maps.LatLng(37.566404, 126.985037),
        mapTypeId: google.maps.MapTypeId.ROADMAP,
        disableDefaultUI: true,
        zoomControl: true
    });
    var polyOptions = {
        strokeWeight: 0,
        fillOpacity: 0.45,
        editable: true
    };
    // Creates a drawing manager attached to the map that allows the user to draw
    // markers, lines, and shapes.
    drawingManager = new google.maps.drawing.DrawingManager({
        // drawingMode: google.maps.drawing.OverlayType.POLYGON,
        markerOptions: {
            draggable: true
        },
        polylineOptions: {
            editable: true
        },
        rectangleOptions: polyOptions,
        circleOptions: polyOptions,
        polygonOptions: polyOptions,
        map: map
    });
    google.maps.event.addListener(drawingManager, 'overlaycomplete', function(e) {
        // Add an event listener that selects the newly-drawn shape when the user
        // mouses down on it.
        var newShape = e.overlay;
        newShape.type = e.type;

        // Switch back to non-drawing mode after drawing a shape.
        drawingManager.setDrawingMode(null);

        if ($('#search-list').length) {
            $('#search-list').empty();
        }

        if (e.type != google.maps.drawing.OverlayType.MARKER) {
            if (e.type == google.maps.drawing.OverlayType.CIRCLE) {
                searchWithRadius(map, newShape);
            } else if (e.type == google.maps.drawing.OverlayType.RECTANGLE) {
                searchWithRectangle(map, newShape);
            } else if (e.type == google.maps.drawing.OverlayType.POLYGON) {
                searchWithPolygon(map, newShape);
            } else if (e.type == google.maps.drawing.OverlayType.POLYLINE) {
                searchWithPolyline(map, newShape);
            }
            google.maps.event.addListener(newShape, 'click', function() {
                setSelection(newShape);
            });
            setSelection(newShape);
        } else {
            searchWithMarker(map, newShape);
        }
    });
    // Clear the current selection when the drawing mode is changed, or when the
    // map is clicked.
    google.maps.event.addListener(drawingManager, 'drawingmode_changed', clearSelection);
    google.maps.event.addListener(map, 'click', clearSelection);
    buildColorPalette();
    global_map = map;
}

google.maps.event.addDomListener(window, 'load', initialize);
