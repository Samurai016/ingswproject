package it.unibs.ingswproject.translations;

/**
 * Interfaccia che definisce un traduttore
 *
 * @author Nicolò Rebaioli
 */
public interface Translator {
    /**
     * Traduce una chiave in una stringa
     *
     * @param key La chiave da tradurre
     * @return La stringa tradotta
     */
    String translate(String key);

    /**
     * Traduce una chiave in una stringa con argomenti
     *
     * @param key  La chiave da tradurre
     * @param args Gli argomenti da sostituire
     * @return La stringa tradotta
     */
    String translate(String key, Object... args);
}
