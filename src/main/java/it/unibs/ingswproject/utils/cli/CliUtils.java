package it.unibs.ingswproject.utils.cli;

import it.unibs.ingswproject.translations.Translator;

import java.util.Scanner;

public class CliUtils {
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
        Scanner scanner = new Scanner(System.in);
        String input;
        boolean isValidInput = false;
        do {
            System.out.printf("%s: ", message);
            input = scanner.nextLine();
            if (!nullable && input.isEmpty()) {
                System.out.println(this.translator.translate("invalid_input"));
            } else {
                isValidInput = true;
            }
        } while (!isValidInput);
        return input;
    }
}
