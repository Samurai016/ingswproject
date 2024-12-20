package it.unibs.ingswproject.platforms.cli.controllers.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.scambi.ScambiPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

public class ScambiPageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;

    @PageConstructor
    public ScambiPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, StorageService storageService, CliUtils cliUtils, ProjectUtils projectUtils) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.view = new ScambiPageView(app, this, translator, cliUtils, projectUtils, storageService, authService);

        this.commands.put('1', this.translator.translate("scambi_page_command_add"));
    }

    @Override
    public String getName() {
        return this.translator.translate("scambi_page_title");
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isFruitore();
    }

    @Override
    public void handleInput(char input) {
        super.handleInput(input); // Handle default commands

        if (input == '1') {
            this.app.navigateTo(this.pageFactory.generatePage(AddPropostaDiScambioPageController.class));
        }
    }

}
