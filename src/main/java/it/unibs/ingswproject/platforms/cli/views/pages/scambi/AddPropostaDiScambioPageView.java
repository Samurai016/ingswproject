package it.unibs.ingswproject.platforms.cli.views.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.logic.routing.RoutingComputationStrategy;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.models.repositories.NodoRepository;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.scambi.AddPropostaDiScambioPageController;
import it.unibs.ingswproject.platforms.cli.elements.TreeRenderer;
import it.unibs.ingswproject.platforms.cli.errors.exceptions.CliQuitException;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.platforms.cli.views.components.NodoSelector;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;
import it.unibs.ingswproject.utils.Utils;

import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class AddPropostaDiScambioPageView extends CliPageView {
    protected final StorageService storageService;
    protected final ErrorManager errorManager;
    protected final RoutingComputationStrategy routingComputationStrategy;
    protected final TreeRenderer treeRenderer;

    @PageConstructor
    public AddPropostaDiScambioPageView(CliApp app, AddPropostaDiScambioPageController controller, Translator translator, StorageService storageService, ErrorManager errorManager, CliUtils cliUtils, ProjectUtils projectUtils, AuthService authService, RoutingComputationStrategy routingComputationStrategy, TreeRenderer treeRenderer) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
        this.errorManager = errorManager;
        this.routingComputationStrategy = routingComputationStrategy;
        this.treeRenderer = treeRenderer;
    }

    @Override
    public void renderContent() {
        try {
            Scambio scambio = new Scambio();

            scambio.setRichiesta(this.askForRichiesta());

            boolean isValidQuantita = false;
            int quantitaRichiesta = 0;
            do {
                try {
                    String quantita = this.cliUtils.readFromConsoleQuittable(this.translator.translate("add_proposta_di_scambio_page_quantita"), false);
                    quantitaRichiesta = Integer.parseInt(quantita);
                    if (quantitaRichiesta <= 0) {
                        throw new IllegalArgumentException();
                    }
                    isValidQuantita = true;
                } catch (IllegalArgumentException e) {
                    System.out.println(this.translator.translate("add_proposta_di_scambio_page_quantita_error"));
                }
            } while (!isValidQuantita);

            scambio.setOfferta(this.askForOfferta());
            scambio.setQuantitaRichiesta(quantitaRichiesta, this.routingComputationStrategy);

            // Notifico all'utente la quantità di offerta
            System.out.println();
            System.out.printf(
                    this.translator.translate("add_proposta_di_scambio_page_quantita_offerta"),
                    scambio.getQuantitaOfferta(),
                    scambio.getOfferta().getNome()
            );

            // Chiedo se l'utente vuole confermare
            boolean conferma = this.cliUtils.askForConfirmation(this.translator.translate("add_proposta_di_scambio_page_confirm"));

            if (!conferma) {
                throw new CliQuitException();
            }

            System.out.println(this.translator.translate("saving_item"));
            this.storageService.getRepository(Scambio.class).save(scambio);

            System.out.printf((this.translator.translate("add_proposta_di_scambio_page_success")) + "%n");
            System.out.println();
            this.cliUtils.waitForInput();
        } catch (CliQuitException e) {
            // Non fare nulla, l'utente ha deciso di uscire
        } catch (Throwable e) {
            this.errorManager.handle(e);
        }
    }

    private Nodo askForRichiesta() throws CliQuitException {
        // Chiedo all'utente di scegliere il nodo che vuole richiedere
        List<Nodo> gerarchie = ((NodoRepository) this.storageService.getRepository(Nodo.class)).findGerarchie();
        Nodo root = new Nodo();
        gerarchie.forEach(gerarchia -> gerarchia.setParent(root));
        root.setNome(Utils.capitalize(this.translator.translate("gerarchia_plural")));
        root.setFigli(gerarchie);
        NodoSelector nodoSelector = new NodoSelector(root, this.treeRenderer, this.cliUtils, this.translator);
        nodoSelector.setValidator(Nodo::isFoglia);
        return nodoSelector.select();
    }

    private Nodo askForOfferta() throws CliQuitException {
        return null;
    }
}