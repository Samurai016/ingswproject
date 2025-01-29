package it.unibs.ingswproject.platforms.cli.installation.platforms;

import io.ebean.datasource.DataSourceConfig;
import it.unibs.ingswproject.installation.DatabasePlatform;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.translations.Translator;

import java.io.File;

/**
 * @author Nicolò Rebaioli
 */
public class SQLitePlatform implements DatabasePlatform {
    private final Translator translator;
    private final CliUtils cliUtils;

    public SQLitePlatform(Translator translator, CliUtils cliUtils) {
        this.translator = translator;
        this.cliUtils = cliUtils;
    }

    @Override
    public String getPlatformName() {
        return "SQLite";
    }

    @Override
    public DataSourceConfig getConfiguration() {
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig.setDriver("org.sqlite.JDBC");

        // Get database file path
        File databasePath;
        do {
            String path = this.cliUtils.readFromConsole(this.translator.translate("sqlite_configuration_file_path"));
            databasePath = new File(path);
            if (!databasePath.exists()) {
                System.out.println(this.translator.translate("sqlite_configuration_file_path_error"));
            }
        } while (!databasePath.exists());
        dataSourceConfig.setUrl("jdbc:sqlite:" + databasePath.getAbsolutePath());

        // Get username and password
        String username = this.cliUtils.readFromConsole(this.translator.translate("sqlite_username"));
        String password = this.cliUtils.readFromConsole(this.translator.translate("sqlite_password"));
        dataSourceConfig.setUsername(username);
        dataSourceConfig.setPassword(password);

        return dataSourceConfig;
    }
}
