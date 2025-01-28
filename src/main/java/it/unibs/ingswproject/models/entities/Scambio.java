package it.unibs.ingswproject.models.entities;

import io.ebean.annotation.ConstraintMode;
import io.ebean.annotation.DbForeignKey;
import io.ebean.annotation.WhenCreated;
import it.unibs.ingswproject.logic.routing.RoutingComputationStrategy;
import jakarta.persistence.*;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

/**
 * @author Nicolò Rebaioli
 */
@SuppressWarnings("CanBeFinal")
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
    @WhenCreated
    protected DateTime dataCreazione = DateTime.now();
    @ManyToOne()
    @DbForeignKey(onDelete = ConstraintMode.SET_NULL)
    protected Scambio chiusoDa;
    @OneToMany(mappedBy = "chiusoDa")
    protected List<Scambio> haChiuso = new ArrayList<>();
    protected boolean hasBeenNotified = false;
    @OneToMany(mappedBy = "scambio")
    protected List<StoricoScambio> storico = new ArrayList<>();

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

    public Nodo getRichiesta() {
        return this.richiesta;
    }

    public boolean hasBeenNotified() {
        return this.hasBeenNotified;
    }

    public Scambio getChiusoDa() {
        return this.chiusoDa;
    }

    public List<Scambio> getHaChiuso() {
        return this.haChiuso;
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

    public List<StoricoScambio> getStorico() {
        return this.storico
                .stream()
                .sorted(Comparator.comparing(StoricoScambio::getData))
                .toList();
    }

    public Scambio chiudi(Scambio chiusoDa) {
        this.stato = Stato.CHIUSO;
        this.chiusoDa = chiusoDa;
        return this;
    }

    public Scambio notifica() {
        if (!this.stato.equals(Stato.CHIUSO)) {
            throw new IllegalStateException("scambio_not_closed_error");
        }
        this.hasBeenNotified = true;
        return this;
    }

    public Scambio ritira() {
        this.stato = Stato.RITIRATO;
        return this;
    }

    @Override
    public String toString() {
        return String.format(
                "r: %s [%d] -> o: %s [%d]",
                this.getRichiesta().getNome(),
                this.getQuantitaRichiesta(),
                this.getOfferta().getNome(),
                this.getQuantitaOfferta()
        );
    }

    /**
     * @author Nicolò Rebaioli
     */
    public enum Stato {
        APERTO,
        CHIUSO,
        RITIRATO
    }
}
