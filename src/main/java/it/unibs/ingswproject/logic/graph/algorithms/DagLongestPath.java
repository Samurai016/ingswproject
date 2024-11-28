package it.unibs.ingswproject.logic.graph.algorithms;

import it.unibs.ingswproject.logic.graph.AdjacencyListNode;
import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.*;
import java.util.function.DoubleBinaryOperator;

/**
 * Classe che implementa l'algoritmo per trovare il percorso più lungo in un grafo orientato aciclico (DAG).
 *
 * @see <a href="https://www.geeksforgeeks.org/topological-sorting/">Topological Sorting (GeeksforGeeks)</a>
 * @see <a href="https://en.wikipedia.org/wiki/Longest_path_problem">Longest path problem (Wikipedia)</a>
 *
 * @author Nicolò Rebaioli
 */
public class DagLongestPath {
    private final DoubleBinaryOperator weightFunction;
    private Set<Nodo> visited;
    private Stack<Nodo> stack;

    public DagLongestPath(DoubleBinaryOperator weightFunction) {
        this.weightFunction = weightFunction;
    }

    // A recursive function used by longestPath. See below
    // link for details
    private void topologicalSortUtil(Map<Nodo, List<AdjacencyListNode>> adj, Nodo vertex) {
        // Mark the current node as visited
        this.visited.add(vertex);

        // Recur for all the vertices adjacent to this vertex
        if (adj.containsKey(vertex)) {
            for (int i = 0; i < adj.get(vertex).size(); i++) {
                AdjacencyListNode node = adj.get(vertex).get(i);
                if (!this.visited.contains(node.vertex())) {
                    this.topologicalSortUtil(adj, node.vertex());
                }
            }
        }

        // Push current vertex to stack which stores topological
        // sort
        this.stack.push(vertex);
    }

    public double run(Graph graph, Nodo startingNode) {
        ChuLiuEdmondsAlgorithm arborescence = new ChuLiuEdmondsAlgorithm();
        Map<Nodo, List<AdjacencyListNode>> adj = arborescence.run(graph, startingNode);

        // Mark all the vertices as not visited
        this.visited = new HashSet<>(adj.size());
        this.stack = new Stack<>();

        // Call the recursive helper function to store Topological
        // Sort starting from all vertices one by one
        for (Nodo vertex : graph.getVertici()) {
            if (!this.visited.contains(vertex)) {
                this.topologicalSortUtil(adj, vertex);
            }
        }

        // Initialize distances to all vertices as infinite and
        // distance to source as 1.0
        HashMap<Nodo, Double> dist = new HashMap<>();
        for (Nodo vertex : graph.getVertici()) {
            dist.put(vertex, -Double.MAX_VALUE); // https://stackoverflow.com/a/3884879/9156223
        }
        dist.put(startingNode, 1.0); // This because the weight is computed as a product

        // Process vertices in topological order
        while (!this.stack.isEmpty()) {
            // Get the next vertex from topological order
            Nodo u = this.stack.peek();
            this.stack.pop();

            // Update distances of all adjacent vertices ;
            if (dist.get(u) != Integer.MIN_VALUE && adj.containsKey(u)) {
                for (int i = 0; i < adj.get(u).size(); i++) {
                    AdjacencyListNode node = adj.get(u).get(i);
                    double currentDistance = dist.get(node.vertex());
                    double distanceFromU = this.weightFunction.applyAsDouble(dist.get(u), node.weight());
                    if (currentDistance < distanceFromU) {
                        dist.put(node.vertex(), distanceFromU);
                    }
                }
            }
        }

        // Ignora il percorso che dal startingNode porta a se stesso
        dist.remove(startingNode);

        // Return the calculated longest distances
        return Collections.max(dist.values());
    }
}
