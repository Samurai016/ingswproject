package it.unibs.ingswproject.platforms.cli.utils;

import it.unibs.ingswproject.platforms.cli.errors.exceptions.CliQuitException;
import it.unibs.ingswproject.translations.Translator;

import java.util.Scanner;

public class CliUtils {
    public static final String QUIT_SHORTCUT = "END";
    protected final Translator translator;

    public CliUtils(Translator translator) {
        this.translator = translator;
    }

    /**
     * Attende l'input dell'utente
     * Stampa a video un messaggio e attende che l'utente prema invio
     */
    public void waitForInput() {
        System.out.println(this.translator.translate("press_enter_to_continue"));
        new Scanner(System.in).nextLine();
    }

    public String readFromConsole(String message) {
        return this.readFromConsole(message, false);
    }

    public String readFromConsole(String message, boolean nullable) {
        try {
            return this.readFromConsole(message, nullable, false);
        } catch (CliQuitException e) {
            // Non verrà mai lanciata perché quittable è false
        }
        return null; // Non verrà mai eseguito perché il metodo non lancia mai l'eccezione
    }

    public String readFromConsoleQuittable(String message) throws CliQuitException {
        return this.readFromConsoleQuittable(message, false);
    }

    public String readFromConsoleQuittable(String message, boolean nullable) throws CliQuitException {
        return this.readFromConsole(message, nullable, true);
    }

    private String readFromConsole(String message, boolean nullable, boolean quittable) throws CliQuitException {
        Scanner scanner = new Scanner(System.in);
        String input;
        String quitString = String.format(this.translator.translate("press_key_to_quit"), QUIT_SHORTCUT);
        boolean isValidInput = false;
        do {
            System.out.printf("%s%s: ", message, quittable ? " " + quitString : "");
            input = scanner.nextLine().trim();
            if (quittable && input.equals(QUIT_SHORTCUT)) {
                throw new CliQuitException();
            } else if (!nullable && input.isEmpty()) {
                System.out.println(this.translator.translate("invalid_input"));
            } else {
                isValidInput = true;
            }
        } while (!isValidInput);
        return input;
    }
}
