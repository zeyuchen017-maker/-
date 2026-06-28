package com.aigovision.util;

import java.util.*;

/**
 * 轻量级 JSON 序列化工具类。
 *
 * <p>不依赖任何第三方 JSON 库（如 Jackson、Gson），完全自研实现。
 * 支持 String、Number、Boolean、int[]、int[][]、
 * {@code Map<String, Integer>} 和 Collection 的 JSON 序列化。
 *
 * <p>字符串转义遵循 JSON 规范，对 {@code " \ / \n \r \t} 等特殊字符正确处理。
 *
 * <p>语言版本：Java 8+
 *
 * @author 王灏
 * @author 陈泽宇
 * @version 1.0
 * @since 2026-06-16
 */
public class JsonUtil {

    /**
     * 对字符串中的 JSON 特殊字符进行转义。
     *
     * @param s 原始字符串（可为 null）
     * @return 转义后的字符串（null 返回空字符串）
     */
    public static String escape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    /**
     * 将字符串包装为 JSON 双引号字符串格式。
     *
     * @param s 原始字符串
     * @return {@code "转义后字符串"}
     */
    public static String string(String s) {
        return "\"" + escape(s) + "\"";
    }

    /**
     * 将数字转换为 JSON 数值（不做引号包装）。
     *
     * @param n 数字对象（可为 null）
     * @return 数字字符串（null 返回字符串 "null"）
     */
    public static String number(Number n) {
        return n == null ? "null" : n.toString();
    }

    /**
     * 将布尔值转换为 JSON 布尔字面量。
     *
     * @param b 布尔值
     * @return {@code "true"} 或 {@code "false"}
     */
    public static String bool(boolean b) {
        return String.valueOf(b);
    }

    /**
     * 构建 JSON 对象字符串。
     *
     * <p>示例:
     * <pre>{@code
     * JsonUtil.object(
     *     JsonUtil.entry("name", "Alice"),
     *     JsonUtil.entry("age", 18)
     * )  // 输出 {"name":"Alice","age":18}
     * }</pre>
     *
     * @param entries 键值对条目数组
     * @param <K>     键类型（通常为 String）
     * @param <V>     值类型
     * @return JSON 对象字符串
     */
    @SafeVarargs
    public static <K, V> String object(Map.Entry<String, ?>... entries) {
        if (entries == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, ?> e : entries) {
            if (!first) sb.append(",");
            first = false;
            sb.append(string(e.getKey())).append(":").append(value(e.getValue()));
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Java 8 兼容的 Map.entry() 替代方法。
     *
     * <p>等效于 JDK 9+ 的 {@code Map.entry(key, value)}。
     *
     * @param key   键
     * @param value 值
     * @param <K>   键类型
     * @param <V>   值类型
     * @return 不可变键值对
     */
    public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    /**
     * 将 Collection 序列化为 JSON 数组。
     *
     * @param items 集合对象（可为 null）
     * @return JSON 数组字符串（null 返回 "[]"）
     */
    public static String array(Collection<?> items) {
        if (items == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (Object item : items) {
            if (!first) sb.append(",");
            first = false;
            sb.append(value(item));
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 将 int 数组序列化为 JSON 数组。
     *
     * @param arr int 数组（可为 null）
     * @return JSON 数组字符串（null 返回 "[]"）
     */
    public static String intArray(int[] arr) {
        if (arr == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(arr[i]);
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 将 String 集合序列化为 JSON 字符串数组。
     *
     * @param items 字符串集合（可为 null）
     * @return JSON 数组字符串（null 返回 "[]"）
     */
    public static String stringArray(Collection<String> items) {
        if (items == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        boolean first = true;
        for (String s : items) {
            if (!first) sb.append(",");
            first = false;
            sb.append(string(s));
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * 将 {@code Map<String, Integer>} 序列化为 JSON 对象。
     *
     * <p>键以 JSON 字符串格式输出，值以 JSON 数字输出（null 值输出 null）。
     *
     * @param map 字符串到整数映射（可为 null）
     * @return JSON 对象字符串（null 返回 "{}"）
     */
    public static String stringMap(Map<String, Integer> map) {
        if (map == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        boolean first = true;
        for (Map.Entry<String, Integer> e : map.entrySet()) {
            if (!first) sb.append(",");
            first = false;
            sb.append(string(e.getKey()))
              .append(":")
              .append(e.getValue() == null ? "null" : e.getValue().toString());
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * 自动推断 Java 对象类型并序列化为对应的 JSON 值。
     *
     * <p>类型分发逻辑：
     * <ul>
     *   <li>String → JSON 字符串（含转义）</li>
     *   <li>Number → JSON 数字</li>
     *   <li>Boolean → JSON 布尔</li>
     *   <li>int[] → JSON 数字数组</li>
     *   <li>int[][] → JSON 嵌套数字数组</li>
     *   <li>Map → JSON 对象</li>
     *   <li>Collection → JSON 数组</li>
     *   <li>其他类型 → 调用 toString() 后以 JSON 字符串输出</li>
     * </ul>
     *
     * @param v 任意 Java 对象（可为 null）
     * @return JSON 值字符串
     */
    public static String value(Object v) {
        if (v == null) return "null";
        if (v instanceof String) return string((String) v);
        if (v instanceof Number) return number((Number) v);
        if (v instanceof Boolean) return bool((Boolean) v);
        if (v instanceof int[]) return intArray((int[]) v);
        if (v instanceof int[][]) return nestedIntArray((int[][]) v);
        if (v instanceof Map) return stringMap((Map<String, Integer>) v);
        if (v instanceof Collection) return array((Collection<?>) v);
        return string(v.toString());
    }

    /**
     * 将二维 int 数组序列化为嵌套 JSON 数组。
     *
     * @param arr 二维 int 数组（可为 null）
     * @return 嵌套 JSON 数组字符串（null 返回 "[]"）
     */
    private static String nestedIntArray(int[][] arr) {
        if (arr == null) return "[]";
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(",");
            sb.append(intArray(arr[i]));
        }
        sb.append("]");
        return sb.toString();
    }
}
