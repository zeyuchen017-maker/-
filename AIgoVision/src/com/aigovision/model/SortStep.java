package com.aigovision.model;

import com.aigovision.util.JsonUtil;

/**
 * 排序算法步骤模型，记录排序过程中单个步骤的完整状态快照。
 *
 * <p>每一步包含：当前数组状态、正在比较的元素索引、pivot 索引（快速排序）、
 * 已排序区域边界、步骤说明文字以及累计的比较/交换次数。
 * 通过 {@link #toJson()} 方法序列化为 JSON 供前端渲染。
 *
 * <p>语言版本：Java 8+
 *
 * @author 陈泽宇
 * @version 1.0
 * @since 2026-06-16
 */
public class SortStep {
    /** 当前步骤的数组快照（拷贝） */
    private final int[] array;

    /** 正在比较的元素索引数组（null 表示无比较） */
    private final int[] compareIndices;

    /** 快速排序的 pivot 基准元素索引（-1 表示无 pivot） */
    private final int pivotIndex;

    /** 已排序区域的左边界索引（-1 表示未开始排序） */
    private final int sortedUntil;

    /** 当前步骤的文字描述信息 */
    private final String message;

    /** 截止当前步骤已执行的比较次数 */
    private final int comparisons;

    /** 截止当前步骤已执行的交换次数 */
    private final int swaps;

    /**
     * 构造一个排序步骤快照。
     *
     * @param array          当前数组快照
     * @param compareIndices 正在比较的元素索引（可为 null）
     * @param pivotIndex     pivot 索引（无 pivot 时传 -1）
     * @param sortedUntil    已排序边界索引（无时传 -1）
     * @param message        步骤说明文字
     * @param comparisons    累计比较次数
     * @param swaps          累计交换次数
     */
    public SortStep(int[] array, int[] compareIndices, int pivotIndex,
                    int sortedUntil, String message, int comparisons, int swaps) {
        this.array = array.clone();
        this.compareIndices = compareIndices;
        this.pivotIndex = pivotIndex;
        this.sortedUntil = sortedUntil;
        this.message = message;
        this.comparisons = comparisons;
        this.swaps = swaps;
    }

    /**
     * 将步骤对象序列化为 JSON 字符串。
     *
     * @return JSON 格式的步骤数据
     * @see JsonUtil#object
     */
    public String toJson() {
        return JsonUtil.object(
                JsonUtil.entry("array", array),
                JsonUtil.entry("compare", compareIndices == null ? new int[0] : compareIndices),
                JsonUtil.entry("pivot", pivotIndex),
                JsonUtil.entry("sortedUntil", sortedUntil),
                JsonUtil.entry("message", message == null ? "" : message),
                JsonUtil.entry("comparisons", comparisons),
                JsonUtil.entry("swaps", swaps)
        );
    }
}
