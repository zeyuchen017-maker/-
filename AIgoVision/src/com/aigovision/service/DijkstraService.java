package com.aigovision.service;

import com.aigovision.model.*;

import java.util.*;

/**
 * Dijkstra 最短路径算法的服务类，负责执行算法并生成完整的步骤序列。
 *
 * <p>使用邻接表（HashMap + ArrayList）表示图结构，支持任意规模的带权无向图。
 * 每一步通过 {@link DijkstraStep} 对象记录完整的搜索状态，供前端 Canvas 渲染。
 *
 * <p>时间复杂度：O(V^2)（当前实现，V 为节点数）
 * <p>空间复杂度：O(V + E)（E 为边数）
 *
 * <p>语言版本：Java 8+
 *
 * @author 陈泽宇
 * @version 1.0
 * @since 2026-06-17
 */
public class DijkstraService {

    /**
     * 执行 Dijkstra 最短路径算法，返回每一步的状态序列。
     *
     * <p>算法流程：
     * <ol>
     *   <li>构建无向图邻接表</li>
     *   <li>初始化距离映射（起点=0，其他=无穷大）</li>
     *   <li>从未访问节点中选取距离最小的节点</li>
     *   <li>标记该节点为已访问</li>
     *   <li>对该节点的所有未访问邻居进行松弛操作</li>
     *   <li>重复 3-5 直到所有可达节点处理完毕</li>
     * </ol>
     *
     * @param startId 起始节点 ID（如 "A"）
     * @param nodes   图的所有节点列表
     * @param edges   图的所有边列表
     * @return 算法全过程的步骤序列列表
     */
    public List<DijkstraStep> findShortestPath(String startId,
                                                List<GraphNode> nodes,
                                                List<Edge> edges) {
        // 构建无向图邻接表
        Map<String, List<Edge>> adj = buildAdjacency(edges);
        Set<String> allNodes = new HashSet<>();
        for (GraphNode n : nodes) allNodes.add(n.getId());

        // 初始化距离映射
        Map<String, Integer> dist = new HashMap<>();
        for (String id : allNodes) dist.put(id, Integer.MAX_VALUE);
        dist.put(startId, 0);

        Set<String> visited = new HashSet<>();
        List<DijkstraStep> steps = new ArrayList<>();

        steps.add(new DijkstraStep(dist, visited, null, null, null, 0,
                "起点设为 " + startId + "，距离为 0"));

        // 主循环：直到所有节点被访问或剩余节点不可达
        while (visited.size() < allNodes.size()) {
            // 选取未访问节点中距离最小的
            String u = null;
            int minDist = Integer.MAX_VALUE;
            for (String id : allNodes) {
                if (!visited.contains(id) && dist.get(id) < minDist) {
                    minDist = dist.get(id);
                    u = id;
                }
            }

            if (u == null || minDist == Integer.MAX_VALUE) {
                break; // 剩余节点均不可达
            }

            steps.add(new DijkstraStep(dist, visited, u, null, null, 0,
                    "选取未访问节点 " + u + "（当前最小距离 " + minDist + "）"));

            visited.add(u);

            steps.add(new DijkstraStep(dist, visited, u, null, null, 0,
                    "标记 " + u + " 为已访问"));

            // 松弛该节点的所有未访问邻居
            List<Edge> neighbors = adj.getOrDefault(u, Collections.emptyList());
            for (Edge edge : neighbors) {
                String v = edge.getTo();
                int weight = edge.getWeight();

                if (!visited.contains(v)) {
                    steps.add(new DijkstraStep(dist, visited, u,
                            u, v, weight,
                            "检查边 " + u + " -> " + v + "（权值 " + weight + "）"));

                    int newDist = dist.get(u) + weight;
                    if (newDist < dist.get(v)) {
                        dist.put(v, newDist);
                        steps.add(new DijkstraStep(dist, visited, u,
                                u, v, weight,
                                "更新 " + v + " 的最短距离为 " + newDist));
                    }
                }
            }
        }

        steps.add(new DijkstraStep(dist, visited, null, null, null, 0,
                "算法完成！"));

        return steps;
    }

    /**
     * 根据有向边列表构建无向图邻接表。
     *
     * <p>对于每条边 (from -> to)，同步建立反向邻接关系 (to -> from)，
     * 从而实现无向图的语义。
     *
     * @param edges 有向边列表
     * @return 节点 ID -> 邻接边列表 的映射
     */
    private Map<String, List<Edge>> buildAdjacency(List<Edge> edges) {
        Map<String, List<Edge>> adj = new HashMap<>();
        for (Edge e : edges) {
            // 正向邻接
            adj.computeIfAbsent(e.getFrom(), k -> new ArrayList<>()).add(e);
            // 反向邻接（实现无向图）
            adj.computeIfAbsent(e.getTo(), k -> new ArrayList<>())
                    .add(new Edge(e.getTo(), e.getFrom(), e.getWeight()));
        }
        return adj;
    }
}
