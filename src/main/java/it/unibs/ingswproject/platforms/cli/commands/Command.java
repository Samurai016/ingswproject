package it.unibs.ingswproject.platforms.cli.commands;

/**
 * @author Nicolò Rebaioli
 */
public class Command {
    private char command;
    private String description;
    private Runnable executor;

    public Command(char command, String description) {
        this.command = command;
        this.description = description;
    }

    public Command(char command, String description, Runnable executor) {
        this.command = command;
        this.description = description;
        this.executor = executor;
    }

    // GETTERS
    public char getCommand() {
        return this.command;
    }

    public String getDescription() {
        return this.description;
    }

    // SETTERS
    public Command setCommand(char command) {
        this.command = command;
        return this;
    }

    public Command setDescription(String description) {
        this.description = description;
        return this;
    }

    public Command setExecutor(Runnable executor) {
        this.executor = executor;
        return this;
    }

    public final void execute() {
        if (this.executor != null) {
            this.executor.run();
        }
    }
}
