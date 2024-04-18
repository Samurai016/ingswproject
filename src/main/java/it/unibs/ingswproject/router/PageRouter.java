package it.unibs.ingswproject.router;

import it.unibs.ingswproject.controllers.PageController;

public interface PageRouter {
    /**
     * Naviga verso una pagina
     */
    PageController navigateTo(PageController page);

    /**
     * Torna indietro di una pagina
     */
    PageController goBack();
}
