package it.unibs.ingswproject.logic.graph;

import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.*;

/**
 * Classe che rappresenta un grafo orientato ottenuto a partire da un albero delle gerarchie
 *
 * @author Nicolò Rebaioli
 */
public class Graph {
    private Collection<Nodo> vertici = new ArrayList<>();
    private Collection<FattoreDiConversione> archi = new ArrayList<>();

    /**
     * Costruttore del grafo
     *
     * @param root Il nodo radice del grafo
     */
    public Graph(Nodo root) {
        this.addVertici(root);
        this.addArchi(root);
    }

    public Graph (Collection<Nodo> vertici, Collection<FattoreDiConversione> archi){
        this.vertici = vertici;
        this.archi = archi;
    }

    private void addVertici(Nodo nodo) {
        // Se il nodo è una foglia, lo aggiungo ai vertici
        if (nodo.isFoglia()) {
            this.vertici.add(nodo);
        }
        for (Nodo figlio : nodo.getFigli()) {
            this.addVertici(figlio);
        }
    }

    private void addArchi(Nodo root) {
        this.archi.addAll(root.getFattoriDiConversioneAndata());
        for (Nodo figlio : root.getFigli()) {
            this.addArchi(figlio);
        }
    }

    public Collection<Nodo> getVertici() {
        return this.vertici;
    }

    public Collection<FattoreDiConversione> getArchi() {
        return this.archi;
    }

    /**
     * Restituisce la lista di adiacenza del grafo
     * La lista di adiacenza è una mappa che associa a ogni nodo la lista dei nodi adiacenti
     * con il relativo peso
     * es. {A: [(B, 1), (C, 2)], B: [(C, 3)], C: []}
     *     Dove A è adiacente a B con peso 1 e a C con peso 2, ecc...
     *
     * @return La lista di adiacenza del grafo
     */
    public Map<Nodo, List<AdjacencyListNode>> getAdjacencyList() {
        Map<Nodo, List<AdjacencyListNode>> adjacencyList = new HashMap<>();

        for (Nodo nodo : this.vertici) {
            adjacencyList.put(nodo, new LinkedList<>());
        }

        for (FattoreDiConversione arco : this.archi) {
            AdjacencyListNode andata = new AdjacencyListNode(arco.getNodo2(), arco.getFattore(arco.getNodo1()));
            if (!adjacencyList.get(arco.getNodo1()).contains(andata)) {
                adjacencyList.get(arco.getNodo1()).add(andata);
            }

            AdjacencyListNode ritorno = new AdjacencyListNode(arco.getNodo1(), arco.getFattore(arco.getNodo2()));
            if (!adjacencyList.get(arco.getNodo2()).contains(ritorno)) {
                adjacencyList.get(arco.getNodo2()).add(ritorno);
            }
        }

        return adjacencyList;
    }
}
