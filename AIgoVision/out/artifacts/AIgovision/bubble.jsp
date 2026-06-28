<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>冒泡排序</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="floating-orb"></div><div class="floating-orb"></div><div class="floating-orb"></div><div class="floating-orb"></div>

<div class="container">

    <p class="back-link">
        <a href="${pageContext.request.contextPath}/index.jsp">← 返回首页</a>
    </p>

    <h1>冒泡排序可视化</h1>

    <input id="arrayInput" type="text" placeholder="例如：5,3,8,2,1">

    <button onclick="generateRandom()">随机生成</button>

    <button onclick="startBubbleSort()">开始排序</button>

    <button onclick="nextStep()">单步执行</button>

    <button onclick="resetChart()">重置</button>
    <div id="status">
        等待开始...
    </div>

    <br>

    <div id="chartArea"></div>

</div>

<script>var CTX="${pageContext.request.contextPath}";</script>
<script src="${pageContext.request.contextPath}/js/animation.js"></script>

</body>
</html>
