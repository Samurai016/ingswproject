package it.unibs.ingswproject.errors;

public abstract class ErrorManager {
    protected boolean debugModeEnabled = false;

    public ErrorManager setDebugModeEnabled(boolean enabled) {
        this.debugModeEnabled = enabled;
        return this;
    }

    public boolean isDebugModeEnabled() {
        return this.debugModeEnabled;
    }

    public abstract void handle(Throwable e);
}
