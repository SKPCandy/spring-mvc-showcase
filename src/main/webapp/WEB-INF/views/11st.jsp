<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title></title>
    <link rel="stylesheet" href="<c:url value="/resources/css/index.css" />">
    <script src="https://code.jquery.com/jquery-2.2.0.min.js"></script>
</head>
<body>
<div class="container">
    <h1 class="title">Watcha!</h1>
    <div class="carousel_handlebar">
        <label for="image_category">Category: </label>
        <select id="image_category" name="image_category">
            <option value="1455">1455</option>
            <option value="15878">15878</option>
            <option value="127682">127682</option>
            <option value="1612">1612</option>
            <option value="15993">15993</option>
        </select>
    </div>
    <div class="carousel_container">
        <a class="carousel_direction carousel_direction--prev">&lt;</a>
        <div class="carousels">
            <a href="#" data-item="0:1365421568,1455" class="carousel"><img src="http://175.126.56.112/october_11st/1365421568.jpg" class="carousel__image"></a>
            <a href="#" data-item="0:1347636714,15878" class="carousel"><img src="http:////175.126.56.112/october_11st/1347636714.jpg" class="carousel__image"></a>
            <a href="#" data-item="0:1361906094,127682" class="carousel"><img src="http://175.126.56.112/october_11st/1361906094.jpg" class="carousel__image"></a>
            <a href="#" data-item="0:1231301968,1612" class="carousel"><img src="http://175.126.56.112/october_11st/1231301968.jpg" class="carousel__image"></a>
            <a href="#" data-item="0:1360216839,127682" class="carousel"><img src="http://175.126.56.112/october_11st/1360216839.jpg" class="carousel__image"></a>
            <a href="#" data-item="0:1276017349,127682" class="carousel"><img src="http://175.126.56.112/october_11st/1276017349.jpg" class="carousel__image"></a>
            <a href="#" data-item="0:1362077000,127682" class="carousel"><img src="http://175.126.56.112/october_11st/1362077000.jpg" class="carousel__image"></a>
            <a href="#" data-item="0:1374329288,127682" class="carousel"><img src="http://175.126.56.112/october_11st/1374329288.jpg" class="carousel__image"></a>
            <a href="#" data-item="0:1306435438,127682" class="carousel"><img src="http://175.126.56.112/october_11st/1306435438.jpg" class="carousel__image"></a>
        </div>
        <a class="carousel_direction carousel_direction--next carousel_direction--has">&gt;</a>
    </div>
        <div class="carousel_container">
        <a class="carousel_direction carousel_direction--prev">&lt;</a>
        <div class="carousels">
            <a href="#" data-item="7:240634989,15993" class="carousel"><img src="http://175.126.56.112/october_11st/240634989.jpg" class="carousel__image"></a>
            <a href="#" data-item="7:1269729947,15993" class="carousel"><img src="http://175.126.56.112/october_11st/1269729947.jpg" class="carousel__image"></a>
            <a href="#" data-item="7:1248526364,15993" class="carousel"><img src="http://175.126.56.112/october_11st/1248526364.jpg" class="carousel__image"></a>
            <a href="#" data-item="7:1049801268,15993" class="carousel"><img src="http://175.126.56.112/october_11st/1049801268.jpg" class="carousel__image"></a>
            <a href="#" data-item="7:1346376867,15993" class="carousel"><img src="http://175.126.56.112/october_11st/1346376867.jpg" class="carousel__image"></a>
            <a href="#" data-item="7:1322518646,15993" class="carousel"><img src="http://175.126.56.112/october_11st/1322518646.jpg" class="carousel__image"></a>
            <a href="#" data-item="7:1268052526,15993" class="carousel"><img src="http://175.126.56.112/october_11st/1268052526.jpg" class="carousel__image"></a>
            <a href="#" data-item="7:901437576,15993" class="carousel"><img src="http://175.126.56.112/october_11st/901437576.jpg" class="carousel__image"></a>
        </div>
        <a class="carousel_direction carousel_direction--next carousel_direction--has">&gt;</a>
    </div>
  
    <div class="watcha_container">
        <div class="watcha_control">
            <input type="radio" name="watcha_size" id="watchaL" value="100%"><label for="watchaL">L</label>
            <input type="radio" name="watcha_size" id="watchaM" value="50%"><label for="watchaM">M</label>
            <input type="radio" name="watcha_size" id="watchaS" value="33%" checked="checked"><label for="watchaS">S</label>
        </div>
        <div class="watcha_box watcha_box--hd">
            <h2 class="watcha_title">HD</h2>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
        </div>
        <div class="watcha_box watcha_box--ud">
            <h2 class="watcha_title">UD</h2>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
        </div>
        <div class="watcha_box watcha_box--cos">
            <h2 class="watcha_title">Cos</h2>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
            <div class="watcha">
                <img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="watcha__image">
                <span class="watcha__value">0.0</span>
            </div>
        </div>
    </div>
</div>
</body>
</html>
<script src="<c:url value="/resources/js/index.js" />"></script>