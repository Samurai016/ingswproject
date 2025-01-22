package it.unibs.ingswproject.logic;

import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.logic.graph.Path;
import it.unibs.ingswproject.logic.graph.algorithms.DijkstraAlgorithm;
import it.unibs.ingswproject.logic.weight.WeightComputationStrategy;
import it.unibs.ingswproject.models.entities.Scambio;

import java.util.*;

/**
 * @author Nicolò Rebaioli
 */
public class BaseScambioStrategy implements ScambioStrategy {
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
    // Salvo il grafo in modo da non doverlo ricreare ogni volta
    private Graph graph = null;

    @Override
    public List<LinkedList<Scambio>> findScambiChiudibili(List<Scambio> scambi) {
        this.generateGraph(scambi);

        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(this.graph, this.weightComputationStrategy);

        // Calcolo tutti i cammini minimi tra i nodi
        List<Path> distanze = new ArrayList<>();
        for (int i = 0; i < scambi.size(); i++) {
            distanze.addAll(dijkstraAlgorithm.getPathsFrom(i));
        }

        // Rimuovo i cammini banali (quelli composti da un solo vertice)
        distanze.removeIf(p -> p.getVertices().size() == 1);

        // Per ogni vertice del grafo (scambio)
        // Cerco se c'è un cammino minimo che ritorna al vertice stesso
        List<LinkedList<Integer>> insiemiChiusi = new ArrayList<>();
        for (int i = 0; i < scambi.size(); i++) {
            if (distanze.isEmpty()) {
                break; // Non ho più cammini da analizzare
            }

            int uIndex = i;
            List<Path> camminiChePartonoDaU = distanze.stream()
                    .filter(p -> p.getVertices().getFirst() == uIndex)
                    .toList();
            List<Path> camminiCheArrivanoAU = distanze.stream()
                    .filter(p -> p.getVertices().getLast() == uIndex)
                    .toList();

            // Se non ci sono cammini che partono da u o che arrivano a u
            // allora non posso chiudere lo scambio
            if (camminiChePartonoDaU.isEmpty() || camminiCheArrivanoAU.isEmpty()) {
                continue;
            }

            // Altrimenti genero un insieme chiuso che chiude lo scambio
            LinkedList<Integer> camminoPartenza = camminiChePartonoDaU.getFirst().getVertices();
            LinkedList<Integer> camminoArrivo = camminiCheArrivanoAU.getFirst().getVertices();

            LinkedList<Integer> camminoArrivoSoloIntermedi = new LinkedList<>(camminoArrivo);
            camminoArrivoSoloIntermedi.removeFirst();
            camminoArrivoSoloIntermedi.removeLast();

            LinkedList<Integer> camminoTotale = new LinkedList<>();
            camminoTotale.addAll(camminoPartenza);
            camminoTotale.addAll(camminoArrivoSoloIntermedi);

            insiemiChiusi.add(camminoTotale);

            // Rimuovo i cammini usati
            distanze.remove(camminiChePartonoDaU.getFirst());
            distanze.remove(camminiCheArrivanoAU.getFirst());
        }

        // Converto gli insiemi chiusi in scambi
        List<LinkedList<Scambio>> insiemiChiusiScambi = new ArrayList<>();
        for (LinkedList<Integer> insiemeChiuso : insiemiChiusi) {
            LinkedList<Scambio> insiemeChiusoScambi = new LinkedList<>();
            insiemeChiuso.stream()
                    .map(scambi::get)
                    .forEach(insiemeChiusoScambi::add);
            insiemiChiusiScambi.add(insiemeChiusoScambi);
        }

        return insiemiChiusiScambi;
    }

    public void generateGraph(List<Scambio> scambi) {
        this.graph = new Graph(scambi.size());

        for (int i = 0; i < scambi.size(); i++) {
            Scambio u = scambi.get(i);

            for (int j = 0; j < scambi.size(); j++) {
                Scambio v = scambi.get(j);

                // Se il prodotto richiesto da v è uguale a quello prodotto da u con quantità uguale
                // allora posso creare un arco tra u e v
                if (
                        u.getOfferta().equals(v.getRichiesta()) &&
                        v.getQuantitaRichiesta() == u.getQuantitaOfferta()
                ) {
                    this.graph.addEdge(i, j, 1);
                }
            }
        }
    }
}
