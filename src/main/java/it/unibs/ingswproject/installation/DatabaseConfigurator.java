package it.unibs.ingswproject.installation;

import io.ebean.Database;

/**
 * @author Nicolò Rebaioli
 */
public interface DatabaseConfigurator {
    boolean isDatabaseConfigured();
    Database configureConnection();
    void disableDdlGeneration();
}
