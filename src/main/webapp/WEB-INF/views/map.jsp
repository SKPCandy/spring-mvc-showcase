
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Daum 지도 API v3 예제: 이미지 마커 올리기</title>
<script
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript"
	src="http://apis.daum.net/maps/maps3.js?apikey=71278e1859e32eaaec520acce198e560"
	charset="utf-8"></script>
<script type="text/javascript">
	var map, icon = new daum.maps.MarkerImage(
			'http://localimg.daum-img.net/localimages/07/2009/map/icon/blog_icon01_on.png',
			new daum.maps.Size(31, 34), new daum.maps.Point(16, 34), "poly",
			"1,20,1,9,5,2,10,0,21,0,27,3,30,9,30,20,17,33,14,33");

	function init() {
		map = new daum.maps.Map(document.getElementById('map'), {
			center : new daum.maps.LatLng(37.39627, 127.10998),
			level : 6
		});

		var datasets = model();
		render(datasets);
	}

	function model() {
		var points = [ new daum.maps.LatLng(37.39765248, 127.1114133),
				new daum.maps.LatLng(37.40128991, 127.1104474),
				new daum.maps.LatLng(37.38556173, 127.1214134), ];
		return points;
	}

	function render(datasets) {
		console.log("render =========================")
		for (var i = 0; i < datasets.length; i++) {
			console.log(datasets[i]);
			
			// 지도에 표시할 원을 생성합니다
			var circle = new daum.maps.Circle({
			    center : datasets[i],  // 원의 중심좌표 입니다 
			    radius: 500, // 미터 단위의 원의 반지름입니다 
			    strokeWeight: 5, // 선의 두께입니다 
			    strokeColor: '#75B8FA', // 선의 색깔입니다
			    strokeOpacity: 0.7, // 선의 불투명도 입니다 1에서 0 사이의 값이며 0에 가까울수록 투명합니다
			    strokeStyle: 'line', // 선의 스타일 입니다
			    fillColor: '#ffffff', // 채우기 색깔입니다
			    fillOpacity: 0  // 채우기 불투명도 입니다   
			}); 
			// 지도에 원을 표시합니다 
            circle.setMap(map); 
			new daum.maps.Marker({
                position : datasets[i],
                image : icon
            }).setMap(map);

			

		}

	}
	//
	function generater() {
		// ajax
		var points = [ new daum.maps.LatLng(37.38486346, 127.1205135) ];
		return points;
	}

	function refresh() {
		console.info('refresh ===========');

		$.get("/spring-mvc-showcase/spatial", function(datasets) {
			var obj = $.parseJSON(datasets), list = [];
			$.each(obj["machants"], function(idx, item) {
				console.log(item);
				list.push(new daum.maps.LatLng(item["latitude"],
						item["longitude"]));
			});

			console.log("list =======")
			console.log(list);
			if (list.length == 0) {
				render(list);
			} else {
				render(list);
			}

		});
	}

	setInterval("refresh()", 2000);
</script>
</head>
<body onload="init()">
	<div id="map" style="width: 1000px; height: 800px;"></div>
</body>
</html>
