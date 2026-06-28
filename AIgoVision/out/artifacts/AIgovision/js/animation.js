let timer = null;
let globalSteps = [];
let currentStepIndex = 0;

function parseArrayInput(input) {
    const trimmed = input.trim();

    if (!trimmed) {
        alert("请输入数组，或点击随机生成");
        return null;
    }

    const arr = trimmed.split(/[,，\s]+/).map(Number);

    if (arr.some(Number.isNaN)) {
        alert("请输入有效数字");
        return null;
    }

    if (arr.length < 1 || arr.length > 20) {
        alert("数组长度应在1~20之间");
        return null;
    }

    return arr;
}

async function fetchSortSteps(type, arr) {
    const url = (typeof CTX!=="undefined"?CTX:"") + "/api/sort.jsp?type=" + encodeURIComponent(type)
        + "&array=" + encodeURIComponent(arr.join(","));
    const res = await fetch(url);
    const data = await res.json();
    if (!data.success) {
        throw new Error(data.error || "请求失败");
    }
    return data.steps;
}

function updateStatus(step) {
    var statusEl = document.getElementById("status");
    statusEl.innerHTML = step.message || "处理中..";
}

function finishSort() {
    const sorted = globalSteps[globalSteps.length - 1].array;
    document.getElementById("arrayInput").value = sorted.join(",");
    document.getElementById("status").innerHTML = "排序完成！";
}

function generateRandom() {
    clearInterval(timer);
    globalSteps = [];
    currentStepIndex = 0;

    const arr = [];

    for (let i = 0; i < 8; i++) {
        arr.push(Math.floor(Math.random() * 90) + 10);
    }

    document.getElementById("arrayInput").value = arr.join(",");

    drawArray({
        array: arr,
        compare: []
    });

    document.getElementById("status").innerHTML = "随机数据生成完成";
}

function drawArray(step) {
    const arr = step.array;
    const compare = step.compare || [];

    const chart = document.getElementById("chartArea");

    chart.innerHTML = "";
    chart.style.display = "flex";
    chart.style.alignItems = "flex-end";
    chart.style.justifyContent = "center";
    chart.style.gap = "10px";
    chart.style.padding = "20px";

    arr.forEach(function (num, index) {
        const bar = document.createElement("div");
        const height = Math.max(num * 3, 24);

        bar.style.width = "50px";
        bar.style.height = height + "px";
        bar.style.background = compare.includes(index) ? "#ef4444" : "#4f46e5";
        bar.style.color = "white";
        bar.style.display = "flex";
        bar.style.alignItems = "center";
        bar.style.justifyContent = "center";
        bar.style.fontSize = "14px";
        bar.style.fontWeight = "500";
        bar.style.borderRadius = "6px 6px 0 0";
        bar.style.transition = "height 0.4s ease, background 0.3s ease";
        bar.textContent = num;

        chart.appendChild(bar);
    });
}

async function startBubbleSort() {
    clearInterval(timer);

    const input = document.getElementById("arrayInput").value;
    const arr = parseArrayInput(input);

    if (!arr) {
        return;
    }

    try {
        globalSteps = await fetchSortSteps("bubble", arr);
    } catch (e) {
        alert("请求失败：" + e.message);
        return;
    }

    currentStepIndex = 0;

    function playNextFrame() {
        if (currentStepIndex >= globalSteps.length) {
            clearInterval(timer);
            finishSort();
            return;
        }

        const step = globalSteps[currentStepIndex];
        drawArray(step);
        updateStatus(step);
        currentStepIndex++;
    }

    playNextFrame();

    if (currentStepIndex < globalSteps.length) {
        timer = setInterval(playNextFrame, 800);
    } else {
        finishSort();
    }
}

async function nextStep() {
    clearInterval(timer);

    if (globalSteps.length === 0) {
        const input = document.getElementById("arrayInput").value;
        const arr = parseArrayInput(input);

        if (!arr) {
            return;
        }

        try {
            globalSteps = await fetchSortSteps("bubble", arr);
        } catch (e) {
            alert("请求失败：" + e.message);
            return;
        }

        currentStepIndex = 0;
    }

    if (currentStepIndex >= globalSteps.length) {
        document.getElementById("status").innerHTML = "排序完成！";
        return;
    }

    const step = globalSteps[currentStepIndex];
    drawArray(step);
    updateStatus(step);
    currentStepIndex++;

    if (currentStepIndex >= globalSteps.length) {
        finishSort();
    }
}

function resetChart() {
    clearInterval(timer);

    globalSteps = [];
    currentStepIndex = 0;

    document.getElementById("arrayInput").value = "";
    document.getElementById("chartArea").innerHTML = "";
    document.getElementById("status").innerHTML = "等待开始..";
}
