package it.unibs.ingswproject.errors.handlers;

import it.unibs.ingswproject.errors.ErrorHandler;

@SuppressWarnings("CallToPrintStackTrace")
public class StackTraceErrorHandler implements ErrorHandler {
    public void handle(Throwable e) {
        e.printStackTrace();
    }
}
