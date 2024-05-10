package it.unibs.ingswproject.platforms.cli;

import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;
import it.unibs.ingswproject.platforms.cli.controllers.pages.LoginPageController;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.router.PageRouter;
import it.unibs.ingswproject.view.Application;
import it.unibs.ingswproject.router.PageFactory;

/**
 * Classe principale dell'applicazione CLI
 * Contiene la logica di navigazione tra le pagine
 *
 * @author Nicolò Rebaioli
 */
public class CliApp implements Application {
    protected final PageRouter router;
    protected final PageFactory pageFactory;
    protected final ErrorManager errorManager;

    public CliApp(PageRouter router, PageFactory pageFactory, ErrorManager errorManager) {
        this.router = router;
        this.pageFactory = pageFactory;
        this.errorManager = errorManager;
    }

    /**
     * Avvia l'applicazione
     */
    public void run() {
        try {
            CliPageController page = this.pageFactory.generatePage(LoginPageController.class);
            this.navigateTo(page);

            // Loop principale dell'applicazione
            // noinspection StatementWithEmptyBody
            while (this.goBack()) {
                // Questo serve a fare si che l'applicazione termini solo quando l'utente esce dall'ultima pagina della cronologia
            }
        } catch (Throwable e) {
            this.errorManager.handle(e);
        }
    }

    /**
     * Naviga verso una pagina e la renderizza
     * @param page Pagina da renderizzare
     */
    public void navigateTo(CliPageController page) {
        this.router.navigateTo(page);
        page.render();
    }

    /**
     * Torna indietro di una pagina
     *
     * @return true se è possibile tornare indietro, false altrimenti
     */
    public boolean goBack() {
        CliPageController page = (CliPageController) this.router.goBack();
        if (page != null) {
            page.render();
            return true;
        }
        return false;
    }

    /**
     * Restituisce il router dell'applicazione
     * @return Router dell'applicazione
     */
    public PageRouter getRouter() {
        return this.router;
    }
}
