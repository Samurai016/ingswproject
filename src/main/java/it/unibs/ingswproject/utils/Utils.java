package it.unibs.ingswproject.utils;

import it.unibs.ingswproject.translations.Translator;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Classe di utilità
 *
 * @author Nicolò Rebaioli
 */
public abstract class Utils {
    /**
     * Restituisce il messaggio di errore di un'eccezione
     *
     * @param throwable Eccezione
     * @return Messaggio di errore
     */
    public static String getErrorMessage(Translator translator, Throwable throwable) {
        String message = throwable.getMessage();

        if (message == null) { // If message is null, try to get the cause message
            message = throwable.getCause().getMessage();
        }

        // If cause message is null, return the class name
        return translator.translate(message == null ? throwable.getClass().getName() : message);
    }

    /**
     * Capitalizza una stringa
     *
     * @param str Stringa da capitalizzare
     * @return Stringa capitalizzata
     */
    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * Appiattisce una lista di liste
     *
     * @param list Lista di liste
     * @return Lista appiattita
     */
    public static List<?> flatten(List<?> list) {
        return list.stream()
                .flatMap(item -> {
                    if (item instanceof List<?>) {
                        return flatten((List<?>) item).stream();
                    } else {
                        return Stream.of(item);
                    }
                })
                .collect(Collectors.toList());
    }
}
