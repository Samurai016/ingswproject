package it.unibs.ingswproject.logic.graph;

import java.util.Arrays;

/**
 * @author Nicolò Rebaioli
 */
public class Graph {
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

    // Remove an edge between vertices u and v
    public void removeEdge(int u, int v) {
        if (u >= 0 && u < this.size && v >= 0 && v < this.size) {
            this.adjMatrix[u][v] = 0;
            // adjMatrix[v][u] = 0; // For undirected graph
        }
    }

    public double[][] getAdjMatrix() {
        return this.adjMatrix;
    }

    public int getSize() {
        return this.size;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                sb.append(this.adjMatrix[i][j]).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
