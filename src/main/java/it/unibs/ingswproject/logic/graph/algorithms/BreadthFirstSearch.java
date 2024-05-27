package it.unibs.ingswproject.logic.graph.algorithms;

import it.unibs.ingswproject.logic.graph.AdjacencyListNode;
import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.*;

public class BreadthFirstSearch {
    private final Map<Nodo, List<AdjacencyListNode>> adjacencyListCache;

    public BreadthFirstSearch(Graph graph) {
        this.adjacencyListCache = graph.getAdjacencyList();
    }

    public Set<Nodo> run(Nodo startNode) {
        // Create a queue for BFS
        Queue<Nodo> queue = new LinkedList<>();
        Set<Nodo> visited = new HashSet<>();

        // Mark the current node as visited and enqueue it
        visited.add(startNode);
        queue.add(startNode);

        // Iterate over the queue
        while (!queue.isEmpty()) {
            // Dequeue a vertex from queue and print it
            Nodo currentNode = queue.poll();

            // Get all adjacent vertices of the dequeued
            // vertex currentNode If an adjacent has not
            // been visited, then mark it visited and
            // enqueue it
            for (AdjacencyListNode neighbor : this.adjacencyListCache.get(currentNode)) {
                Nodo neighborNode = neighbor.vertex();
                if (!visited.contains(neighborNode)) {
                    visited.add(neighborNode);
                    queue.add(neighborNode);
                }
            }
        }

        return visited;
    }
}
