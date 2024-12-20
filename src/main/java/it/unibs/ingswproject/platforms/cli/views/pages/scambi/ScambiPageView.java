package it.unibs.ingswproject.platforms.cli.views.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.scambi.ScambiPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;
import it.unibs.ingswproject.utils.Utils;

import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class ScambiPageView extends CliPageView {
    protected final StorageService storageService;

    @PageConstructor
    public ScambiPageView(CliApp app, ScambiPageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, StorageService storageService, AuthService authService) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();

        // Show scambi
        renderScambi(this.translator, this.storageService);

        System.out.println();
    }

    public static void renderScambi(Translator translator, StorageService storageService) {
        System.out.printf("%s:", Utils.capitalize(translator.translate("scambio_plural")));
        System.out.println();
        List<Scambio> scambi = storageService.getRepository(Scambio.class).findAll();

        if (scambi.isEmpty()) {
            System.out.printf("\t%s", translator.translate("no_items_found"));
            System.out.println();
        } else {
            for (int i = 0; i < scambi.size(); i++) {
                Scambio scambio = scambi.get(i);
                System.out.printf(
                        translator.translate("scambi_page_scambio_pattern"),
                        i + 1,
                        scambio.getRichiesta().getNome(),
                        scambio.getQuantitaOfferta(),
                        scambio.getOfferta().getNome(),
                        scambio.getQuantitaRichiesta(),
                        scambio.getStato().name()
                );
                System.out.println();
            }
        }
    }
}