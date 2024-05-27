package it.unibs.ingswproject.platforms.cli.utils;

import it.unibs.ingswproject.controllers.PageController;
import it.unibs.ingswproject.platforms.cli.controllers.CliPageController;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe che si occupa di generare i breadcrumb a partire dalla history
 *
 * @author Nicolò Rebaioli
 */
public class BreadcrumbsGenerator {
    /**
     * Separatore tra i breadcrumb
     */
    public static final String SEPARATOR = " > ";
    /**
     * Numero massimo di elementi da visualizzare prima che vengano sostituiti con "..."
     */
    public static final int MAX_ITEMS = 5;
    protected final List<PageController> history;

    /**
     * Costruttore
     * @param history La history da cui generare i breadcrumb
     */
    public BreadcrumbsGenerator(List<PageController> history) {
        this.history = history;
    }

    /**
     * Ottiene i breadcrumb della history
     * @return Breadcrumb della history
     */
    public List<String> getBreadcrumbs() {
        // Genero breadcrumbs
        ArrayList<String> breadcrumbs = new ArrayList<>(this.history.stream().map(p -> ((CliPageController)p).getName()).toList());

        // Limito il numero di breadcrumbs
        if (breadcrumbs.size() > MAX_ITEMS) {
            // Ottengo gli ultimi MAX_ITEMS-1 elementi
            breadcrumbs = new ArrayList<>(breadcrumbs.subList(Math.max(0, breadcrumbs.size() - MAX_ITEMS + 1), breadcrumbs.size()));
            breadcrumbs.addFirst("...");
        }

        return breadcrumbs;
    }

    /**
     * Ottiene i breadcrumb della history come stringa utilizzando il separatore
     * @return Breadcrumb della history come stringa
     */
    public String getBreadcrumbsString() {
        return String.join(SEPARATOR, this.getBreadcrumbs());
    }
}
