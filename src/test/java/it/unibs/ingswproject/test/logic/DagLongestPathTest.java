package it.unibs.ingswproject.test.logic;

import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.logic.graph.algorithms.DagLongestPath;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.test.logic.utils.GraphUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class DagLongestPathTest {
    @Test
    void TestLongestPath2Nodi() {
        Map<Integer, Nodo> vertici = GraphUtils.getVertici();
        Graph graph = GraphUtils.getGraph();
        DagLongestPath dagLongestPath = new DagLongestPath((a, b) -> a * b);

        double result = dagLongestPath.run(graph, vertici.get(1));
        double expected = 1/1.4;

        Assertions.assertEquals(expected, result);
    }
}
