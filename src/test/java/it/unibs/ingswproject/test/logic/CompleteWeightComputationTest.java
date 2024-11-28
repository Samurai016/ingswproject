package it.unibs.ingswproject.test.logic;

import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.logic.graph.CompleteWeightComputation;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.test.logic.utils.GraphUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class CompleteWeightComputationTest {
    @Test
    public void Peso2Nodi() {
        Map<Integer, Nodo> vertici = GraphUtils.getVertici();
        Graph graph = GraphUtils.getGraph();

        Nodo nuovoNodo = GraphUtils.createFakeFoglia("2");
        FattoreDiConversione nuovoFDC = new FattoreDiConversione(nuovoNodo, vertici.get(0));

        CompleteWeightComputation weightComputation = new CompleteWeightComputation((a, b) -> a * b);
        double maxWeight = weightComputation.getMaxAcceptedWeight(graph, nuovoFDC);

        Assertions.assertEquals(maxWeight, 1.42);
    }

}
