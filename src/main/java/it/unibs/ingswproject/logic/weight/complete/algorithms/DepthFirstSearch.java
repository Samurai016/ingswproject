package it.unibs.ingswproject.logic.weight.complete.algorithms;

import it.unibs.ingswproject.logic.weight.complete.AdjacencyListNode;
import it.unibs.ingswproject.logic.weight.complete.Graph;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Classe che implementa la visita in profondità di un grafo.
 *
 * @see "<a href="https://en.wikipedia.org/wiki/Depth-first_search">Depth-first search (Wikipedia)</a>"
 *
 * @author Nicolò Rebaioli
 */
public class DepthFirstSearch {
    private final Map<Nodo, List<AdjacencyListNode>> adjacencyListCache;
    private Set<Nodo> visited;

    public DepthFirstSearch(Graph graph) {
        this.adjacencyListCache = graph.getAdjacencyList();
    }

    public Set<Nodo> run(Nodo startNode) {
        this.visited = new HashSet<>();
        return this.runRecursive(startNode);
    }

    private Set<Nodo> runRecursive(Nodo current) {
        // Mark the vertex visited as True
        this.visited.add(current);

        // Travel the adjacent neighbours
        for (AdjacencyListNode destination : this.adjacencyListCache.get(current)) {
            Nodo destinationNode = destination.vertex();
            if (!this.visited.contains(destinationNode)) {
                this.runRecursive(destinationNode);
            }
        }

        return this.visited;
    }
}