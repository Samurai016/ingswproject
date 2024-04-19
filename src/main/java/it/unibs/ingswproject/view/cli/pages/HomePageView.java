package it.unibs.ingswproject.view.cli.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.controllers.cli.pages.HomePageController;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPageView;
import it.unibs.ingswproject.utils.cli.CliUtils;

/**
 * Pagina iniziale dell'applicazione
 *
 * @author Nicolò Rebaioli
 */
public class HomePageView extends CliPageView {
    public HomePageView(CliApp app, HomePageController controller, Translator translator, CliUtils cliUtils, AuthService authService) {
        super(app, controller, translator, cliUtils, authService);
    }
}
