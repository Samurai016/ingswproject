package it.unibs.ingswproject.logic;

import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.repositories.FattoreDiConversioneRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe fornisce metodi per gestire i fattori di conversione.
 *
 * @author Nicolò Rebaioli
 */
public class FattoreDiConversioneHandler {
    protected StorageService storageService;
    
    public FattoreDiConversioneHandler(StorageService storageService) {
        this.storageService = storageService;
    }
    
    /**
     * Restituisce i fattori di conversione da aggiungere quando si aggiungono dei nodi figli
     * @param nodo Il nodo padre a cui si aggiungono i figli
     * @return I fattori di conversione da aggiungere (senza valore di conversione)
     */
    public List<FattoreDiConversione> getFattoriDiConversionToSet(Nodo nodo) {
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
        FattoreDiConversioneRepository fdcRepository = (FattoreDiConversioneRepository) this.storageService.getRepository(FattoreDiConversione.class);

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

    /**
     * Restituisce i fattori di conversione da rimuovere quando si rimuove un nodo oppure diventa foglia
     * @param nodo Il nodo padre a cui si aggiungono i figli
     * @return I fattori di conversione da rimuovere
     */
    public List<FattoreDiConversione> getFattoriDiConversionToDelete(Nodo nodo) {
        // I fattori di conversione e i nodi costituiscono un grafo
        // I fattore di conversione rappresentano gli archi e i nodi rappresentano i vertici
        // Quando si rimuovono dei nodi o essi non sono più foglie,
        // bisogna rimuovere tutti i fattori di conversione inc cui il nodo è coinvolto

        FattoreDiConversioneRepository fdcRepository = (FattoreDiConversioneRepository) this.storageService.getRepository(FattoreDiConversione.class);
        return fdcRepository.findByNodo(nodo);
    }
}
