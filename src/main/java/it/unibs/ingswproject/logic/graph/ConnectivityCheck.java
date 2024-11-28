package it.unibs.ingswproject.logic.graph;

import it.unibs.ingswproject.logic.graph.algorithms.DepthFirstSearch;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.*;

/**
 * Classe che implementa il controllo di connettività di un grafo.
 * Il controllo di connettività di un grafo è un problema che consiste nel verificare se un grafo è connesso o meno.
 *
 * @see <a href="https://en.wikipedia.org/wiki/Connectivity_(graph_theory)">Connettività (Wikipedia)</a>
 *
 * @author Nicolò Rebaioli
 */
public class ConnectivityCheck {
    protected final Nodo startingNode;
    protected final Graph graph;

    public ConnectivityCheck(Graph graph) {
        this.graph = graph;

        if (graph.getVertici().isEmpty()) {
            throw new IllegalArgumentException("connectivitycheck_empty_graph");
        }

        this.startingNode = graph.getVertici().iterator().next(); // Get the first node
    }

    public boolean isConnected() {
        Map<Nodo, List<AdjacencyListNode>> adjacencyList = this.graph.getAdjacencyList();
        DepthFirstSearch dfs = new DepthFirstSearch(this.graph);
        Set<Nodo> result = dfs.run(this.startingNode);

        // Check if all the vertices are visited
        return result.size() == adjacencyList.size();
    }
}
