package it.unibs.ingswproject.utils;

import org.apache.commons.cli.*;

public class CommandLineParser {
    private final Options options = new Options();

    public CommandLineParser() {
        this.options.addOption("p", "platform", true, "Platform Name");
        this.options.addOption("db", "database", true, "Database Name");

        this.options.addOption("u", "username", true, "Username");
        this.options.addOption("pw", "password", true, "Password");
    }

    public CommandLine parse(String[] args) throws ParseException {
        org.apache.commons.cli.CommandLineParser parser = new DefaultParser();
        return parser.parse(this.options, args);
    }
}
