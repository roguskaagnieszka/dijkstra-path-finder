package project;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;


import java.util.List;

public class GraphVisualizer {
    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");

        int[][] edges = {
                {0, 1, 6}, {0, 4, 2}, {0, 3, 4}, {1, 4, 1}, {1, 3, 3}, {1, 2, 3},
                {2, 4, 5}, {2, 5, 2}, {2, 6, 4}, {3, 4, 5}, {3, 7, 3}, {4, 5, 1},
                {4, 7, 2}, {4, 8, 4}, {4, 9, 2}, {5, 6, 2}, {5, 8, 7}, {5, 9, 1},
                {6, 9, 3}, {7, 8, 2}, {8, 9, 3}
        };

        int size = 10;
        int start = 2;

        List<List<GraphUtils.Edge>> graph = GraphUtils.buildGraph(size, edges);
        int[] predecessors = new int[size];
        int[] distances = GraphUtils.dijkstra(graph, start, predecessors);

        Graph gsGraph = new SingleGraph("Dijkstra");
        gsGraph.setStrict(false);
        gsGraph.setAutoCreate(true);

        // Dodaj krawędzie do GraphStream
        for (int[] e : edges) {
            String edgeId = e[0] + "-" + e[1];
            org.graphstream.graph.Edge edge = gsGraph.addEdge(edgeId, String.valueOf(e[0]), String.valueOf(e[1]));
            edge.setAttribute("ui.label", e[2]);
        }

        // Etykiety wierzchołków
        for (Node node : gsGraph) {
            node.setAttribute("ui.label", node.getId());
        }

        // Pokoloruj najkrótsze ścieżki na czerwono
        for (int i = 0; i < predecessors.length; i++) {
            int prev = predecessors[i];
            if (prev != -1) {
                String e1 = prev + "-" + i;
                String e2 = i + "-" + prev;

                if (gsGraph.getEdge(e1) != null) {
                    gsGraph.getEdge(e1).setAttribute("ui.style", "fill-color: red;");
                } else if (gsGraph.getEdge(e2) != null) {
                    gsGraph.getEdge(e2).setAttribute("ui.style", "fill-color: red;");
                }
            }
        }

        gsGraph.display();
    }
}
