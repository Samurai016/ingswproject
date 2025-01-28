package it.unibs.ingswproject;

import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.errors.handlers.FileLogErrorHandler;
import it.unibs.ingswproject.errors.handlers.DefaultErrorHandler;
import it.unibs.ingswproject.platforms.cli.CliAppFactory;
import it.unibs.ingswproject.utils.CommandLineParser;
import it.unibs.ingswproject.utils.FileUtils;
import it.unibs.ingswproject.view.ApplicationFactory;
import org.apache.commons.cli.CommandLine;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        boolean verbose = false;
        try {
            // Disabilito il logging di Ebean (imposta a "debug" per abilitarlo)
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

            // Imposto il file di configurazione
            // Avaje è il plugin usato da Ebean per il caricamento delle impostazioni
            // https://github.com/avaje/avaje-config?tab=readme-ov-file#loading-properties
            System.setProperty("props.file", FileUtils.getConfigurationFile().toString());

            // Parsing degli argomenti
            CommandLineParser parser = new CommandLineParser();
            CommandLine arguments = parser.parse(args);

            // Verbose mode
            verbose = arguments.hasOption("verbose");

            // Display the help message if the user asks for it
            if (arguments.hasOption("help")) {
                parser.printHelp();
                return;
            }

            // Creo la factory per l'applicazione
            ApplicationFactory factory;
            if (arguments.getOptionValue("platform", "cli").equals("cli")) {
                factory = new CliAppFactory();
            } else {
                throw new IllegalArgumentException("Invalid platform");
            }

            // Creo e avvio l'applicazione
            factory.createApp(arguments).run();
        } catch (Throwable e) {
            ErrorManager errorManager = new ErrorManager();
            errorManager.addErrorHandler(new DefaultErrorHandler(verbose));
            errorManager.addErrorHandler(new FileLogErrorHandler());
            errorManager.handle(e);

            System.out.println("Press any key to continue...");
            new Scanner(System.in).nextLine();
        }
    }
}