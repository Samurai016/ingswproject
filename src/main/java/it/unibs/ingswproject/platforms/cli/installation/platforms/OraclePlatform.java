package it.unibs.ingswproject.platforms.cli.installation.platforms;

import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.translations.Translator;

/**
 * @author Nicolò Rebaioli
 */
public class OraclePlatform extends HostedPlatform {
    public OraclePlatform(Translator translator, CliUtils cliUtils) {
        super(translator, cliUtils);
    }

    @Override
    public String getPlatformName() {
        return "Oracle";
    }

    @Override
    protected int getDefaultPort() {
        return 1521;
    }

    @Override
    protected String getUrl(String host, int port, String databaseName) {
        return String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, databaseName);
    }

    @Override
    protected String getDriver() {
        return "oracle.jdbc.driver.OracleDriver";
    }
}
