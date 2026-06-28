<%@ page contentType="application/json;charset=UTF-8" %>
<%@ page import="com.aigovision.service.DijkstraService,com.aigovision.model.*,java.util.*,java.util.stream.Collectors" %>
<%
    response.setContentType("application/json;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");

    try {
        String startId = request.getParameter("start");
        String nodesParam = request.getParameter("nodes");
        String edgesParam = request.getParameter("edges");

        if (startId == null || nodesParam == null || edgesParam == null) {
            out.print("{\"success\":false,\"error\":\"Missing parameters\"}");
            return;
        }

        List<GraphNode> nodes = new ArrayList<>();
        for (String part : nodesParam.split(",")) {
            String[] f = part.split(":");
            if (f.length >= 4) nodes.add(new GraphNode(f[0], f[1], Integer.parseInt(f[2]), Integer.parseInt(f[3])));
        }

        List<Edge> edges = new ArrayList<>();
        for (String part : edgesParam.split(",")) {
            String[] f = part.split(":");
            if (f.length >= 3) edges.add(new Edge(f[0], f[1], Integer.parseInt(f[2])));
        }

        DijkstraService service = new DijkstraService();
        List<DijkstraStep> steps = service.findShortestPath(startId, nodes, edges);

        String stepsJson = steps.stream()
            .map(s -> s.toJson())
            .collect(Collectors.joining(",", "[", "]"));
        
        String nodesJson = nodes.stream()
            .map(n -> "{\"id\":\"" + n.getId() + "\",\"label\":\"" + n.getLabel() + "\",\"x\":" + n.getX() + ",\"y\":" + n.getY() + "}")
            .collect(Collectors.joining(",", "[", "]"));
        
        String edgesJson = edges.stream()
            .map(e -> "{\"from\":\"" + e.getFrom() + "\",\"to\":\"" + e.getTo() + "\",\"weight\":" + e.getWeight() + "}")
            .collect(Collectors.joining(",", "[", "]"));

        out.print("{\"success\":true,\"graph\":{\"nodes\":" + nodesJson + ",\"edges\":" + edgesJson + "},\"steps\":" + stepsJson + ",\"totalSteps\":" + steps.size() + "}");

    } catch (Exception e) {
        out.print("{\"success\":false,\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
    }
%>
