package it.unibs.ingswproject.platforms.cli.views.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.models.repositories.NodoRepository;
import it.unibs.ingswproject.models.repositories.ScambioRepository;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.scambi.ScambiPageController;
import it.unibs.ingswproject.platforms.cli.controllers.pages.scambi.ScambioPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;
import it.unibs.ingswproject.utils.Utils;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public void renderContent() {
        // Ogni pagina è composta in questo modo:
        // 2. Stampa dei comandi
        // 3. Richiesta di input
        // 4. Gestione dell'input
        this.beforeRender();

        // 2. Stampa dei comandi
        this.printCommands();

        this.afterRender();

        // 3. Richiesta di input
        System.out.println();
        String input;
        boolean isValidInput = false;
        do {
            input = this.cliUtils.readFromConsole(this.translator.translate("scambi_page_command_or_select_scambio"), false);
            if (!((ScambiPageController)this.controller).isValidInput(input.charAt(0))) {
                System.out.println(this.translator.translate("invalid_command"));
            } else {
                isValidInput = true;
            }
        } while (!isValidInput);

        // 4. Gestione dell'input
        this.controller.handleInput(input.charAt(0));
    }

    public void renderScambi() {
        // Controllo se ci sono gerarchie, altrimenti notifico che non è possibile creare scambi
        List<Nodo> gerarchie = ((NodoRepository) this.storageService.getRepository(Nodo.class)).findGerarchie();
        if (gerarchie.isEmpty()) {
            System.out.println(this.translator.translate("scambi_page_no_gerarchie_error"));
            System.out.println();
        }


        System.out.printf("%s:", Utils.capitalize(this.translator.translate("scambio_plural")));
        System.out.println();

        // Ottengo gli scambi dell'utente corrente
        HashMap<Character, Scambio> scambi = ((ScambiPageController) this.controller).getScambi();
        if (scambi.isEmpty()) {
            System.out.printf("\t%s", this.translator.translate("no_items_found"));
            System.out.println();
        } else {
            for (Map.Entry<Character, Scambio> entry : scambi.entrySet()) {
                Scambio scambio = entry.getValue();
                String dataCreazione = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, this.translator.getLocale()).format(scambio.getDataCreazione().toDate());
                System.out.println(this.translator.translate("scambi_page_scambio_pattern",
                        entry.getKey(),
                        dataCreazione,
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