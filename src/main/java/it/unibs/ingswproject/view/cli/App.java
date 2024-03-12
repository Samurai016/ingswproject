package it.unibs.ingswproject.view.cli;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.view.cli.pages.LoginPage;

import java.util.Scanner;
import java.util.Stack;

/**
 * Classe principale dell'applicazione CLI
 * Contiene la logica di navigazione tra le pagine
 *
 * @author Nicolò Rebaioli
 */
public class App {
    public static final String HEADER = """

              _____               __  __    __     ___           _           _  \s
              \\_   \\_ __   __ _  / _\\/ / /\\ \\ \\   / _ \\_ __ ___ (_) ___  ___| |_\s
               / /\\/ '_ \\ / _` | \\ \\ \\ \\/  \\/ /  / /_)/ '__/ _ \\| |/ _ \\/ __| __|
            /\\/ /_ | | | | (_| | _\\ \\ \\  /\\  /  / ___/| | | (_) | |  __/ (__| |_\s
            \\____/ |_| |_|\\__, | \\__/  \\/  \\/   \\/    |_|  \\___// |\\___|\\___|\\__|
                          |___/                               |__/              \s
            """;
    public static final String BREADCRUMB_SEPARATOR = " > ";

    protected Stack<CliPage> history = new Stack<>();

    /**
     * Avvia l'applicazione
     */
    public void run() {
        try {
            this.navigateTo(new LoginPage(this));
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
        this.renderPage(this.history.push(page));
    }

    /**
     * Torna indietro di una pagina
     * Influenza la history
     */
    public void goBack() {
        if (this.history.size() <= 1) {
            return;
        }
        this.history.pop(); // Remove current page from history
        this.renderPage(this.history.lastElement()); // Render previous page
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
        AuthService authService = AuthService.getInstance();
        if (authService.isLoggedIn()) {
            System.out.printf("%s (%s)\n", authService.getCurrentUser().getUsername(), authService.getCurrentUser().getRuolo());
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
        return this.history.stream().map(CliPage::getName).toArray(String[]::new);
    }

    /**
     * Attende l'input dell'utente
     * Stampa a video un messaggio e attende che l'utente prema invio
     */
    public static void waitForInput() {
        System.out.println("Premi invio per continuare...");
        new Scanner(System.in).nextLine();
    }

    public Stack<CliPage> getHistory() {
        return this.history;
    }
}
