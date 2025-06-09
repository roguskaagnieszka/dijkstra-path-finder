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
            graph.get(e[1]).add(new Edge(e[0], e[2])); // nieskierowany graf
        }
        return graph;
    }

    public static int[] dijkstra(List<List<Edge>> graph, int start, int[] predecessors) {
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(predecessors, -1);
        dist[start] = 0;

        PriorityQueue<Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        pq.add(new Edge(start, 0));

        while (!pq.isEmpty()) {
            Edge current = pq.poll();
            int u = current.to;

            for (Edge e : graph.get(u)) {
                if (dist[u] + e.weight < dist[e.to]) {
                    dist[e.to] = dist[u] + e.weight;
                    predecessors[e.to] = u;
                    pq.add(new Edge(e.to, dist[e.to]));
                }
            }
        }

        return dist;
    }
}
