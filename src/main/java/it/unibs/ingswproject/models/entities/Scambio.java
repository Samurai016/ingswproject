package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.DbEnumValue;
import it.unibs.ingswproject.logic.routing.RoutingComputationStrategy;
import jakarta.persistence.*;

import java.util.UUID;

/**
 * @author Nicolò Rebaioli
 */
@Entity
public class Scambio {
    @Id
    protected UUID id;
    @ManyToOne
    protected Utente autore;
    @ManyToOne
    protected Nodo richiesta;
    @ManyToOne
    protected Nodo offerta;
    protected int quantitaRichiesta;
    protected int quantitaOfferta;
    @Enumerated(EnumType.STRING)
    protected Stato stato;

    public Scambio() {
        // Costruttore vuoto per Ebean
        this.id = UUID.randomUUID();
    }

    public Scambio(Utente autore, Nodo richiesta, Nodo offerta, int quantitaRichiesta, RoutingComputationStrategy routingComputationStrategy) {
        this();
        this
            .setRichiesta(richiesta)
            .setOfferta(offerta)
            .setQuantitaRichiesta(quantitaRichiesta, routingComputationStrategy)
            .setStato(Stato.APERTO);
    }

    // GETTERS
    public UUID getId() {
        return this.id;
    }

    public Utente getAutore() {
        return this.autore;
    }

    // SETTERS
    public Nodo getRichiesta() {
        return this.richiesta;
    }

    public Scambio setRichiesta(Nodo richiesta) {
        this.richiesta = richiesta;
        return this;
    }

    public Nodo getOfferta() {
        return this.offerta;
    }

    public Scambio setOfferta(Nodo offerta) {
        this.offerta = offerta;
        return this;
    }

    public int getQuantitaRichiesta() {
        return this.quantitaRichiesta;
    }

    public Scambio setQuantitaRichiesta(int quantitaRichiesta, RoutingComputationStrategy routingComputationStrategy) {
        if (quantitaRichiesta <= 0) {
            throw new IllegalArgumentException("scambio_quantita_not_negative");
        }

        this.quantitaRichiesta = quantitaRichiesta;

        // Imposto automaticamente la quantità offerta in base alla quantità richiesta
        double fattore = routingComputationStrategy.getRoutingCost(this.richiesta, this.offerta);
        this.quantitaOfferta = (int) Math.round(this.quantitaRichiesta * fattore);

        return this;
    }

    public int getQuantitaOfferta() {
        return this.quantitaOfferta;
    }

    public Stato getStato() {
        return this.stato;
    }

    public Scambio setStato(Stato stato) {
        this.stato = stato;
        return this;
    }

    public enum Stato {
        APERTO, CHIUSO;

        @DbEnumValue
        public String getDbValue() {
            return this.name().toLowerCase();
        }
    }
}
