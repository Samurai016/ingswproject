package it.unibs.ingswproject.platforms.cli.views.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Comprensorio;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.platforms.cli.CliApp;
import it.unibs.ingswproject.platforms.cli.controllers.pages.LoginPageController;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.platforms.cli.views.pages.comprensori.ComprensoriPageView;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;

/**
 * Pagina di login dell'applicazione
 * Si occupa di gestire il login dell'utente e di reindirizzarlo alla home
 * Se l'utente è già autenticato, viene reindirizzato alla home
 * Dovrebbe essere l'unico punto di accesso all'applicazione
 *
 * @author Nicolò Rebaioli
 */
public class LoginPageView extends CliPageView {
    protected final StorageService storageService;
    protected final PageFactory pageFactory;

    public LoginPageView(CliApp app, LoginPageController controller, Translator translator, AuthService authService, StorageService storageService, PageFactory pageFactory, CliUtils cliUtils, ProjectUtils projectUtils) {
        super(app, controller, translator, cliUtils, projectUtils, authService);
        this.storageService = storageService;
        this.pageFactory = pageFactory;
    }

    @Override
    public void renderContent() {
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

        System.out.println();
        System.out.println(this.translator.translate("login_page_login_success", this.authService.getCurrentUser().getUsername()));

        // Controllo se devo cambiare password
        // Il cambio password è obbligatorio solo per i CONFIGURATORI
        Utente currentUser = this.authService.getCurrentUser();
        if (currentUser.isConfiguratore() && !currentUser.hasMadeFirstLogin()) {
            this.askForNewPassword();
        }

        // Controllo se deve impostare l'indirizzo email
        // L'indirizzo email è obbligatorio solo per i FRUITORI
        if (currentUser.isFruitore()) {
            if (currentUser.getComprensorio() == null) {
                this.askForComprensorio();
                System.out.println();
            }

            if (currentUser.getEmailAddress() == null || currentUser.getEmailAddress().isEmpty()) {
                this.askForEmailAddress();
                System.out.println();
            }
        }

        // Reindirizzo alla home
        ((LoginPageController) this.controller).checkLogin();
    }

    private void askForNewPassword() {
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

    private void askForEmailAddress() {
        System.out.println(this.translator.translate("login_page_set_email_alert"));

        boolean emailSet = false;
        do {
            try {
                String newEmail = this.cliUtils.readFromConsole(this.translator.translate("login_page_insert_email"), false);

                if (newEmail.isEmpty()) {
                    System.out.println(this.translator.translate("login_page_email_empty"));
                    continue;
                }

                this.authService.getCurrentUser().setEmailAddress(newEmail);
                this.storageService.getRepository(Utente.class).save(this.authService.getCurrentUser());
                System.out.println(this.translator.translate("login_page_email_changed"));
                emailSet = true;
            } catch (IllegalArgumentException e) {
                System.out.println(this.translator.translate(e.getMessage()));
            }
        } while (!emailSet);
    }

    private void askForComprensorio() {
        System.out.println(this.translator.translate("login_page_set_comprensorio_alert"));

        // Show all comprensori
        ComprensoriPageView.renderComprensori(this.translator, this.storageService);

        boolean comprensorioSet = false;
        do {
            String nomeComprensorio = this.cliUtils.readFromConsole(this.translator.translate("login_page_insert_comprensorio"), false);

            if (nomeComprensorio.isEmpty()) {
                System.out.println(this.translator.translate("login_page_comprensorio_empty"));
                continue;
            }

            Comprensorio comprensorio = this.storageService.getRepository(Comprensorio.class).findByLike("nome", nomeComprensorio);
            if (comprensorio == null) {
                System.out.println(this.translator.translate("login_page_comprensorio_not_found"));
                continue;
            }

            this.authService.getCurrentUser().setComprensorio(comprensorio);
            this.storageService.getRepository(Utente.class).save(this.authService.getCurrentUser());
            System.out.println(this.translator.translate("login_page_comprensorio_changed"));
            comprensorioSet = true;
        } while (!comprensorioSet);
    }
}
