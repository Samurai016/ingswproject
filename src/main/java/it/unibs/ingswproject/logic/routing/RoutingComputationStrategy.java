package it.unibs.ingswproject.logic.routing;

import it.unibs.ingswproject.models.entities.Nodo;

import java.util.Map;

/**
 * @author Nicolò Rebaioli
 */
public interface RoutingComputationStrategy {
    double getRoutingCost(Nodo nodo1, Nodo nodo2);
    Map<Nodo, Double> getRoutingCostsFrom(Nodo nodo1);
}
