package it.unibs.ingswproject.router;

import it.unibs.ingswproject.controllers.PageController;

/**
 * Interfaccia che definisce un router di pagine
 *
 * @author Nicolò Rebaioli
 */
public interface PageRouter {
    /**
     * Naviga verso una pagina
     *
     * @param page pagina verso cui navigare
     * @return la pagina a cui si è navigato
     */
    PageController navigateTo(PageController page);

    /**
     * Torna indietro di una pagina
     *
     * @return la pagina a cui si è tornati
     */
    PageController goBack();
}
