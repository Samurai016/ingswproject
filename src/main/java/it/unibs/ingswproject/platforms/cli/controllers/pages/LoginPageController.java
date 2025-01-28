package it.unibs.ingswproject.platforms.cli.controllers.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.LoginPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.utils.ProjectUtils;

public class LoginPageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;

    @PageConstructor
    public LoginPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, CliUtils cliUtils, ProjectUtils projectUtils, StorageService storageService) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.view = new LoginPageView(app, this, translator, authService, storageService, cliUtils, projectUtils);
    }

    @Override
    public String getName() {
        return this.translator.translate("login_page_title");
    }

    @Override
    public boolean canView() {
        return true;
    }

    @Override
    public void render() {
        // Se l'utente è già autenticato, non renderizzo la pagina
        if (!this.checkLogin()) {
            super.render();
        }
    }

    /**
     * Controlla se l'utente è già autenticato e in caso positivo lo reindirizza alla home
     * Se l'utente è già autenticato, la history viene cancellata
     *
     * @return true se l'utente è già autenticato, false altrimenti
     */
    public boolean checkLogin() {
        if (this.authService.isLoggedIn()) {
            this.app.getRouter().clearHistory(); // Clear history
            this.app.navigateTo(this.pageFactory.generatePage(HomePageController.class));
            return true;
        }
        return false;
    }
}
