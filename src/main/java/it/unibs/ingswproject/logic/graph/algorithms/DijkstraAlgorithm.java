package it.unibs.ingswproject.logic.graph.algorithms;

import it.unibs.ingswproject.logic.weight.WeightComputationStrategy;

import java.util.Arrays;

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
    public double[] getDistancesFrom(int startVertex) {
        if (startVertex == -1) {
            throw new IllegalArgumentException("Start vertex not found in graph!");
        }

        double[] distances = new double[this.graph.size];
        boolean[] visited = new boolean[this.graph.size];

        Arrays.fill(distances, Integer.MAX_VALUE);
        distances[startVertex] = this.weightComputationStrategy.getInitialWeight();

        for (int i = 0; i < this.graph.size; i++) {
            int u = this.getMinDistanceVertex(distances, visited);
            if (u == -1) {
                break; // No more reachable vertices
            }

            visited[u] = true;

            for (int v = 0; v < this.graph.size; v++) {
                if (this.graph.adjMatrix[u][v] != 0 && !visited[v]) {
                    double alt = this.weightComputationStrategy.computeWeight(distances[u], this.graph.adjMatrix[u][v]);
                    if (alt < distances[v]) {
                        distances[v] = alt;
                    }
                }
            }
        }

        return distances;
    }

    // Helper method to find the vertex with the minimum distance
    private int getMinDistanceVertex(double[] distances, boolean[] visited) {
        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;
        for (int i = 0; i < this.graph.size; i++) {
            if (!visited[i] && distances[i] < minDistance) {
                minDistance = distances[i];
                minIndex = i;
            }
        }
        return minIndex;
    }

    public static class Graph {
        private final double[][] adjMatrix;       // Adjacency Matrix
        private final int size;                // Number of vertices

        public Graph(int size) {
            this.size = size;
            this.adjMatrix = new double[size][size];
            for (int i = 0; i < size; i++) {
                Arrays.fill(this.adjMatrix[i], 0);
            }
        }

        // Add an edge between vertices u and v with a given weight
        public void addEdge(int u, int v, double weight) {
            if (u >= 0 && u < this.size && v >= 0 && v < this.size) {
                this.adjMatrix[u][v] = weight;
                // adjMatrix[v][u] = weight; // For undirected graph
            }
        }
    }
}
