package it.unibs.ingswproject.view.cli;

import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.view.cli.pages.HomePage;

import java.util.Scanner;
import java.util.Stack;

/**
 * Classe principale dell'applicazione CLI
 * Contiene la logica di navigazione tra le pagine
 *
 * @author Nicolò Rebaioli
 */
public class App {
    public static final String HEADER = "\n=== Ing. SW Project ===";
    public static final String BREADCRUMB_SEPARATOR = " > ";

    protected Stack<CliPage> history = new Stack<>();

    /**
     * Avvia l'applicazione
     */
    public void run() {
        try {
            this.navigateTo(new HomePage(this));
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
        // 1. Stampa dei breadcrumb e del header
        System.out.println(HEADER);
        System.out.println(String.join(BREADCRUMB_SEPARATOR, this.getBreadcrumbs()));

        // 2.1 Controlla autorizzazione
        if (!page.canView()) {
            System.out.println("\nNon sei autorizzato a visualizzare questa pagina");
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
}
