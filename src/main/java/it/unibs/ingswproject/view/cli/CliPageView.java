package it.unibs.ingswproject.view.cli;

import it.unibs.ingswproject.controllers.cli.CliPageController;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.cli.CliUtils;

/**
 * Classe astratta che rappresenta una pagina della CLI
 * Ogni pagina dell'applicazione è una sottoclasse di questa classe
 *
 * @author Nicolò Rebaioli
 */
public abstract class CliPageView {
    protected CliApp app;
    protected CliPageController controller;
    protected Translator translator;
    protected CliUtils cliUtils;

    /**
     * Costruttore che istanza la pagina
     * Inserisce il comando 0 per tornare indietro
     *
     * @param app        Applicazione che ha generato la pagina
     * @param controller Controller della pagina
     * @param translator Traduttore per la lingua
     * @param cliUtils   Classe di utilità per la CLI
     */
    public CliPageView(CliApp app, CliPageController controller, Translator translator, CliUtils cliUtils) {
        this.app = app;
        this.controller = controller;
        this.translator = translator;
        this.cliUtils = cliUtils;
    }

    /**
     * Metodo che renderizza la pagina
     * Stampa i comandi disponibili e gestisce l'input
     */
    public void render() {
        // Ogni pagina è composta in questo modo:
        // 2. Stampa dei comandi
        // 3. Richiesta di input
        // 4. Gestione dell'input

        this.beforeRender();

        // 2. Stampa dei comandi
        System.out.println(this.translator.translate("available_commands"));
        this.controller.getCommands().forEach((key, value) -> {
            String command = String.format(this.translator.translate("command_pattern"), key, value);
            System.out.println(command);
        });

        this.afterRender();

        // 3. Richiesta di input
        System.out.println();
        String input;
        boolean isValidInput = false;
        do {
            input = this.cliUtils.readFromConsole(this.translator.translate("insert_command"), false);
            if (input.length() > 1) {
                System.out.println(this.translator.translate("invalid_input"));
            } else if (!this.controller.getCommands().containsKey(input.charAt(0))) {
                System.out.println(this.translator.translate("invalid_command"));
            } else {
                isValidInput = true;
            }
        } while (!isValidInput);

        // 4. Gestione dell'input
        this.controller.handleInput(input.charAt(0));
    }

    /**
     * Metodo che esegue del codice prima di renderizzare la pagina
     * Il comportamento di default è vuoto
     */
    protected void beforeRender() {
        // Override this method to execute code before rendering the page
    }

    /**
     * Metodo che esegue del codice dopo aver renderizzato i comandi disponibili
     * Il comportamento di default è vuoto
     */
    protected void afterRender() {
        // Override this method to execute code before rendering the page
    }
}
