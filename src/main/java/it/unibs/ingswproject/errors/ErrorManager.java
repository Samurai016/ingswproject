package it.unibs.ingswproject.errors;

import java.util.ArrayList;
import java.util.List;

public class ErrorManager {
    private final List<ErrorHandler> errorHandlers = new ArrayList<>();

    public void handle(Throwable e) {
        for (ErrorHandler errorHandler : this.errorHandlers) {
            errorHandler.handle(e);
        }
    }

    public void addErrorHandler(ErrorHandler errorHandler) {
        this.errorHandlers.add(errorHandler);
    }

    public void removeErrorHandler(ErrorHandler errorHandler) {
        this.errorHandlers.remove(errorHandler);
    }
}