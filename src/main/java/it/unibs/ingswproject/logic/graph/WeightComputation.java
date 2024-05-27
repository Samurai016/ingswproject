package it.unibs.ingswproject.logic.graph;

import it.unibs.ingswproject.logic.FattoreDiConversioneStrategy;
import it.unibs.ingswproject.logic.graph.algorithms.DagLongestPath;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;

public class WeightComputation {
    private final Graph graph;
    private final FattoreDiConversioneStrategy fdcStrategy;

    public WeightComputation(Graph graph, FattoreDiConversioneStrategy fdcStrategy) {
        this.graph = graph;
        this.fdcStrategy = fdcStrategy;
    }

    public double getMaxAcceptedWeight(FattoreDiConversione newFDC) {
        // Steps:
        // 1. Compute the longest path from the starting node in the graph
        // 2. Let W be the weight of the longest path
        // 3. Let M be the weight of the new FDC
        // 4. MW <= MAX_WEIGHT
        //    M <= MAX_WEIGHT/W
        // 5. M is the maximum weight accepted

        // 1. Compute the longest path from the starting node in the graph
        DagLongestPath dagLongestPath = new DagLongestPath(this.fdcStrategy::getWeight);
        double weight = dagLongestPath.run(this.graph, newFDC.getNodo2());

        // 2. Let W be the weight of the longest path
        // 3. Let M be the maximum weight accepted
        // 4. MW <= MAX_WEIGHT
        //    M <= MAX_WEIGHT/W

        // Se la matrice di adiacenza del nodo 2 è vuota, allora devo considerare il FDC al contrario
        // Questo perché il grafo è bidirezionale
        return FattoreDiConversione.MAX_WEIGHT / weight;
    }
}
