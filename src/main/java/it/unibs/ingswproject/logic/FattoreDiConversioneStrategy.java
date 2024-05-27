package it.unibs.ingswproject.logic;

import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;

import java.util.List;

/**
 * Interfaccia per la strategia di calcolo dei fattori di conversione
 *
 * @author Nicolò Rebaioli
 */
public interface FattoreDiConversioneStrategy {
    /**
     * Restituisce i fattori di conversione da aggiungere quando si aggiungono dei nodi figli
     *
     * @param nodo Il nodo padre a cui si aggiungono i figli
     * @return I fattori di conversione da aggiungere (senza valore di conversione)
     */
    List<FattoreDiConversione> getFattoriDiConversionToSet(Nodo nodo);

    /**
     * Restituisce i fattori di conversione da rimuovere quando si rimuove un nodo oppure diventa foglia
     *
     * @param nodo Il nodo padre a cui si aggiungono i figli
     * @return I fattori di conversione da rimuovere
     */
    List<FattoreDiConversione> getFattoriDiConversionToDelete(Nodo nodo);

    /**
     * Verifica se la gerarchia di un nodo è valida
     *
     * @param nodo La root della gerarchia da verificare
     * @return true se la gerarchia è valida, false altrimenti
     */
    boolean isGerarchiaValida(Nodo nodo);

    /**
     * Calcola il peso di un cammino tra 2 archi
     *
     * @param prevDistance La distanza del nodo precedente
     * @param nextDistance La distanza del nodo successivo
     * @return Il peso del cammino
     */
    double getWeight(double prevDistance, double nextDistance);
}
