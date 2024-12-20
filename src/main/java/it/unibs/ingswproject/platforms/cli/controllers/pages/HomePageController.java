package it.unibs.ingswproject.platforms.cli.controllers.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.controllers.pages.comprensori.ComprensoriPageController;
import it.unibs.ingswproject.platforms.cli.controllers.pages.gerarchie.NodoPageController;
import it.unibs.ingswproject.platforms.cli.controllers.pages.scambi.ScambiPageController;
import it.unibs.ingswproject.platforms.cli.controllers.pages.utenti.UtentiPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.pages.HomePageView;
import it.unibs.ingswproject.router.PageConstructor;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

public class HomePageController extends CliPageController {
    protected final AuthService authService;
    protected final PageFactory pageFactory;

    @PageConstructor
    public HomePageController(CliApp app, Translator translator, AuthService authService, PageFactory pageFactory, CliUtils cliUtils, ProjectUtils projectUtils) {
        super(app, translator);
        this.authService = authService;
        this.pageFactory = pageFactory;
        this.view = new HomePageView(app, this, translator, cliUtils, projectUtils, authService);

        this.commands.put(CliPageController.COMMAND_BACK, this.translator.translate("home_page_command_exit")); // Override default command (0 -> Esci)

        // Aggiungi comandi in base al ruolo dell'utente
        switch (this.authService.getCurrentUser().getRuolo()) {
            case Utente.Ruolo.CONFIGURATORE:
                this.commands.put('1', this.translator.translate("home_page_command_comprensori")); // Aggiungi comando (1 -> Comprensori)
                this.commands.put('2', this.translator.translate("home_page_command_gerarchie")); // Aggiungi comando (2 -> Gerarchie)
                this.commands.put('3', this.translator.translate("home_page_command_utenti")); // Aggiungi comando (2 -> Gerarchie)
                this.commands.put('4', this.translator.translate("home_page_command_system_info")); // Aggiungi comando (3 -> Info sistema)
                break;
            case Utente.Ruolo.FRUITORE:
                this.commands.put('1', this.translator.translate("home_page_command_gerarchie")); // Aggiungi comando (1 -> Gerarchie)
                this.commands.put('2', this.translator.translate("home_page_command_scambi")); // Aggiungi comando (2 -> Scambi)
                this.commands.put('3', this.translator.translate("home_page_command_system_info")); // Aggiungi comando (3 -> Info sistema)
                break;
        }
    }

    @Override
    public String getName() {
        return this.translator.translate("home_page_title");
    }

    @Override
    public boolean canView() {
        return this.authService.isLoggedIn();
    }

    @Override
    public void handleInput(char input) {
        super.handleInput(input); // Handle default commands

        // Check if input is a valid command
        if (!this.commands.containsKey(input)) {
            return;
        }

        if (this.authService.getCurrentUser().isConfiguratore()) {
            switch (input) {
                case '1':
                    this.app.navigateTo(this.pageFactory.generatePage(ComprensoriPageController.class));
                    break;
                case '2':
                    this.app.navigateTo(this.pageFactory.generatePage(NodoPageController.class));
                    break;
                case '3':
                    this.app.navigateTo(this.pageFactory.generatePage(UtentiPageController.class));
                    break;
                case '4':
                    this.app.navigateTo(this.pageFactory.generatePage(SystemInfoPageController.class));
                    break;
            }
        } else {
            switch (input) {
                case '1':
                    this.app.navigateTo(this.pageFactory.generatePage(NodoPageController.class));
                    break;
                case '2':
                    this.app.navigateTo(this.pageFactory.generatePage(ScambiPageController.class));
                    break;
                case '3':
                    this.app.navigateTo(this.pageFactory.generatePage(SystemInfoPageController.class));
                    break;
            }
        }
    }
}
