package it.unibs.ingswproject.platforms.cli.controllers.pages.gerarchie;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.logic.graph.SimpleWeightComputation;
import it.unibs.ingswproject.logic.graph.WeightComputationStrategy;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.logic.BaseFattoreDiConversioneStrategy;
import it.unibs.ingswproject.logic.FattoreDiConversioneStrategy;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.gerarchie.AddNodoPageView;

public class AddNodoPageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;
    protected Nodo root;

    @PageConstructor
    public AddNodoPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, CliUtils cliUtils, StorageService storageService, ErrorManager errorManager) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        FattoreDiConversioneStrategy fdcStrategy = new BaseFattoreDiConversioneStrategy(storageService);
        WeightComputationStrategy weightComputationStrategy = new SimpleWeightComputation();
        this.view = new AddNodoPageView(app, this, translator, cliUtils, storageService, errorManager, fdcStrategy, weightComputationStrategy, authService);

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

    public AddNodoPageController setRoot(Nodo root) {
        this.root = root;
        return this;
    }

    public Nodo getRoot() {
        return this.root;
    }

    @Override
    public String getName() {
        return String.format(
                this.translator.translate("add_node_page_title"),
                this.translator.translate(this.root == null ? "gerarchia" : "foglia")
        );
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }
}
