package it.unibs.ingswproject.models.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
public class FattoreDiConversione {
    @Embeddable
    public static class FattoreDiConversioneId implements Serializable {
        @Serial
        private static final long serialVersionUID = -5978971331323711359L;

        @Column
        private UUID nodo1;

        @Column
        private UUID nodo2;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || this.getClass() != o.getClass()) return false;
            FattoreDiConversioneId entity = (FattoreDiConversioneId) o;
            return Objects.equals(this.nodo2, entity.nodo2) &&
                   Objects.equals(this.nodo1, entity.nodo1);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.nodo2, this.nodo1);
        }
    }

    @EmbeddedId
    private FattoreDiConversioneId id;

    @MapsId("nodo1")
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "nodo1", nullable = false)
    private Nodo nodo1;

    @MapsId("nodo2")
    @ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "nodo2", nullable = false)
    private Nodo nodo2;

    private Double fattore; // Double invece di double per poter essere null

    public FattoreDiConversione() {
        // Costruttore vuoto per Ebean
    }
    public FattoreDiConversione(Nodo nodo1, Nodo nodo2, Double fattore) {
        this();
        this.setNodo1(nodo1)
            .setNodo2(nodo2)
            .setFattore(fattore);
    }
    public FattoreDiConversione(Nodo nodo1, Nodo nodo2) {
        this(nodo1, nodo2, null);
    }

    // GETTERS
    public Nodo getNodo1() {
        return this.nodo1;
    }

    public Nodo getNodo2() {
        return this.nodo2;
    }

    public Double getFattore() {
        return this.fattore;
    }

    // SETTERS
    public FattoreDiConversione setNodo1(Nodo nodo1) {
        if (nodo1 == null) {
            throw new IllegalArgumentException("Il nodo 1 non può essere nullo");
        }
        if (!nodo1.isFoglia()) {
            throw new IllegalArgumentException("Il nodo 1 deve essere una foglia");
        }
        this.nodo1 = nodo1;
        return this;
    }

    public FattoreDiConversione setNodo2(Nodo nodo2) {
        if (nodo2 == null) {
            throw new IllegalArgumentException("Il nodo 2 non può essere nullo");
        }
        if (!nodo2.isFoglia()) {
            throw new IllegalArgumentException("Il nodo 2 deve essere una foglia");
        }
        this.nodo2 = nodo2;
        return this;
    }

    public FattoreDiConversione setFattore(Double fattore) {
        if (fattore != null && (fattore < 0 || fattore > 2)) {
            throw new IllegalArgumentException("Il fattore di conversione deve essere compreso tra 0 e 2");
        }

        this.fattore = fattore;
        return this;
    }
}
