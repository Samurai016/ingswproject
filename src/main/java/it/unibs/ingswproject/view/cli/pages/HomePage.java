package it.unibs.ingswproject.view.cli.pages;

import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.entities.Utente;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.CliPage;
import it.unibs.ingswproject.view.cli.pages.comprensori.ComprensoriPage;
import it.unibs.ingswproject.view.cli.router.CliConstructor;
import it.unibs.ingswproject.view.cli.router.CliPageFactory;

/**
 * Pagina iniziale dell'applicazione
 *
 * @author Nicolò Rebaioli
 */
public class HomePage extends CliPage {
    protected AuthService authService;
    protected CliPageFactory pageFactory;

    @CliConstructor
    public HomePage(CliApp app, AuthService authService) {
        super(app);
        this.authService = authService;

        this.commands.put('0', "Esci"); // Override default command (0 -> Esci)

        // Aggiungi comandi in base al ruolo dell'utente
        switch (this.authService.getCurrentUser().getRuolo()) {
            case Utente.Ruolo.CONFIGURATORE:
                this.commands.put('1', "Comprensori");
                this.commands.put('2', "Gerarchie");
                break;
            case Utente.Ruolo.FRUITORE:
                break;
        }
    }

    @Override
    protected String getName() {
        return "Home";
    }

    @Override
    protected boolean canView() {
        return this.authService.isLoggedIn();
    }

    @Override
    protected void handleInput(char input) {
        super.handleInput(input); // Handle default commands

        try {
            switch (input) {
                case '1':
                    this.app.navigateTo(this.pageFactory.generatePage(ComprensoriPage.class));
                    break;
                case '2':
                    //this.app.navigateTo(new GerarchiePage(this.app));
                    break;
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
        }
    }
}
