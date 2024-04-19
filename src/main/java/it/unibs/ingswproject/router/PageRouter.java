package it.unibs.ingswproject.router;

import it.unibs.ingswproject.controllers.PageController;

import java.util.List;

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
     */
    void navigateTo(PageController page);

    /**
     * Torna indietro di una pagina
     *
     * @return la pagina a cui si è tornati
     */
    PageController goBack();

    /**
     * Pulisce la cronologia
     */
    void clearHistory();

    /**
     * Restituisce la cronologia
     *
     * @return la cronologia
     */
    List<PageController> getHistory();
}
