package it.unibs.ingswproject.test.logic;

import io.ebean.DB;
import it.unibs.ingswproject.logic.BaseFattoreDiConversioneStrategy;
import it.unibs.ingswproject.logic.FattoreDiConversioneStrategy;
import it.unibs.ingswproject.logic.graph.AdjacencyListNode;
import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.logic.graph.algorithms.DagLongestPath;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.test.logic.utils.GraphUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class DagLongestPathTest {
    @Test
    void TestLongestPath() {
        // Disabilito il logging di Ebean
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");
        System.setProperty("org.slf4j.simpleLogger.log.io.ebean", "off");

        Map<Integer, Nodo> vertici = GraphUtils.getVertici();
        List<FattoreDiConversione> archi = GraphUtils.getArchi();

        Graph graph = new Graph(vertici.values(), archi);
        StorageService storageService = new StorageService(DB.getDefault());
        FattoreDiConversioneStrategy fdcStrategy = new BaseFattoreDiConversioneStrategy(storageService);
        DagLongestPath dagLongestPath = new DagLongestPath(fdcStrategy::getWeight);
        double result = dagLongestPath.run(graph, vertici.get(1));
        double expected = 1/1.2;

        Assertions.assertEquals(expected, result);
    }
}
