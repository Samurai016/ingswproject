package it.unibs.ingswproject.logic.graph.algorithms;

import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.logic.graph.Path;
import it.unibs.ingswproject.logic.weight.WeightComputationStrategy;

import java.util.*;

/**
 * @author Nicolò Rebaioli
 */
public class DijkstraAlgorithm {
    private final Graph graph;
    private final WeightComputationStrategy weightComputationStrategy;

    public DijkstraAlgorithm(Graph graph, WeightComputationStrategy weightComputationStrategy) {
        this.graph = graph;
        this.weightComputationStrategy = weightComputationStrategy;
    }

    // Dijkstra's algorithm to find the shortest distances from a starting vertex
    public List<Path> getPathsFrom(int startVertex) {
        if (startVertex == -1) {
            throw new IllegalArgumentException("Start vertex not found in graph!");
        }

        int size = this.graph.getSize();
        double[][] adjMatrix = this.graph.getAdjMatrix();
        double[] distances = new double[size];
        boolean[] visited = new boolean[size];
        int[] predecessors = new int[size]; // To track the path to each vertex
        Arrays.fill(distances, Integer.MAX_VALUE);
        Arrays.fill(predecessors, -1); // Initialize predecessors with -1
        distances[startVertex] = this.weightComputationStrategy.getInitialWeight();

        for (int i = 0; i < size; i++) {
            int u = this.getMinDistanceVertex(distances, visited);
            if (u == -1) {
                break; // No more reachable vertices
            }

            visited[u] = true;

            for (int v = 0; v < size; v++) {
                if (adjMatrix[u][v] != 0 && !visited[v]) {
                    double alt = this.weightComputationStrategy.computeWeight(distances[u], adjMatrix[u][v]);
                    if (alt < distances[v]) {
                        distances[v] = alt;
                        predecessors[v] = u; // Update the predecessor
                    }
                }
            }
        }

        // Reconstruct paths for all vertices
        return rebuildPaths(size, distances, predecessors);
    }

    private static List<Path> rebuildPaths(int size, double[] distances, int[] predecessors) {
        List<Path> paths = new ArrayList<>();
        for (int v = 0; v < size; v++) {
            if (distances[v] != Integer.MAX_VALUE) { // If the vertex is reachable
                LinkedList<Integer> path = new LinkedList<>();
                double weight = distances[v];
                for (int current = v; current != -1; current = predecessors[current]) {
                    path.addFirst(current); // Add to the start of the path (reverse order)
                }
                paths.add(new Path(path, weight));
            }
        }
        return paths;
    }


    // Helper method to find the vertex with the minimum distance
    private int getMinDistanceVertex(double[] distances, boolean[] visited) {
        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < this.graph.getSize(); i++) {
            if (!visited[i] && distances[i] < minDistance) {
                minDistance = distances[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

}
