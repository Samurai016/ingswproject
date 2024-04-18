package it.unibs.ingswproject.view.cli.pages.comprensori;

import it.unibs.ingswproject.controllers.cli.pages.comprensori.AddComprensorioPageController;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPageView;
import it.unibs.ingswproject.utils.cli.CliUtils;
import it.unibs.ingswproject.router.PageConstructor;

/**
 * @author Nicolò Rebaioli
 */
public class AddComprensorioPageView extends CliPageView {
    protected StorageService storageService;
    protected ErrorManager errorManager;

    @PageConstructor
    public AddComprensorioPageView(CliApp app, AddComprensorioPageController controller, Translator translator, StorageService storageService, ErrorManager errorManager, CliUtils cliUtils) {
        super(app, controller, translator, cliUtils);
        this.storageService = storageService;
        this.errorManager = errorManager;
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