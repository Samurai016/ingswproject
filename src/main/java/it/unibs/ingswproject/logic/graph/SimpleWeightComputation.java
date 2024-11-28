package it.unibs.ingswproject.logic.graph;

import it.unibs.ingswproject.models.entities.FattoreDiConversione;

public class SimpleWeightComputation implements WeightComputationStrategy {
    @Override
    public double getMaxAcceptedWeight(Graph graph, FattoreDiConversione newFDC) {
        return FattoreDiConversione.getMaxAcceptedWeight();
    }

    @Override
    public double getMinAcceptedWeight(Graph graph, FattoreDiConversione newFDC) {
        return FattoreDiConversione.getMinAcceptedWeight();
    }
}
