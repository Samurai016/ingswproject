package it.unibs.ingswproject.logic.graph.algorithms;

import it.unibs.ingswproject.logic.graph.AdjacencyListNode;
import it.unibs.ingswproject.logic.graph.Graph;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.*;

/**
 * Classe che implementa l'algoritmo di Chu-Liu-Edmonds per trovare l'arborescenza
 * di un grafo orientato.
 *
 * @see <a href="https://stackoverflow.com/questions/23988236/chu-liu-edmonds-algorithm-for-minimum-spanning-tree-on-directed-graphs">Chu-Liu-Edmonds Algorithm (Stack Overflow)</a>
 * @see <a href="https://en.wikipedia.org/wiki/Arborescence_(graph_theory)">Arborescenza (Wikipedia)</a>
 *
 * @author Nicolò Rebaioli
 */
public class ChuLiuEdmondsAlgorithm {
    public record Arc(Nodo nodo1, Nodo nodo2, double weight) {}

    private Map<Nodo, Arc> minSpanningArborescence(List<Arc> arcs, Nodo sink) {
        List<Arc> goodArcs = new ArrayList<>();
        Map<Nodo, Nodo> quotientMap = new HashMap<>();
        for (Arc arc : arcs) {
            quotientMap.put(arc.nodo2(), arc.nodo2());
        }
        quotientMap.put(sink, sink);

        while (true) {
            Map<Nodo, Arc> minArcByTailRep = new HashMap<>();
            Map<Nodo, Nodo> successorRep = new HashMap<>();
            for (Arc arc : arcs) {
                if (arc.nodo2() == sink) {
                    continue;
                }
                Nodo tailRep = quotientMap.get(arc.nodo2());
                Nodo headRep = quotientMap.get(arc.nodo1());
                if (tailRep == headRep) {
                    continue;
                }
                if (!minArcByTailRep.containsKey(tailRep) ||
                    minArcByTailRep.get(tailRep).weight() > arc.weight()
                ) {
                    minArcByTailRep.put(tailRep, arc);
                    successorRep.put(tailRep, headRep);
                }
            }
            List<Nodo> cycleReps = this.findCycle(successorRep, sink);
            if (cycleReps == null) {
                goodArcs.addAll(minArcByTailRep.values());
                return this.spanningArborescence(goodArcs, sink);
            }
            for (Nodo cycleRep : cycleReps) {
                goodArcs.add(minArcByTailRep.get(cycleRep));
            }
            Set<Nodo> cycleRepSet = new HashSet<>(cycleReps);
            Nodo cycleRep = cycleRepSet.iterator().next();
            for (Map.Entry<Nodo, Nodo> entry : quotientMap.entrySet()) {
                Nodo node = entry.getKey();
                Nodo nodeRep = entry.getValue();
                if (cycleRepSet.contains(nodeRep)) {
                    quotientMap.put(node, cycleRep);
                }
            }
        }
    }

    private List<Nodo> findCycle(Map<Nodo, Nodo> successor, Nodo sink) {
        Set<Nodo> visited = new HashSet<>();
        visited.add(sink);
        for (Nodo node : successor.keySet()) {
            List<Nodo> cycle = new ArrayList<>();
            while (!visited.contains(node)) {
                visited.add(node);
                cycle.add(node);
                node = successor.get(node);
            }
            if (cycle.contains(node)) {
                return cycle.subList(cycle.indexOf(node), cycle.size());
            }
        }
        return null;
    }

    private Map<Nodo, Arc> spanningArborescence(List<Arc> arcs, Nodo sink) {
        Map<Nodo, List<Arc>> arcsByHead = new HashMap<>();
        for (Arc arc : arcs) {
            if (arc.nodo2() == sink) {
                continue;
            }
            arcsByHead.computeIfAbsent(arc.nodo1(), k -> new ArrayList<>()).add(arc);
        }
        Map<Nodo, Arc> solutionArcByTail = new HashMap<>();
        Stack<Arc> stack = new Stack<>();
        stack.addAll(arcsByHead.getOrDefault(sink, new ArrayList<>()));
        while (!stack.isEmpty()) {
            Arc arc = stack.pop();
            if (solutionArcByTail.containsKey(arc.nodo2())) {
                continue;
            }
            solutionArcByTail.put(arc.nodo2(), arc);
            stack.addAll(arcsByHead.getOrDefault(arc.nodo2(), new ArrayList<>()));
        }
        return solutionArcByTail;
    }

    private Map<Nodo, FattoreDiConversione> minSpanningArborescence(Graph graph, Nodo startingNode) {
        List<Arc> arcs = new ArrayList<>();
        for (FattoreDiConversione edge : graph.getArchi()) {
            arcs.add(new Arc(edge.getNodo1(), edge.getNodo2(), edge.getFattore(edge.getNodo1())));
            arcs.add(new Arc(edge.getNodo2(), edge.getNodo1(), edge.getFattore(edge.getNodo2())));
        }

        Map<Nodo, Arc> result = this.minSpanningArborescence(arcs, startingNode);

        Map<Nodo, FattoreDiConversione> convertedResult = new HashMap<>();
        for (Map.Entry<Nodo, Arc> entry : result.entrySet()) {
            Nodo node = entry.getKey();
            Arc arc = entry.getValue();
            convertedResult.put(node, new FattoreDiConversione(arc.nodo1(), arc.nodo2(), arc.weight()));
        }

        return convertedResult;
    }

    public Map<Nodo, List<AdjacencyListNode>> run(Graph graph, Nodo startingNode) {
        Map<Nodo, FattoreDiConversione> fattori = this.minSpanningArborescence(graph, startingNode);
        Map<Nodo, List<AdjacencyListNode>> adjacencyList = new HashMap<>();

        // Aggiungo archi uscenti da startingNode
        graph.getArchi()
                .stream()
                .filter(arco -> arco.getNodo1().equals(startingNode))
                .forEach(arco -> {
                    fattori.put(startingNode, arco);
                });

        for (Nodo nodo : fattori.keySet()) {
            adjacencyList.put(nodo, new LinkedList<>());
        }

        for (FattoreDiConversione arco : fattori.values()) {
            AdjacencyListNode adjNode = new AdjacencyListNode(arco.getNodo2(), arco.getFattore(arco.getNodo1()));
            if (!adjacencyList.containsKey(arco.getNodo1())) {
                adjacencyList.put(arco.getNodo1(), new LinkedList<>());
            }
            adjacencyList.get(arco.getNodo1()).add(adjNode);
        }

        return adjacencyList;
    }
}
