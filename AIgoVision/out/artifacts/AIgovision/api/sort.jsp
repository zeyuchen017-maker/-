<%@ page contentType="application/json;charset=UTF-8" %>
<%@ page import="com.aigovision.service.SortService,com.aigovision.model.SortStep,java.util.List,java.util.stream.Collectors" %>
<%
    response.setContentType("application/json;charset=UTF-8");
    response.setCharacterEncoding("UTF-8");

    try {
        String type = request.getParameter("type");
        String arrayParam = request.getParameter("array");

        if (type == null || type.isEmpty()) {
            out.print("{\"success\":false,\"error\":\"Missing type parameter\"}");
            return;
        }
        if (arrayParam == null || arrayParam.trim().isEmpty()) {
            out.print("{\"success\":false,\"error\":\"Missing array parameter\"}");
            return;
        }

        String[] parts = arrayParam.trim().split("[,\\s]+");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.parseInt(parts[i].trim());
        }

        List<SortStep> steps;
        SortService service = new SortService();
        switch (type.toLowerCase()) {
            case "bubble": steps = service.bubbleSort(arr); break;
            case "quick":  steps = service.quickSort(arr);  break;
            default:
                out.print("{\"success\":false,\"error\":\"Unsupported type: " + type + "\"}");
                return;
        }

        String stepsJson = steps.stream()
            .map(s -> s.toJson())
            .collect(Collectors.joining(",", "[", "]"));

        out.print("{\"success\":true,\"steps\":" + stepsJson + ",\"totalSteps\":" + steps.size() + "}");

    } catch (Exception e) {
        out.print("{\"success\":false,\"error\":\"" + e.getMessage().replace("\"", "\\\"") + "\"}");
    }
%>
