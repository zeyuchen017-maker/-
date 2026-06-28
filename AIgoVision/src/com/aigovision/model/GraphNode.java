package com.aigovision.model;

/**
 * 图的节点模型，表示图中的一个节点及其在 Canvas 上的显示坐标。
 *
 * <p>节点为不可变对象（immutable），所有属性通过构造器初始化。
 * x、y 坐标用于前端 Canvas 绘制定位，由后端预设。
 *
 * <p>语言版本：Java 8+
 *
 * @author 陈泽宇
 * @version 1.0
 * @since 2026-06-15
 */
public class GraphNode {
    /** 节点唯一标识符 */
    private final String id;

    /** 节点显示标签（如 A、B、C） */
    private final String label;

    /** Canvas 上的 X 坐标（像素） */
    private final int x;

    /** Canvas 上的 Y 坐标（像素） */
    private final int y;

    /**
     * 构造一个图节点。
     *
     * @param id    节点唯一标识符
     * @param label 节点显示标签
     * @param x     Canvas X 坐标
     * @param y     Canvas Y 坐标
     */
    public GraphNode(String id, String label, int x, int y) {
        this.id = id;
        this.label = label;
        this.x = x;
        this.y = y;
    }

    /**
     * 获取节点 ID。
     * @return 节点唯一标识符
     */
    public String getId() { return id; }

    /**
     * 获取节点显示标签。
     * @return 节点标签
     */
    public String getLabel() { return label; }

    /**
     * 获取 Canvas X 坐标。
     * @return X 坐标（像素）
     */
    public int getX() { return x; }

    /**
     * 获取 Canvas Y 坐标。
     * @return Y 坐标（像素）
     */
    public int getY() { return y; }
}
