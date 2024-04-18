package it.unibs.ingswproject.view.cli.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.controllers.cli.pages.HomePageController;
import it.unibs.ingswproject.controllers.cli.pages.LoginPageController;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPageView;
import it.unibs.ingswproject.utils.cli.CliUtils;
import it.unibs.ingswproject.router.PageFactory;

/**
 * Pagina di login dell'applicazione
 * Si occupa di gestire il login dell'utente e di reindirizzarlo alla home
 * Se l'utente è già autenticato, viene reindirizzato alla home
 * Dovrebbe essere l'unico punto di accesso all'applicazione
 *
 * @author Nicolò Rebaioli
 */
public class LoginPageView extends CliPageView {
    protected AuthService authService;
    protected StorageService storageService;
    protected PageFactory pageFactory;

    public LoginPageView(CliApp app, LoginPageController controller, Translator translator, AuthService authService, StorageService storageService, PageFactory pageFactory, CliUtils cliUtils) {
        super(app, controller, translator, cliUtils);
        this.authService = authService;
        this.storageService = storageService;
        this.pageFactory = pageFactory;
    }

    /**
     * Controlla se l'utente è già autenticato e in caso positivo lo reindirizza alla home
     * Se l'utente è già autenticato, la history viene cancellata
     */
    protected void checkLogin() {
        if (this.authService.isLoggedIn()) {
            this.app.getRouter().clearHistory(); // Clear history
            this.app.navigateTo(this.pageFactory.generatePage(HomePageController.class));
        }
    }

    @Override
    public void render() {
        this.checkLogin();
        System.out.println(this.translator.translate("login_page_welcome"));

        // Effettua il login
        boolean loggedIn;
        do {
            String username = this.cliUtils.readFromConsole(this.translator.translate("login_page_insert_username"), false);
            String password = this.cliUtils.readFromConsole(this.translator.translate("login_page_insert_password"), false);

            System.out.println();
            System.out.println(this.translator.translate("login_page_loading_login"));
            loggedIn = this.authService.login(username, password);
            if (!loggedIn) {
                System.out.println(this.translator.translate("login_page_login_error"));
            }
        } while (!loggedIn);

        // Controllo se devo cambiare password
        if (!this.authService.getCurrentUser().hasMadeFirstLogin()) {
            System.out.println();
            System.out.printf((this.translator.translate("login_page_login_success")) + "\n", this.authService.getCurrentUser().getUsername());
            System.out.println(this.translator.translate("login_page_change_password_alert"));

            boolean passwordChanged = false;
            do {
                String newPassword = this.cliUtils.readFromConsole(this.translator.translate("login_page_insert_new_password"), false);

                if (newPassword.isEmpty()) {
                    System.out.println();
                    System.out.println(this.translator.translate("login_page_password_empty"));
                    continue;
                }

                String confirmPassword = this.cliUtils.readFromConsole(this.translator.translate("login_page_insert_new_password_confirm"), false);
                System.out.println();
                if (newPassword.equals(confirmPassword)) {
                    this.authService.getCurrentUser().changePassword(newPassword);
                    this.storageService.getRepository(Utente.class).save(this.authService.getCurrentUser());
                    System.out.println(this.translator.translate("login_page_password_changed"));
                    passwordChanged = true;
                } else {
                    System.out.println(this.translator.translate("login_page_password_mismatch"));
                }
            } while (!passwordChanged);
        }

        // Reindirizzo alla home
        this.checkLogin();
    }
}
