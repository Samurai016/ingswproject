package it.unibs.ingswproject.platforms.cli.controllers.pages.comprensori;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.comprensori.AddComprensorioPageView;
import it.unibs.ingswproject.utils.ProjectUtils;

public class AddComprensorioPageController extends CliPageController {
    protected final AuthService authService;

    @PageConstructor
    public AddComprensorioPageController(CliApp app, Translator translator, AuthService authService, StorageService storageService, ErrorManager errorManager, CliUtils cliUtils, ProjectUtils projectUtils) {
        super(app, translator);
        this.authService = authService;
        this.view = new AddComprensorioPageView(app, this, translator, storageService, errorManager, cliUtils, projectUtils, authService);

        this.commands.put('1', this.translator.translate("comprensori_page_command_add"));
    }

    @Override
    public String getName() {
        return this.translator.translate("add_comprensorio_page_title");
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }
}
