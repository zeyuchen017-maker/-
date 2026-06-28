<%@ page contentType="text/html;charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Dijkstra 最短路径</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
<div class="floating-orb"></div><div class="floating-orb"></div><div class="floating-orb"></div><div class="floating-orb"></div>

<div class="container">

    <p class="back-link">
        <a href="${pageContext.request.contextPath}/index.jsp">← 返回首页</a>
    </p>

    <h1>Dijkstra 最短路径可视化</h1>

    <div class="algo-hint algo-hint-dijkstra">
        <p><strong>算法思路：</strong>从起点出发，每次选取当前距离最小的未访问节点，更新其邻居的最短距离，直至所有可达节点处理完毕。</p>
        <p class="algo-hint-legend">
            <span class="legend-item"><i class="legend-color legend-current"></i>橙色 = 当前处理节点</span>
            <span class="legend-item"><i class="legend-color legend-visited"></i>绿色 = 已访问</span>
            <span class="legend-item"><i class="legend-color legend-compare"></i>红色 = 正在检查的边</span>
            <span class="legend-item"><i class="legend-color legend-normal"></i>蓝色 = 未访问</span>
        </p>
    </div>

    <label class="start-label" for="startNode">起点：</label>
    <select id="startNode">
        <option value="A">A</option>
        <option value="B">B</option>
        <option value="C">C</option>
        <option value="D">D</option>
        <option value="E">E</option>
    </select>

    <button type="button" onclick="startDijkstra()">开始算法</button>
    <button type="button" onclick="nextStep()">单步执行</button>
    <button type="button" onclick="resetChart()">重置</button>

    <div id="status">等待开始...</div>

    <div id="chartArea">
        <canvas id="graphCanvas" width="760" height="360"></canvas>
    </div>

</div>

<script>var CTX="${pageContext.request.contextPath}";</script>
<script src="${pageContext.request.contextPath}/js/dijkstra.js"></script>

</body>
</html>
