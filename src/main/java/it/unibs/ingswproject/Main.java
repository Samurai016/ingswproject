package it.unibs.ingswproject;

import it.unibs.ingswproject.platforms.cli.CliAppFactory;

public class Main {
    public static void main(String[] args) {
        // Disabilito il logging di Ebean
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "off");

        // Creo la factory per l'applicazione
        CliAppFactory factory = new CliAppFactory();

        // Creo e avvio l'applicazione
        factory.createApp().run();
    }
}