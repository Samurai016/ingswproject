package it.unibs.ingswproject.platforms.cli.controllers.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.scambi.RitiraPropostaPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

/**
 * @author Nicolò Rebaioli
 */
public class RitiraPropostaPageController extends CliPageController {
    protected final AuthService authService;
    protected Scambio scambio;

    @PageConstructor
    public RitiraPropostaPageController(CliApp app, Translator translator, AuthService authService, StorageService storageService, CliUtils cliUtils, ProjectUtils projectUtils) {
        super(app, translator);
        this.authService = authService;
        this.view = new RitiraPropostaPageView(app, this, translator, cliUtils, projectUtils, authService, storageService);
    }

    @Override
    public String getName() {
        return this.translator.translate("ritira_scambio_page_title");
    }

    @Override
    public boolean canView() {
        if (!this.authService.isLoggedIn()) {
            return false;
        }
        if (this.scambio == null) {
            return false;
        }
        return this.scambio.getAutore() == this.authService.getCurrentUser();
    }

    public RitiraPropostaPageController setScambio(Scambio scambio) {
        this.scambio = scambio;
        return this;
    }

    public Scambio getScambio() {
        return this.scambio;
    }
}
