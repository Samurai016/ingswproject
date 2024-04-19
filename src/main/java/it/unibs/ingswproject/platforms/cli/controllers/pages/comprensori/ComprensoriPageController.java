package it.unibs.ingswproject.platforms.cli.controllers.pages.comprensori;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.comprensori.ComprensoriPageView;

public class ComprensoriPageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;

    @PageConstructor
    public ComprensoriPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, StorageService storageService, CliUtils cliUtils) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.view = new ComprensoriPageView(app, this, translator, cliUtils, storageService, authService);

        this.commands.put('1', this.translator.translate("comprensori_page_command_add"));
    }

    @Override
    public String getName() {
        return this.translator.translate("comprensori_page_title");
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }

    @Override
    public void handleInput(char input) {
        super.handleInput(input); // Handle default commands

        if (input == '1') {
            this.app.navigateTo(this.pageFactory.generatePage(AddComprensorioPageController.class));
        }
    }

}
