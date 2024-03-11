package it.unibs.ingswproject.models.entities;

import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.repositories.FattoreDiConversioneRepository;
import jakarta.persistence.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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

    /**
     * Restituisce i fattori di conversione da aggiungere quando si aggiungono dei nodi figli
     * @param nodo Il nodo padre a cui si aggiungono i figli
     * @return I fattori di conversione da aggiungere (senza valore di conversione)
     */
    public static List<FattoreDiConversione> getFattoriDiConversionToSet(Nodo nodo) {
        // I fattori di conversione e i nodi costituiscono un grafo
        // I fattore di conversione rappresentano gli archi e i nodi rappresentano i vertici
        // Quando si aggiungono dei nodi figli bisogna aggiungere tutti i fattori di conversione
        // I fattori di conversione da aggiungere sono quelli forniti dalla formula:
        // fdc = (archi tra le foglie aggiunte) + (archi_in_cui_parent_era_incluso)
        // Il numero di archi è dato da:
        // #nodi_to_add = (foglie_aggiunte - 1) + (n_archi_in_cui_parent_era_incluso)

        // Se non ci sono figli non c'è nulla da fare
        if (nodo.getFigli().isEmpty()) {
            return new ArrayList<>();
        }

        ArrayList<FattoreDiConversione> fattoriDaAggiungere = new ArrayList<>();
        FattoreDiConversioneRepository fdcRepository = (FattoreDiConversioneRepository) StorageService.getInstance().getRepository(FattoreDiConversione.class);

        // Aggiungo i fattori di conversione tra le foglie aggiunte
        // Per ogni coppia di foglie aggiunte, aggiungo un fattore di conversione se non esiste già
        List<Nodo> foglie = nodo.getFigli().stream().filter(Nodo::isFoglia).toList();
        for (int i = 0; i < foglie.size() - 1;  i++) {
            Nodo nodo1 = foglie.get(i);
            Nodo nodo2 = foglie.get(i + 1);
            FattoreDiConversione fdc = fdcRepository.findByNodi(nodo1, nodo2);

            if (fdc != null) { // Se il fattore di conversione esiste già, non lo aggiungo
                continue;
            }

            fattoriDaAggiungere.add(new FattoreDiConversione(nodo1, nodo2));
        }

        // Aggiungo i fattori di conversione in cui parent era incluso
        // Questi fdc vengono eliminati e devo rimpiazzarli con degli archi tra i destinatari dei vecchi archi e un qualunque figlio
        List<FattoreDiConversione> fdcInCuiParentEraIncluso = fdcRepository.findByNodo(nodo);
        Nodo primoFiglio = nodo.getFigli().getFirst();
        for (FattoreDiConversione fdc : fdcInCuiParentEraIncluso) {
            Nodo destinatario = fdc.getNodo1().equals(nodo) ? fdc.getNodo2() : fdc.getNodo1();
            fattoriDaAggiungere.add(new FattoreDiConversione(primoFiglio, destinatario));
        }

        return fattoriDaAggiungere;
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

    public FattoreDiConversione setFattore(Double fattore) {
        if (fattore != null && (fattore < 0 || fattore > 2)) {
            throw new IllegalArgumentException("Il fattore di conversione deve essere compreso tra 0 e 2");
        }

        this.fattore = fattore;
        return this;
    }
}
