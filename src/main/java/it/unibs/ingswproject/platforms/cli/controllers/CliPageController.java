package it.unibs.ingswproject.platforms.cli.controllers;

import it.unibs.ingswproject.controllers.PageController;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;

import java.util.HashMap;
import java.util.Map;

public abstract class CliPageController implements PageController {
    public static final char COMMAND_BACK = '0';
    protected final Map<Character, String> commands = new HashMap<>();
    protected final CliApp app;
    protected CliPageView view;
    protected final Translator translator;

    /**
     * Costruttore che istanza la pagina
     * Inserisce il comando 0 per tornare indietro
     *
     * @param app Applicazione che ha generato la pagina
     */
    protected CliPageController(CliApp app, Translator translator) {
        this.app = app;
        this.translator = translator;

        this.commands.put(COMMAND_BACK, this.translator.translate("command_back"));
    }

    public void render() {
        this.view.render();
    }

    public Map<Character, String> getCommands() {
        return this.commands;
    }

    /**
     * Metodo che restituisce il nome della pagina
     * Viene utilizzato per stampare i breadcrumb
     * @return Il nome della pagina
     */
    public abstract String getName();

    /**
     * Metodo che restituisce se l'utente può visualizzare la pagina
     * @return True se l'utente può visualizzare la pagina, false altrimenti
     */
    public abstract boolean canView();

    /**
     * Metodo che gestisce l'input dell'utente
     * @param input Il carattere inserito dall'utente
     */
    public void handleInput(char input) {
        // Override this method to handle input
        if (input == COMMAND_BACK) {
            this.app.goBack();
        }
    }
}
