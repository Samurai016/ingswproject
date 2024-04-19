package it.unibs.ingswproject.view.cli.pages.gerarchie;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.controllers.cli.pages.gerarchie.GerarchiePageController;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.repositories.NodoRepository;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPageView;
import it.unibs.ingswproject.utils.cli.CliUtils;
import it.unibs.ingswproject.router.PageConstructor;

import java.util.HashMap;
import java.util.List;

public class GerarchiePageView extends CliPageView {
    protected final HashMap<Character, Nodo> nodi = new HashMap<>();
    protected final StorageService storageService;

    @PageConstructor
    public GerarchiePageView(CliApp app, GerarchiePageController controller, Translator translator, StorageService storageService, CliUtils cliUtils, AuthService authService) {
        super(app, controller, translator, cliUtils, authService);
        this.storageService = storageService;
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();
        this.nodi.clear();

        // Show gerarchie
        Nodo root = ((GerarchiePageController) this.controller).getRoot();
        if (root == null) {
            System.out.printf("%s:", Utils.capitalize(this.translator.translate("gerarchia_plural")));
            System.out.println();
            NodoRepository repository = (NodoRepository) this.storageService.getRepository(Nodo.class);
            List<Nodo> gerarchie = repository.findGerarchie();
            if (gerarchie.isEmpty()) {
                System.out.printf("\t%s", this.translator.translate("no_items_found"));
                System.out.println();
            } else {
                for (int i = 0; i < gerarchie.size(); i++) {
                    Nodo gerarchia = gerarchie.get(i);

                    String nomeAttributo = gerarchia.getNomeAttributo() == null
                            ? ""
                            : String.format(
                                this.translator.translate("gerarchie_page_attribute_pattern"),
                                gerarchia.getNomeAttributo()
                            );
                    System.out.printf(this.translator.translate("gerarchie_page_gerarchia_pattern"), i + 1, gerarchia.getNome(), nomeAttributo);
                    System.out.println();
                    this.nodi.put((char) ('1' + i), gerarchia);
                }
            }

            this.controller.getCommands().put('a', this.translator.translate("gerarchie_page_command_add_gerarchia"));
        } else { // Visualizza nodi di una gerarchie
            GerarchiePageController controller = (GerarchiePageController) this.controller;
            controller.setRoot(this.storageService.getRepository(Nodo.class).find(root.getId()));

            String nomeAttributo = root.getNomeAttributo() == null
                    ? ""
                    : String.format(
                        this.translator.translate("gerarchie_page_attribute_pattern"),
                        root.getNomeAttributo()
                    );
            System.out.printf(this.translator.translate("gerarchie_page_nodo_pattern"), root.getNome(), nomeAttributo);
            System.out.println();

            List<Nodo> figli = root.getFigli();
            if (figli.isEmpty()) {
                System.out.printf("\t%s", this.translator.translate("no_items_found"));
                System.out.println();
            } else {
                for (int i = 0; i < figli.size(); i++) {
                    Nodo figlio = figli.get(i);
                    System.out.printf(this.translator.translate("gerarchie_page_child_pattern"), i+1, figlio.getNome());
                    System.out.println();
                    this.nodi.put((char) ('1' + i), figlio);
                }
            }

            this.controller.getCommands().put('a', this.translator.translate("gerarchie_page_command_add_foglia"));
        }

        System.out.println();
    }

    @Override
    public void renderContent() {
        // Ogni pagina è composta in questo modo:
        // 2. Stampa dei comandi
        // 3. Richiesta di input
        // 4. Gestione dell'input
        this.beforeRender();

        // 2. Stampa dei comandi
        System.out.println(this.translator.translate("available_commands"));
        this.controller.getCommands().forEach((key, value) -> {
            String command = String.format(this.translator.translate("command_pattern"), key, value);
            System.out.println(command);
        });

        this.afterRender();

        // 3. Richiesta di input
        System.out.println();
        String input;
        boolean isValidInput = false;
        do {
            input = this.cliUtils.readFromConsole(this.translator.translate("gerarchie_page_insert_command"), false);
            if (!((GerarchiePageController)this.controller).isValidInput(input.charAt(0))) {
                System.out.println(this.translator.translate("invalid_command"));
            } else {
                isValidInput = true;
            }
        } while (!isValidInput);

        // 4. Gestione dell'input
        this.controller.handleInput(input.charAt(0));
    }
}
