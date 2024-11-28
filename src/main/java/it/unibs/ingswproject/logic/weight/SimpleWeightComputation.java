package it.unibs.ingswproject.logic.weight;

import it.unibs.ingswproject.logic.weight.complete.Graph;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;

public class SimpleWeightComputation implements WeightComputationStrategy {
    @Override
    public double getMaxAcceptedWeight(Graph graph, FattoreDiConversione newFDC) {
        return FattoreDiConversione.MAX_WEIGHT;
    }

    @Override
    public double getMinAcceptedWeight(Graph graph, FattoreDiConversione newFDC) {
        return FattoreDiConversione.MIN_WEIGHT;
    }
}
