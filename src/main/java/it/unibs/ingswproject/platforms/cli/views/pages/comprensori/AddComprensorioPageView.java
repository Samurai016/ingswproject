package it.unibs.ingswproject.platforms.cli.views.pages.comprensori;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.platforms.cli.controllers.pages.comprensori.AddComprensorioPageController;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.platforms.cli.errors.exceptions.CliQuitException;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.router.PageConstructor;

/**
 * @author Nicolò Rebaioli
 */
public class AddComprensorioPageView extends CliPageView {
    protected final StorageService storageService;
    protected final ErrorManager errorManager;

    @PageConstructor
    public AddComprensorioPageView(CliApp app, AddComprensorioPageController controller, Translator translator, StorageService storageService, ErrorManager errorManager, CliUtils cliUtils, AuthService authService) {
        super(app, controller, translator, cliUtils, authService);
        this.storageService = storageService;
        this.errorManager = errorManager;
    }

    @Override
    public void renderContent() {
        System.out.println();

        try {
            Comprensorio comprensorio = new Comprensorio();

            String nome = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_comprensorio_page_name"), false);
            comprensorio.setNome(nome);

            System.out.println();
            System.out.println(this.translator.translate("saving_item"));
            this.storageService.getRepository(Comprensorio.class).save(comprensorio);

            System.out.println(this.translator.translate("add_comprensorio_page_success"));
            this.cliUtils.waitForInput();
        } catch (CliQuitException e) {
            // Non fare nulla, l'utente ha deciso di uscire
        } catch (Throwable e) {
            this.errorManager.handle(e);
        }
    }
}