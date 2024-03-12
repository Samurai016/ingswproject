package it.unibs.ingswproject.view.cli;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Classe astratta che rappresenta una pagina della CLI
 * Ogni pagina dell'applicazione è una sottoclasse di questa classe
 *
 * @author Nicolò Rebaioli
 */
public abstract class CliPage {
    protected HashMap<Character, String> commands = new HashMap<>();
    protected App app;

    /**
     * Costruttore che istanza la pagina
     * Inserisce il comando 0 per tornare indietro
     *
     * @param app Applicazione che ha generato la pagina
     */
    public CliPage(App app) {
        this.app = app;

        this.commands.put('0', "Indietro");
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
        System.out.println("Comandi disponibili:");
        this.commands.forEach((key, value) -> System.out.println(key + ". " + value));

        this.afterRender();

        // 3. Richiesta di input
        System.out.println();
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean isValidInput = false;
        do {
            System.out.print("Inserisci un comando: ");
            input = scanner.nextLine();
            if (input.length() > 1) {
                System.out.println("!! Input non valido");
            } else if (!this.commands.containsKey(input.charAt(0))) {
                System.out.println("!! Comando non valido");
            } else {
                isValidInput = true;
            }
        } while (!isValidInput);

        // 4. Gestione dell'input
        this.handleInput(input.charAt(0));
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
     * Metodo che restituisce il nome della pagina
     * Viene utilizzato per stampare i breadcrumb
     * @return Il nome della pagina
     */
    protected abstract String getName();

    /**
     * Metodo che restituisce se l'utente può visualizzare la pagina
     * @return True se l'utente può visualizzare la pagina, false altrimenti
     */
    protected abstract boolean canView();

    /**
     * Metodo che gestisce l'input dell'utente
     * @param input Il carattere inserito dall'utente
     */
    protected void handleInput(char input) {
        // Override this method to handle input
        if (input == '0') {
            this.app.goBack();
        }
    }
}
