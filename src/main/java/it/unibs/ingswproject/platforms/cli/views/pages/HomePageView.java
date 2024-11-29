package it.unibs.ingswproject.platforms.cli.views.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.platforms.cli.controllers.pages.HomePageController;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.utils.ProjectUtils;

/**
 * Pagina iniziale dell'applicazione
 *
 * @author Nicolò Rebaioli
 */
public class HomePageView extends CliPageView {
    public HomePageView(CliApp app, HomePageController controller, Translator translator, CliUtils cliUtils, ProjectUtils projectUtils, AuthService authService) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
    }
}
