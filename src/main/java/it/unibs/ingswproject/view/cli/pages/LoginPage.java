package it.unibs.ingswproject.view.cli.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPage;
import it.unibs.ingswproject.view.cli.router.CliConstructor;
import it.unibs.ingswproject.view.cli.router.CliPageFactory;

import java.util.Scanner;

/**
 * Pagina di login dell'applicazione
 * Si occupa di gestire il login dell'utente e di reindirizzarlo alla home
 * Se l'utente è già loggato, viene reindirizzato alla home
 * Dovrebbe essere l'unico punto di accesso all'applicazione
 *
 * @author Nicolò Rebaioli
 */
public class LoginPage extends CliPage {
    protected AuthService authService;
    protected StorageService storageService;
    protected CliPageFactory pageFactory;

    @CliConstructor
    public LoginPage(CliApp app, AuthService authService, StorageService storageService, CliPageFactory pageFactory) {
        super(app);
        this.authService = authService;
        this.storageService = storageService;
        this.pageFactory = pageFactory;
        this.checkLogin();
    }

    /**
     * Controlla se l'utente è già loggato e in caso positivo lo reindirizza alla home
     * Se l'utente è già loggato, la history viene cancellata
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
        return "Login";
    }

    @Override
    protected boolean canView() {
        return true;
    }

    @Override
    public void render() {
        System.out.println("Benvenuto! Effettua il login per accedere all'applicazione.");
        Scanner scanner = new Scanner(System.in);

        // Effettua il login
        boolean loggedIn;
        do {
            System.out.print("Inserisci il tuo username: ");
            String username = scanner.nextLine();
            System.out.print("Inserisci la tua password: ");
            String password = scanner.nextLine();

            System.out.println("\nEffettuo il login...");
            loggedIn = this.authService.login(username, password);
            if (!loggedIn) {
                System.out.println("Credenziali non valide. Riprova.\n");
            }
        } while (!loggedIn);

        // Controllo se devo cambiare password
        if (!this.authService.getCurrentUser().hasMadeFirstLogin()) {
            System.out.println("\nBenvenuto, " + this.authService.getCurrentUser().getUsername() + "!");
            System.out.println("Prima di continuare, è necessario cambiare la password.");

            boolean passwordChanged = false;
            do {
                System.out.print("Inserisci la nuova password: ");
                String newPassword = scanner.nextLine();

                if (newPassword.isEmpty()) {
                    System.out.println("\nLa password non può essere vuota. Riprova.");
                    continue;
                }

                System.out.print("Conferma la nuova password: ");
                String confirmPassword = scanner.nextLine();
                if (newPassword.equals(confirmPassword)) {
                    this.authService.getCurrentUser().changePassword(newPassword);
                    this.storageService.getRepository(Utente.class).update(this.authService.getCurrentUser());
                    System.out.println("\nPassword cambiata con successo!");
                    passwordChanged = true;
                } else {
                    System.out.println("\nLe password non corrispondono. Riprova.");
                }
            } while (!passwordChanged);
        }

        // Reindirizzo alla home
        this.checkLogin();
    }
}
