package it.unibs.ingswproject.platforms.cli.controllers.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.scambi.NotificaScambiPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

/**
 * @author Nicolò Rebaioli
 */
public class NotificaScambiPageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;

    @PageConstructor
    public NotificaScambiPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, StorageService storageService, CliUtils cliUtils, ProjectUtils projectUtils) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.view = new NotificaScambiPageView(app, this, translator, cliUtils, projectUtils, storageService, authService);
    }

    @Override
    public String getName() {
        return this.translator.translate("notifica_scambi_page_title");
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }
}
