package it.unibs.ingswproject.platforms.cli.views;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.controllers.PageController;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.utils.BreadcrumbsGenerator;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.utils.ProjectUtils;

import java.util.List;

/**
 * Classe astratta che rappresenta una pagina della CLI
 * Ogni pagina dell'applicazione è una sottoclasse di questa classe
 *
 * @author Nicolò Rebaioli
 */
public abstract class CliPageView {
    public static final String HEADER = """

              _____               __  __    __     ___           _           _  \s
              \\_   \\_ __   __ _  / _\\/ / /\\ \\ \\   / _ \\_ __ ___ (_) ___  ___| |_\s
               / /\\/ '_ \\ / _` | \\ \\ \\ \\/  \\/ /  / /_)/ '__/ _ \\| |/ _ \\/ __| __|
            /\\/ /_ | | | | (_| | _\\ \\ \\  /\\  /  / ___/| | | (_) | |  __/ (__| |_\s
            \\____/ |_| |_|\\__, | \\__/  \\/  \\/   \\/    |_|  \\___// |\\___|\\___|\\__|
                          |___/                               |__/         v%s\s
            """;
    protected final CliApp app;
    protected final CliPageController controller;
    protected final Translator translator;
    protected final CliUtils cliUtils;
    protected final ProjectUtils projectUtils;
    protected final AuthService authService;

    /**
     * Costruttore che istanza la pagina
     * Inserisce il comando 0 per tornare indietro
     *
     * @param app        Applicazione che ha generato la pagina
     * @param controller Controller della pagina
     * @param translator Traduttore per la lingua
     * @param cliUtils   Classe di utilità per la CLI
     */
    public CliPageView(CliApp app, CliPageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, AuthService authService) {
        this.app = app;
        this.controller = controller;
        this.translator = translator;
        this.cliUtils = cliUtils;
        this.projectUtils = projectUtils;
        this.authService = authService;
    }

    /**
     * Metodo che renderizza la pagina
     * Stampa i comandi disponibili e gestisce l'input
     */
    protected void renderContent() {
        // Ogni pagina è composta in questo modo:
        // 2. Stampa dei comandi
        // 3. Richiesta di input
        // 4. Gestione dell'input
        this.beforeRender();

        // 2. Stampa dei comandi
        this.printCommands();

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
     * Renderizza la pagina
     * Si occupa di visualizzare lo scaffold dell'applicazione e le autorizzazioni
     * La view fornisce il contenuto
     */
    public final void render() {
        // 1. Stampa del header, del nome utente e dei breadcrumb
        System.out.printf(HEADER, this.projectUtils.getProjectVersion());
        System.out.println();
        if (this.authService.isLoggedIn()) {
            String ruolo = this.translator.translate(this.authService.getCurrentUser().getRuolo().toString().toLowerCase());
            String userMessage = String.format(this.translator.translate("user_header_pattern"), this.authService.getCurrentUser().getUsername(), ruolo);
            System.out.println(userMessage);
        }

        List<PageController> history = this.app.getRouter().getHistory();
        BreadcrumbsGenerator breadcrumbsGenerator = new BreadcrumbsGenerator(history);
        System.out.println(breadcrumbsGenerator.getBreadcrumbsString());
        System.out.println();

        // 2.1 Controlla autorizzazione
        if (!this.controller.canView()) {
            System.out.println(this.translator.translate("unauthorized_view_access"));
            this.cliUtils.waitForInput();
            return; // Return to previous page
        }

        // 2.2 Stampa della pagina
        this.renderContent();
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

    /**
     * Metodo che stampa i comandi disponibili
     */
    protected void printCommands() {
        System.out.println(this.translator.translate("available_commands"));
        this.controller.getCommands().forEach((key, value) -> {
            String command = String.format(this.translator.translate("command_pattern"), key, value);
            System.out.println(command);
        });
    }
}
