package it.unibs.ingswproject.test.logic;

import it.unibs.ingswproject.logic.graph.AdjacencyListNode;
import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.logic.graph.algorithms.ChuLiuEdmondsAlgorithm;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.test.logic.utils.GraphUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ChuLiuEdmondsAlgorithmTest {
    @Test
    void TestArborescence() {
        Map<Integer, Nodo> vertici = GraphUtils.getVertici();
        Graph graph = GraphUtils.getGraph();
        ChuLiuEdmondsAlgorithm arborescence = new ChuLiuEdmondsAlgorithm();
        Map<Nodo, List<AdjacencyListNode>> result = arborescence.run(graph, vertici.get(1));

        Map<Nodo, List<AdjacencyListNode>> expected = Map.ofEntries(
                Map.entry(vertici.get(0), List.of(
                        //new AdjacencyListNode(vertici.get(1), 1.2),
                        new AdjacencyListNode(vertici.get(2), 0.5),
                        new AdjacencyListNode(vertici.get(5), 0.5)
                )),
                Map.entry(vertici.get(1), List.of(
                        new AdjacencyListNode(vertici.get(0), 1 / 1.2),
                        new AdjacencyListNode(vertici.get(4), 1 / 1.4)
                )),
                Map.entry(vertici.get(2), List.of(
                        //new AdjacencyListNode(vertici.get(0), 2.0),
                        new AdjacencyListNode(vertici.get(3), 1.0)
                )),
                Map.entry(vertici.get(3), List.of(
                        //new AdjacencyListNode(vertici.get(2), 1.0)
                )),
                Map.entry(vertici.get(4), List.of(
                        //new AdjacencyListNode(vertici.get(1), 1.4)
                )),
                Map.entry(vertici.get(5), List.of(
                        //new AdjacencyListNode(vertici.get(0), 2.0)
                ))
        );

        Assertions.assertEquals(result, expected);
    }
}
