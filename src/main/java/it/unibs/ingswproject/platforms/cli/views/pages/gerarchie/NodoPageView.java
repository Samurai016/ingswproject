package it.unibs.ingswproject.platforms.cli.views.pages.gerarchie;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.platforms.cli.controllers.pages.gerarchie.NodoPageController;
import it.unibs.ingswproject.platforms.cli.elements.OneLevelTreeRenderer;
import it.unibs.ingswproject.platforms.cli.elements.TreeRenderer;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.utils.Utils;

import java.util.ArrayList;

public class NodoPageView extends CliPageView {
    protected final TreeRenderer treeRenderer;

    @PageConstructor
    public NodoPageView(CliApp app, NodoPageController controller, Translator translator, CliUtils cliUtils, AuthService authService) {
        super(app, controller, translator, cliUtils, authService);
        this.treeRenderer = new OneLevelTreeRenderer(translator);
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();

        // Renderizzo nodi
        NodoPageController controller = (NodoPageController) this.controller;
        Nodo controllerRoot = controller.getRoot();
        Nodo renderRoot;
        if (controllerRoot == null) {
            renderRoot = new Nodo();
            renderRoot.setNome(Utils.capitalize(this.translator.translate("gerarchia_plural")));
            renderRoot.setFigli(new ArrayList<>(controller.getNodi()));
        } else {
            renderRoot = controllerRoot;
        }
        this.treeRenderer.setRoot(renderRoot);
        this.treeRenderer.render();

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
}
