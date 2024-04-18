package it.unibs.ingswproject.view.cli;

import it.unibs.ingswproject.translations.Translator;

import java.util.HashMap;
import java.util.Scanner;

/**
 * Classe astratta che rappresenta una pagina della CLI
 * Ogni pagina dell'applicazione è una sottoclasse di questa classe
 *
 * @author Nicolò Rebaioli
 */
public abstract class CliPage {
    public static final char COMMAND_BACK = '0';
    protected HashMap<Character, String> commands = new HashMap<>();
    protected CliApp app;
    protected Translator translator;
    protected CliUtils cliUtils;

    /**
     * Costruttore che istanza la pagina
     * Inserisce il comando 0 per tornare indietro
     *
     * @param app Applicazione che ha generato la pagina
     */
    public CliPage(CliApp app, Translator translator, CliUtils cliUtils) {
        this.app = app;
        this.translator = translator;
        this.cliUtils = cliUtils;

        this.commands.put(COMMAND_BACK, this.translator.translate("command_back"));
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
        this.commands.forEach((key, value) -> {
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
            } else if (!this.commands.containsKey(input.charAt(0))) {
                System.out.println(this.translator.translate("invalid_command"));
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
        if (input == COMMAND_BACK) {
            this.app.goBack();
        }
    }
}
