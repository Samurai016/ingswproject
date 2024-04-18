package it.unibs.ingswproject.view.cli.pages.comprensori;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPage;
import it.unibs.ingswproject.view.cli.CliUtils;
import it.unibs.ingswproject.view.cli.router.CliConstructor;
import it.unibs.ingswproject.view.cli.router.CliPageFactory;

import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class ComprensoriPage extends CliPage {
    protected AuthService authService;
    protected StorageService storageService;
    protected CliPageFactory pageFactory;

    @CliConstructor
    public ComprensoriPage(CliApp app, Translator translator, CliUtils cliUtils, AuthService authService, StorageService storageService, CliPageFactory pageFactory) {
        super(app, translator, cliUtils);
        this.authService = authService;
        this.storageService = storageService;
        this.pageFactory = pageFactory;

        this.commands.put('1', this.translator.translate("comprensori_page_command_add"));
    }

    @Override
    protected String getName() {
        return this.translator.translate("comprensori_page_title");
    }

    @Override
    protected boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }

    @Override
    protected void handleInput(char input) {
        super.handleInput(input); // Handle default commands

        try {
            if (input == '1') {
                this.app.navigateTo(this.pageFactory.generatePage(AddComprensorioPage.class));
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();

        // Show comprensori
        System.out.printf("%s:", Utils.capitalize(this.translator.translate("comprensorio_plural")));
        System.out.println();
        List<Comprensorio> comprensori = this.storageService.getRepository(Comprensorio.class).findAll();

        if (comprensori.isEmpty()) {
            System.out.printf("\t%s", this.translator.translate("no_items_found"));
            System.out.println();
        } else {
            for (int i = 0; i < comprensori.size(); i++) {
                Comprensorio comprensorio = comprensori.get(i);
                System.out.printf(this.translator.translate("comprensori_page_comprensorio_pattern"), i + 1, comprensorio.getNome());
                System.out.println();
            }
        }

        System.out.println();
    }
}