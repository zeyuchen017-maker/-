package com.aigovision.model;

import com.aigovision.util.JsonUtil;

import java.util.*;

/**
 * Dijkstra 最短路径算法的单个步骤状态快照。
 *
 * <p>每一步记录：各节点的当前最短距离、已访问节点集合、当前处理的节点、
 * 正在检查的边信息（起点/终点/权重）以及步骤文字描述。
 * 通过 {@link #toJson()} 方法序列化为 JSON 供前端 Canvas 渲染。
 *
 * <p>语言版本：Java 8+
 *
 * @author 陈泽宇
 * @version 1.0
 * @since 2026-06-16
 */
public class DijkstraStep {
    /** 各节点 ID 到当前最短距离的映射 */
    private final Map<String, Integer> distances;

    /** 已确定最短路径的节点 ID 集合 */
    private final Set<String> visited;

    /** 当前正在处理的节点 ID（null 表示无当前节点） */
    private final String currentNode;

    /** 正在检查的边的起点（null 表示无） */
    private final String visitingEdgeFrom;

    /** 正在检查的边的终点（null 表示无） */
    private final String visitingEdgeTo;

    /** 正在检查的边的权重 */
    private final int visitingEdgeWeight;

    /** 当前步骤的文字描述信息 */
    private final String message;

    /**
     * 构造一个 Dijkstra 算法步骤快照。
     *
     * @param distances          各节点的当前最短距离映射
     * @param visited            已访问节点集合
     * @param currentNode        当前正在处理的节点
     * @param visitingEdgeFrom   正在检查的边起点（无则传 null）
     * @param visitingEdgeTo     正在检查的边终点（无则传 null）
     * @param visitingEdgeWeight 正在检查的边权重
     * @param message            步骤文字说明
     */
    public DijkstraStep(Map<String, Integer> distances, Set<String> visited,
                        String currentNode, String visitingEdgeFrom,
                        String visitingEdgeTo, int visitingEdgeWeight,
                        String message) {
        this.distances = new HashMap<>(distances);
        this.visited = new HashSet<>(visited);
        this.currentNode = currentNode;
        this.visitingEdgeFrom = visitingEdgeFrom;
        this.visitingEdgeTo = visitingEdgeTo;
        this.visitingEdgeWeight = visitingEdgeWeight;
        this.message = message;
    }

    /**
     * 将步骤对象序列化为 JSON 字符串。
     *
     * @return JSON 格式的步骤数据
     * @see JsonUtil#stringMap
     * @see JsonUtil#stringArray
     */
    public String toJson() {
        String visitingEdgeJson = "null";
        if (visitingEdgeFrom != null && visitingEdgeTo != null) {
            visitingEdgeJson = JsonUtil.object(
                    JsonUtil.entry("from", visitingEdgeFrom),
                    JsonUtil.entry("to", visitingEdgeTo),
                    JsonUtil.entry("weight", visitingEdgeWeight)
            );
        }

        StringBuilder sb = new StringBuilder("{");
        sb.append("\"distances\":").append(JsonUtil.stringMap(distances));
        sb.append(",\"visited\":").append(JsonUtil.stringArray(visited));
        sb.append(",\"current\":").append(JsonUtil.value(currentNode));
        sb.append(",\"visitingEdge\":").append(visitingEdgeJson);
        sb.append(",\"message\":").append(JsonUtil.value(message));
        sb.append("}");
        return sb.toString();
    }
}
