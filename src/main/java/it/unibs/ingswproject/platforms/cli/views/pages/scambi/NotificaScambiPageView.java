package it.unibs.ingswproject.platforms.cli.views.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.models.repositories.ScambioRepository;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.scambi.NotificaScambiPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

import java.util.*;

/**
 * @author Nicolò Rebaioli
 */
public class NotificaScambiPageView extends CliPageView {
    protected final StorageService storageService;

    @PageConstructor
    public NotificaScambiPageView(CliApp app, NotificaScambiPageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, StorageService storageService, AuthService authService) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();

        // Controllo se ci sono scambi chiusi da notificare
        ScambioRepository scambioRepository = (ScambioRepository) this.storageService.getRepository(Scambio.class);
        List<Scambio> scambiDaNotificare = scambioRepository.findDaNotificare();
        if (scambiDaNotificare.isEmpty()) {
            return;
        }

        List<LinkedList<Scambio>> insiemiChiusi = this.ricostruisciInsiemiChiusi(scambiDaNotificare);
        System.out.println(this.translator.translate("notifica_scambi_page_notification", insiemiChiusi.size()));

        for (int i = 0; i < insiemiChiusi.size(); i++) {
            System.out.println();
            System.out.println(this.translator.translate("notifica_scambi_page_title_pattern", i + 1));

            LinkedList<Scambio> insiemeChiuso = insiemiChiusi.get(i);
            for (Scambio scambio : insiemeChiuso) {
                String proposta = this.formatScambio(scambio);
                String chiusoDa = this.formatScambio(scambio.getChiusoDa());

                System.out.println(this.translator.translate("notifica_scambi_page_proposta_pattern", proposta));
                System.out.println(this.translator.translate("notifica_scambi_page_chiusa_da_pattern", chiusoDa));
                System.out.println();
            }

            boolean hasNotified = this.cliUtils.askForConfirmation(this.translator.translate("notifica_scambi_page_ask_for_confirmation"));
            if (hasNotified) {
                scambioRepository.notificaInsiemeChiuso(insiemeChiuso);
            }

            System.out.println();
        }
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

    private List<LinkedList<Scambio>> ricostruisciInsiemiChiusi(List<Scambio> scambi) {
        List<LinkedList<Scambio>> insiemiChiusi = new ArrayList<>();
        Set<Scambio> visitati = new HashSet<>();

        for (Scambio scambio : scambi) {
            if (visitati.contains(scambio)) {
                continue;
            }

            LinkedList<Scambio> insiemeChiuso = new LinkedList<>();
            Scambio corrente = scambio;

            while (corrente != null && !visitati.contains(corrente)) {
                insiemeChiuso.add(corrente);
                visitati.add(corrente);
                corrente = corrente.getChiusoDa();
            }

            if (!insiemeChiuso.isEmpty()) {
                insiemiChiusi.add(insiemeChiuso);
            }
        }

        return insiemiChiusi;
    }
}