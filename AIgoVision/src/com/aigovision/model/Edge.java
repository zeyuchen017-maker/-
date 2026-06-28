package com.aigovision.model;

/**
 * 图的有向边模型，表示从起点到终点的一条带权边。
 *
 * <p>边为不可变对象（immutable），所有属性通过构造器初始化。
 * 在无向图中，通过为同一条边建立两条反向有向边来表示。
 *
 * <p>语言版本：Java 8+
 * <p>编译器：javac 1.8+（兼容 JDK 8 及以上版本）
 *
 * @author 陈泽宇
 * @version 1.0
 * @since 2026-06-15
 */
public class Edge {
    /** 边的起点节点 ID */
    private final String from;

    /** 边的终点节点 ID */
    private final String to;

    /** 边的权重（非负整数） */
    private final int weight;

    /**
     * 构造一条带权有向边。
     *
     * @param from   起点节点 ID
     * @param to     终点节点 ID
     * @param weight 边权重
     */
    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    /**
     * 获取起点节点 ID。
     * @return 起点节点 ID
     */
    public String getFrom() { return from; }

    /**
     * 获取终点节点 ID。
     * @return 终点节点 ID
     */
    public String getTo() { return to; }

    /**
     * 获取边的权重。
     * @return 边权重
     */
    public int getWeight() { return weight; }
}
