package it.unibs.ingswproject.platforms.cli.controllers.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.scambi.ScambioPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

/**
 * @author Nicolò Rebaioli
 */
public class ScambioPageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;
    protected final StorageService storageService;
    protected Scambio scambio;

    @PageConstructor
    public ScambioPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, CliUtils cliUtils, ProjectUtils projectUtils, StorageService storageService) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.storageService = storageService;
        this.view = new ScambioPageView(app, this, translator, cliUtils, projectUtils, authService);
    }

    public Scambio getScambio() {
        return this.scambio;
    }

    public ScambioPageController setScambio(Scambio scambio) {
        this.scambio = scambio;
        if (this.scambio.getStato() == Scambio.Stato.APERTO && this.authService.getCurrentUser() == this.scambio.getAutore()) {
            this.commands.put('1', this.translator.translate("scambio_page_ritira_command"));
        }
        return this;
    }

    @Override
    public String getName() {
        return this.translator.translate("scambio_page_title", this.scambio != null ? this.scambio.getId() : "");
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn();
    }

    @Override
    public void handleInput(char input) {
        super.handleInput(input);

        // Check if input is a valid command
        if (!this.commands.containsKey(input)) {
            return;
        }

        if (input == '1') {
            this.app.navigateTo(this.pageFactory.generatePage(RitiraPropostaPageController.class).setScambio(this.scambio));
        }
    }
}
