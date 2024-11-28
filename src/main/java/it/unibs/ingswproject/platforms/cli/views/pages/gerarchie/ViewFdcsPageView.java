package it.unibs.ingswproject.platforms.cli.views.pages.gerarchie;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorHandler;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.repositories.FattoreDiConversioneRepository;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.gerarchie.ViewFdcsPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.Utils;

import java.util.List;

public class ViewFdcsPageView extends CliPageView {
    protected final StorageService storageService;
    protected final ErrorHandler errorHandler;

    @PageConstructor
    public ViewFdcsPageView(CliApp app, ViewFdcsPageController controller, Translator translator, CliUtils cliUtils, StorageService storageService, ErrorHandler errorHandler, AuthService authService) {
        super(app, controller, translator, cliUtils, authService);
        this.storageService = storageService;
        this.errorHandler = errorHandler;
    }


    @Override
    public void beforeRender() {
        super.beforeRender();

        ViewFdcsPageController controller = (ViewFdcsPageController) this.controller;
        Nodo root = controller.getRoot();

        // Show FDCs
        System.out.printf("%s:", Utils.capitalize(this.translator.translate("fdc_plural")));
        System.out.println();
        FattoreDiConversioneRepository repository = (FattoreDiConversioneRepository) this.storageService.getRepository(FattoreDiConversione.class);
        List<FattoreDiConversione> fdcs = repository.findByNodo(root);

        if (fdcs.isEmpty()) {
            System.out.printf("\t%s", this.translator.translate("no_items_found"));
            System.out.println();
        } else {
            for (FattoreDiConversione fdc : fdcs) {
                Nodo altroNodo = fdc.getNodo1().equals(root) ? fdc.getNodo2() : fdc.getNodo1();
                System.out.printf(
                        this.translator.translate("view_fdcs_page_fdc_pattern"),
                        root.getNome(),
                        altroNodo.getNome(),
                        fdc.getFattore(root)
                );
                System.out.println();
            }
        }

        System.out.println();
    }
}