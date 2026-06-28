# AlgoVision — 算法过程可视化系统

> 程序设计课程设计 · 第三组  
> 班级：软件2406 · 指导教师：赵紫娟  
> 组长：陈泽宇 · 组员：王崇力、王灏
> **语言版本**：Java 8（JDK 8+/javac 1.8+），前端 JavaScript（ES6）  
> **运行环境**：Apache Tomcat 9.x  


---

## 项目简介

AlgoVision 是一个基于 **B/S 架构** 的算法过程可视化与实验平台。系统通过 Web 页面以**柱状图**和 **Canvas 节点图**的方式，动态展示经典算法的执行全过程，帮助学生直观理解算法的核心思想。

### 已实现的算法

| 算法 | 页面 | 可视化方式 | 交互模式 |
|------|------|------------|----------|
| 冒泡排序 | `bubble.jsp` | 柱状图（div） | 自动播放 · 单步执行 · 重置 |
| 快速排序 | `quick.jsp` | 柱状图（div + pivot 标记） | 自动播放 · 单步执行 · 重置 |
| Dijkstra 最短路径 | `dijkstra.jsp` | Canvas 2D 节点-边图 | 自动播放 · 单步执行 · 重置 |

---

## 技术栈与版本

| 层次 | 技术 | 版本 | 说明 |
|------|------|------|------|
| **后端语言** | Java | **JDK 8+** | 纯 Java 实现算法，不依赖第三方算法库 |
| **前端页面** | JSP + HTML5 + CSS3 | Jakarta EE 8 | 动态页面渲染 |
| **前端交互** | 原生 JavaScript（ES6） | — | fetch API + Canvas 2D，无前端框架 |
| **运行容器** | Apache Tomcat | **9.x**（推荐 9.0+） | JSP/Servlet 标准容器 |
| **数据格式** | JSON | — | 自研 `JsonUtil` 序列化，无 Jackson/Gson 依赖 |
| **开发工具** | IntelliJ IDEA | 2024+（教育版免费） | 支持 Web 模块和 Tomcat 集成 |

> **编译器要求**：`javac` 1.8 及以上。项目使用 Java 8 语言特性（Lambda、Stream API），不做更高版本要求。

---

## 目录结构

```
AIgovision/
├── README.md                          # 本文档
├── .gitignore                         # Git 忽略规则
│
├── src/com/aigovision/                # —— Java 源代码 ——
│   ├── model/
│   │   ├── Edge.java                  # 图的有向边模型
│   │   ├── GraphNode.java             # 图节点模型（含 Canvas 坐标）
│   │   ├── SortStep.java              # 排序步骤快照模型
│   │   └── DijkstraStep.java          # Dijkstra 步骤快照模型
│   ├── service/
│   │   ├── SortService.java           # 排序算法服务（冒泡 + 快速）
│   │   └── DijkstraService.java       # Dijkstra 最短路径算法服务
│   └── util/
│       └── JsonUtil.java              # 轻量 JSON 序列化工具
│
├── web/                               # —— Web 应用根目录 ——
│   ├── index.jsp                      # 首页（算法卡片导航）
│   ├── bubble.jsp                     # 冒泡排序可视化页面
│   ├── quick.jsp                      # 快速排序可视化页面
│   ├── dijkstra.jsp                   # Dijkstra 可视化页面
│   ├── api/
│   │   ├── sort.jsp                   # 排序 API 端点
│   │   └── dijkstra.jsp               # Dijkstra API 端点
│   ├── js/
│   │   ├── animation.js               # 冒泡排序动画引擎
│   │   ├── quick.js                   # 快速排序动画引擎
│   │   └── dijkstra.js               # Dijkstra 图渲染与动画引擎
│   ├── css/
│   │   └── style.css                  # 全局样式表
│   └── WEB-INF/
│       └── web.xml                    # Web 部署描述文件
│
├── docs/                              # —— 项目文档 ——
│   ├── 最终项目文档_完整版.docx        # 最终课程设计报告
│   ├── 需求分析文档.docx              # 需求分析文档
│   ├── 系统设计文档.docx              # 系统设计文档
│   ├── 个人开发日志.docx              # 三人开发日志
│   └── images/                        # 文档插图
│       ├── architecture.png
│       ├── dataflow.png
│       ├── modules.png
│       └── usecase.png
│
└── test/                              # —— 测试（手动验收） ——
    └── .gitkeep                       # 当前以手动验收为主，见文档第5章
```

---

## 快速开始

### 1. 环境准备

- 安装 **JDK 8+** 并配置 `JAVA_HOME`
- 安装 **Apache Tomcat 9.x** 并确保可正常启动
- 安装 **IntelliJ IDEA**（可选，也可用其他 IDE 或直接部署）

### 2. 项目导入

```bash
# 克隆或拷贝项目到本地
git clone <repo-url> AIgovision
cd AIgovision
```

在 IntelliJ IDEA 中：
1. `File → Open` 选择 `AIgovision` 目录
2. `File → Project Structure → Modules` 将 `web/` 设为 Web Resource Directory
3. `Run → Edit Configurations` 添加 Tomcat Server → Local，部署 `AIgoVision:war exploded`

### 3. 编译与部署

**方式一：IDEA 直接运行**
- 点击 Run 按钮，项目自动编译并部署到 Tomcat
- 浏览器访问 `http://localhost:8080/AIgovision/`

**方式二：手动部署**
```bash
# 编译 Java 源文件
javac -d out/classes -sourcepath src/ src/com/aigovision/**/*.java

# 将编译后的 class 文件和 web/ 目录打包为 war
jar -cvf AIgoVision.war -C out/classes . -C web .

# 将 war 放入 Tomcat 的 webapps/ 目录，启动 Tomcat
```

### 4. 使用说明

1. 打开浏览器访问首页 `http://localhost:8080/AIgovision/`
2. 点击任一算法卡片进入可视化页面
3. **排序算法**：手动输入数据（逗号分隔，1~20 个数字）或点击「随机生成」
4. **Dijkstra**：从下拉框选择起点节点（A~E）
5. 点击「开始」自动播放动画，或点击「单步执行」逐步骤观察
6. 点击「重置」恢复初始状态

---

## API 接口

### 排序 API

```
GET /api/sort.jsp?type=<bubble|quick>&array=<comma_separated_integers>
```

**响应示例**：
```json
{
  "success": true,
  "steps": [
    {
      "array": [5,3,8,2,1],
      "compare": [0,1],
      "pivot": -1,
      "sortedUntil": -1,
      "message": "比较位置 0 和 1（值 5 和 3）",
      "comparisons": 1,
      "swaps": 0
    }
  ],
  "totalSteps": 17
}
```

### Dijkstra API

```
GET /api/dijkstra.jsp?start=<node_id>&nodes=<id:label:x:y,...>&edges=<from:to:weight,...>
```

**响应示例**：
```json
{
  "success": true,
  "graph": {
    "nodes": [{"id":"A","label":"A","x":100,"y":90}],
    "edges": [{"from":"A","to":"B","weight":4}]
  },
  "steps": [{ "distances":{"A":0,"B":4}, "visited":["A"], "current":"A", "message":"..." }],
  "totalSteps": 23
}
```

---

## 系统架构

```
 ┌──────────────────────────────────────────┐
 │         表示层（View）                    │
 │  index.jsp / bubble.jsp / quick.jsp      │
 │  dijkstra.jsp + .js 动画引擎 + style.css │
 └──────────────┬───────────────────────────┘
                │  HTTP / AJAX (fetch)
                ▼
 ┌──────────────────────────────────────────┐
 │         API 接口层                        │
 │  /api/sort.jsp       /api/dijkstra.jsp   │
 └──────────────┬───────────────────────────┘
                │  调用
                ▼
 ┌──────────────────────────────────────────┐
 │       业务逻辑层（Service）               │
 │  SortService           DijkstraService   │
 │  (冒泡排序 + 快速排序)   (最短路径算法)    │
 └──────────────┬───────────────────────────┘
                │  使用
                ▼
 ┌──────────────────────────────────────────┐
 │        数据模型层（Model + Util）          │
 │  SortStep / DijkstraStep / Edge          │
 │  / GraphNode / JsonUtil                  │
 └──────────────────────────────────────────┘
```

---

## 代码规范

- **命名规范**：Java 类名采用 PascalCase，方法/变量采用 camelCase；JS 变量采用 camelCase；常量全大写
- **注释规范**：所有 public 类和方法均包含 Javadoc 注释（含 `@author`、`@param`、`@return`）
- **不可变对象**：所有 Model 类字段均为 `final`，通过构造器初始化，提供 getter 方法
- **分层架构**：严格遵循 Model → Service → API → View 的分层调用，禁止跨层依赖
- **异常处理**：API 层统一 try-catch，返回 `{"success":false,"error":"..."}` 的 JSON 错误响应
- **前端输入校验**：对用户输入进行范围（1~20）和格式（数字）校验，阻断非法请求

---

## 测试

详见 `docs/最终项目文档_完整版.docx` 第五章。已编写 6 条测试用例覆盖三个算法：

| 编号 | 算法 | 输入 | 预期结果 | 状态 |
|------|------|------|----------|------|
| TC-1 | 冒泡排序 | 5,3,8,2,1 | [1,2,3,5,8] | 通过 |
| TC-2 | 冒泡排序 | 9,7,5,3,1 | [1,3,5,7,9] | 通过 |
| TC-3 | 快速排序 | 5,3,8,2,1 | [1,2,3,5,8] | 通过 |
| TC-4 | 快速排序 | 4,2,4,1,3 | [1,2,3,4,4] | 通过 |
| TC-5 | Dijkstra(A) | 预置图 | A=0,B=4,C=6,D=2,E=7 | 通过 |
| TC-6 | Dijkstra(D) | 预置图 | D=0,A=2,B=3,E=5,C=6 | 通过 |

---

## 小组成员与分工

| 成员 | 角色 | 负责模块 |
|------|------|----------|
| 陈泽宇 | 组长 | 系统架构设计 · Dijkstra算法实现 · 冒泡排序算法实现 · 前后端集成 · 整体进度协调 |
| 王灏 | 组员 | 快速排序算法实现 · JsonUtil工具类开发 · API接口开发 · 前端柱状图绘制与动画引擎 |
| 王崇力 | 组员 | 需求分析文档 · 系统设计文档 · 最终项目文档 · 个人开发日志 · 项目答辩材料 |

---

## 许可证

本项目为太原理工大学《程序设计课程设计》课程项目，仅供教学使用。

---

> **提交日期**：2026 年 6 月 25 日  
> **项目版本**：V1.0


