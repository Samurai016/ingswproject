package it.unibs.ingswproject.platforms.cli.views.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.scambi.RitiraPropostaPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

/**
 * @author Nicolò Rebaioli
 */
public class RitiraPropostaPageView extends CliPageView {
    protected final StorageService storageService;

    @PageConstructor
    public RitiraPropostaPageView(CliApp app, RitiraPropostaPageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, AuthService authService, StorageService storageService) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
    }

    @Override
    public void beforeRender() {
        RitiraPropostaPageController controller = (RitiraPropostaPageController) this.controller;
        assert controller.getScambio() != null;

        boolean confirm = this.cliUtils.askForConfirmation(this.translator.translate(("scambio_page_confirm_ritira")));
        if (confirm) {
            this.storageService.getRepository(Scambio.class).save(controller.getScambio().ritira());
            System.out.println(this.translator.translate("scambio_page_success_ritira"));
        }

        System.out.println();
    }
}
