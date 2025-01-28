package it.unibs.ingswproject.logic.graph;

import java.util.LinkedList;

/**
 * @author Nicolò Rebaioli
 */
public record Path(LinkedList<Integer> vertices, double weight) {
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
