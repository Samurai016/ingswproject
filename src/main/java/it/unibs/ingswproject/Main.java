package it.unibs.ingswproject;

import it.unibs.ingswproject.errors.ErrorManager;
import it.unibs.ingswproject.errors.handlers.FileLogErrorHandler;
import it.unibs.ingswproject.errors.handlers.StackTraceErrorHandler;
import it.unibs.ingswproject.platforms.cli.CliAppFactory;
import it.unibs.ingswproject.utils.CommandLineParser;
import it.unibs.ingswproject.utils.FileUtils;
import it.unibs.ingswproject.view.ApplicationFactory;
import org.apache.commons.cli.CommandLine;

public class Main {
    public static void main(String[] args) {
        try {
            // Disabilito il logging di Ebean (imposta a "debug" per abilitarlo)
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

            // Imposto il file di configurazione
            // Avaje è il plugin usato da Ebean per il caricamento delle impostazioni
            // https://github.com/avaje/avaje-config?tab=readme-ov-file#loading-properties
            System.setProperty("props.file", FileUtils.getConfigurationFile().toString());

            // Creo la factory per l'applicazione
            CommandLine arguments = new CommandLineParser().parse(args);
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
            errorManager.addErrorHandler(new StackTraceErrorHandler());
            errorManager.addErrorHandler(new FileLogErrorHandler());
            errorManager.handle(e);
        }
    }
}