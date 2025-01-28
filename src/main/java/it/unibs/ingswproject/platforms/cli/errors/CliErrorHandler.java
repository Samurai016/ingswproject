package it.unibs.ingswproject.platforms.cli.errors;

import it.unibs.ingswproject.errors.ErrorHandler;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;

/**
 * Classe che gestisce gli errori in modalità CLI
 * Visualizza a video l'errore e attende l'input dell'utente per continuare
 *
 * @author Nicolò Rebaioli
 */
public class CliErrorHandler implements ErrorHandler {
    protected final Translator translator;
    protected final CliUtils cliUtils;
    private boolean isDebugModeEnabled = false;

    public CliErrorHandler(Translator translator, CliUtils cliUtils) {
        this.translator = translator;
        this.cliUtils = cliUtils;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void handle(Throwable e) {
        if (this.isDebugModeEnabled) {
            e.printStackTrace();
        } else {
            System.out.println(this.translator.translate("error_pattern", Utils.getErrorMessage(this.translator, e)));
        }
        this.cliUtils.waitForInput();
    }

    public ErrorHandler setDebugMode(boolean isDebugModeEnabled) {
        this.isDebugModeEnabled = isDebugModeEnabled;
        return this;
    }
}
