package it.unibs.ingswproject.logic.routing;

import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.logic.graph.Path;
import it.unibs.ingswproject.logic.graph.algorithms.DijkstraAlgorithm;
import it.unibs.ingswproject.logic.weight.WeightComputationStrategy;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.repositories.NodoRepository;
import it.unibs.ingswproject.utils.Utils;

import java.util.*;

/**
 * @author Nicolò Rebaioli
 */
public class SimpleRoutingComputation implements RoutingComputationStrategy {
    private final StorageService storageService;
    private final WeightComputationStrategy weightComputationStrategy;

    // Salvo il grafo in modo da non doverlo ricreare ogni volta
    private Graph graph = null;
    private HashMap<Nodo, Integer> nodeIndexMap = null;

    public SimpleRoutingComputation(StorageService storageService, WeightComputationStrategy weightComputationStrategy) {
        this.storageService = storageService;
        this.weightComputationStrategy = weightComputationStrategy;
    }

    private static <T, E> E getValueByKeyValue(Map<T, E> map, T value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getKey(), value))
                .map(Map.Entry::getValue)
                .findFirst()
                .orElse(null);
    }

    private static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        return map.entrySet()
                .stream()
                .filter(entry -> Objects.equals(entry.getValue(), value))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElse(null);
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
        this.graph = new Graph(vertici.size());
        for (FattoreDiConversione arco : archi) {
            int index1 = this.nodeIndexMap.get(arco.getNodo1());
            int index2 = this.nodeIndexMap.get(arco.getNodo2());
            this.graph.addEdge(index1, index2, arco.getFattore(arco.getNodo1()));
            this.graph.addEdge(index2, index1, arco.getFattore(arco.getNodo2()));
        }
    }

    @Override
    public Map<Nodo, Double> getRoutingCostsFrom(Nodo nodo1) {
        this.generateGraph();

        int indexOfNodo1 = getValueByKeyValue(this.nodeIndexMap, nodo1);

        // Calcolo il percorso minimo
        DijkstraAlgorithm dijkstraAlgorithm = new DijkstraAlgorithm(this.graph, this.weightComputationStrategy);
        List<Path> paths = dijkstraAlgorithm.getPathsFrom(indexOfNodo1);

        HashMap<Nodo, Double> result = new HashMap<>();
        for (Path path : paths) {
            int nodoArrivoIndex = path.getVertices().getLast();
            Nodo nodoArrivo = getKeyByValue(this.nodeIndexMap, nodoArrivoIndex);
            if (nodoArrivo != null) {
                result.put(nodoArrivo, path.getWeight());
            }
        }

        return result;
    }

    @Override
    public double getRoutingCost(Nodo nodo1, Nodo nodo2) {
        // Ottengo i costi
        Map<Nodo, Double> costs = this.getRoutingCostsFrom(nodo1);

        // Ritorno il peso del percorso minimo
        return getValueByKeyValue(costs, nodo2);
    }

    private List<Nodo> getNodi(Nodo startingNode) {
        if (startingNode.isFoglia()) {
            return List.of(startingNode);
        }

        //noinspection unchecked
        return (List<Nodo>) Utils.flatten(startingNode.getFigli().stream().map(this::getNodi).toList());
    }
}
