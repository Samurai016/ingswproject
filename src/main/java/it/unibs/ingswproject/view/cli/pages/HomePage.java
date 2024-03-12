package it.unibs.ingswproject.view.cli.pages;

import it.unibs.ingswproject.view.cli.App;
import it.unibs.ingswproject.view.cli.CliPage;
import it.unibs.ingswproject.view.cli.pages.comprensori.ComprensoriPage;

/**
 * Pagina iniziale dell'applicazione
 *
 * @author Nicolò Rebaioli
 */
public class HomePage extends CliPage {
    public HomePage(App app) {
        super(app);

        this.commands.put('0', "Esci"); // Override default command (0 -> Esci)
        this.commands.put('1', "Comprensori");
        this.commands.put('2', "Gerarchie");
    }

    @Override
    protected String getName() {
        return "Home";
    }

    @Override
    protected boolean canView() {
        return true;
    }

    @Override
    protected void handleInput(char input) {
        super.handleInput(input); // Handle default commands

        switch (input) {
            case '1':
                this.app.navigateTo(new ComprensoriPage(this.app));
                break;
            case '2':
                //this.app.navigateTo(new GerarchiePage(this.app));
                break;
        }
    }
}
