<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>快速排序</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="floating-orb"></div><div class="floating-orb"></div><div class="floating-orb"></div><div class="floating-orb"></div>

<div class="container">

    <p class="back-link">
        <a href="${pageContext.request.contextPath}/index.jsp">← 返回首页</a>
    </p>

    <h1>快速排序可视化</h1>

    <div class="algo-hint">
        <p><strong>基准规则：</strong>每轮分区以当前子数组的<strong>第一个元素</strong>作为基准（pivot）</p>
        <p class="algo-hint-legend">
            <span class="legend-item"><i class="legend-color legend-pivot"></i>橙色 = 基准元素</span>
            <span class="legend-item"><i class="legend-color legend-compare"></i>红色 = 正在比较/交换</span>
            <span class="legend-item"><i class="legend-color legend-normal"></i>蓝色 = 普通元素</span>
        </p>
    </div>

    <input id="arrayInput" type="text" placeholder="例如：5,3,8,2,1">

    <button type="button" onclick="generateRandom()">随机生成</button>
    <button type="button" onclick="startQuickSort()">开始排序</button>
    <button type="button" onclick="nextStep()">单步执行</button>
    <button type="button" onclick="resetChart()">重置</button>

    <div id="status">等待开始...</div>

    <div id="chartArea"></div>

</div>

<script>var CTX="${pageContext.request.contextPath}";</script>
<script src="${pageContext.request.contextPath}/js/quick.js"></script>

</body>
</html>
