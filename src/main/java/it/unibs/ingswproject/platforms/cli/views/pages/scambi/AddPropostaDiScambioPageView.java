package it.unibs.ingswproject.platforms.cli.views.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.logic.ScambioStrategy;
import it.unibs.ingswproject.logic.routing.RoutingComputationStrategy;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.models.repositories.NodoRepository;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.components.NodoSelector;
import it.unibs.ingswproject.platforms.cli.components.TreeRenderer;
import it.unibs.ingswproject.platforms.cli.controllers.pages.scambi.AddPropostaDiScambioPageController;
import it.unibs.ingswproject.platforms.cli.errors.exceptions.CliQuitException;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;
import it.unibs.ingswproject.utils.Utils;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Nicolò Rebaioli
 */
public class AddPropostaDiScambioPageView extends CliPageView {
    protected final StorageService storageService;
    protected final ErrorManager errorManager;
    protected final RoutingComputationStrategy routingComputationStrategy;
    protected final TreeRenderer treeRenderer;
    protected final ScambioStrategy scambioStrategy;

    @PageConstructor
    public AddPropostaDiScambioPageView(CliApp app, AddPropostaDiScambioPageController controller, Translator translator, StorageService storageService, ErrorManager errorManager, CliUtils cliUtils, ProjectUtils projectUtils, AuthService authService, RoutingComputationStrategy routingComputationStrategy, TreeRenderer treeRenderer, ScambioStrategy scambioStrategy) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
        this.errorManager = errorManager;
        this.routingComputationStrategy = routingComputationStrategy;
        this.treeRenderer = treeRenderer;
        this.scambioStrategy = scambioStrategy;
    }

    @Override
    public void renderContent() {
        try {
            Scambio scambio = new Scambio(this.authService.getCurrentUser());

            Nodo nodoRichiesta = this.askForNodo(this.translator.translate("add_proposta_di_scambio_page_select_richiesta"), null);
            scambio.setRichiesta(nodoRichiesta);
            System.out.println();

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

            System.out.println();
            scambio.setOfferta(this.askForNodo(this.translator.translate("add_proposta_di_scambio_page_select_offerta"), nodoRichiesta));
            scambio.setQuantitaRichiesta(quantitaRichiesta, this.routingComputationStrategy);

            // Notifico all'utente la quantità di offerta
            System.out.println();
            System.out.println(this.translator.translate("add_proposta_di_scambio_page_quantita_offerta", scambio.getQuantitaOfferta(), scambio.getOfferta().getNome()));

            // Chiedo se l'utente vuole confermare
            System.out.println();
            boolean conferma = this.cliUtils.askForConfirmation(this.translator.translate("add_proposta_di_scambio_page_confirm"));

            if (!conferma) {
                throw new CliQuitException();
            }

            System.out.println(this.translator.translate("saving_item"));
            this.storageService.getRepository(Scambio.class).save(scambio);

            System.out.println((this.translator.translate("add_proposta_di_scambio_page_success")));

            // Chiudo gli scambi
            List<LinkedList<Scambio>> insiemiChiusi = this.scambioStrategy.chiudiScambi();
            boolean currentScambioClosed = insiemiChiusi
                    .stream()
                    .anyMatch(insieme -> insieme.contains(scambio));
            if (currentScambioClosed) {
                System.out.println(this.translator.translate("add_proposta_di_scambio_page_success_closed"));
            }

            this.cliUtils.waitForInput();
        } catch (CliQuitException e) {
            // Non fare nulla, l'utente ha deciso di uscire
        } catch (Throwable e) {
            this.errorManager.handle(e);
        }
    }

    /**
     * Chiede all'utente di selezionare un nodo
     *
     * @param prompt Messaggio da mostrare all'utente
     * @param nodoNotToBeEqual Nodo che non deve essere selezionato
     * @return Nodo selezionato
     * @throws CliQuitException Eccezione lanciata quando l'utente decide di uscire
     */
    private Nodo askForNodo(String prompt, Nodo nodoNotToBeEqual) throws CliQuitException {
        // Ottengo le varie gerarchie
        List<Nodo> gerarchie = ((NodoRepository) this.storageService.getRepository(Nodo.class)).findGerarchie();
        Nodo root = new Nodo();
        gerarchie.forEach(gerarchia -> gerarchia.setParent(root));
        root.setNome(Utils.capitalize(this.translator.translate("gerarchia_plural")));
        root.setFigli(gerarchie);

        // Seleziono il nodo, possono essere selezionati solo i nodi foglia
        NodoSelector nodoSelector = new NodoSelector(root, this.treeRenderer, this.cliUtils, this.translator, true);
        nodoSelector.setValidator((n) -> n.isFoglia() && !n.isRoot() && (nodoNotToBeEqual == null || !nodoNotToBeEqual.equals(n)));
        nodoSelector.setInitialPromptMessage(prompt);

        return nodoSelector.select();
    }
}