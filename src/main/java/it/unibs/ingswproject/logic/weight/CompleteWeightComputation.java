package it.unibs.ingswproject.logic.weight;

import it.unibs.ingswproject.logic.weight.complete.Graph;
import it.unibs.ingswproject.logic.weight.complete.algorithms.DagLongestPath;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.function.DoubleBinaryOperator;

/**
 * Classe che si occupa di calcolare il peso massimo accettato per un nuovo fattore di conversione
 *
 * @author Nicolò Rebaioli
 */
public class CompleteWeightComputation implements WeightComputationStrategy {
    private final DoubleBinaryOperator weightFunction;

    public CompleteWeightComputation(DoubleBinaryOperator weightFunction) {
        this.weightFunction = weightFunction;
    }

    /**
     * Calcola il peso massimo accettato per un nuovo fattore di conversione
     *
     * @param graph Il grafo in cui cercare il peso massimo accettato
     * @param newFDC Il nuovo fattore di conversione (il peso di questo fattore verrà ignorato)
     * @return Il peso massimo accettato per il nuovo fattore di conversione
     */
    public double getMaxAcceptedWeight(Graph graph, FattoreDiConversione newFDC) {
        // Steps:
        // 1. Compute the longest path from the starting node in the graph
        // 2. Let W be the weight of the longest path
        // 3. Let M be the weight of the new FDC
        // 4. MW <= MAX_WEIGHT
        //    M <= MAX_WEIGHT/W
        // 5. M is the maximum weight accepted

        if (graph.getArchi().isEmpty()) {
            return FattoreDiConversione.MAX_WEIGHT;
        }

        // 1. Compute the longest path from the starting node in the graph
        Nodo startingNode = newFDC.getNodo1();
        if (!graph.getAdjacencyList().containsKey(startingNode) || graph.getAdjacencyList().get(startingNode).isEmpty()) {
            startingNode = newFDC.getNodo2();
        }

        DagLongestPath dagLongestPath = new DagLongestPath(this.weightFunction);
        double weight = dagLongestPath.run(graph, startingNode);

        // 2. Let W be the weight of the longest path
        // 3. Let M be the maximum weight accepted
        // 4. MW <= MAX_WEIGHT
        //    M <= MAX_WEIGHT/W

        return Math.min(FattoreDiConversione.MAX_WEIGHT / weight, FattoreDiConversione.MAX_WEIGHT);
    }

    @Override
    public double getMinAcceptedWeight(Graph graph, FattoreDiConversione newFDC) {
        return FattoreDiConversione.MIN_WEIGHT;
    }
}
