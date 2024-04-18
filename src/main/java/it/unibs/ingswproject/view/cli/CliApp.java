package it.unibs.ingswproject.view.cli;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.controllers.cli.CliPageController;
import it.unibs.ingswproject.controllers.cli.pages.LoginPageController;
import it.unibs.ingswproject.errors.cli.CliErrorManager;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.cli.CliUtils;
import it.unibs.ingswproject.view.AppInterface;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.router.cli.CliRouter;

/**
 * Classe principale dell'applicazione CLI
 * Contiene la logica di navigazione tra le pagine
 *
 * @author Nicolò Rebaioli
 */
public class CliApp implements AppInterface {
    public static final String HEADER = """

              _____               __  __    __     ___           _           _  \s
              \\_   \\_ __   __ _  / _\\/ / /\\ \\ \\   / _ \\_ __ ___ (_) ___  ___| |_\s
               / /\\/ '_ \\ / _` | \\ \\ \\ \\/  \\/ /  / /_)/ '__/ _ \\| |/ _ \\/ __| __|
            /\\/ /_ | | | | (_| | _\\ \\ \\  /\\  /  / ___/| | | (_) | |  __/ (__| |_\s
            \\____/ |_| |_|\\__, | \\__/  \\/  \\/   \\/    |_|  \\___// |\\___|\\___|\\__|
                          |___/                               |__/              \s
            """;
    public static final String BREADCRUMB_SEPARATOR = " > ";

    protected final CliRouter router;
    protected final PageFactory pageFactory;
    protected final AuthService authService;
    protected final CliUtils cliUtils;
    protected final Translator translator;
    protected final CliErrorManager errorManager;

    public CliApp(CliRouter router, PageFactory pageFactory, AuthService authService, CliUtils cliUtils, Translator translator, CliErrorManager errorManager) {
        this.router = router;
        this.pageFactory = pageFactory;
        this.authService = authService;
        this.cliUtils = cliUtils;
        this.translator = translator;
        this.errorManager = errorManager;
    }

    /**
     * Avvia l'applicazione
     */
    public void run() {
        try {
            CliPageController page = this.pageFactory.generatePage(LoginPageController.class);
            this.router.navigateTo(page);
            this.renderPage(page);
        } catch (Throwable e) {
            this.errorManager.handle(e);
        }
    }

    /**
     * Naviga verso una pagina e la renderizza
     * Influenza la history
     * @param page Pagina da renderizzare
     */
    public void navigateTo(CliPageController page) {
        this.renderPage((CliPageController) this.router.navigateTo(page));
    }

    /**
     * Torna indietro di una pagina
     * Influenza la history
     */
    public void goBack() {
        CliPageController page = (CliPageController) this.router.goBack();
        if (page != null) {
            this.renderPage(page);
        }
    }

    /**
     * Renderizza una pagina
     * Si occupa di visualizzare lo scaffold dell'applicazione e le autorizzazioni
     * La pagina fornisce il contenuto
     * @param page Pagina da renderizzare
     */
    protected void renderPage(CliPageController page) {
        // 1. Stampa del header, del nome utente e dei breadcrumb
        System.out.println(HEADER);
        if (this.authService.isLoggedIn()) {
            String ruolo = this.translator.translate(this.authService.getCurrentUser().getRuolo().toString().toLowerCase());
            String userMessage = String.format(this.translator.translate("user_header_pattern"), this.authService.getCurrentUser().getUsername(), ruolo);
            System.out.println(userMessage);
        }
        System.out.println(String.join(BREADCRUMB_SEPARATOR, this.getBreadcrumbs()));
        System.out.println();

        // 2.1 Controlla autorizzazione
        if (!page.canView()) {
            System.out.println(this.translator.translate("unauthorized_view_access"));
            this.cliUtils.waitForInput();
            this.goBack();
            return;
        }

        // 2.2 Stampa della pagina
        page.render();
    }

    /**
     * Ottiene i breadcrumb della history
     * @return Breadcrumb della history
     */
    protected String[] getBreadcrumbs() {
        return this.router.getHistory().stream().map(p -> ((CliPageController)p).getName()).toArray(String[]::new);
    }

    public CliRouter getRouter() {
        return this.router;
    }
}
