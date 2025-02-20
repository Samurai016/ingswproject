package it.unibs.ingswproject.platforms.cli.controllers.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.logic.ScambioStrategy;
import it.unibs.ingswproject.logic.routing.RoutingComputationStrategy;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.components.OneLevelTreeRenderer;
import it.unibs.ingswproject.platforms.cli.components.TreeRenderer;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.scambi.AddPropostaDiScambioPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

public class AddPropostaDiScambioPageController extends CliPageController {
    protected final AuthService authService;

    @PageConstructor
    public AddPropostaDiScambioPageController(CliApp app, Translator translator, AuthService authService, StorageService storageService, ErrorManager errorManager, CliUtils cliUtils, ProjectUtils projectUtils, RoutingComputationStrategy routingComputationStrategy, ScambioStrategy scambioStrategy) {
        super(app, translator);
        this.authService = authService;
        TreeRenderer treeRenderer = new OneLevelTreeRenderer(this.translator);
        this.view = new AddPropostaDiScambioPageView(app, this, translator, storageService, errorManager, cliUtils, projectUtils, authService, routingComputationStrategy, treeRenderer, scambioStrategy);

        this.commands.put('1', this.translator.translate("scambi_page_command_add"));
    }

    @Override
    public String getName() {
        return this.translator.translate("add_proposta_di_scambio_page_title");
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isFruitore();
    }
}
