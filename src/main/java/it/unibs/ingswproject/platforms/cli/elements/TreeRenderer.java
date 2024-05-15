package it.unibs.ingswproject.platforms.cli.elements;

import it.unibs.ingswproject.models.entities.Nodo;

public abstract class TreeRenderer {
    protected Nodo root;

    public void setRoot(Nodo root) {
        if (root == null) {
            throw new IllegalArgumentException("root_cannot_be_null");
        }
        this.root = root;
    }

    public abstract void render();
}
