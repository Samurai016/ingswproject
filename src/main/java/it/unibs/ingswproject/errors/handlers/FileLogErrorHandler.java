package it.unibs.ingswproject.errors.handlers;

import it.unibs.ingswproject.errors.ErrorHandler;
import it.unibs.ingswproject.utils.FileUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

public class FileLogErrorHandler implements ErrorHandler {
    public static final String LOG_FILE_NAME = "log.txt";

    @Override
    public void handle(Throwable e) {
        try (FileWriter writer = new FileWriter(FileUtils.getFile(LOG_FILE_NAME), true)) {
            writer.write(this.formatString(this.getMessage(e)));
            writer.write("\n");
            writer.write(this.formatString(Arrays.toString(e.getStackTrace())));
            writer.write("\n");
        } catch (IOException ex) {
            // Do nothing
        }
    }

    /**
     * Format the line to be written in the log file
     *
     * @param line The line to format
     * @return The formatted line
     */
    private String formatString(String line) {
        return String.format("[%s] %s", new Date(), line);
    }

    /**
     * Recursively get the message of the exception
     *
     * @param e The exception
     * @return The message of the exception, if present, otherwise the message of the cause, otherwise "Unknown error"
     */
    private String getMessage(Throwable e) {
        if (e.getMessage() != null) {
            return e.getMessage();
        } else if (e.getCause() != null) {
            return this.getMessage(e.getCause());
        }
        return "Unknown error";
    }
}
