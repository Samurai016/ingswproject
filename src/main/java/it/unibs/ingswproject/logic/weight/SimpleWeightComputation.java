package it.unibs.ingswproject.logic.weight;

import it.unibs.ingswproject.models.entities.FattoreDiConversione;

public class SimpleWeightComputation implements WeightComputationStrategy {
    @Override
    public double getMaxAcceptedWeight() {
        return FattoreDiConversione.MAX_WEIGHT;
    }

    @Override
    public double getMinAcceptedWeight() {
        return FattoreDiConversione.MIN_WEIGHT;
    }

    @Override
    public double getInitialWeight() {
        return 1.0; // Elemento neutro del prodotto di numeri reali
    }

    @Override
    public double computeWeight(double currentWeight, double targetWeight) {
        return targetWeight * currentWeight;
    }
}
