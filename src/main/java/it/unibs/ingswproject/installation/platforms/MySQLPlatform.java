package it.unibs.ingswproject.installation.platforms;

import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.translations.Translator;

/**
 * @author Nicolò Rebaioli
 */
public class MySQLPlatform extends HostedPlatform {
    public MySQLPlatform(Translator translator, CliUtils cliUtils) {
        super(translator, cliUtils);
    }

    @Override
    public String getPlatformName() {
        return "MySQL";
    }

    @Override
    protected int getDefaultPort() {
        return 3306;
    }

    @Override
    protected String getUrl(String host, int port, String databaseName) {
        return String.format("jdbc:mysql://%s:%d/%s", host, port, databaseName);
    }

    @Override
    protected String getDriver() {
        return "com.mysql.cj.jdbc.Driver";
    }
}
