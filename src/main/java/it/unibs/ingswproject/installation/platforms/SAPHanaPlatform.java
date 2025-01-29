package it.unibs.ingswproject.installation.platforms;

import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.translations.Translator;

/**
 * @author Nicolò Rebaioli
 */
public class SAPHanaPlatform extends HostedPlatform {
    public SAPHanaPlatform(Translator translator, CliUtils cliUtils) {
        super(translator, cliUtils);
    }

    @Override
    public String getPlatformName() {
        return "SAP Hana";
    }

    @Override
    protected int getDefaultPort() {
        return 39017;
    }

    @Override
    protected String getUrl(String host, int port, String databaseName) {
        return String.format("jdbc:sap://%s:%d?databaseName=%s", host, port, databaseName);
    }

    @Override
    protected String getDriver() {
        return "com.sap.db.jdbc.Driver";
    }
}
