package it.unibs.ingswproject;

import it.unibs.ingswproject.view.cli.CliApp;

public class Main {
    public static void main(String[] args) {
        // Disabilito il logging di Ebean
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        // Avvio l'applicazione
        new CliApp().run();
    }
}