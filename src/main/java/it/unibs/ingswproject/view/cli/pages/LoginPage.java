package it.unibs.ingswproject.view.cli.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPage;
import it.unibs.ingswproject.view.cli.CliUtils;
import it.unibs.ingswproject.view.cli.router.CliConstructor;
import it.unibs.ingswproject.view.cli.router.CliPageFactory;

import java.util.Scanner;

/**
 * Pagina di login dell'applicazione
 * Si occupa di gestire il login dell'utente e di reindirizzarlo alla home
 * Se l'utente è già autenticato, viene reindirizzato alla home
 * Dovrebbe essere l'unico punto di accesso all'applicazione
 *
 * @author Nicolò Rebaioli
 */
public class LoginPage extends CliPage {
    protected AuthService authService;
    protected StorageService storageService;
    protected CliPageFactory pageFactory;

    @CliConstructor
    public LoginPage(CliApp app, Translator translator, AuthService authService, StorageService storageService, CliPageFactory pageFactory, CliUtils cliUtils) {
        super(app, translator, cliUtils);
        this.authService = authService;
        this.storageService = storageService;
        this.pageFactory = pageFactory;
        this.checkLogin();
    }

    /**
     * Controlla se l'utente è già autenticato e in caso positivo lo reindirizza alla home
     * Se l'utente è già autenticato, la history viene cancellata
     */
    protected void checkLogin() {
        try {
            if (this.authService.isLoggedIn()) {
                this.app.getRouter().clearHistory(); // Clear history
                this.app.navigateTo(this.pageFactory.generatePage(HomePage.class));
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getName() {
        return this.translator.translate("login_page_title");
    }

    @Override
    protected boolean canView() {
        return true;
    }

    @Override
    public void render() {
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
