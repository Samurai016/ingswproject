package it.unibs.ingswproject;

import it.unibs.ingswproject.view.cli.App;

public class Main {
    public static void main(String[] args) {
        // Disabilito il logging di Ebean
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        // TODO: Avviare l'applicazione CLI
        new App().run();
    }
}