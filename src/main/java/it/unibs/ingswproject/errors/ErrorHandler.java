package it.unibs.ingswproject.errors;

/**
 * Classe astratta che rappresenta un gestore degli errori.
 * Si occupa di gestire un'eccezione lanciata da un'applicazione.
 *
 * @author Nicolò Rebaioli
 */
public interface ErrorHandler {
    /**
     * Gestisce l'eccezione lanciata dall'applicazione
     * @param e L'eccezione lanciata
     */
    void handle(Throwable e);
}
