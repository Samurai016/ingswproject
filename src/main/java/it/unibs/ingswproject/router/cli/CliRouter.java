package it.unibs.ingswproject.router.cli;

import it.unibs.ingswproject.controllers.PageController;
import it.unibs.ingswproject.router.PageRouter;

import java.util.List;
import java.util.Stack;

public class CliRouter implements PageRouter {
    protected final Stack<PageController> history = new Stack<>();

    @Override
    public PageController navigateTo(PageController page) {
        this.history.push(page);
        return page;
    }


    @Override
    public PageController goBack() {
        if (this.history.size() <= 1) {
            return null;
        }
        this.history.pop(); // Remove current page from history
        return this.history.lastElement(); // Render previous page
    }

    public void clearHistory() {
        this.history.clear();
    }

    public List<PageController> getHistory() {
        return this.history;
    }
}
