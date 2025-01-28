package it.unibs.ingswproject.platforms.cli.views.pages.gerarchie;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.logic.routing.RoutingComputationStrategy;
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
import it.unibs.ingswproject.utils.ProjectUtils;
import it.unibs.ingswproject.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ViewFdcsPageView extends CliPageView {
    protected final StorageService storageService;
    protected final ErrorManager errorManager;
    protected final RoutingComputationStrategy routingComputationStrategy;

    @PageConstructor
    public ViewFdcsPageView(CliApp app, ViewFdcsPageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, StorageService storageService, ErrorManager errorManager, AuthService authService, RoutingComputationStrategy routingComputationStrategy) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
        this.errorManager = errorManager;
        this.routingComputationStrategy = routingComputationStrategy;
    }

    @Override
    public void beforeRender() {
        super.beforeRender();

        ViewFdcsPageController controller = (ViewFdcsPageController) this.controller;
        Nodo root = controller.getRoot();

        // Show FDCs
        System.out.printf("%s:", Utils.capitalize(this.translator.translate("fdc_plural")));
        System.out.println();
        List<FattoreDiConversione> fdcs = this.getFDCsToView(root);

        if (fdcs.isEmpty()) {
            System.out.printf("\t%s", this.translator.translate("no_items_found"));
            System.out.println();
        } else {
            for (FattoreDiConversione fdc : fdcs) {
                double fattore = fdc.getFattore(root);
                if (fattore > FattoreDiConversione.MAX_TRESHOLD_BEFORE_INFINITE) {
                    fattore = Double.POSITIVE_INFINITY;
                }

                Nodo altroNodo = fdc.getNodo1().equals(root) ? fdc.getNodo2() : fdc.getNodo1();
                boolean isOfAltraGerarchia = altroNodo.getRoot() != root.getRoot();
                String nomeAltroNodo = !isOfAltraGerarchia
                    ? altroNodo.getNome()
                    : String.format(
                        this.translator.translate("view_fdcs_page_fdc_pattern_other_root"),
                        altroNodo.getNome(),
                        altroNodo.getRoot().getNome()
                    );

                System.out.println(this.translator.translate("view_fdcs_page_fdc_pattern", root.getNome(), nomeAltroNodo, fattore));
            }
        }

        System.out.println();
    }

    private List<FattoreDiConversione> getFDCsToView(Nodo root) {
        List<FattoreDiConversione> fdcs = new ArrayList<>();

        // Calcolo tutti gli FDC
        Map<Nodo, Double> distances = this.routingComputationStrategy.getRoutingCostsFrom(root);
        for (Nodo nodo : distances.keySet()) {
            if (nodo.equals(root)) {
                continue;
            }

            fdcs.add(new FattoreDiConversione(root, nodo, distances.get(nodo)));
        }

        return fdcs;
    }
}