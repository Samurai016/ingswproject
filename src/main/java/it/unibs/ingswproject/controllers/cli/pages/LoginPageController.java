package it.unibs.ingswproject.controllers.cli.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.controllers.cli.CliPageController;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.utils.cli.CliUtils;
import it.unibs.ingswproject.view.cli.pages.LoginPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;

public class LoginPageController extends CliPageController {
    @PageConstructor
    public LoginPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, CliUtils cliUtils, StorageService storageService) {
        super(app, translator);
        this.view = new LoginPageView(app, this, translator, authService, storageService, pageFactory, cliUtils);
    }

    @Override
    public String getName() {
        return this.translator.translate("login_page_title");
    }

    @Override
    public boolean canView() {
        return true;
    }
}
