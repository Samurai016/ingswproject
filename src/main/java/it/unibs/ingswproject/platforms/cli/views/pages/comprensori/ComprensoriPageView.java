package it.unibs.ingswproject.platforms.cli.views.pages.comprensori;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.platforms.cli.controllers.pages.comprensori.ComprensoriPageController;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.router.PageConstructor;

import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class ComprensoriPageView extends CliPageView {
    protected final StorageService storageService;

    @PageConstructor
    public ComprensoriPageView(CliApp app, ComprensoriPageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, StorageService storageService, AuthService authService) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
    }

    @Override
    protected void beforeRender() {
        super.beforeRender();

        // Show comprensori
        renderComprensori(this.translator, this.storageService);

        System.out.println();
    }

    public static void renderComprensori(Translator translator, StorageService storageService) {
        System.out.printf("%s:", Utils.capitalize(translator.translate("comprensorio_plural")));
        System.out.println();
        List<Comprensorio> comprensori = storageService.getRepository(Comprensorio.class).findAll();

        if (comprensori.isEmpty()) {
            System.out.printf("\t%s", translator.translate("no_items_found"));
            System.out.println();
        } else {
            for (int i = 0; i < comprensori.size(); i++) {
                Comprensorio comprensorio = comprensori.get(i);
                System.out.println(translator.translate("comprensori_page_comprensorio_pattern", i + 1, comprensorio.getNome()));
            }
        }
    }
}