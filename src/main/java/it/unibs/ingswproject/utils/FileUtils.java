package it.unibs.ingswproject.utils;

import java.io.File;
import java.nio.file.Path;

public abstract class FileUtils {
    public static final String FOLDER_NAME = "ingsw_project";
    public static final String CONFIGURATION_FILE_NAME = "application.yaml";

    /**
     * Get the file path for the configuration file
     *
     * @param fileName the name of the file (e.g. "application.yaml") without the path
     * @return the file path for the configuration file
     */
    public static File getFile(String fileName) {
        File file = new File(Path.of(System.getenv("APPDATA"), FOLDER_NAME, fileName).toString());
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();

        return file;
    }

    public static File getConfigurationFile() {
        return getFile(CONFIGURATION_FILE_NAME);
    }
}
