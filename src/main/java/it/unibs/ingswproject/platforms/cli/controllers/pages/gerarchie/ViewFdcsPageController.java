package it.unibs.ingswproject.platforms.cli.controllers.pages.gerarchie;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.gerarchie.ViewFdcsPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

public class ViewFdcsPageController  extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;
    protected Nodo root;

    @PageConstructor
    public ViewFdcsPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, CliUtils cliUtils, ProjectUtils projectUtils, StorageService storageService, ErrorManager errorManager) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.view = new ViewFdcsPageView(app, this, translator, cliUtils, projectUtils, storageService, errorManager, authService);
    }

    public ViewFdcsPageController setRoot(Nodo root) {
        this.root = root;
        return this;
    }

    public Nodo getRoot() {
        return this.root;
    }

    @Override
    public String getName() {
        return this.translator.translate("view_fdcs_page_title");
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }
}
