package it.unibs.ingswproject.logic.routing;

import it.unibs.ingswproject.logic.graph.algorithms.DijkstraAlgorithm;
import it.unibs.ingswproject.logic.weight.WeightComputationStrategy;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.repositories.NodoRepository;
import it.unibs.ingswproject.utils.Utils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Nicolò Rebaioli
 */
public class SimpleRoutingComputation implements RoutingComputationStrategy {
    private final StorageService storageService;
    private final WeightComputationStrategy weightComputationStrategy;

    // Salvo il grafo in modo da non doverlo ricreare ogni volta
    private DijkstraAlgorithm.Graph graph = null;
    private HashMap<Nodo, Integer> nodeIndexMap = null;

    public SimpleRoutingComputation(StorageService storageService, WeightComputationStrategy weightComputationStrategy) {
        this.storageService = storageService;
        this.weightComputationStrategy = weightComputationStrategy;
    }

    private static <T, E> Set<E> getValueByKeyValue(Map<T, E> map, T value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getKey(), value))
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    public void generateGraph() {
        NodoRepository nodoRepository = (NodoRepository) this.storageService.getRepository(Nodo.class);
        List<Nodo> gerarchie = nodoRepository.findGerarchie();

        Set<Nodo> vertici = new HashSet<>();
        Set<FattoreDiConversione> archi = new HashSet<>();

        // Estraggo tutti i vertici e gli archi del grafo
        for (Nodo gerarchia : gerarchie) {
            List<Nodo> nodi = this.getNodi(gerarchia);
            vertici.addAll(nodi);
        }
        for (Nodo vertice : vertici) {
            archi.addAll(vertice.getFattoriDiConversioneAndata());
            archi.addAll(vertice.getFattoriDiConversioneRitorno());
        }

        // Assegno un indice a ciascun nodo
        int index = 0;
        this.nodeIndexMap = new HashMap<>();
        for (Nodo vertice : vertici) {
            this.nodeIndexMap.put(vertice, index++);
        }

        // Creo il grafo
        this.graph = new DijkstraAlgorithm.Graph(vertici.size());
        for (FattoreDiConversione arco : archi) {
            int index1 = this.nodeIndexMap.get(arco.getNodo1());
            int index2 = this.nodeIndexMap.get(arco.getNodo2());
            this.graph.addEdge(index1, index2, arco.getFattore(arco.getNodo1()));
        }
    }

    @Override
    public HashMap<Nodo, Double> getRoutingCostsFrom(Nodo nodo1) {
        this.generateGraph();

        int indexOfNodo1 = getValueByKeyValue(this.nodeIndexMap, nodo1).stream().findFirst().orElseThrow();

        // Calcolo il percorso minimo
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(this.graph, this.weightComputationStrategy);
        double[] weights = dijkstraAlgorithm.getDistancesFrom(indexOfNodo1);

        HashMap<Nodo, Double> result = new HashMap<>();
        for (Map.Entry<Nodo, Integer> entry : this.nodeIndexMap.entrySet()) {
            result.put(entry.getKey(), weights[entry.getValue()]);
        }

        return result;
    }

    @Override
    public double getRoutingCost(Nodo nodo1, Nodo nodo2) {
        // Ottengo i costi
        HashMap<Nodo, Double> costs = this.getRoutingCostsFrom(nodo1);

        // Ritorno il peso del percorso minimo
        return getValueByKeyValue(costs, nodo2).stream().findFirst().orElseThrow();
    }

    private List<Nodo> getNodi(Nodo startingNode) {
        if (startingNode.isFoglia()) {
            return List.of(startingNode);
        }

        //noinspection unchecked
        return (List<Nodo>) Utils.flatten(startingNode.getFigli().stream().map(this::getNodi).toList());
    }
}
