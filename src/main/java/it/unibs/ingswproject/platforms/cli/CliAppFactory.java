package it.unibs.ingswproject.platforms.cli;

import io.ebean.DB;
import io.ebean.Database;
import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.platforms.cli.errors.CliErrorManager;
import it.unibs.ingswproject.platforms.cli.router.CliRouter;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.BaseTranslator;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.view.ApplicationFactory;

import java.net.MalformedURLException;

/**
 * Factory per la creazione di un'applicazione CLI
 *
 * @author Nicolò Rebaioli
 */
public class CliAppFactory implements ApplicationFactory {
    public CliApp createApp() {
        PageFactory pageFactory = new PageFactory();
        pageFactory.registerDependency(PageFactory.class, pageFactory);

        // Translator
        BaseTranslator translator;
        try {
            translator = new BaseTranslator("file:i18n/")
                    .addResourceBundle("models")
                    .addResourceBundle("cli");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        pageFactory.registerDependency(Translator.class, translator);

        // Utils
        CliUtils cliUtils = new CliUtils(translator);
        pageFactory.registerDependency(CliUtils.class, cliUtils);

        // Error manager
        CliErrorManager errorManager = new CliErrorManager(translator, cliUtils);
        errorManager.setDebugMode(true);
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
        CliApp app = new CliApp(router, pageFactory, errorManager);
        pageFactory.registerDependency(CliRouter.class, router);
        pageFactory.registerDependency(CliApp.class, app);

        // Debug mode
        //authService.login("admin", "gelato01");

        return app;
    }
}
