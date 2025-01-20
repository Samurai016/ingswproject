package it.unibs.ingswproject.test.logic;

import it.unibs.ingswproject.logic.BaseScambioStrategy;
import it.unibs.ingswproject.logic.routing.RoutingComputationStrategy;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.Scambio;
import org.junit.jupiter.api.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BaseScambioStrategyTest {
    private record FakeRoutingStrategy(Collection<Nodo> nodi) implements RoutingComputationStrategy {
        @Override
            public double getRoutingCost(Nodo nodo1, Nodo nodo2) {
                return 1;
            }

            @Override
            public HashMap<Nodo, Double> getRoutingCostsFrom(Nodo nodo1) {
                HashMap<Nodo, Double> result = new HashMap<>();
                for (Nodo nodo2 : this.nodi) {
                    if (!nodo1.equals(nodo2)) {
                        result.put(nodo2, this.getRoutingCost(nodo1, nodo2));
                    }
                }
                return result;
            }
        }

    private final BaseScambioStrategy baseScambioStrategy = new BaseScambioStrategy();
    private HashMap<Integer, Nodo> nodi = new HashMap<>();

    private static Nodo createFoglia(String nome) {
        return Nodo.createFoglia(nome, "", "", null);
    }

    private Scambio createScambio(int richiestaIndex, int offertaIndex, int quantitaRichiesta) {
        return new Scambio(
                null,
                this.nodi.get(richiestaIndex),
                this.nodi.get(offertaIndex),
                quantitaRichiesta,
                new FakeRoutingStrategy(this.nodi.values())
        );
    }

    @BeforeEach
    void setUp() {
        this.nodi = new HashMap<>();
        this.nodi.put(1, createFoglia("Ripetizioni di matematica per scuola superiore"));
        this.nodi.put(2, createFoglia("Lezioni di pianoforte per principianti"));
        this.nodi.put(3, createFoglia("Stiratura"));
    }

    @Test
    @Order(1)
    void testChiusuraImmediata() {
        List<Scambio> scambi = List.of(
                this.createScambio(2, 1, 10),
                this.createScambio(1, 2, 10),
                this.createScambio(3, 1, 15)
        );
        HashMap<Scambio, Collection<Scambio>> expected = new HashMap<>();
        expected.put(scambi.get(0), List.of(scambi.get(1)));
        expected.put(scambi.get(1), List.of(scambi.get(0)));

        HashMap<Scambio, Collection<Scambio>> result = this.baseScambioStrategy.findScambiChiudibili(scambi);
        Assertions.assertEquals(expected, result);
    }

    @Test
    @Order(1)
    void testChiusuraCombinata() {
        List<Scambio> scambi = List.of(
                this.createScambio(1, 2, 10),
                this.createScambio(2, 3, 10),
                this.createScambio(3, 1, 10)
        );
        HashMap<Scambio, Collection<Scambio>> expected = new HashMap<>();
        expected.put(scambi.get(0), List.of(scambi.get(1), scambi.get(2)));

        HashMap<Scambio, Collection<Scambio>> result = this.baseScambioStrategy.findScambiChiudibili(scambi);
        Assertions.assertEquals(expected, result);
    }
}
