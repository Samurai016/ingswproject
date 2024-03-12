package it.unibs.ingswproject.utils;

/**
 * Classe di utilità
 * @author Nicolò Rebaioli
 */
public abstract class Utils {
    /**
     * Restituisce il messaggio di errore di un'eccezione
     * @param throwable Eccezione
     * @return Messaggio di errore
     */
    public static String getErrorMessage(Throwable throwable) {
        String message = throwable.getMessage();

        if (message == null) { // If message is null, try to get the cause message
            message = throwable.getCause().getMessage();
        }

        // If cause message is null, return the class name
        return message==null ? throwable.getClass().getName() : message;
    }
}
