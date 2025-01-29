package it.unibs.ingswproject.installation.platforms;

import io.ebean.datasource.DataSourceConfig;
import it.unibs.ingswproject.installation.DatabasePlatform;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.translations.Translator;

import java.net.InetAddress;

/**
 * @author Nicolò Rebaioli
 */
public abstract class HostedPlatform implements DatabasePlatform {
    private final Translator translator;
    private final CliUtils cliUtils;
    
    public HostedPlatform(Translator translator, CliUtils cliUtils) {
        this.translator = translator;
        this.cliUtils = cliUtils;
    }
    
    @Override
    public DataSourceConfig getConfiguration() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver(this.getDriver());

        // Get database host
        InetAddress host;
        do {
            String hostName = this.cliUtils.readFromConsole(this.translator.translate("db_host"));
            try {
                host = InetAddress.getByName(hostName);
            } catch (Exception e) {
                System.out.println(this.translator.translate("db_host_error"));
                host = null;
            }
        } while (host == null);

        // Get database port
        int port;
        do {
            try {
                port = Integer.parseInt(this.cliUtils.readFromConsole(this.translator.translate("db_port", this.getDefaultPort())));
                if (port <= 0) {
                    System.out.println(this.translator.translate("db_port_error"));
                }
            } catch (Exception e) {
                System.out.println(this.translator.translate("db_port_error"));
                port = 0;
            }
        } while (port <= 0);

        // Get database name
        String databaseName = this.cliUtils.readFromConsole(this.translator.translate("db_database"));
        String url = this.getUrl(host.getHostName(), port, databaseName);
        dataSourceConfig.setUrl(url);

        // Get username and password
        String username = this.cliUtils.readFromConsole(this.translator.translate("db_username"));
        String password = this.cliUtils.readFromConsole(this.translator.translate("db_password"), true);
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);

        return dataSourceConfig;
    }

    protected abstract int getDefaultPort();
    protected abstract String getUrl(String host, int port, String databaseName);
    protected abstract String getDriver();
}
