package it.unibs.ingswproject.platforms.cli.views.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.models.entities.StoricoScambio;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.scambi.ScambioPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

import java.text.DateFormat;
import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class ScambioPageView extends CliPageView {
    @PageConstructor
    public ScambioPageView(CliApp app, ScambioPageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, AuthService authService) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
    }

    @Override
    public void beforeRender() {
        Scambio scambio = ((ScambioPageController) this.controller).getScambio();

        System.out.println(this.translator.translate("scambio_page_data_title"));
        System.out.println(this.translator.translate("scambio_page_pattern",
                scambio.getId(),
                scambio.getRichiesta().getNome(),
                scambio.getQuantitaRichiesta(),
                scambio.getOfferta().getNome(),
                scambio.getQuantitaOfferta(),
                scambio.getStato()
        ));

        // Mostro al configuratore ulteriori dettagli
        if (this.authService.getCurrentUser().isConfiguratore()) {
            System.out.println();

            // Mostro i dettagli dell'utente che ha creato lo scambio
            System.out.println(this.translator.translate("scambio_page_autore_pattern",
                    scambio.getAutore().getUsername(),
                    scambio.getAutore().getEmailAddress()
            ));

            // Mostro i dettagli dello scambio che ha chiuso lo scambio
            if (scambio.getStato() == Scambio.Stato.CHIUSO) {
                String chiusoDaString = this.formatScambio(scambio.getChiusoDa());
                System.out.println(this.translator.translate("scambio_page_chiuso_da_pattern", chiusoDaString));

                if (scambio.hasBeenNotified()) {
                    System.out.println(this.translator.translate("scambio_page_notifica_sent"));
                } else {
                    System.out.println(this.translator.translate("scambio_page_notifica_not_sent"));
                }
            }
        }

        System.out.println();
        System.out.println(this.translator.translate("scambio_page_storico_title"));
        List<StoricoScambio> storico = scambio.getStorico();
        for (int i = 0; i < storico.size(); i++) {
            StoricoScambio storicoScambio = storico.get(i);
            String data = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, this.translator.getLocale()).format(storicoScambio.getData());
            if (i == 0) { // Creazione della proposta
                System.out.println(this.translator.translate("scambio_page_storico_pattern_creation", data));
            } else { // Stato successivo
                StoricoScambio prevStoricoScambio = storico.get(i - 1);

                System.out.println(this.translator.translate("scambio_page_storico_pattern",
                        data,
                        prevStoricoScambio.getStato(),
                        storicoScambio.getStato()
                ));
            }
        }

        System.out.println();
    }

    private String formatScambio(Scambio scambio) {
        return this.translator.translate("scambio_pattern",
                scambio.getId(),
                scambio.getRichiesta().getNome(),
                scambio.getQuantitaRichiesta(),
                scambio.getOfferta().getNome(),
                scambio.getQuantitaOfferta(),
                scambio.getAutore().getUsername(),
                scambio.getAutore().getEmailAddress()
        );
    }
}