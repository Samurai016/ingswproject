package it.unibs.ingswproject.view.cli.router;

import it.unibs.ingswproject.view.cli.CliPage;

import java.util.List;
import java.util.Stack;

public class CliRouter {
    protected Stack<CliPage> history = new Stack<>();

    public CliPage navigateTo(CliPage page) {
        this.history.push(page);
        return page;
    }

    /**
     * Torna indietro di una pagina
     */
    public CliPage goBack() {
        if (this.history.size() <= 1) {
            return null;
        }
        this.history.pop(); // Remove current page from history
        return this.history.lastElement(); // Render previous page
    }

    public void clearHistory() {
        this.history.clear();
    }

    public List<CliPage> getHistory() {
        return this.history;
    }
}
