package it.unibs.ingswproject.platforms.cli.installation.platforms;

import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.translations.Translator;

/**
 * @author Nicolò Rebaioli
 */
public class PostgresPlatform extends HostedPlatform {
    public PostgresPlatform(Translator translator, CliUtils cliUtils) {
        super(translator, cliUtils);
    }

    @Override
    public String getPlatformName() {
        return "Postgres";
    }

    @Override
    protected int getDefaultPort() {
        return 5432;
    }

    @Override
    protected String getUrl(String host, int port, String databaseName) {
        return String.format("jdbc:postgresql://%s:%d/%s", host, port, databaseName);
    }

    @Override
    protected String getDriver() {
        return "org.postgresql.Driver";
    }
}
