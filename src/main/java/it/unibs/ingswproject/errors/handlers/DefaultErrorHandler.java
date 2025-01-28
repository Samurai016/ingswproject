package it.unibs.ingswproject.errors.handlers;

import it.unibs.ingswproject.errors.ErrorHandler;

@SuppressWarnings("CallToPrintStackTrace")
public class DefaultErrorHandler implements ErrorHandler {
    private final boolean printStackTrace;

    /**
     * Costruttore
     *
     * @param printStackTrace Se true, verrà stampato lo stack trace dell'errore
     */
    public DefaultErrorHandler(boolean printStackTrace) {
        this.printStackTrace = printStackTrace;
    }

    public void handle(Throwable e) {
        if (this.printStackTrace) {
            e.printStackTrace();
        } else {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
