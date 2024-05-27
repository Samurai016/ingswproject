package it.unibs.ingswproject.platforms.cli.elements;

import it.unibs.ingswproject.models.entities.Nodo;

/**
 * Classe astratta per il rendering di un albero
 *
 * @author Nicolò Rebaioli
 */
public abstract class TreeRenderer {
    protected Nodo root;

    /**
     * Setta la radice dell'albero
     *
     * @param root La radice dell'albero, non può essere null
     * @throws IllegalArgumentException Se la radice è null
     */
    public void setRoot(Nodo root) throws IllegalArgumentException {
        if (root == null) {
            throw new IllegalArgumentException("root_cannot_be_null");
        }
        this.root = root;
    }

    /**
     * Renderizza l'albero
     */
    public abstract void render();
}
