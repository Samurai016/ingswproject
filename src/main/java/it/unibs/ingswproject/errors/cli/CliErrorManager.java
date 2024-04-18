package it.unibs.ingswproject.errors.cli;

import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.utils.cli.CliUtils;

@SuppressWarnings("CallToPrintStackTrace")
public class CliErrorManager extends ErrorManager {
    protected Translator translator;
    protected CliUtils cliUtils;

    public CliErrorManager(Translator translator, CliUtils cliUtils) {
        this.translator = translator;
        this.cliUtils = cliUtils;
    }

    public void handle(Throwable e) {
        if (this.isDebugModeEnabled()) {
            e.printStackTrace();
        } else {
            System.out.printf((this.translator.translate("error_pattern")) + "\n", Utils.getErrorMessage(e));
        }
        this.cliUtils.waitForInput();
    }
}
