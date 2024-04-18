package it.unibs.ingswproject.view.cli.pages.comprensori;

import it.unibs.ingswproject.controllers.cli.pages.comprensori.ComprensoriPageController;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPageView;
import it.unibs.ingswproject.utils.cli.CliUtils;
import it.unibs.ingswproject.router.PageConstructor;

import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class ComprensoriPageView extends CliPageView {
    protected StorageService storageService;

    @PageConstructor
    public ComprensoriPageView(CliApp app, ComprensoriPageController controller, Translator translator, CliUtils cliUtils, StorageService storageService) {
        super(app, controller, translator, cliUtils);
        this.storageService = storageService;
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