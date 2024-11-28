package it.unibs.ingswproject.logic.weight.complete;

import it.unibs.ingswproject.models.entities.Nodo;

// Graph is represented using adjacency list. Every
// node of adjacency list contains vertex number of
// the vertex to which edge connects. It also
// contains weight of the edge

/**
 * Record che rappresenta un nodo adiacente.
 * Ogni nodo della lista di adiacenza contiene il vertice a cui è connesso e il peso dell'arco.
 *
 * @param vertex vertice a cui è connesso
 * @param weight peso dell'arco
 *
 * @author Nicolò Rebaioli
 */
public record AdjacencyListNode(Nodo vertex, double weight) {
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }
        AdjacencyListNode that = (AdjacencyListNode) obj;
        return Double.compare(that.weight, this.weight) == 0 && this.vertex.equals(that.vertex);
    }
}
