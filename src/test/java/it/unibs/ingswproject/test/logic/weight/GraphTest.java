package it.unibs.ingswproject.test.logic.weight;

import it.unibs.ingswproject.logic.weight.complete.AdjacencyListNode;
import it.unibs.ingswproject.logic.weight.complete.Graph;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.test.logic.utils.GraphUtils;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Map;

public class GraphTest {
    @Test
    void AdjacencyListTest() {
        Map<Integer, Nodo> vertici = GraphUtils.getVertici();
        List<FattoreDiConversione> archi = GraphUtils.getArchi();

        Map<Nodo, List<AdjacencyListNode>> expected = Map.ofEntries(
                Map.entry(vertici.get(1), List.of(
                        new AdjacencyListNode(vertici.get(2), 1.2),
                        new AdjacencyListNode(vertici.get(3), 0.5),
                        new AdjacencyListNode(vertici.get(6), 0.5)
                )),
                Map.entry(vertici.get(2), List.of(
                        new AdjacencyListNode(vertici.get(1), 1/1.2),
                        new AdjacencyListNode(vertici.get(5), 1/1.4)
                )),
                Map.entry(vertici.get(3), List.of(
                        new AdjacencyListNode(vertici.get(1), 2.0),
                        new AdjacencyListNode(vertici.get(4), 1.0)
                )),
                Map.entry(vertici.get(4), List.of(
                        new AdjacencyListNode(vertici.get(3), 1.0)
                )),
                Map.entry(vertici.get(5), List.of(
                        new AdjacencyListNode(vertici.get(2), 1.4)
                )),
                Map.entry(vertici.get(6), List.of(
                        new AdjacencyListNode(vertici.get(1), 2.0)
                ))
        );

        Graph graph = new Graph(vertici.values(), archi);
        Map<Nodo, List<AdjacencyListNode>> result = graph.getAdjacencyList();
        Assertions.assertEquals(expected, result);
    }

    @Test
    void AdjacencyWrongListTest() {
        Map<Integer, Nodo> vertici = GraphUtils.getVertici();
        List<FattoreDiConversione> archi = GraphUtils.getArchi();

        Map<Nodo, List<AdjacencyListNode>> expected = Map.ofEntries(
                Map.entry(vertici.get(1), List.of(
                        new AdjacencyListNode(vertici.get(2), 1.2),
                        new AdjacencyListNode(vertici.get(3), 0.5)
                        // Test fails here
                        // new AdjacencyListNode(vertici.get(6), 0.5)
                )),
                Map.entry(vertici.get(2), List.of(
                        new AdjacencyListNode(vertici.get(1), 1/1.2),
                        new AdjacencyListNode(vertici.get(5), 1/1.4)
                )),
                Map.entry(vertici.get(3), List.of(
                        new AdjacencyListNode(vertici.get(1), 2.0),
                        new AdjacencyListNode(vertici.get(4), 1.0)
                )),
                Map.entry(vertici.get(4), List.of(
                        new AdjacencyListNode(vertici.get(3), 1.0)
                )),
                Map.entry(vertici.get(5), List.of(
                        new AdjacencyListNode(vertici.get(2), 1.4)
                )),
                Map.entry(vertici.get(6), List.of(
                        new AdjacencyListNode(vertici.get(1), 2.0)
                ))
        );

        Graph graph = new Graph(vertici.values(), archi);
        Map<Nodo, List<AdjacencyListNode>> result = graph.getAdjacencyList();
        Assertions.assertNotEquals(expected, result);
    }
}
