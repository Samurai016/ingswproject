package it.unibs.ingswproject.platforms.cli.controllers.pages.utenti;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.utenti.AddUtentePageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

/**
 * @author Nicolò Rebaioli
 * @created 02/12/2024 - 17:20
 * @project Progetto
 */
public class AddUtentePageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;

    @PageConstructor
    public AddUtentePageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, StorageService storageService, ErrorManager errorManager, CliUtils cliUtils, ProjectUtils projectUtils) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.view = new AddUtentePageView(app, this, translator, storageService, errorManager, cliUtils, projectUtils, authService);

        this.commands.put('1', this.translator.translate("utenti_page_command_add"));
    }

    @Override
    public String getName() {
        return this.translator.translate("add_utente_page_title");
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }
}

