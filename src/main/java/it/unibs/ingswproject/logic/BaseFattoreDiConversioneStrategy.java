package it.unibs.ingswproject.logic;

import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.repositories.FattoreDiConversioneRepository;
import it.unibs.ingswproject.models.repositories.NodoRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe fornisce metodi per gestire i fattori di conversione.
 *
 * @author Nicolò Rebaioli
 */
public class BaseFattoreDiConversioneStrategy implements FattoreDiConversioneStrategy {
    protected final StorageService storageService;

    /**
     * Costruttore del gestore dei fattori di conversione.
     *
     * @param storageService Il servizio di storage
     */
    public BaseFattoreDiConversioneStrategy(StorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public List<FattoreDiConversione> getFattoriDiConversionToSet(Nodo nodo) {
        // I fattori di conversione e i nodi costituiscono un grafo
        // I fattore di conversione rappresentano gli archi e i nodi rappresentano i vertici
        // Quando si aggiungono dei nodi figli bisogna aggiungere tutti i fattori di conversione
        // I fattori di conversione da aggiungere sono quelli forniti dalla formula:
        // fdc = (archi tra le foglie aggiunte) + (archi_in_cui_parent_era_incluso) + (archi tra le foglie aggiunte e una foglia di un'altra gerarchia se è una nuova gerarchia)
        // Il numero di archi è dato da:
        // #nodi_to_add = (foglie_aggiunte - 1) + (n_archi_in_cui_parent_era_incluso) + (1 se è una nuova gerarchia e non è la prima gerarchia)

        // Se non ci sono figli non c'è nulla da fare
        if (nodo.getFigli().isEmpty()) {
            return new ArrayList<>();
        }

        FattoreDiConversioneRepository fdcRepository = (FattoreDiConversioneRepository) this.storageService.getRepository(FattoreDiConversione.class);
        List<Nodo> foglie = nodo.getFigli().stream().filter(Nodo::isFoglia).toList();
        ArrayList<FattoreDiConversione> fattoriDaAggiungere = new ArrayList<>();

        // Aggiungo i fattori di conversione tra le foglie aggiunte
        fattoriDaAggiungere.addAll(this.calcolaFattoriTraFoglie(foglie, fdcRepository));

        // Aggiungo i fattori di conversione in cui parent era incluso
        fattoriDaAggiungere.addAll(this.calcolaFattoriConParentIncluso(nodo, fdcRepository));

        // Se è una nuova gerarchia, e non è la prima gerarchia,
        // aggiungo un fattore di conversione tra una delle foglie e una foglia di un'altra gerarchia
        FattoreDiConversione fdcInterGerarchie = this.calcolaFattoriTraGerarchie(nodo, foglie);
        if (fdcInterGerarchie != null) {
            fattoriDaAggiungere.add(fdcInterGerarchie);
        }

        return fattoriDaAggiungere;
    }

    /**
     * Calcola i fattori di conversione tra le foglie aggiunte e una foglia di un'altra gerarchia.
     * Se è una nuova gerarchia, e non è la prima gerarchia, aggiungo un fattore di conversione tra una delle foglie e una foglia di un'altra gerarchia
     *
     * @param nodo Il nodo per cui calcolare i fattori di conversione
     * @param foglie Le foglie del nodo
     * @return Un fattore di conversione tra una foglia del nodo e una foglia di un'altra gerarchia, se esiste
     */
    private FattoreDiConversione calcolaFattoriTraGerarchie(Nodo nodo, List<Nodo> foglie) {
        NodoRepository nodoRepository = (NodoRepository) this.storageService.getRepository(Nodo.class);
        List<Nodo> gerarchie = nodoRepository.findGerarchie().stream().filter(n -> !n.equals(nodo)).toList();
        if (nodo.isRoot() && !gerarchie.isEmpty()) {
            Nodo altraGerarchiaConFoglie = gerarchie.stream().filter(g -> !g.getFoglie().isEmpty()).findFirst().orElse(null);
            if (altraGerarchiaConFoglie != null) {
                Nodo fogliaAltraGerarchia = altraGerarchiaConFoglie.getFigli().stream().filter(Nodo::isFoglia).findFirst().orElse(null);
                Nodo foglia = foglie.stream().findFirst().orElse(null);
                if (fogliaAltraGerarchia != null && foglia != null) {
                    return new FattoreDiConversione(foglia, fogliaAltraGerarchia);
                }
            }
        }

        return null; // Se non è una nuova gerarchia o non ci sono altre gerarchie con foglie, non aggiungo nulla
    }

    /**
     * Aggiungo i fattori di conversione in cui parent era incluso
     * Questi fdc vengono eliminati e devo rimpiazzarli con degli archi tra i destinatari dei vecchi archi e un qualunque figlio
     *
     * @param nodo Il nodo per cui calcolare i fattori di conversione
     * @param fdcRepository Il repository dei fattori di conversione
     * @return Una lista di fattori di conversione da aggiungere
     */
    private List<FattoreDiConversione> calcolaFattoriConParentIncluso(Nodo nodo, FattoreDiConversioneRepository fdcRepository) {
        ArrayList<FattoreDiConversione> fattoriDaAggiungere = new ArrayList<>();
        List<FattoreDiConversione> fdcInCuiParentEraIncluso = fdcRepository.findByNodo(nodo);
        Nodo primoFiglio = nodo.getFigli().getFirst();
        for (FattoreDiConversione fdc : fdcInCuiParentEraIncluso) {
            Nodo destinatario = fdc.getNodo1().equals(nodo) ? fdc.getNodo2() : fdc.getNodo1();
            fattoriDaAggiungere.add(new FattoreDiConversione(primoFiglio, destinatario));
        }
        return fattoriDaAggiungere;
    }

    /**
     * Calcolo i fattori di conversione tra le foglie aggiunte
     * Per ogni coppia di foglie aggiunte, aggiungo un fattore di conversione se non esiste già
     *
     * @param foglie Le foglie del nodo
     * @param fdcRepository Il repository dei fattori di conversione
     * @return Una lista di fattori di conversione da aggiungere
     */
    private List<FattoreDiConversione> calcolaFattoriTraFoglie(List<Nodo> foglie, FattoreDiConversioneRepository fdcRepository) {
        List<FattoreDiConversione> fattoriDaAggiungere = new ArrayList<>();
        for (int i = 0; i < foglie.size() - 1; i++) {
            Nodo nodo1 = foglie.get(i);
            Nodo nodo2 = foglie.get(i + 1);
            FattoreDiConversione fdc = fdcRepository.findByNodi(nodo1, nodo2);

            if (fdc != null) { // Se il fattore di conversione esiste già, non lo aggiungo
                continue;
            }

            fattoriDaAggiungere.add(new FattoreDiConversione(nodo1, nodo2));
        }
        return fattoriDaAggiungere;
    }

    @Override
    public List<FattoreDiConversione> getFattoriDiConversionToDelete(Nodo nodo) {
        // I fattori di conversione e i nodi costituiscono un grafo
        // I fattore di conversione rappresentano gli archi e i nodi rappresentano i vertici
        // Quando si rimuovono dei nodi o essi non sono più foglie,
        // bisogna rimuovere tutti i fattori di conversione inc cui il nodo è coinvolto

        FattoreDiConversioneRepository fdcRepository = (FattoreDiConversioneRepository) this.storageService.getRepository(FattoreDiConversione.class);
        return fdcRepository.findByNodo(nodo);
    }
}
