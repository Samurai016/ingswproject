package it.unibs.ingswproject.installation.platforms;

import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.translations.Translator;

/**
 * @author Nicolò Rebaioli
 */
public class SQLServerPlatform extends HostedPlatform {
    public SQLServerPlatform(Translator translator, CliUtils cliUtils) {
        super(translator, cliUtils);
    }

    @Override
    public String getPlatformName() {
        return "SQL Server";
    }

    @Override
    protected int getDefaultPort() {
        return 1433;
    }

    @Override
    protected String getUrl(String host, int port, String databaseName) {
        return String.format("jdbc:sqlserver://%s:%d;databaseName=%s", host, port, databaseName);
    }

    @Override
    protected String getDriver() {
        return "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    }
}
