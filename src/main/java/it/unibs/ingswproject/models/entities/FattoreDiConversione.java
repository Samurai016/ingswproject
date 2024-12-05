package it.unibs.ingswproject.models.entities;

import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
public class FattoreDiConversione {
    public static final double MIN_WEIGHT = 0.5;
    public static final double MAX_WEIGHT = 2.0;

    @Embeddable
    public static class FattoreDiConversioneId implements Serializable {
        @Serial
        private static final long serialVersionUID = -5978971331323711359L;

        @Column
        protected UUID nodo1;

        @Column
        protected UUID nodo2;

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
    protected FattoreDiConversioneId id;

    @MapsId("nodo1")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nodo1", nullable = false)
    protected Nodo nodo1;

    @MapsId("nodo2")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "nodo2", nullable = false)
    protected Nodo nodo2;

    protected Double fattore; // Double invece di double per poter essere null

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

    /**
     * Restituisce il fattore di conversione tra i due nodi
     *
     * @param startingNode Il nodo di partenza, indica la direzione del fattore
     * @return Il fattore di conversione se il nodo di partenza è nodo1, altrimenti 1/fattore
     */
    public Double getFattore(Nodo startingNode) {
        return startingNode == this.nodo1
                ? this.fattore
                : 1 / this.fattore;
    }

    // SETTERS
    public FattoreDiConversione setNodo1(Nodo nodo1) {
        if (nodo1 == null) {
            throw new IllegalArgumentException("fdc_nodo1_not_empty");
        }
        if (!nodo1.isFoglia()) {
            throw new IllegalArgumentException("fdc_nodo1_must_be_leaf");
        }
        this.nodo1 = nodo1;
        return this;
    }

    public FattoreDiConversione setNodo2(Nodo nodo2) {
        if (nodo2 == null) {
            throw new IllegalArgumentException("fdc_nodo2_not_empty");
        }
        if (!nodo2.isFoglia()) {
            throw new IllegalArgumentException("fdc_nodo2_must_be_leaf");
        }
        this.nodo2 = nodo2;
        return this;
    }

    public FattoreDiConversione setFattore(Double fattore) {
        if (fattore != null && (fattore < MIN_WEIGHT || fattore > MAX_WEIGHT)) {
            throw new IllegalArgumentException("fdc_fattore_range");
        }

        this.fattore = fattore;
        return this;
    }
}
