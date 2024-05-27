package it.unibs.ingswproject.test.logic.utils;

import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/*
6 10
0 1 12
0 2 5
0 5 5
1 0 8
1 4 7
2 0 20
2 3 10
3 2 10
4 1 14
5 0 20
 */
public abstract class GraphUtils {
    private static final Map<Integer, Nodo> vertici = Map.ofEntries(
            Map.entry(0, createFakeFoglia("0")),
            Map.entry(1, createFakeFoglia("1")),
            Map.entry(2, createFakeFoglia("2")),
            Map.entry(3, createFakeFoglia("3")),
            Map.entry(4, createFakeFoglia("4")),
            Map.entry(5, createFakeFoglia("5"))
    );
    private static final List<FattoreDiConversione> archi = List.of(
            new FattoreDiConversione(vertici.get(0), vertici.get(1), 1.2),
            new FattoreDiConversione(vertici.get(0), vertici.get(2), 0.5),
            new FattoreDiConversione(vertici.get(0), vertici.get(5), 0.5),
            new FattoreDiConversione(vertici.get(1), vertici.get(0), 1 / 1.2),
            new FattoreDiConversione(vertici.get(1), vertici.get(4), 1 / 1.4),
            new FattoreDiConversione(vertici.get(2), vertici.get(0), 2.0),
            new FattoreDiConversione(vertici.get(2), vertici.get(3), 1.0),
            new FattoreDiConversione(vertici.get(3), vertici.get(2), 1.0),
            new FattoreDiConversione(vertici.get(4), vertici.get(1), 1.4),
            new FattoreDiConversione(vertici.get(5), vertici.get(0), 2.0)
    );

    public static Graph getGraph() {
        return new Graph(vertici.values(), archi);
    }

    public static Map<Integer, Nodo> getVertici() {
        return vertici;
    }

    public static List<FattoreDiConversione> getArchi() {
        return archi;
    }

    public static Nodo createFakeFoglia(String nome) {
        return Nodo
                .createFoglia(nome, null, null, null)
                .setId(UUID.randomUUID());
    }
}
