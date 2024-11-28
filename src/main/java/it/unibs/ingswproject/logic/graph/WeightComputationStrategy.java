package it.unibs.ingswproject.logic.graph;

import it.unibs.ingswproject.models.entities.FattoreDiConversione;

public interface WeightComputationStrategy {
    double getMaxAcceptedWeight(Graph graph, FattoreDiConversione newFDC);
    double getMinAcceptedWeight(Graph graph, FattoreDiConversione newFDC);
}
