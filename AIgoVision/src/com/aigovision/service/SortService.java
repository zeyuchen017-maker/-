package com.aigovision.service;

import com.aigovision.model.SortStep;

import java.util.ArrayList;
import java.util.List;

/**
 * 排序算法服务类，负责执行排序算法并生成完整的步骤序列供前端渲染。
 *
 * <p>当前支持两种排序算法：
 * <ul>
 *   <li>{@link #bubbleSort(int[])}  冒泡排序（带提前终止优化）</li>
 *   <li>{@link #quickSort(int[])}   快速排序（以第一个元素为 pivot）</li>
 * </ul>
 *
 * <p>每个方法返回的 {@code List<SortStep>} 记录了算法执行过程中每一步的完整状态，
 * 包括数组快照、比较/交换信息、文字描述和累计统计。
 *
 * <p>语言版本：Java 8+
 *
 * @author 陈泽宇
 * @version 1.0
 * @since 2026-06-17
 */
public class SortService {

    /**
     * 执行冒泡排序，返回完整的步骤序列。
     *
     * <p>优化：如果在某一轮中没有发生任何交换，说明数组已有序，提前结束。
     *
     * <p>时间复杂度：O(n^2) 最坏/平均，O(n) 最好（已有序）
     * <p>空间复杂度：O(1)（原地排序）
     *
     * @param input 待排序的整数数组（不会被修改）
     * @return 排序全过程的步骤序列列表
     */
    public List<SortStep> bubbleSort(int[] input) {
        int[] arr = input.clone();
        List<SortStep> steps = new ArrayList<>();
        int comparisons = 0;
        int swaps = 0;
        int n = arr.length;

        steps.add(new SortStep(arr, null, -1, -1,
                "准备开始冒泡排序，数组长度 " + n, comparisons, swaps));

        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                comparisons++;
                steps.add(new SortStep(arr, new int[]{j, j + 1}, -1, n - 1 - i,
                        "第 " + (i + 1) + " 轮：比较位置 " + j + " 和 " + (j + 1)
                                + "（值 " + arr[j] + " 和 " + arr[j + 1] + "）",
                        comparisons, swaps));

                if (arr[j] > arr[j + 1]) {
                    // 交换相邻元素
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                    swaps++;
                    swapped = true;
                    steps.add(new SortStep(arr, new int[]{j, j + 1}, -1, n - 1 - i,
                            "交换位置 " + j + " 和 " + (j + 1)
                                    + "（值 " + arr[j + 1] + " -> " + arr[j] + "）",
                            comparisons, swaps));
                }
            }
            // 该轮结束，标记已排序区域
            steps.add(new SortStep(arr, null, -1, n - 1 - i,
                    "第 " + (i + 1) + " 轮结束，位置 " + (n - 1 - i) + " 已就位",
                    comparisons, swaps));

            if (!swapped) {
                // 优化：没有交换说明已经有序，提前结束
                break;
            }
        }

        steps.add(new SortStep(arr, null, -1, 0,
                "排序完成！共比较 " + comparisons + " 次，交换 " + swaps + " 次",
                comparisons, swaps));

        return steps;
    }

    /**
     * 执行快速排序，返回完整的步骤序列。
     *
     * <p>以子数组的第一个元素作为基准（pivot）。
     * 通过递归方式对左右子数组分别排序。
     *
     * <p>时间复杂度：O(n log n) 平均/最好，O(n^2) 最坏（已有序或逆序）
     * <p>空间复杂度：O(log n)（递归调用栈深度）
     *
     * @param input 待排序的整数数组（不会被修改）
     * @return 排序全过程的步骤序列列表
     */
    public List<SortStep> quickSort(int[] input) {
        int[] arr = input.clone();
        List<SortStep> steps = new ArrayList<>();
        int[] counters = new int[2]; // [0]=comparisons, [1]=swaps

        steps.add(new SortStep(arr, null, -1, -1,
                "准备开始快速排序，数组长度 " + arr.length,
                counters[0], counters[1]));

        if (arr.length > 1) {
            quickSortRecursive(arr, 0, arr.length - 1, steps, counters);
        }

        steps.add(new SortStep(arr, null, -1, -1,
                "排序完成！共比较 " + counters[0] + " 次，交换 " + counters[1] + " 次",
                counters[0], counters[1]));

        return steps;
    }

    /**
     * 快速排序递归体。
     *
     * @param arr      当前数组
     * @param low      子数组左边界（包含）
     * @param high     子数组右边界（包含）
     * @param steps    步骤列表（累积写入）
     * @param counters 统计计数器数组（0=比较次数, 1=交换次数）
     */
    private void quickSortRecursive(int[] arr, int low, int high,
                                    List<SortStep> steps, int[] counters) {
        if (low >= high) return;

        int pivotIndex = partition(arr, low, high, steps, counters);
        quickSortRecursive(arr, low, pivotIndex - 1, steps, counters);
        quickSortRecursive(arr, pivotIndex + 1, high, steps, counters);
    }

    /**
     * 快速排序分区操作（Hoare 分割方案）。
     *
     * <p>以 arr[low] 为 pivot，将小于 pivot 的元素移到左侧，
     * 大于等于 pivot 的元素留在右侧，最后将 pivot 归位。
     *
     * @param arr      当前数组
     * @param low      分区左边界
     * @param high     分区右边界
     * @param steps    步骤列表
     * @param counters 统计计数器
     * @return pivot 最终位置索引
     */
    private int partition(int[] arr, int low, int high,
                          List<SortStep> steps, int[] counters) {
        int pivotValue = arr[low];

        steps.add(new SortStep(arr, null, low, -1,
                "基准 pivot 位于位置 " + low + "，值为 " + pivotValue,
                counters[0], counters[1]));

        int i = low;

        for (int j = low + 1; j <= high; j++) {
            counters[0]++;
            steps.add(new SortStep(arr, new int[]{j, low}, low, -1,
                    "比较位置 " + j + "（值 " + arr[j] + "）与 pivot（值 " + pivotValue + "）",
                    counters[0], counters[1]));

            if (arr[j] < pivotValue) {
                i++;
                if (i != j) {
                    // 交换 arr[i] 和 arr[j]
                    int temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                    counters[1]++;
                    steps.add(new SortStep(arr, new int[]{i, j}, low, -1,
                            "交换位置 " + i + " 和 " + j
                                    + "（值 " + arr[j] + " <-> " + arr[i] + "）",
                            counters[0], counters[1]));
                }
            }
        }

        // 将 pivot 归位到正确位置
        if (low != i) {
            int temp = arr[low];
            arr[low] = arr[i];
            arr[i] = temp;
            counters[1]++;
        }

        steps.add(new SortStep(arr, null, i, -1,
                "pivot 归位到位置 " + i + "（值 " + arr[i] + "）",
                counters[0], counters[1]));

        return i;
    }
}
