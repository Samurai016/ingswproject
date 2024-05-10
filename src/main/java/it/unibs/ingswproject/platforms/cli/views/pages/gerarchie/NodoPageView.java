package it.unibs.ingswproject.platforms.cli.views.pages.gerarchie;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.platforms.cli.controllers.pages.gerarchie.NodoPageController;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.router.PageConstructor;

import java.util.ArrayList;
import java.util.List;

public class NodoPageView extends CliPageView {
    @PageConstructor
    public NodoPageView(CliApp app, NodoPageController controller, Translator translator, CliUtils cliUtils, AuthService authService) {
        super(app, controller, translator, cliUtils, authService);
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();
        NodoPageController controller = (NodoPageController) this.controller;
        Nodo root = controller.getRoot();
        List<Nodo> nodi = new ArrayList<>(controller.getNodi());

        // Show gerarchie
        if (root == null) {
            this.renderGerarchie(nodi);
        } else { // Visualizza nodi di una gerarchie
            this.renderFoglie(root, nodi);
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
            if (!((NodoPageController)this.controller).isValidInput(input.charAt(0))) {
                System.out.println(this.translator.translate("invalid_command"));
            } else {
                isValidInput = true;
            }
        } while (!isValidInput);

        // 4. Gestione dell'input
        this.controller.handleInput(input.charAt(0));
    }

    protected void renderGerarchie(List<Nodo> nodi) {
        System.out.printf("%s:", Utils.capitalize(this.translator.translate("gerarchia_plural")));
        System.out.println();

        if (nodi.isEmpty()) {
            System.out.printf("\t%s", this.translator.translate("no_items_found"));
            System.out.println();
        } else {
            for (int i = 0; i < nodi.size(); i++) {
                Nodo gerarchia = nodi.get(i);

                String nomeAttributo = gerarchia.getNomeAttributo() == null
                        ? ""
                        : String.format(
                        this.translator.translate("gerarchie_page_attribute_pattern"),
                        gerarchia.getNomeAttributo()
                );
                System.out.printf(this.translator.translate("gerarchie_page_gerarchia_pattern"), i + 1, gerarchia.getNome(), nomeAttributo);
                System.out.println();
            }
        }
    }

    protected void renderFoglie(Nodo root, List<Nodo> nodi) {
        String nomeAttributo = root.getNomeAttributo() == null
                ? ""
                : String.format(
                this.translator.translate("gerarchie_page_attribute_pattern"),
                root.getNomeAttributo()
        );
        System.out.printf(this.translator.translate("gerarchie_page_nodo_pattern"), root.getNome(), nomeAttributo);
        System.out.println();

        if (nodi.isEmpty()) {
            System.out.printf("\t%s", this.translator.translate("no_items_found"));
            System.out.println();
        } else {
            for (int i = 0; i < nodi.size(); i++) {
                Nodo figlio = nodi.get(i);
                System.out.printf(this.translator.translate("gerarchie_page_child_pattern"), i+1, figlio.getNome());
                System.out.println();
            }
        }
    }
}
