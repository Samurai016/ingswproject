package it.unibs.ingswproject;

import io.ebean.DB;
import io.ebean.Database;
import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.translations.BaseTranslator;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.view.cli.CliApp;
import it.unibs.ingswproject.errors.cli.CliErrorManager;
import it.unibs.ingswproject.utils.cli.CliUtils;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.router.cli.CliRouter;

import java.net.MalformedURLException;

public class Main {
    public static void main(String[] args) throws MalformedURLException {
        // Disabilito il logging di Ebean
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        // Avvio l'applicazione
        PageFactory pageFactory = new PageFactory();
        pageFactory.registerDependency(PageFactory.class, pageFactory);

        // Translator
        BaseTranslator translator = new BaseTranslator( "file:i18n/");
        translator.addResourceBundle("models");
        translator.addResourceBundle("cli");
        pageFactory.registerDependency(Translator.class, translator);

        // Utils
        CliUtils cliUtils = new CliUtils(translator);
        pageFactory.registerDependency(CliUtils.class, cliUtils);

        // Error manager
        CliErrorManager errorManager = new CliErrorManager(translator, cliUtils);
        errorManager.setDebugModeEnabled(true);
        pageFactory.registerDependency(ErrorManager.class, errorManager);

        // Persistence
        Database database = DB.getDefault();
        StorageService storageService = new StorageService(database);
        pageFactory.registerDependency(Database.class, database);
        pageFactory.registerDependency(StorageService.class, storageService);

        // Authentication
        AuthService authService = new AuthService(storageService);
        pageFactory.registerDependency(AuthService.class, authService);

        // Cli setup
        CliRouter router = new CliRouter();
        CliApp app = new CliApp(router, pageFactory, authService, cliUtils, translator, errorManager);
        pageFactory.registerDependency(CliRouter.class, router);
        pageFactory.registerDependency(CliApp.class, app);

        // Run the app
        app.run();
    }
}