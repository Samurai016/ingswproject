package it.unibs.ingswproject.view.cli.pages.comprensori;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPage;
import it.unibs.ingswproject.view.cli.CliUtils;
import it.unibs.ingswproject.view.cli.router.CliConstructor;

import java.util.Scanner;

/**
 * @author Nicolò Rebaioli
 */
public class AddComprensorioPage extends CliPage {
    protected AuthService authService;
    protected StorageService storageService;
    protected ErrorManager errorManager;

    @CliConstructor
    public AddComprensorioPage(CliApp app, Translator translator, AuthService authService, StorageService storageService, ErrorManager errorManager, CliUtils cliUtils) {
        super(app, translator, cliUtils);
        this.authService = authService;
        this.storageService = storageService;
        this.errorManager = errorManager;
    }

    @Override
    protected String getName() {
        return this.translator.translate("add_comprensorio_page_title");
    }

    @Override
    protected boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }

    @Override
    public void render() {
        System.out.println();

        try {
            Comprensorio comprensorio = new Comprensorio();

            String nome = this.cliUtils.readFromConsole(this.translator.translate("add_comprensorio_page_name"), false);
            comprensorio.setNome(nome);

            System.out.println();
            System.out.println(this.translator.translate("saving_item"));
            this.storageService.getRepository(Comprensorio.class).save(comprensorio);

            System.out.println(this.translator.translate("add_comprensorio_page_success"));
            this.cliUtils.waitForInput();
        } catch (Throwable e) {
            this.errorManager.handle(e);
        }

        this.app.goBack();
    }
}