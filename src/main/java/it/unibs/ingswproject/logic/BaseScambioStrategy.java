package it.unibs.ingswproject.logic;

import it.unibs.ingswproject.logic.graph.algorithms.DijkstraAlgorithm;
import it.unibs.ingswproject.logic.weight.WeightComputationStrategy;
import it.unibs.ingswproject.models.entities.Scambio;

import java.util.*;

/**
 * @author Nicolò Rebaioli
 */
public class BaseScambioStrategy implements ScambioStrategy {
    // Salvo il grafo in modo da non doverlo ricreare ogni volta
    private DijkstraAlgorithm.Graph graph = null;
    private final WeightComputationStrategy weightComputationStrategy = new WeightComputationStrategy() {
        @Override
        public double getMaxAcceptedWeight() {
            return 0;
        }

        @Override
        public double getMinAcceptedWeight() {
            return 0;
        }

        @Override
        public double getInitialWeight() {
            return 0;
        }

        @Override
        public double computeWeight(double currentWeight, double targetWeight) {
            return currentWeight + targetWeight;
        }
    };

    @Override
    public HashMap<Scambio, Collection<Scambio>> findScambiChiudibili(List<Scambio> scambi) {
        this.generateGraph(scambi);

        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(this.graph, this.weightComputationStrategy);

        for (int i = 0; i < scambi.size(); i++) {
            double[] distanze = dijkstraAlgorithm.getDistancesFrom(i);
            System.out.printf("Lo scambio %s ha distanze: \n", scambi.get(i));
            for (int j = 0; j < distanze.length; j++) {
                System.out.printf("Verso %s: %f\n", scambi.get(j), distanze[j]);
            }
            System.out.println();
        }

        return null;
    }

    public void generateGraph(List<Scambio> scambi) {
        this.graph = new DijkstraAlgorithm.Graph(scambi.size());

        for (int i = 0; i < scambi.size(); i++) {
            Scambio u = scambi.get(i);

            for (int j = 0; j < scambi.size(); j++) {
                Scambio v = scambi.get(j);

                // Se il prodotto richiesto da v è uguale a quello prodotto da u con quantità minore o uguale
                // allora posso creare un arco tra u e v
                if (
                    u.getOfferta().equals(v.getRichiesta()) &&
                    v.getQuantitaRichiesta() <= u.getQuantitaOfferta()
                ) {
                    this.graph.addEdge(i, j, 1);
                }
            }
        }
    }
}
