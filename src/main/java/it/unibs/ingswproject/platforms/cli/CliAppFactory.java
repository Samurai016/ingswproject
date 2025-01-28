package it.unibs.ingswproject.platforms.cli;

import io.ebean.DB;
import io.ebean.Database;
import it.unibs.ingswproject.auth.AuthService;
import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.errors.handlers.FileLogErrorHandler;
import it.unibs.ingswproject.logic.BaseScambioStrategy;
import it.unibs.ingswproject.logic.ScambioStrategy;
import it.unibs.ingswproject.logic.routing.RoutingComputationStrategy;
import it.unibs.ingswproject.logic.routing.SimpleRoutingComputation;
import it.unibs.ingswproject.logic.weight.SimpleWeightComputation;
import it.unibs.ingswproject.logic.weight.WeightComputationStrategy;
import it.unibs.ingswproject.models.StorageService;
import it.unibs.ingswproject.platforms.cli.errors.CliErrorHandler;
import it.unibs.ingswproject.platforms.cli.router.CliRouter;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.router.PageFactory;
import it.unibs.ingswproject.translations.BaseTranslator;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.ProjectUtils;
import it.unibs.ingswproject.view.ApplicationFactory;
import org.apache.commons.cli.CommandLine;

/**
 * Factory per la creazione di un'applicazione CLI
 *
 * @author Nicolò Rebaioli
 */
public class CliAppFactory implements ApplicationFactory {
    public CliApp createApp(CommandLine arguments) {
        PageFactory pageFactory = new PageFactory();
        pageFactory.registerDependency(PageFactory.class, pageFactory);

        // Translator
        BaseTranslator translator = new BaseTranslator(CliAppFactory.class.getClassLoader())
                    .addResourceBundle("i18n/models")
                    .addResourceBundle("i18n/cli");
        pageFactory.registerDependency(Translator.class, translator);

        // Utils
        CliUtils cliUtils = new CliUtils(translator);
        pageFactory.registerDependency(CliUtils.class, cliUtils);

        ProjectUtils projectUtils = new ProjectUtils();
        pageFactory.registerDependency(ProjectUtils.class, projectUtils);

        // Error manager
        ErrorManager errorManager = new ErrorManager();
        errorManager.addErrorHandler(new FileLogErrorHandler());
        errorManager.addErrorHandler(new CliErrorHandler(translator, cliUtils));
        pageFactory.registerDependency(ErrorManager.class, errorManager);

        // Persistence
        Database database = arguments.hasOption("database") ? DB.byName(arguments.getOptionValue("database")) : DB.getDefault();
        StorageService storageService = new StorageService(database);
        pageFactory.registerDependency(Database.class, database);
        pageFactory.registerDependency(StorageService.class, storageService);

        // Authentication
        AuthService authService = new AuthService(storageService);
        pageFactory.registerDependency(AuthService.class, authService);
        if (arguments.hasOption("username") && arguments.hasOption("password")) {
            authService.login(arguments.getOptionValue("username"), arguments.getOptionValue("password"));
        }

        // Cli setup
        CliRouter router = new CliRouter();
        CliApp app = new CliApp(router, pageFactory, errorManager);
        pageFactory.registerDependency(CliRouter.class, router);
        pageFactory.registerDependency(CliApp.class, app);

        // Logic
        WeightComputationStrategy weightComputationStrategy = new SimpleWeightComputation();
        pageFactory.registerDependency(WeightComputationStrategy.class, weightComputationStrategy);

        RoutingComputationStrategy routingComputationStrategy = new SimpleRoutingComputation(storageService, weightComputationStrategy);
        pageFactory.registerDependency(RoutingComputationStrategy.class, routingComputationStrategy);

        ScambioStrategy scambioStrategy = new BaseScambioStrategy(storageService, authService);
        pageFactory.registerDependency(ScambioStrategy.class, scambioStrategy);

        return app;
    }
}
