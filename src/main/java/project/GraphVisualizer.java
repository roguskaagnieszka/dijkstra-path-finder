package project;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.util.*;

public class GraphVisualizer {
    public static void main(String[] args) {
        System.setProperty("org.graphstream.ui", "swing");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Algorytm Dijkstry ===");
            System.out.println("Wierzchołki są numerowane od 0.");
            System.out.println("0 - Zakończ program");
            System.out.println("1 - Przykładowe dane");
            System.out.println("2 - Losowy graf");
            System.out.println("3 - Wprowadź dane ręcznie");
            System.out.print("Wybierz opcję (0-3): ");
            int option = readInt(scanner, 0, 3);

            if (option == 0) {
                System.out.println("Do zobaczenia!");
                break;
            }

            int[][] edges;
            int size;

            if (option == 1) {
                edges = new int[][]{
                        {0, 1, 6}, {0, 4, 2}, {0, 3, 4}, {1, 4, 1}, {1, 3, 3}, {1, 2, 3},
                        {2, 4, 5}, {2, 5, 2}, {2, 6, 4}, {3, 4, 5}, {3, 7, 3}, {4, 5, 1},
                        {4, 7, 2}, {4, 8, 4}, {4, 9, 2}, {5, 6, 2}, {5, 8, 7}, {5, 9, 1},
                        {6, 9, 3}, {7, 8, 2}, {8, 9, 3}
                };
                size = 10;
            } else if (option == 2) {
                System.out.print("Podaj liczbę wierzchołków: ");
                size = readInt(scanner, 2, 100);
                edges = generateRandomEdges(size);
            } else {
                System.out.print("Podaj liczbę wierzchołków: ");
                size = readInt(scanner, 2, 100);
                System.out.print("Podaj liczbę krawędzi: ");
                int m = readInt(scanner, 1, size * (size - 1) / 2);
                edges = readEdgesFromUser(scanner, size, m);
            }

            System.out.print("Podaj numer wierzchołka startowego (0 - " + (size - 1) + "): ");
            int start = readInt(scanner, 0, size - 1);

            List<List<GraphUtils.Edge>> graph = GraphUtils.buildGraph(size, edges);
            int[] predecessors = runDijkstra(graph, start, size);
            drawGraph(size, edges, start, predecessors);

            System.out.println("\nCo chcesz zrobić dalej?");
            System.out.println("0 - Zakończ program");
            System.out.println("1 - Zacznij od nowa");
            System.out.print("Wybór: ");
            int again = readInt(scanner, 0, 1);
            if (again == 0) {
                System.out.println("Do zobaczenia!");
                break;
            }
        }
    }

    private static int readInt(Scanner scanner, int min, int max) {
        while (true) {
            try {
                int val = Integer.parseInt(scanner.nextLine().trim());
                if (val < min || val > max) throw new NumberFormatException();
                return val;
            } catch (NumberFormatException e) {
                System.out.print("Błąd. Podaj liczbę całkowitą z przedziału " + min + " - " + max + ": ");
            }
        }
    }

    private static int[][] readEdgesFromUser(Scanner scanner, int size, int count) {
        Set<String> seen = new HashSet<>();
        List<int[]> edges = new ArrayList<>();

        System.out.println("\n--- Wprowadzanie krawędzi ---");
        System.out.println("Wierzchołki numerujemy od 0 do " + (size - 1) + ".");
        System.out.println("Wprowadź " + count + " krawędzi w formacie: a b w");

        while (edges.size() < count) {
            System.out.print("[" + (edges.size() + 1) + "/" + count + "] Wprowadź: ");
            String line = scanner.nextLine();
            String[] parts = line.trim().split("\\s+");
            if (parts.length != 3) {
                System.out.println("❌ Błąd: podaj dokładnie 3 liczby oddzielone spacją.");
                continue;
            }
            try {
                int a = Integer.parseInt(parts[0]);
                int b = Integer.parseInt(parts[1]);
                int w = Integer.parseInt(parts[2]);
                if (a < 0 || b < 0 || a >= size || b >= size || a == b || w <= 0) {
                    System.out.println("❌ Błędne dane. Sprawdź zakresy.");
                    continue;
                }
                String key = Math.min(a, b) + "-" + Math.max(a, b);
                if (seen.contains(key)) {
                    System.out.println("❌ Taka krawędź już istnieje.");
                    continue;
                }
                edges.add(new int[]{a, b, w});
                seen.add(key);
            } catch (NumberFormatException e) {
                System.out.println("❌ Wprowadź tylko liczby.");
            }
        }

        return edges.toArray(new int[0][]);
    }

    private static int[][] generateRandomEdges(int size) {
        Random rand = new Random();
        Set<String> seen = new HashSet<>();
        List<int[]> edges = new ArrayList<>();
        int maxEdges = Math.min(size * 3, size * (size - 1) / 2);

        while (edges.size() < maxEdges) {
            int a = rand.nextInt(size);
            int b = rand.nextInt(size);
            if (a == b) continue;
            int w = rand.nextInt(9) + 1;
            String key = Math.min(a, b) + "-" + Math.max(a, b);
            if (!seen.contains(key)) {
                edges.add(new int[]{a, b, w});
                seen.add(key);
            }
        }

        return edges.toArray(new int[0][]);
    }

    private static int[] runDijkstra(List<List<GraphUtils.Edge>> graph, int start, int size) {
        int[] dist = new int[size];
        int[] prev = new int[size];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(prev, -1);
        dist[start] = 0;

        PriorityQueue<GraphUtils.Edge> pq = new PriorityQueue<>(Comparator.comparingInt(e -> e.weight));
        pq.add(new GraphUtils.Edge(start, 0));

        List<int[]> distHistory = new ArrayList<>();
        List<int[]> prevHistory = new ArrayList<>();
        distHistory.add(dist.clone());
        prevHistory.add(prev.clone());

        while (!pq.isEmpty()) {
            GraphUtils.Edge current = pq.poll();
            int u = current.to;

            for (GraphUtils.Edge e : graph.get(u)) {
                if (dist[u] + e.weight < dist[e.to]) {
                    dist[e.to] = dist[u] + e.weight;
                    prev[e.to] = u;
                    pq.add(new GraphUtils.Edge(e.to, dist[e.to]));
                }
            }

            distHistory.add(dist.clone());
            prevHistory.add(prev.clone());
        }

        printDijkstraStepsTable(distHistory, prevHistory, size, start);
        return prev;
    }

    private static void printDijkstraStepsTable(List<int[]> distHistory, List<int[]> prevHistory, int size, int start) {
        System.out.println("\n📋 Tabela kroków algorytmu Dijkstry (start: " + start + "):\n");
        System.out.print("Krok  |");
        for (int i = 0; i < size; i++) {
            System.out.printf(" %4d", i);
        }
        System.out.println();
        System.out.println("------+----------------------------------------------------------");

        for (int step = 0; step < distHistory.size(); step++) {
            System.out.printf(" %3d  |", step);
            for (int i = 0; i < size; i++) {
                int d = distHistory.get(step)[i];
                int p = prevHistory.get(step)[i];
                String val = (d == Integer.MAX_VALUE ? "∞" : String.valueOf(d));
                String pred = (p == -1 ? "-" : String.valueOf(p));
                System.out.printf(" %2s/%s", val, pred);
            }
            System.out.println();
        }
    }

    private static void drawGraph(int size, int[][] edges, int start, int[] predecessors) {
        Graph gsGraph = new SingleGraph("Dijkstra");
        gsGraph.setStrict(false);
        gsGraph.setAutoCreate(false);

        double radius = 8.0;
        for (int i = 0; i < size; i++) {
            Node node = gsGraph.addNode(String.valueOf(i));
            double angle = 2 * Math.PI * i / size;
            double x = radius * Math.cos(angle);
            double y = radius * Math.sin(angle);
            node.setAttribute("xy", x, y);
            node.setAttribute("ui.label", String.valueOf(i));
            if (i == start) node.setAttribute("ui.class", "start");
        }

        for (int[] e : edges) {
            String id = e[0] + "-" + e[1];
            org.graphstream.graph.Edge edge = gsGraph.addEdge(id, String.valueOf(e[0]), String.valueOf(e[1]));
            edge.setAttribute("ui.label", String.valueOf(e[2]));
        }

        for (int i = 0; i < predecessors.length; i++) {
            int prev = predecessors[i];
            if (prev != -1) {
                String e1 = prev + "-" + i;
                String e2 = i + "-" + prev;
                if (gsGraph.getEdge(e1) != null)
                    gsGraph.getEdge(e1).setAttribute("ui.class", "marked");
                else if (gsGraph.getEdge(e2) != null)
                    gsGraph.getEdge(e2).setAttribute("ui.class", "marked");
            }
        }

        String styleSheet =
                "node {" +
                        " fill-color: #bde0fe; size: 20px;" +
                        " text-size: 24px; text-color: black; text-style: bold;" +
                        " stroke-mode: plain; stroke-color: black;" +
                        "}" +
                        "node.start {" +
                        " fill-color: orange; size: 30px;" +
                        " text-color: black; text-size: 26px;" +
                        " stroke-color: red; stroke-width: 3px;" +
                        "}" +
                        "edge {" +
                        " fill-color: #444; text-size: 20px; size: 1px;" +
                        " text-color: black;" +
                        "}" +
                        "edge.marked {" +
                        " fill-color: red; size: 2px;" +
                        "}";

        gsGraph.setAttribute("ui.stylesheet", styleSheet);
        gsGraph.setAttribute("ui.quality");
        gsGraph.setAttribute("ui.antialias");
        gsGraph.display();
    }
}
