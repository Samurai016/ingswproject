package it.unibs.ingswproject.controllers.cli.pages.gerarchie;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.controllers.cli.CliPageController;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.utils.cli.CliUtils;
import it.unibs.ingswproject.view.cli.pages.gerarchie.GerarchiePageView;

import java.util.HashMap;

public class GerarchiePageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;
    protected Nodo root;
    protected final HashMap<Character, Nodo> nodi = new HashMap<>();

    @PageConstructor
    public GerarchiePageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, CliUtils cliUtils, StorageService storageService) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.view = new GerarchiePageView(app, this, translator, storageService, cliUtils, authService);

        this.commands.put(CliPageController.COMMAND_BACK, this.translator.translate("home_page_command_exit")); // Override default command (0 -> Esci)

        // Aggiungi comandi in base al ruolo dell'utente
        switch (this.authService.getCurrentUser().getRuolo()) {
            case Utente.Ruolo.CONFIGURATORE:
                this.commands.put('1', this.translator.translate("home_page_command_comprensori")); // Aggiungi comando (1 -> Comprensori)
                this.commands.put('2', this.translator.translate("home_page_command_gerarchie")); // Aggiungi comando (2 -> Gerarchie)
                break;
            case Utente.Ruolo.FRUITORE:
                break;
        }
    }

    public GerarchiePageController setRoot(Nodo root) {
        this.root = root;
        return this;
    }

    public Nodo getRoot() {
        return this.root;
    }

    public boolean isValidInput(char input) {
        return this.nodi.containsKey(input) || this.commands.containsKey(input);
    }

    @Override
    public String getName() {
        return this.root == null ? Utils.capitalize(this.translator.translate("gerarchia_plural")) : this.root.getNome();
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }

    @Override
    public void handleInput(char input) {
        super.handleInput(input);

        if (this.nodi.containsKey(input)) {
            this.app.navigateTo(this.pageFactory.generatePage(GerarchiePageController.class).setRoot(this.nodi.get(input)));
        } else {
            if (input == 'a') {
                this.app.navigateTo(this.pageFactory.generatePage(AddNodoPageController.class).setRoot(this.root));
            }
        }
    }
}
