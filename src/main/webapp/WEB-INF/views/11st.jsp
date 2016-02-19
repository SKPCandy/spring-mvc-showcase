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
    <div class="carousel_container">
        <a class="carousel_direction carousel_direction--prev">&lt;</a>
        <div class="carousels">
            <a href="#" data-item="IMG1550" class="carousel"><img src="http://i.011st.com/al/2/1/6/0/1/6/1334216016_B_V3.jpg" class="carousel__image"></a>
            <a href="#" data-item="IMG7030" class="carousel"><img src="http://i.011st.com/al/4/2/4/5/9/3/1365424593_B.jpg" class="carousel__image"></a>
            <a href="#" data-item="IMG1944" class="carousel"><img src="http://i.011st.com/al/9/2/5/9/2/6/1293925926_B_V14.jpg" class="carousel__image"></a>
            <a href="#" data-item="IMG7240" class="carousel"><img src="http://i.011st.com/ab/2/7/9/9/7/8/1017279978_B.jpg" class="carousel__image"></a>
            <a href="#" data-item="IMG1089" class="carousel"><img src="http://i.011st.com/ak/4/1/5/7/2/6/1136415726_B_V2.jpg" class="carousel__image"></a>
            <a href="#" data-item="IMG302" class="carousel"><img src="http://i.011st.com/ak/3/3/6/1/4/5/1290336145_B_V6.jpg" class="carousel__image"></a>
            <a href="#" data-item="IMG317" class="carousel"><img src="http://i.011st.com/al/7/9/6/4/9/0/1355796490_B_V18.jpg" class="carousel__image"></a>
        
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