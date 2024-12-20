package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.DbEnumValue;
import it.unibs.ingswproject.logic.routing.RoutingComputationStrategy;
import jakarta.persistence.*;
import org.joda.time.DateTime;

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
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    protected DateTime dataCreazione = DateTime.now();
    protected DateTime dataChiusura;

    public Scambio() {
        // Costruttore vuoto per Ebean
        this.id = UUID.randomUUID();
        this.stato = Stato.APERTO;
    }

    public Scambio(Utente autore) {
        this();
        this.autore = autore;
    }

    public Scambio(Utente autore, Nodo richiesta, Nodo offerta, int quantitaRichiesta, RoutingComputationStrategy routingComputationStrategy) {
        this(autore);
        this
                .setRichiesta(richiesta)
                .setOfferta(offerta)
                .setQuantitaRichiesta(quantitaRichiesta, routingComputationStrategy);
    }

    // GETTERS
    public UUID getId() {
        return this.id;
    }

    public Utente getAutore() {
        return this.autore;
    }

    public DateTime getDataCreazione() {
        return this.dataCreazione;
    }

    public DateTime getDataChiusura() {
        return this.dataChiusura;
    }

    public Nodo getRichiesta() {
        return this.richiesta;
    }

    // SETTERS
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
    
    public int getQuantitaOfferta() {
        return this.quantitaOfferta;
    }

    public Stato getStato() {
        return this.stato;
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

    public Scambio chiudi() {
        this.stato = Stato.CHIUSO;
        this.dataChiusura = DateTime.now();
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
