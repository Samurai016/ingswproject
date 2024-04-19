package it.unibs.ingswproject.view.cli;

import it.unibs.ingswproject.controllers.cli.CliPageController;
import it.unibs.ingswproject.controllers.cli.pages.LoginPageController;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.router.PageRouter;
import it.unibs.ingswproject.view.AppInterface;
import it.unibs.ingswproject.router.PageFactory;

/**
 * Classe principale dell'applicazione CLI
 * Contiene la logica di navigazione tra le pagine
 *
 * @author Nicolò Rebaioli
 */
public class CliApp implements AppInterface {
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
            this.router.navigateTo(page);
            page.render();
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
     */
    public void goBack() {
        CliPageController page = (CliPageController) this.router.goBack();
        if (page != null) {
            page.render();
        }
    }

    /**
     * Restituisce il router dell'applicazione
     * @return Router dell'applicazione
     */
    public PageRouter getRouter() {
        return this.router;
    }
}
