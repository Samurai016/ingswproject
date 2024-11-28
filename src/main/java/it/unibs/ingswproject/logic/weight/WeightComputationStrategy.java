package it.unibs.ingswproject.logic.weight;

import it.unibs.ingswproject.logic.weight.complete.Graph;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;

public interface WeightComputationStrategy {
    double getMaxAcceptedWeight(Graph graph, FattoreDiConversione newFDC);
    double getMinAcceptedWeight(Graph graph, FattoreDiConversione newFDC);
}
