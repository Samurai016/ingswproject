package it.unibs.ingswproject.view.cli;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.view.AppInterface;
import it.unibs.ingswproject.view.cli.pages.LoginPage;
import it.unibs.ingswproject.view.cli.router.CliPageFactory;
import it.unibs.ingswproject.view.cli.router.CliRouter;

import java.util.Scanner;
import java.util.Stack;

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

    protected CliRouter router;
    protected CliPageFactory pageFactory;
    protected AuthService authService;

    public CliApp(CliRouter router, CliPageFactory pageFactory, AuthService authService) {
        this.router = router;
        this.pageFactory = pageFactory;
        this.authService = authService;
    }

    /**
     * Avvia l'applicazione
     */
    public void run() {
        try {
            LoginPage page = this.pageFactory.generatePage(LoginPage.class);
            this.router.navigateTo(page);
            this.renderPage(page);
        } catch (Throwable e) {
            System.out.println("Errore: " + Utils.getErrorMessage(e));
        }
    }

    /**
     * Naviga verso una pagina e la renderizza
     * Influenza la history
     * @param page Pagina da renderizzare
     */
    public void navigateTo(CliPage page) {
        this.renderPage(this.router.navigateTo(page));
    }

    /**
     * Torna indietro di una pagina
     * Influenza la history
     */
    public void goBack() {
        CliPage page = this.router.goBack();
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
    protected void renderPage(CliPage page) {
        // 1. Stampa del header, del nome utente e dei breadcrumb
        System.out.println(HEADER);
        if (this.authService.isLoggedIn()) {
            System.out.printf("%s (%s)\n", this.authService.getCurrentUser().getUsername(), this.authService.getCurrentUser().getRuolo());
        }
        System.out.println(String.join(BREADCRUMB_SEPARATOR, this.getBreadcrumbs()));
        System.out.println();

        // 2.1 Controlla autorizzazione
        if (!page.canView()) {
            System.out.println("Non sei autorizzato a visualizzare questa pagina");
            waitForInput();
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
        return this.router.getHistory().stream().map(CliPage::getName).toArray(String[]::new);
    }

    /**
     * Attende l'input dell'utente
     * Stampa a video un messaggio e attende che l'utente prema invio
     */
    public static void waitForInput() {
        System.out.println("Premi invio per continuare...");
        new Scanner(System.in).nextLine();
    }

    public CliRouter getRouter() {
        return this.router;
    }
}
