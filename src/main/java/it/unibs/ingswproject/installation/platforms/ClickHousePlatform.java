package it.unibs.ingswproject.installation.platforms;

import it.unibs.ingswproject.platforms.cli.utils.CliUtils;
import it.unibs.ingswproject.translations.Translator;

/**
 * @author Nicolò Rebaioli
 */
public class ClickHousePlatform extends HostedPlatform {
    public ClickHousePlatform(Translator translator, CliUtils cliUtils) {
        super(translator, cliUtils);
    }

    @Override
    public String getPlatformName() {
        return "ClickHouse";
    }

    @Override
    protected int getDefaultPort() {
        return 8123;
    }

    @Override
    protected String getUrl(String host, int port, String databaseName) {
        return String.format("jdbc:clickhouse://%s:%d/%s", host, port, databaseName);
    }

    @Override
    protected String getDriver() {
        return "ru.yandex.clickhouse.ClickHouseDriver";
    }
}
