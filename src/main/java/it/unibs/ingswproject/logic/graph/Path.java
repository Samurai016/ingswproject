package it.unibs.ingswproject.logic.graph;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class Path {
    private final LinkedList<Integer> vertices;
    private final double weight;

    public Path(LinkedList<Integer> vertices, double weight) {
        this.vertices = vertices;
        this.weight = weight;
    }

    public LinkedList<Integer> getVertices() {
        return this.vertices;
    }

    public double getWeight() {
        return this.weight;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < this.vertices.size(); i++) {
            builder.append(this.vertices.get(i));
            if (i < this.vertices.size() - 1) {
                builder.append(" -> ");
            }
        }
        builder.append(" (weight: ").append(this.weight).append(")");
        return builder.toString();
    }
}
