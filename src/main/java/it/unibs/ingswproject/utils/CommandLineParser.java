package it.unibs.ingswproject.utils;

import org.apache.commons.cli.*;

public class CommandLineParser {
    private final Options options = new Options();

    public CommandLineParser() {
        this.options.addOption("h", "help", false, "Print this message");

        this.options.addOption("p", "platform", true, "Platform name (currently supported: cli). If not specified, the cli platform is used");
        this.options.addOption("db", "database", true, "Database name as defined in the application.yaml file. If not specified, the default database is used");

        this.options.addOption("u", "username", true, "Username for authentication. If not specified, the login page will be displayed");
        this.options.addOption("pw", "password", true, "Password for authentication");

        this.options.addOption("l", "language", true, "Interface language (ISO 639 code). If not specified or the language is not supported, the default language is used");
    }

    public CommandLine parse(String[] args) throws ParseException {
        org.apache.commons.cli.CommandLineParser parser = new DefaultParser();
        return parser.parse(this.options, args);
    }

    public void printHelp() {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("ingswproject.exe [options]", this.options);
    }
}
