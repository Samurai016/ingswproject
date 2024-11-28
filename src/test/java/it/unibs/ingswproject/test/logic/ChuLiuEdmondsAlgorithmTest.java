package it.unibs.ingswproject.test.logic;

import it.unibs.ingswproject.logic.weight.complete.AdjacencyListNode;
import it.unibs.ingswproject.logic.weight.complete.Graph;
import it.unibs.ingswproject.logic.weight.complete.algorithms.ChuLiuEdmondsAlgorithm;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.test.logic.utils.GraphUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class ChuLiuEdmondsAlgorithmTest {
    @Test
    void TestArborescenza2Nodi() {
        Map<Integer, Nodo> vertici = GraphUtils.getVertici();
        Graph graph = GraphUtils.getGraph();
        ChuLiuEdmondsAlgorithm arborescence = new ChuLiuEdmondsAlgorithm();
        Map<Nodo, List<AdjacencyListNode>> result = arborescence.run(graph, vertici.get(1));

        Map<Nodo, List<AdjacencyListNode>> expected = Map.ofEntries(
                Map.entry(vertici.get(0), List.of()),
                Map.entry(vertici.get(1), List.of(
                        new AdjacencyListNode(vertici.get(0), 1 / 1.4)
                ))
        );

        Assertions.assertEquals(result, expected);
    }
}
