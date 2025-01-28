package it.unibs.ingswproject.platforms.cli.components;

import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.platforms.cli.controllers.pages.gerarchie.NodoPageController;
import it.unibs.ingswproject.platforms.cli.errors.exceptions.CliQuitException;
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
    private final boolean quittable;
    private Predicate<Nodo> validator = (nodo) -> true;
    private String initialPromptMessage;


    public NodoSelector(Nodo root, TreeRenderer treeRenderer, CliUtils cliUtils, Translator translator, boolean quittable) {
        this.root = root;
        this.treeRenderer = treeRenderer;
        this.cliUtils = cliUtils;
        this.translator = translator;
        this.quittable = quittable;

        this.setInitialPromptMessage(this.translator.translate("nodo_selector_select_nodo"));
    }

    public NodoSelector setValidator(Predicate<Nodo> validator) {
        this.validator = validator;
        return this;
    }

    public NodoSelector setInitialPromptMessage(String initialPromptMessage) {
        this.initialPromptMessage = initialPromptMessage;
        return this;
    }

    public Nodo select() throws CliQuitException {
        Nodo currentNode = this.root;
        Nodo selectedNode = null;
        boolean hasChosen = false;
        boolean isFirstIteration = true;

        do {
            // Creo mappa comandi
            HashMap<Character, String> commands = new HashMap<>();
            if (currentNode.getParent() != null) {
                commands.put(NodoPageController.COMMAND_BACK, this.translator.translate("command_back"));
            }
            if (this.validator.test(currentNode)) {
                commands.put(COMMAND_CHOOSE, this.translator.translate("nodo_selector_command_select"));
            }

            // Creo mappa nodi
            HashMap<Character, Nodo> nodi = new HashMap<>();
            List<Nodo> figli = currentNode.getFigli();
            for (int i = 0; i < figli.size(); i++) {
                nodi.put((char) ('1' + i), figli.get(i));
            }

            // Stampo una nuova linea solo se non è la prima iterazione
            if (!isFirstIteration) {
                System.out.println();
            } else {
                isFirstIteration = false;
            }
            System.out.println(this.initialPromptMessage);
            this.renderNodi(currentNode);

            // Stampo i comandi
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
                // Genero il prompt
                String prompt = currentNode.getFigli().isEmpty()
                        ? this.translator.translate("insert_command")
                        : this.translator.translate("nodo_insert_command_or_select_nodo");
                if (commands.isEmpty()) {
                    prompt = this.translator.translate("nodo_selector_access_node_request");
                }

                // Leggo l'input
                if (this.quittable) {
                    input = this.cliUtils.readFromConsoleQuittable(prompt, false);
                } else {
                    input = this.cliUtils.readFromConsole(prompt);
                }

                char inputChar = input.charAt(0);
                isValidInput = nodi.containsKey(inputChar) || commands.containsKey(inputChar);
                if (!commands.containsKey(inputChar) && !nodi.containsKey(inputChar)) {
                    System.out.println(this.translator.translate("invalid_command"));
                } else if (inputChar == COMMAND_CHOOSE) {
                    hasChosen = true;
                } else if (inputChar == NodoPageController.COMMAND_BACK) {
                    currentNode = currentNode.getParent();
                } else if (!nodi.containsKey(inputChar)) {
                    System.out.println(this.translator.translate("invalid_command"));
                } else {
                    currentNode = nodi.get(input.charAt(0));
                    if (this.validator.test(nodi.get(inputChar))) {
                        selectedNode = currentNode;
                    }
                }
            } while (!isValidInput);
        } while (!hasChosen);

        // Stampo il nodo selezionato
        assert selectedNode != null;
        System.out.println(this.translator.translate("nodo_selector_select_summary", selectedNode.getNome()));

        return selectedNode;
    }

    public void renderNodi(Nodo root) {
        this.treeRenderer.setRoot(root);
        this.treeRenderer.render();
    }
}
