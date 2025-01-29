package it.unibs.ingswproject.installation;

import io.ebean.datasource.DataSourceConfig;

/**
 * @author Nicolò Rebaioli
 */
public interface DatabasePlatform {
    String getPlatformName();
    DataSourceConfig getConfiguration();
}
