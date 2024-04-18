package it.unibs.ingswproject;

import io.ebean.DB;
import io.ebean.Database;
import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.view.cli.router.CliPageFactory;
import it.unibs.ingswproject.view.cli.router.CliRouter;

public class Main {
    public static void main(String[] args) {
        // Disabilito il logging di Ebean
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        // Avvio l'applicazione
        CliPageFactory pageFactory = new CliPageFactory();
        CliRouter router = new CliRouter();
        Database database = DB.getDefault();
        StorageService storageService = new StorageService(database);
        AuthService authService = new AuthService(storageService);
        CliApp app = new CliApp(router, pageFactory, authService);

        // Registrazione delle dipendenze
        pageFactory.registerDependency(Database.class, database);
        pageFactory.registerDependency(AuthService.class, authService);
        pageFactory.registerDependency(StorageService.class, storageService);
        pageFactory.registerDependency(CliPageFactory.class, pageFactory);
        pageFactory.registerDependency(CliRouter.class, router);
        pageFactory.registerDependency(CliApp.class, app);

        app.run();
    }
}