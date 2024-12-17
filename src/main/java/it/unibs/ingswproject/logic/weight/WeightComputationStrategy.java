package it.unibs.ingswproject.logic.weight;

public interface WeightComputationStrategy {
    double getMaxAcceptedWeight();
    double getMinAcceptedWeight();
    double getInitialWeight();
    double computeWeight(double currentWeight, double targetWeight);
}
