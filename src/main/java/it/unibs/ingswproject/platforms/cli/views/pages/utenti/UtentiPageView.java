package it.unibs.ingswproject.platforms.cli.views.pages.utenti;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.utenti.UtentiPageController;
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
public class UtentiPageView extends CliPageView {
    protected final StorageService storageService;

    @PageConstructor
    public UtentiPageView(CliApp app, UtentiPageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, StorageService storageService, AuthService authService) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();

        // Show utenti
        System.out.printf("%s:", Utils.capitalize(this.translator.translate("utente_plural")));
        System.out.println();
        List<Utente> utenti = this.storageService.getRepository(Utente.class).findAll();

        if (utenti.isEmpty()) {
            System.out.printf("\t%s", this.translator.translate("no_items_found"));
            System.out.println();
        } else {
            for (int i = 0; i < utenti.size(); i++) {
                Utente utente = utenti.get(i);
                if (utente.isConfiguratore()) {
                    System.out.printf(
                            this.translator.translate("utenti_page_configuratore_pattern"),
                            i + 1,
                            utente.getUsername(),
                            utente.getRuolo().name()
                    );
                } else {
                    System.out.printf(
                            this.translator.translate("utenti_page_fruitore_pattern"),
                            i + 1,
                            utente.getUsername(),
                            utente.getEmailAddress() != null ? utente.getEmailAddress() : this.translator.translate("utenti_page_email_not_set"),
                            utente.getRuolo().name()
                    );
                }
                System.out.println();
            }
        }

        System.out.println();
    }
}
