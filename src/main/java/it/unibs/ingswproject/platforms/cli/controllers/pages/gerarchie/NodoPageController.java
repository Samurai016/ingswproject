package it.unibs.ingswproject.platforms.cli.controllers.pages.gerarchie;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.repositories.NodoRepository;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Nodo;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.Utils;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.gerarchie.NodoPageView;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class NodoPageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;
    protected final StorageService storageService;
    protected Nodo root;
    protected final HashMap<Character, Nodo> nodi = new HashMap<>();

    @PageConstructor
    public NodoPageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, CliUtils cliUtils, StorageService storageService) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.storageService = storageService;
        this.view = new NodoPageView(app, this, translator, cliUtils, authService);
    }

    public NodoPageController setRoot(Nodo root) {
        this.root = root;
        return this;
    }

    public Nodo getRoot() {
        return this.root;
    }

    public Collection<Nodo> getNodi() {
        return this.nodi.values();
    }

    public boolean isValidInput(char input) {
        return this.nodi.containsKey(input) || this.commands.containsKey(input);
    }

    @Override
    public void render() {
        NodoRepository repository = (NodoRepository) this.storageService.getRepository(Nodo.class);
        this.nodi.clear();
        if (this.getRoot() == null) {
            this.commands.put('a', this.translator.translate("gerarchie_page_command_add_gerarchia"));

            List<Nodo> gerarchie = repository.findGerarchie();
            for (int i = 0; i < gerarchie.size(); i++) {
                this.nodi.put((char) ('1' + i), gerarchie.get(i));
            }
        } else {
            this.commands.put('a', this.translator.translate("gerarchie_page_command_add_foglia"));

            this.setRoot(repository.find(this.getRoot().getId())); // Refresh root
            List<Nodo> figli = this.getRoot().getFigli();
            for (int i = 0; i < figli.size(); i++) {
                this.nodi.put((char) ('1' + i), figli.get(i));
            }
        }

        super.render();
    }

    @Override
    public String getName() {
        return this.root == null ? Utils.capitalize(this.translator.translate("gerarchia_plural")) : this.root.getNome();
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn() && this.authService.getCurrentUser().isConfiguratore();
    }

    @Override
    public void handleInput(char input) {
        super.handleInput(input);

        if (this.nodi.containsKey(input)) {
            this.app.navigateTo(this.pageFactory.generatePage(NodoPageController.class).setRoot(this.nodi.get(input)));
        } else {
            if (input == 'a') {
                this.app.navigateTo(this.pageFactory.generatePage(AddNodoPageController.class).setRoot(this.root));
            }
        }
    }
}
