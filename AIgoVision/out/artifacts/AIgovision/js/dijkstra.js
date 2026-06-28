let timer = null;
let globalSteps = [];
let currentStepIndex = 0;

const INF = 2147483647; // Integer.MAX_VALUE（后端用这个表示无穷）

const GRAPH = {
    nodes: [
        { id: "A", x: 100, y: 90, label: "A" },
        { id: "B", x: 300, y: 70, label: "B" },
        { id: "C", x: 560, y: 90, label: "C" },
        { id: "D", x: 200, y: 240, label: "D" },
        { id: "E", x: 460, y: 260, label: "E" }
    ],
    edges: [
        { from: "A", to: "B", weight: 4 },
        { from: "A", to: "D", weight: 2 },
        { from: "B", to: "C", weight: 3 },
        { from: "B", to: "D", weight: 1 },
        { from: "D", to: "E", weight: 5 },
        { from: "C", to: "E", weight: 2 }
    ]
};

const NODE_RADIUS = 26;

function getNodeById(id) {
    return GRAPH.nodes.find(function (n) { return n.id === id; });
}

async function fetchDijkstraSteps(startId) {
    var nodesParam = GRAPH.nodes.map(function (n) {
        return n.id + ":" + n.label + ":" + n.x + ":" + n.y;
    }).join(",");

    var edgesParam = GRAPH.edges.map(function (e) {
        return e.from + ":" + e.to + ":" + e.weight;
    }).join(",");

    var url = (typeof CTX!=="undefined"?CTX:"") + "/api/dijkstra.jsp?start=" + encodeURIComponent(startId)
        + "&nodes=" + encodeURIComponent(nodesParam)
        + "&edges=" + encodeURIComponent(edgesParam);

    var res = await fetch(url);
    var data = await res.json();

    if (!data.success) {
        throw new Error(data.error || "请求失败");
    }

    // visited 是 JSON 数组，转为 Set；distances 里的 INF 转为 Infinity
    return data.steps.map(function (s) {
        s.visited = new Set(s.visited);

        var dist = s.distances;
        var keys = Object.keys(dist);
        for (var i = 0; i < keys.length; i++) {
            if (dist[keys[i]] === null || dist[keys[i]] >= INF) {
                dist[keys[i]] = Infinity;
            }
        }

        return s;
    });
}

function formatDist(d) {
    if (d === Infinity || d >= INF) {
        return "∞";
    }
    return String(d);
}

function getNodeColor(nodeId, step) {
    if (step.current === nodeId) {
        return "#f59e0b";
    }

    if (step.visited.has(nodeId)) {
        return "#22c55e";
    }

    return "#4f46e5";
}

function drawGraph(step) {
    const canvas = document.getElementById("graphCanvas");
    const ctx = canvas.getContext("2d");

    ctx.clearRect(0, 0, canvas.width, canvas.height);

    GRAPH.edges.forEach(function (edge) {
        const from = getNodeById(edge.from);
        const to = getNodeById(edge.to);
        const isActive = step.visitingEdge &&
            ((step.visitingEdge.from === edge.from && step.visitingEdge.to === edge.to) ||
                (step.visitingEdge.from === edge.to && step.visitingEdge.to === edge.from));

        ctx.beginPath();
        ctx.moveTo(from.x, from.y);
        ctx.lineTo(to.x, to.y);
        ctx.strokeStyle = isActive ? "#ef4444" : "#cbd5e1";
        ctx.lineWidth = isActive ? 4 : 2;
        ctx.stroke();

        const mx = (from.x + to.x) / 2;
        const my = (from.y + to.y) / 2;

        ctx.fillStyle = isActive ? "#ef4444" : "#64748b";
        ctx.font = "bold 13px Microsoft YaHei, sans-serif";
        ctx.textAlign = "center";
        ctx.textBaseline = "middle";
        ctx.fillText(String(edge.weight), mx, my - 10);
    });

    GRAPH.nodes.forEach(function (node) {
        const color = getNodeColor(node.id, step);

        ctx.beginPath();
        ctx.arc(node.x, node.y, NODE_RADIUS, 0, Math.PI * 2);
        ctx.fillStyle = color;
        ctx.fill();
        ctx.strokeStyle = "#ffffff";
        ctx.lineWidth = 3;
        ctx.stroke();

        ctx.fillStyle = "#ffffff";
        ctx.font = "bold 18px Microsoft YaHei, sans-serif";
        ctx.textAlign = "center";
        ctx.textBaseline = "middle";
        ctx.fillText(node.label, node.x, node.y);

        const distVal = step.distances[node.id];
        ctx.fillStyle = "#374151";
        ctx.font = "13px Microsoft YaHei, sans-serif";
        ctx.fillText("dist: " + formatDist(distVal), node.x, node.y + NODE_RADIUS + 16);
    });
}

function getInitialStep() {
    const dist = {};
    GRAPH.nodes.forEach(function (n) {
        dist[n.id] = Infinity;
    });

    return {
        distances: dist,
        visited: new Set(),
        current: null,
        visitingEdge: null,
        message: "请选择起点，然后开始算法"
    };
}

function updateStatus(step) {
    document.getElementById("status").innerHTML = step.message || "处理中..";
}

async function startDijkstra() {
    clearInterval(timer);

    const startId = document.getElementById("startNode").value;

    try {
        globalSteps = await fetchDijkstraSteps(startId);
    } catch (e) {
        alert("请求失败：" + e.message);
        return;
    }

    currentStepIndex = 0;

    function playNextFrame() {
        if (currentStepIndex >= globalSteps.length) {
            clearInterval(timer);
            return;
        }

        const step = globalSteps[currentStepIndex];
        drawGraph(step);
        updateStatus(step);
        currentStepIndex++;
    }

    playNextFrame();

    if (currentStepIndex < globalSteps.length) {
        timer = setInterval(playNextFrame, 1000);
    }
}

async function nextStep() {
    clearInterval(timer);

    if (globalSteps.length === 0) {
        const startId = document.getElementById("startNode").value;

        try {
            globalSteps = await fetchDijkstraSteps(startId);
        } catch (e) {
            alert("请求失败：" + e.message);
            return;
        }

        currentStepIndex = 0;
    }

    if (currentStepIndex >= globalSteps.length) {
        document.getElementById("status").innerHTML = "算法完成！";
        return;
    }

    const step = globalSteps[currentStepIndex];
    drawGraph(step);
    updateStatus(step);
    currentStepIndex++;
}

function resetChart() {
    clearInterval(timer);
    globalSteps = [];
    currentStepIndex = 0;

    document.getElementById("startNode").value = "A";
    drawGraph(getInitialStep());
    document.getElementById("status").innerHTML = "等待开始..";
}

document.addEventListener("DOMContentLoaded", function () {
    resetChart();
});
