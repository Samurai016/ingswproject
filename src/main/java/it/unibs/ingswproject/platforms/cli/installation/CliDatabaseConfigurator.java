package it.unibs.ingswproject.platforms.cli.installation;

import io.ebean.DB;
import io.ebean.Database;
import io.ebean.DatabaseFactory;
import io.ebean.config.DatabaseConfig;
import io.ebean.datasource.DataSourceConfig;
import io.ebean.datasource.DataSourceInitialiseException;
import it.unibs.ingswproject.installation.DatabaseConfigurator;
import it.unibs.ingswproject.installation.DatabasePlatform;
import it.unibs.ingswproject.platforms.cli.installation.platforms.*;
import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.platforms.cli.views.CliPageView;
import it.unibs.ingswproject.translations.Translator;
import it.unibs.ingswproject.utils.FileUtils;
import it.unibs.ingswproject.utils.ProjectUtils;
import it.unibs.ingswproject.utils.YamlUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author Nicolò Rebaioli
 */
public class CliDatabaseConfigurator implements DatabaseConfigurator {
    private final CliUtils cliUtils;
    private final Translator translator;
    private final ProjectUtils projectUtils;
    private final Map<Character, DatabasePlatform> platforms = new HashMap<>();

    public CliDatabaseConfigurator(Translator translator, CliUtils cliUtils, ProjectUtils projectUtils) {
        this.translator = translator;
        this.cliUtils = cliUtils;
        this.projectUtils = projectUtils;

        // Utils
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

    @SuppressWarnings("unchecked")
    private static Map<String, Object> getOrCreate(Map<String, Object> map, String key) {
        if (!map.containsKey(key)) {
            map.put(key, new HashMap<>());
        }
        return (Map<String, Object>) map.get(key);
    }

    @Override
    public boolean isDatabaseConfigured() {
        try {
            DB.getDefault();
            return true;
        } catch (Throwable e) {
            return false;
        }
    }

    @Override
    public Database configureConnection() {
        try {
            System.out.printf(CliPageView.HEADER, this.projectUtils.getProjectVersion());
            System.out.println();
            System.out.println(this.translator.translate("db_connection_configuration_tool_header"));
            System.out.println(this.translator.translate("db_connection_configuration_tool_description"));
            System.out.println();

            boolean valid = false;
            // noinspection ConstantValue
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
                    System.out.println(this.translator.translate("db_connection_test_success"));

                    // Ask for DDL generation
                    System.out.println();
                    boolean shouldGenerateDDL = this.cliUtils.askForConfirmation(this.translator.translate("db_connection_generate_ddl_question"));
                    if (shouldGenerateDDL) {
                        config.setRunMigration(true);
                        config.setDdlGenerate(true);
                        config.setDdlRun(true);
                        System.out.println(this.translator.translate("db_connection_generate_ddl_warning"));
                    }

                    this.writeConfigurationFile(config, dataSourceConfig);
                    System.out.println();
                    System.out.println(this.translator.translate("db_connection_saving"));
                    System.out.println(this.translator.translate("db_connection_reboot"));

                    valid = true;
                    return database;
                } catch (DataSourceInitialiseException e) {
                    System.out.println(this.translator.translate("db_connection_test_failure", e.getMessage()));
                    System.out.println();
                }
            } while (!valid);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public void disableDdlGeneration() {
        try {
            File configurationFile = FileUtils.getConfigurationFile();
            if (!configurationFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                configurationFile.createNewFile();
            }

            Map<String, Object> currentConfiguration = YamlUtils.readYaml(configurationFile);
            if (currentConfiguration == null) {
                currentConfiguration = new HashMap<>();
            }

            // ebean
            Map<String, Object> ebean = getOrCreate(currentConfiguration, "ebean");
            // ebean.migration
            Map<String, Object> migration = getOrCreate(ebean, "migration");
            // ebean.ddl
            Map<String, Object> ddl = getOrCreate(ebean, "ddl");

            // Imposto generazione DDL
            migration.put("run", false);
            ddl.put("generate", false);
            ddl.put("run", false);

            // Write configuration file
            YamlUtils.writeYaml(configurationFile, currentConfiguration);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void writeConfigurationFile(DatabaseConfig databaseConfig, DataSourceConfig dataSourceConfig) throws IOException {
        File configurationFile = FileUtils.getConfigurationFile();
        if (!configurationFile.exists()) {
            //noinspection ResultOfMethodCallIgnored
            configurationFile.createNewFile();
        }

        String configurationName = String.format("db_%d", (new Random()).nextInt(9999));

        // Read configuration file
        Map<String, Object> currentConfiguration = YamlUtils.readYaml(configurationFile);
        if (currentConfiguration == null) {
            currentConfiguration = new HashMap<>();
        }

        // datasource
        Map<String, Object> datasource = getOrCreate(currentConfiguration, "datasource");

        // Creo datasource.default: CONFIGURATION_NAME (se non esiste)
        datasource.putIfAbsent("default", configurationName);

        // Creo datasource.CONFIGURATION_NAME
        Map<String, Object> configuration = new HashMap<>();
        configuration.put("username", dataSourceConfig.getUsername());
        configuration.put("password", dataSourceConfig.getPassword());
        configuration.put("url", dataSourceConfig.getUrl());
        configuration.put("driver", dataSourceConfig.getDriver());
        datasource.put(configurationName, configuration);

        // ebean
        Map<String, Object> ebean = getOrCreate(currentConfiguration, "ebean");
        // ebean.migration
        Map<String, Object> migration = getOrCreate(ebean, "migration");
        // ebean.ddl
        Map<String, Object> ddl = getOrCreate(ebean, "ddl");

        // Imposto generazione DDL
        migration.put("run", databaseConfig.isRunMigration());
        ddl.put("generate", databaseConfig.isDdlGenerate());
        ddl.put("run", databaseConfig.isDdlRun());

        // Write configuration file
        YamlUtils.writeYaml(configurationFile, currentConfiguration);
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
