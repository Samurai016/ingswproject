package it.unibs.ingswproject.installation;

import io.ebean.DB;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.datasource.DataSourceInitialiseException;
import it.unibs.ingswproject.installation.platforms.*;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.translations.BaseTranslator;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.FileUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Nicolò Rebaioli
 */
public class DatabaseConfigurator {
    private final CliUtils cliUtils;
    private final Translator translator;
    Map<Character, DatabasePlatform> platforms = new HashMap<>();

    public DatabaseConfigurator() {
        // Translator
        this.translator = new BaseTranslator(DatabaseConfigurator.class.getClassLoader())
                .addResourceBundle("i18n/models")
                .addResourceBundle("i18n/installation");

        // Utils
        this.cliUtils = new CliUtils(this.translator);

        List<DatabasePlatform> platformList = List.of(
                new MySQLPlatform(this.translator, this.cliUtils),
                new SQLitePlatform(this.translator, this.cliUtils),
                new PostgresPlatform(this.translator, this.cliUtils),
                new OraclePlatform(this.translator, this.cliUtils),
                new SQLServerPlatform(this.translator, this.cliUtils),
                new SAPHanaPlatform(this.translator, this.cliUtils),
                new ClickHousePlatform(this.translator, this.cliUtils)
        );
        for (int i = 0; i < platformList.size(); i++) {
            DatabasePlatform platform = platformList.get(i);
            this.platforms.put((char) ('1' + i), platform);
        }
    }

    public boolean isDatabaseConfigured() {
        try {
            DB.getDefault();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    public Database configureConnection() throws IOException {
        System.out.println(this.translator.translate("db_connection_configuration_tool_header"));
        System.out.println(this.translator.translate("db_connection_configuration_tool_description"));
        System.out.println();

        boolean valid = false;
        do {
            try {
                // Get platform
                DatabasePlatform platform = this.getPlatform();

                // Get configuration
                DataSourceConfig dataSourceConfig = platform.getConfiguration();

                // Creating database configuration
                System.out.println(this.translator.translate("db_connection_test"));
                DatabaseConfig config = new DatabaseConfig();
                config.setDataSourceConfig(dataSourceConfig);
                config.setRegister(false);

                // Creating database instance
                Database database = DatabaseFactory.create(config);
                this.writeConfigurationFile(dataSourceConfig);
                valid = true;
                System.out.println(this.translator.translate("db_connection_test_success"));
                System.out.println(this.translator.translate("db_connection_reboot"));
                return database;
            } catch (DataSourceInitialiseException e) {
                System.out.println(this.translator.translate("db_connection_test_failure", e.getMessage()));
                System.out.println();
            }
        } while (!valid);
        return null;
    }

    @SuppressWarnings("unchecked")
    private void writeConfigurationFile(DataSourceConfig dataSourceConfig) throws IOException {
        Map<String, Object> currentConfiguration;
        File configurationFile = FileUtils.getConfigurationFile();

        // Set style liked this:
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setIndent(2);
        options.setPrettyFlow(true);
        options.setSplitLines(false);
        options.setAllowReadOnlyProperties(true);
        options.setAllowUnicode(true);
        options.setLineBreak(DumperOptions.LineBreak.UNIX);
        Yaml yaml = new Yaml(options);

        // Read configuration file
        try (FileReader fileReader = new FileReader(configurationFile)) {
            currentConfiguration = yaml.load(fileReader);

            String configurationName = String.format("db_%d", (new Random()).nextInt(9999));

            // Creo o ottengo datasource
            if (!currentConfiguration.containsKey("datasource")) {
                currentConfiguration.put("datasource", new HashMap<>());
            }
            Map<String, Object> datasource = (Map<String, Object>) currentConfiguration.get("datasource");

            // Creo datasource.default: CONFIGURATION_NAME (se non esiste)
            datasource.putIfAbsent("default", configurationName);

            // Creo datasource.CONFIGURATION_NAME
            Map<String, Object> configuration = new HashMap<>();
            configuration.put("username", dataSourceConfig.getUsername());
            configuration.put("password", dataSourceConfig.getPassword());
            configuration.put("url", dataSourceConfig.getUrl());
            configuration.put("driver", dataSourceConfig.getDriver());
            datasource.put(configurationName, configuration);

        }

        // Write configuration file
        try (FileWriter fileWriter = new FileWriter(configurationFile)) {
            yaml.dump(currentConfiguration, fileWriter);
        }
    }

    private DatabasePlatform getPlatform() {
        // Print available platforms
        System.out.println(this.translator.translate("select_platform_title"));
        for (Map.Entry<Character, DatabasePlatform> entry : this.platforms.entrySet()) {
            System.out.println(this.translator.translate("select_platform_pattern", entry.getKey(), entry.getValue().getPlatformName()));
        }

        // Ask user to choose a platform
        DatabasePlatform platform = null;
        while (platform == null) {
            String choice = this.cliUtils.readFromConsole(this.translator.translate("select_platform_command"));
            if (choice.length() != 1) {
                System.out.println(this.translator.translate("select_platform_invalid_option"));
                continue;
            }

            platform = this.platforms.get(choice.charAt(0));
            if (platform == null) {
                System.out.println(this.translator.translate("select_platform_invalid_option"));
            } else {
                System.out.println();
                System.out.println(this.translator.translate("select_platform_subtitle", platform.getPlatformName()));
            }
        }

        return platform;
    }
}
