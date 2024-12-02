package it.unibs.ingswproject.platforms.cli.views.pages.gerarchie;

import io.ebean.Transaction;
import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.logic.FattoreDiConversioneStrategy;
import it.unibs.ingswproject.logic.weight.WeightComputationStrategy;
import it.unibs.ingswproject.models.EntityRepository;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.gerarchie.AddNodoPageController;
import it.unibs.ingswproject.platforms.cli.errors.exceptions.CliQuitException;
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
public class AddNodoPageView extends CliPageView {
    protected final StorageService storageService;
    protected final ErrorManager errorManager;
    protected final FattoreDiConversioneStrategy fattoreDiConversioneStrategy;
    protected final WeightComputationStrategy weightComputationStrategy;

    @PageConstructor
    public AddNodoPageView(CliApp app, AddNodoPageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, StorageService storageService, ErrorManager errorManager, FattoreDiConversioneStrategy fattoreDiConversioneStrategy, WeightComputationStrategy weightComputationStrategy, AuthService authService) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
        this.errorManager = errorManager;
        this.fattoreDiConversioneStrategy = fattoreDiConversioneStrategy;
        this.weightComputationStrategy = weightComputationStrategy;
    }


    @Override
    public void renderContent() {
        try {
            AddNodoPageController controller = (AddNodoPageController) this.controller;
            Nodo root = controller.getRoot();
            Nodo nodo = root == null ? this.enterGerarchia() : this.enterFoglia(root);

            // Richiesta FDC
            if (root != null) {
                root.getFigli().add(nodo);
            }
            List<FattoreDiConversione> FDCsToSet = this.fattoreDiConversioneStrategy.getFattoriDiConversionToSet(root == null ? nodo : root);
            if (!FDCsToSet.isEmpty()) {
                System.out.println();
                System.out.println(this.translator.translate("add_node_page_gerarchia_step3"));
                System.out.println(this.translator.translate("add_node_page_gerarchia_step3_helper"));
                this.fillFDCs(FDCsToSet);
            }

            List<FattoreDiConversione> FDCsToDelete = this.fattoreDiConversioneStrategy.getFattoriDiConversionToDelete(root==null ? nodo : root);

            System.out.println();
            System.out.println(this.translator.translate("saving_item"));

            try (Transaction transaction = this.storageService.getDatabase().beginTransaction()) {
                this.storageService.getRepository(Nodo.class).save(nodo);
                EntityRepository<FattoreDiConversione> repository = this.storageService.getRepository(FattoreDiConversione.class);
                for (FattoreDiConversione fdc : FDCsToSet) {
                    repository.save(fdc);
                }
                for (FattoreDiConversione fdc : FDCsToDelete) {
                    repository.delete(fdc);
                }
                transaction.commit();
            }

            System.out.println(this.translator.translate(root == null ? "add_node_page_gerarchia_success" : "add_node_page_foglia_success"));
            this.cliUtils.waitForInput();
        } catch (CliQuitException e) {
            // Non fare nulla, l'utente ha deciso di uscire
        } catch (Throwable e) {
            this.errorManager.handle(e);
        }
    }

    protected Nodo enterGerarchia() throws CliQuitException {
        Nodo gerarchia = new Nodo();

        System.out.println(this.translator.translate("add_node_page_gerarchia_step1"));

        // Nome
        String nome = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_node_page_gerarchia_name"));
        gerarchia.setNome(nome);

        // Descrizione
        String descrizione = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_node_page_gerarchia_description"), true);
        gerarchia.setDescrizione(descrizione);

        // Nome attributo
        String nomeAttributo = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_node_page_gerarchia_attribute"), true);
        gerarchia.setNomeAttributo(nomeAttributo);

        if (!nomeAttributo.isEmpty()) {
            // Dominio
            System.out.println();
            System.out.println(this.translator.translate("add_node_page_gerarchia_dominio_helper"));
            String valoriAttributo = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_node_page_gerarchia_dominio"), true);

            if (!valoriAttributo.isEmpty()) {
                // Richiesta foglie
                System.out.println();
                System.out.println(this.translator.translate("add_node_page_gerarchia_step2"));
                for (String valore : valoriAttributo.split(",")) {
                    System.out.printf(this.translator.translate("add_node_page_gerarchia_foglia_header"), valore);
                    Nodo foglia = this.enterFoglia(gerarchia, new Nodo().setValoreAttributo(valore));
                    gerarchia.getFigli().add(foglia);
                }
            }
        }


        return gerarchia;
    }

    protected Nodo enterFoglia(Nodo root) throws CliQuitException {
        return this.enterFoglia(root, new Nodo());
    }

    protected Nodo enterFoglia(Nodo root, Nodo foglia) throws CliQuitException {
        foglia.setParent(root);

        if (root != null && root.getNomeAttributo() == null) {
            System.out.println(this.translator.translate("add_node_page_foglia_helper"));
            String nomeAttributo = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_node_page_foglia_attribute"));
            root.setNomeAttributo(nomeAttributo);
        }

        if (foglia.getValoreAttributo() == null) {
            String valoreAttributo = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_node_page_foglia_valore"));
            foglia.setValoreAttributo(valoreAttributo);
        }

        String nomeFoglia = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_node_page_foglia_name"));
        foglia.setNome(nomeFoglia);

        String descrizioneFoglia = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_node_page_foglia_description"), true);
        foglia.setDescrizione(descrizioneFoglia);

        return foglia;
    }

    protected void fillFDCs(List<FattoreDiConversione> FDCs) throws CliQuitException {
        for (FattoreDiConversione fdc : FDCs) {
            Double fattore;
            double maxWeight = this.weightComputationStrategy.getMaxAcceptedWeight();
            double minWeight = this.weightComputationStrategy.getMinAcceptedWeight();

            do {
                try {
                    String fattoreInput = this.cliUtils.readFromConsoleQuittable(String.format(
                            this.translator.translate("add_node_page_gerarchia_fdc_pattern"),
                            fdc.getNodo1().getNome(), fdc.getNodo2().getNome()
                    ));
                    fattore = Double.parseDouble(fattoreInput);

                    if (fattore<minWeight || fattore>maxWeight) {
                        throw new IllegalArgumentException(String.format(this.translator.translate("add_node_page_gerarchia_fdc_error_weight_range"), minWeight, maxWeight));
                    }

                    fdc.setFattore(fattore);
                } catch (NumberFormatException e) {
                    System.out.println(this.translator.translate("add_node_page_gerarchia_fdc_error"));
                    fattore = null;
                } catch (IllegalArgumentException e) {
                    System.out.println("\t" + Utils.getErrorMessage(this.translator, e));
                    fattore = null;
                }
            } while (fattore == null);
        }
    }
}