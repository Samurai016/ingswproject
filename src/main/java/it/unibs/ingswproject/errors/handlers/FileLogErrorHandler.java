package it.unibs.ingswproject.errors.handlers;

import it.unibs.ingswproject.errors.ErrorHandler;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;

public class FileLogErrorHandler implements ErrorHandler {
    public static final String LOG_FILE_NAME = "ingsw_project/log.txt";

    @Override
    public void handle(Throwable e) {
        try (FileWriter writer = new FileWriter(getFile(), true)) {
            writer.write(this.formatString(e.getMessage()));
            writer.write("\n");
            writer.write(this.formatString(Arrays.toString(e.getStackTrace())));
            writer.write("\n");
        } catch (IOException ex) {
            // Do nothing
        }
    }

    private String formatString(String line) {
        return String.format("[%s] %s", new Date(), line);
    }

    private static File getFile() {
        Path path = Path.of(System.getenv("APPDATA"), LOG_FILE_NAME);
        File file = new File(path.toString());
        //noinspection ResultOfMethodCallIgnored
        file.getParentFile().mkdirs();

        return file;
    }
}
