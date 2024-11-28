package it.unibs.ingswproject.test.logic.utils;

import it.unibs.ingswproject.logic.weight.complete.Graph;
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
            Map.entry(1, createFakeFoglia("1"))
    );
    private static final List<FattoreDiConversione> archi = List.of(
            new FattoreDiConversione(vertici.get(0), vertici.get(1), 1.4)
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
