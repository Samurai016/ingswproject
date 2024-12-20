package it.unibs.ingswproject.platforms.cli.views.components;

import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.platforms.cli.controllers.pages.gerarchie.NodoPageController;
import it.unibs.ingswproject.platforms.cli.elements.TreeRenderer;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.translations.Translator;

import java.util.HashMap;
import java.util.List;
import java.util.function.Predicate;

/**
 * @author Nicolò Rebaioli
 */
public class NodoSelector {
    public static final char COMMAND_CHOOSE = 'y';
    private final Nodo root;
    private final TreeRenderer treeRenderer;
    private final CliUtils cliUtils;
    private final Translator translator;
    private Predicate<Nodo> validator = (nodo) -> true;

    public NodoSelector(Nodo root, TreeRenderer treeRenderer, CliUtils cliUtils, Translator translator) {
        this.root = root;
        this.treeRenderer = treeRenderer;
        this.cliUtils = cliUtils;
        this.translator = translator;
    }

    public NodoSelector setValidator(Predicate<Nodo> validator) {
        this.validator = validator;
        return this;
    }

    public Nodo select() {
        Nodo currentNode = this.root;
        Nodo selectedNode = null;
        boolean hasChosen = false;

        do {
            // Creo mappa comandi
            HashMap<Character, String> commands = new HashMap<>();
            if (currentNode.getParent() != null) {
                commands.put(NodoPageController.COMMAND_BACK, this.translator.translate("command_back"));
            }
            if (this.validator.test(currentNode)) {
                commands.put(COMMAND_CHOOSE, "Conferma selezione");
            }

            // Creo mappa nodi
            HashMap<Character, Nodo> nodi = new HashMap<>();
            List<Nodo> figli = currentNode.getFigli();
            for (int i = 0; i < figli.size(); i++) {
                nodi.put((char) ('1' + i), figli.get(i));
            }

            System.out.println("Seleziona nodo:");
            this.renderNodi(currentNode);

            if (!commands.isEmpty()) {
                System.out.println();
                System.out.println(this.translator.translate("available_commands"));
                commands.forEach((key, value) -> {
                    String command = String.format(this.translator.translate("command_pattern"), key, value);
                    System.out.println(command);
                });
            }

            // 3. Richiesta di input
            System.out.println();
            String input;
            boolean isValidInput;
            do {
                String prompt = currentNode.getFigli().isEmpty()
                        ? this.translator.translate("insert_command")
                        : this.translator.translate("gerarchie_page_insert_command");
                if (commands.isEmpty()) {
                    prompt = "Inserisci un numero per accedere al nodo";
                }

                input = this.cliUtils.readFromConsole(prompt, false);
                char inputChar = input.charAt(0);
                isValidInput = nodi.containsKey(inputChar) || commands.containsKey(inputChar);

                boolean printLine = true;
                if (inputChar == COMMAND_CHOOSE) {
                    hasChosen = true;
                } else if (inputChar == NodoPageController.COMMAND_BACK) {
                    currentNode = currentNode.getParent();
                } else if (!nodi.containsKey(inputChar)) {
                    System.out.println(this.translator.translate("invalid_command"));
                    printLine = false;
                } else if (!this.validator.test(nodi.get(inputChar))) {
                    System.out.println(this.translator.translate("invalid_node"));
                    printLine = false;
                } else {
                    selectedNode = nodi.get(input.charAt(0));
                    currentNode = selectedNode;
                }

                if (printLine) {
                    System.out.println();
                }
            } while (!isValidInput);
        } while (!hasChosen);

        return selectedNode;
    }

    public void renderNodi(Nodo root) {
        this.treeRenderer.setRoot(root);
        this.treeRenderer.render();
    }
}
