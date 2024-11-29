package it.unibs.ingswproject.view;

import org.apache.commons.cli.CommandLine;

/**
 * Interfaccia che rappresenta una factory per un'applicazione
 * Serve a creare un'istanza di tipo Application
 *
 * @author Nicolò Rebaioli
 */
public interface ApplicationFactory {
    /**
     * Crea un'applicazione con la configurazione di default
     *
     * @return Applicazione creata
     */
    Application createApp(CommandLine args);
}
