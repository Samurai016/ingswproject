package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.ConstraintMode;
import io.ebean.annotation.DbForeignKey;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Nicolò Rebaioli
 */
@Entity
public class StoricoScambio {
    @EmbeddedId
    protected StoricoScambioId id;

    @MapsId("scambio")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "scambio", nullable = false)
    @DbForeignKey(onDelete = ConstraintMode.CASCADE)
    protected Scambio scambio;

    @MapsId("data")
    @JoinColumn(name = "data", nullable = false)
    protected Date data;

    protected Scambio.Stato stato;

    public StoricoScambio() {
        // Costruttore vuoto per Ebean
    }

    public StoricoScambio(Scambio scambio, Date data, Scambio.Stato stato) {
        this();
        this.setScambio(scambio)
                .setData(data)
                .setStato(stato);
    }

    public StoricoScambio(Scambio scambio, Date data) {
        this(scambio, data, scambio.getStato());
    }

    public StoricoScambio(Scambio scambio) {
        this(scambio, new Date());
    }

    public Scambio getScambio() {
        return this.scambio;
    }

    public StoricoScambio setScambio(Scambio scambio) {
        this.scambio = scambio;
        return this;
    }

    public StoricoScambioId getId() {
        return this.id;
    }

    public StoricoScambio setId(StoricoScambioId id) {
        this.id = id;
        return this;
    }

    public Date getData() {
        return this.data;
    }

    public StoricoScambio setData(Date data) {
        this.data = data;
        return this;
    }

    public Scambio.Stato getStato() {
        return this.stato;
    }

    public StoricoScambio setStato(Scambio.Stato stato) {
        this.stato = stato;
        return this;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s -> %s", this.data, this.scambio, this.stato);
    }

    @Embeddable
    public static class StoricoScambioId implements Serializable {
        @Serial
        private static final long serialVersionUID = -5687971331323711359L;

        @Column
        protected UUID scambio;

        @Column
        protected Date data;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }
            StoricoScambioId entity = (StoricoScambioId) o;
            return Objects.equals(this.scambio, entity.scambio) &&
                   Objects.equals(this.data, entity.data);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.scambio, this.data);
        }
    }
}
