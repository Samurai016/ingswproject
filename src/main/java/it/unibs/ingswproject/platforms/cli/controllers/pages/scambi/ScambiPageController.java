package it.unibs.ingswproject.platforms.cli.controllers.pages.scambi;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.models.entities.Scambio;
import it.unibs.ingswproject.models.repositories.NodoRepository;
import it.unibs.ingswproject.models.repositories.ScambioRepository;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.scambi.ScambiPageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

import java.util.HashMap;
import java.util.List;

public class ScambiPageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;
    protected final StorageService storageService;
    protected final HashMap<Character, Scambio> scambi = new HashMap<>();
    /**
     * Se impostata a true, ogni volta che si accede alla pagina verranno ricaricati gli scambi
     * altrimenti verranno mantenuti quelli già presenti
     */
    protected boolean shouldRefreshScambi = true;

    @PageConstructor
    public ScambiPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, StorageService storageService, CliUtils cliUtils, ProjectUtils projectUtils) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.storageService = storageService;
        this.view = new ScambiPageView(app, this, translator, cliUtils, projectUtils, storageService, authService);

        List<Nodo> gerarchie = ((NodoRepository) storageService.getRepository(Nodo.class)).findGerarchie();
        if (!gerarchie.isEmpty() && this.authService.getCurrentUser().isFruitore()) {
            this.commands.put('a', this.translator.translate("scambi_page_command_add"));
        }

        // Get default scambi
        this.fetchScambi();
    }

    @Override
    public String getName() {
        return this.translator.translate("scambi_page_title");
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn();
    }

    public boolean isValidInput(char input) {
        return this.scambi.containsKey(input) || this.commands.containsKey(input);
    }

    public ScambiPageController setScambi(List<Scambio> scambi) {
        this.scambi.clear();
        for (int i = 0; i < scambi.size(); i++) {
            this.scambi.put((char) ('1' + i), scambi.get(i));
        }
        this.shouldRefreshScambi = false;
        return this;
    }

    public HashMap<Character, Scambio> getScambi() {
        if (this.shouldRefreshScambi) {
            this.fetchScambi();
        }
        return this.scambi;
    }

    private void fetchScambi() {
        List<Scambio> scambi = ((ScambioRepository) this.storageService.getRepository(Scambio.class))
                .findByAutore(this.authService.getCurrentUser())
                .stream()
                .sorted((s1, s2) -> s2.getDataCreazione().compareTo(s1.getDataCreazione()))
                .toList();
        this.setScambi(scambi);
        this.shouldRefreshScambi = true;
    }

    @Override
    public void handleInput(char input) {
        super.handleInput(input);

        if (this.scambi.containsKey(input)) {
            this.app.navigateTo(this.pageFactory.generatePage(ScambioPageController.class).setScambio(this.scambi.get(input)));
        } else if (this.commands.containsKey('a') && input == 'a') {
            this.app.navigateTo(this.pageFactory.generatePage(AddPropostaDiScambioPageController.class));
        }
    }
}
