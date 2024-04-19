package it.unibs.ingswproject.router.cli;

import it.unibs.ingswproject.controllers.PageController;
import it.unibs.ingswproject.router.PageRouter;

import java.util.List;
import java.util.Stack;

/**
 * Classe che gestisce il router per applicazioni CLI
 *
 * @author Nicolò Rebaioli
 */
public class CliRouter implements PageRouter {
    protected final Stack<PageController> history = new Stack<>();

    @Override
    public void navigateTo(PageController page) {
        this.history.push(page);
    }


    @Override
    public PageController goBack() {
        if (this.history.size() <= 1) {
            return null;
        }
        this.history.pop(); // Remove current page from history
        return this.history.lastElement(); // Render previous page
    }

    /**
     * Pulisce la cronologia
     */
    @Override
    public void clearHistory() {
        this.history.clear();
    }

    /**
     * Restituisce la cronologia
     *
     * @return la cronologia
     */
    @Override
    public List<PageController> getHistory() {
        return this.history;
    }
}
