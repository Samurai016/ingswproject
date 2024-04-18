package it.unibs.ingswproject.errors;

/**
 * Classe astratta che rappresenta un gestore degli errori.
 * Si occupa di gestire un'eccezione lanciata da un'applicazione.
 *
 * @author Nicolò Rebaioli
 */
public abstract class ErrorManager {
    /**
     * Indica se il debug mode è abilitato o meno
     */
    protected boolean debugMode = false;

    /**
     * Imposta il debug mode
     * @param enabled True se abilitato, false altrimenti
     * @return L'istanza dell'oggetto (builder pattern)
     */
    public ErrorManager setDebugMode(boolean enabled) {
        this.debugMode = enabled;
        return this;
    }

    /**
     * Restituisce se il debug mode è abilitato
     * @return True se abilitato, false altrimenti
     */
    public boolean isDebugModeEnabled() {
        return this.debugMode;
    }

    /**
     * Gestisce l'eccezione lanciata dall'applicazione
     * @param e L'eccezione lanciata
     */
    public abstract void handle(Throwable e);
}
