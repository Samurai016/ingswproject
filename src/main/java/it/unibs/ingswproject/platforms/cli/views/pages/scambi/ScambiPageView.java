package it.unibs.ingswproject.platforms.cli.views.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.models.repositories.ScambioRepository;
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
        this.renderScambi();

        System.out.println();
    }

    public void renderScambi() {
        System.out.printf("%s:", Utils.capitalize(this.translator.translate("scambio_plural")));
        System.out.println();

        // Ottengo gli scambi dell'utente corrente
        List<Scambio> scambi = ((ScambioRepository) this.storageService.getRepository(Scambio.class))
                .findByAutore(this.authService.getCurrentUser())
                .stream()
                .sorted((a, b) -> {
                    // Metto prima gli scambi aperti, ordinandoli per data di creazione
                    boolean isAOpen = a.getStato().equals(Scambio.Stato.APERTO);
                    boolean isBOpen = b.getStato().equals(Scambio.Stato.APERTO);
                    if (isAOpen && !isBOpen) {
                        return -1;
                    }
                    if (!isAOpen && isBOpen) {
                        return 1;
                    }
                    return a.getDataCreazione().compareTo(b.getDataCreazione());
                })
                .toList();

        if (scambi.isEmpty()) {
            System.out.printf("\t%s", this.translator.translate("no_items_found"));
            System.out.println();
        } else {
            for (int i = 0; i < scambi.size(); i++) {
                Scambio scambio = scambi.get(i);
                System.out.println(this.translator.translate("scambi_page_scambio_pattern",
                        i + 1,
                        scambio.getRichiesta().getNome(),
                        scambio.getQuantitaRichiesta(),
                        scambio.getOfferta().getNome(),
                        scambio.getQuantitaOfferta(),
                        scambio.getStato().name()
                ));
            }
        }
    }
}