package it.unibs.ingswproject.platforms.cli.views.pages.utenti;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.utenti.AddUtentePageController;
import it.unibs.ingswproject.platforms.cli.errors.exceptions.CliQuitException;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

/**
 * @author Nicolò Rebaioli
 */
public class AddUtentePageView extends CliPageView {
    protected final StorageService storageService;
    protected final ErrorManager errorManager;

    @PageConstructor
    public AddUtentePageView(CliApp app, AddUtentePageController controller, Translator translator, StorageService storageService, ErrorManager errorManager, CliUtils cliUtils, ProjectUtils projectUtils, AuthService authService) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
        this.errorManager = errorManager;
    }

    @Override
    public void renderContent() {
        try {
            Utente utente = new Utente();
            String password = utente.setRandomPassword();

            String username = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_utente_page_username"), false);
            utente.setUsername(username);

            utente.setRuolo(this.askForRuolo());

            System.out.println();
            System.out.println(this.translator.translate("saving_item"));
            this.storageService.getRepository(Utente.class).save(utente);

            System.out.println(this.translator.translate("add_utente_page_success", password));

            this.cliUtils.waitForInput();
        } catch (CliQuitException e) {
            // Non fare nulla, l'utente ha deciso di uscire
        } catch (Throwable e) {
            this.errorManager.handle(e);
        }
    }

    private Utente.Ruolo askForRuolo() throws CliQuitException {
        do {
            String nomeComprensorio = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_utente_page_ruolo"), false);

            if (nomeComprensorio.isEmpty()) {
                System.out.println(this.translator.translate("add_utente_page_ruolo_not_empty"));
                continue;
            }

            try {
                return Utente.Ruolo.valueOf(nomeComprensorio.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println(this.translator.translate("add_utente_page_ruolo_not_valid"));
            }
        } while (true);
    }
}