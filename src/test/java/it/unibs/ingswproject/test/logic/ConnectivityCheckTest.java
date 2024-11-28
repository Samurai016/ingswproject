package it.unibs.ingswproject.test.logic;

import it.unibs.ingswproject.logic.weight.complete.ConnectivityCheck;
import it.unibs.ingswproject.logic.weight.complete.Graph;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConnectivityCheckTest {
    @Test
    @Order(1)
    @DisplayName("Test connected")
    void TestConnected() {
        /*
         * Root
         *  - Foglia1
         *      - Foglia1.1
         *          - Foglia1.1.1
         *          - Foglia1.1.2
         *  - Foglia2
         *  - Foglia3
         *      - Foglia3.1
         */
        Nodo root = Nodo.createRoot("Root", null, "valore");

        Nodo foglia1 = Nodo.createFoglia("Foglia1", null, "valore", root);
        Nodo foglia2 = Nodo.createFoglia("Foglia2", null, "valore", root);
        Nodo foglia3 = Nodo.createFoglia("Foglia3", null, "valore", root);
        root.setFigli(new ArrayList<>(Arrays.asList(foglia1, foglia2, foglia3)));

        Nodo foglia1_1 = Nodo.createFoglia("Foglia1.1", null, "valore", foglia1);
        foglia1.setFigli(new ArrayList<>(Collections.singletonList(foglia1_1)));
        Nodo foglia3_1 = Nodo.createFoglia("Foglia3.1", null, "valore", foglia3);
        foglia3.setFigli(new ArrayList<>(Collections.singletonList(foglia3_1)));

        Nodo foglia1_1_1 = Nodo.createFoglia("Foglia1.1.1", null, "valore", foglia1_1);
        Nodo foglia1_1_2 = Nodo.createFoglia("Foglia1.1.2", null, "valore", foglia1_1);
        foglia1_1.setFigli(new ArrayList<>(Arrays.asList(foglia1_1_1, foglia1_1_2)));

        foglia1_1_1.setFattoDiConversioneAndata(List.of(
                new FattoreDiConversione(foglia1_1_1, foglia1_1_2, 1.0),
                new FattoreDiConversione(foglia1_1_1, foglia3_1, 1.0)
        ));
        foglia3_1.setFattoDiConversioneAndata(List.of(
                new FattoreDiConversione(foglia3_1, foglia2, 1.0)
        ));

        Graph graph = new Graph(root);
        ConnectivityCheck connectivityCheck = new ConnectivityCheck(graph);
        Assertions.assertTrue(connectivityCheck.isConnected());
    }

    @Test
    @Order(1)
    @DisplayName("Test not connected")
    void TestNotConnected() {
        /*
         * Root
         *  - Foglia1
         *      - Foglia1.1
         *          - Foglia1.1.1
         *          - Foglia1.1.2
         *  - Foglia2
         *  - Foglia3
         *      - Foglia3.1
         */
        Nodo root = Nodo.createRoot("Root", null, "valore");

        Nodo foglia1 = Nodo.createFoglia("Foglia1", null, "valore", root);
        Nodo foglia2 = Nodo.createFoglia("Foglia2", null, "valore", root);
        Nodo foglia3 = Nodo.createFoglia("Foglia3", null, "valore", root);
        root.setFigli(new ArrayList<>(Arrays.asList(foglia1, foglia2, foglia3)));

        Nodo foglia1_1 = Nodo.createFoglia("Foglia1.1", null, "valore", foglia1);
        foglia1.setFigli(new ArrayList<>(Collections.singletonList(foglia1_1)));
        Nodo foglia3_1 = Nodo.createFoglia("Foglia3.1", null, "valore", foglia3);
        foglia3.setFigli(new ArrayList<>(Collections.singletonList(foglia3_1)));

        Nodo foglia1_1_1 = Nodo.createFoglia("Foglia1.1.1", null, "valore", foglia1_1);
        Nodo foglia1_1_2 = Nodo.createFoglia("Foglia1.1.2", null, "valore", foglia1_1);
        foglia1_1.setFigli(new ArrayList<>(Arrays.asList(foglia1_1_1, foglia1_1_2)));

        foglia1_1_1.setFattoDiConversioneAndata(List.of(
                new FattoreDiConversione(foglia1_1_1, foglia1_1_2, 1.0),
                new FattoreDiConversione(foglia1_1_1, foglia3_1, 1.0)
        ));

        Graph graph = new Graph(root);
        ConnectivityCheck connectivityCheck = new ConnectivityCheck(graph);
        Assertions.assertFalse(connectivityCheck.isConnected());
    }
}
