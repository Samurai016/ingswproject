package it.unibs.ingswproject.models.entities;

import jakarta.persistence.*;

@Entity
public class FattoreDiConversione {
    @Id
    private Nodo nodo1;
    @Id
    private Nodo nodo2;
    private double fattore;

    public FattoreDiConversione(Nodo nodo1, Nodo nodo2, double fattore) {
        this.setNodo1(nodo1)
            .setNodo2(nodo2)
            .setFattore(fattore);
    }

    // GETTERS
    public Nodo getNodo1() {
        return this.nodo1;
    }

    public Nodo getNodo2() {
        return this.nodo2;
    }

    public double getFattore() {
        return this.fattore;
    }

    // SETTERS
    public FattoreDiConversione setNodo1(Nodo nodo1) {
        if (!nodo1.isFoglia()) {
            throw new IllegalArgumentException("Il nodo 1 deve essere una foglia");
        }
        this.nodo1 = nodo1;
        return this;
    }

    public FattoreDiConversione setNodo2(Nodo nodo2) {
        if (!nodo2.isFoglia()) {
            throw new IllegalArgumentException("Il nodo 2 deve essere una foglia");
        }
        this.nodo2 = nodo2;
        return this;
    }

    public FattoreDiConversione setFattore(double fattore) {
        if (fattore < 0 || fattore > 2) {
            throw new IllegalArgumentException("Il fattore di conversione deve essere compreso tra 0 e 2");
        }

        this.fattore = fattore;
        return this;
    }
}
