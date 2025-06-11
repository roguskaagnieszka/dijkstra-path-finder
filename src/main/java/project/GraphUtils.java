package project;

import java.util.*;

public class GraphUtils {
    public static class Edge {
        public int to, weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }

    public static List<List<Edge>> buildGraph(int size, int[][] edges) {
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < size; i++) graph.add(new ArrayList<>());

        for (int[] e : edges) {
            graph.get(e[0]).add(new Edge(e[1], e[2]));
            graph.get(e[1]).add(new Edge(e[0], e[2]));
        }
        return graph;
    }
}
