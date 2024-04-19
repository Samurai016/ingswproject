package it.unibs.ingswproject.view.cli.pages.gerarchie;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.controllers.cli.pages.gerarchie.AddNodoPageController;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.logic.FattoreDiConversioneStrategy;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.FattoreDiConversione;
import it.unibs.ingswproject.models.entities.Nodo;
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
public class AddNodoPageView extends CliPageView {
    protected final StorageService storageService;
    protected final ErrorManager errorManager;
    protected final FattoreDiConversioneStrategy fattoreDiConversioneStrategy;
    protected final Nodo root;

    @PageConstructor
    public AddNodoPageView(CliApp app, AddNodoPageController controller, Translator translator, CliUtils cliUtils, StorageService storageService, ErrorManager errorManager, FattoreDiConversioneStrategy fattoreDiConversioneStrategy, AuthService authService) {
        super(app, controller, translator, cliUtils, authService);
        this.storageService = storageService;
        this.errorManager = errorManager;
        this.fattoreDiConversioneStrategy = fattoreDiConversioneStrategy;
        this.root = controller.getRoot();
    }


    @Override
    public void renderContent() {
        try {
            Nodo nodo = this.root == null ? this.enterGerarchia() : this.enterFoglia();
            System.out.println();
            System.out.println(this.translator.translate("saving_item"));
            this.storageService.getRepository(Nodo.class).save(nodo);

            if (this.root == null) {
                System.out.println(this.translator.translate("add_node_page_gerarchia_success"));
            } else {
                System.out.println(this.translator.translate("add_node_page_foglia_success"));
            }
            this.cliUtils.waitForInput();
        } // Operazione annullata, torna indietro
        catch (Throwable e) {
            this.errorManager.handle(e);
        }
    }

    protected Nodo enterGerarchia() {
        Nodo gerarchia = new Nodo();

        System.out.println(this.translator.translate("add_node_page_gerarchia_step1"));

        // Nome
        String nome = this.cliUtils.readFromConsole(this.translator.translate("add_node_page_gerarchia_name"));
        gerarchia.setNome(nome);

        // Descrizione
        String descrizione = this.cliUtils.readFromConsole(this.translator.translate("add_node_page_gerarchia_description"));
        gerarchia.setDescrizione(descrizione);

        // Nome attributo
        String nomeAttributo = this.cliUtils.readFromConsole(this.translator.translate("add_node_page_gerarchia_attribute"));
        gerarchia.setNomeAttributo(nomeAttributo);

        // Dominio
        System.out.println(this.translator.translate("add_node_page_gerarchia_dominio_helper"));
        String valoriAttributo = this.cliUtils.readFromConsole(this.translator.translate("add_node_page_gerarchia_dominio"));

        if (!valoriAttributo.isEmpty()) {
            // Richiesta foglie
            System.out.println(this.translator.translate("add_node_page_gerarchia_step2"));
            for (String valore : valoriAttributo.split(",")) {
                System.out.printf(this.translator.translate("add_node_page_gerarchia_foglia_header"), valore);
                Nodo foglia = this.enterFoglia(new Nodo().setValoreAttributo(valore));
                gerarchia.getFigli().add(foglia);
            }
        }

        // FDC
        // TODO: Implementare richiesta FDC
        List<FattoreDiConversione> FDCs = this.fattoreDiConversioneStrategy.getFattoriDiConversionToSet(gerarchia);
        if (!FDCs.isEmpty()) {
            System.out.println(this.translator.translate("add_node_page_gerarchia_step3"));
            System.out.println(this.translator.translate("add_node_page_gerarchia_step3_helper"));
            for (FattoreDiConversione fdc : FDCs) {
                Double fattore;
                do {
                    try {
                        String fattoreInput = this.cliUtils.readFromConsole(String.format(
                                this.translator.translate("add_node_page_gerarchia_fdc_pattern"),
                                fdc.getNodo1().getNome(), fdc.getNodo2().getNome()
                        ));
                        fattore = Double.parseDouble(fattoreInput);
                        fdc.setFattore(fattore);
                    } catch (NumberFormatException e) {
                        System.out.println(this.translator.translate("add_node_page_gerarchia_fdc_error"));
                        fattore = null;
                    } catch (IllegalArgumentException e) {
                        System.out.println("\t" + Utils.getErrorMessage(e));
                        fattore = null;
                    }
                } while (fattore == null);
            }
        }

        return gerarchia;
    }

    protected Nodo enterFoglia() {
        return this.enterFoglia(new Nodo());
    }

    protected Nodo enterFoglia(Nodo foglia) {
        foglia.setParent(this.root);

        if (this.root != null && this.root.getNomeAttributo() == null) {
            System.out.println(this.translator.translate("add_node_page_foglia_helper"));
            String nomeAttributo = this.cliUtils.readFromConsole(this.translator.translate("add_node_page_foglia_attribute"));
            this.root.setNomeAttributo(nomeAttributo);
        }

        if (foglia.getValoreAttributo() == null) {
            String valoreAttributo = this.cliUtils.readFromConsole(this.translator.translate("add_node_page_foglia_valore"));
            foglia.setValoreAttributo(valoreAttributo);
        }

        String nomeFoglia = this.cliUtils.readFromConsole(this.translator.translate("add_node_page_foglia_name"));
        foglia.setNome(nomeFoglia);

        String descrizioneFoglia = this.cliUtils.readFromConsole(this.translator.translate("add_node_page_foglia_description"), true);
        foglia.setDescrizione(descrizioneFoglia);

        return foglia;
    }
}