package protocol;

public class CommandParser {
    private final String command;
    private final String argument;

    public CommandParser(String rawMessage) {
        if (rawMessage == null || rawMessage.trim().isEmpty()) {
            this.command = "";
            this.argument = "";
            return;
        }

        String[] parts = rawMessage.trim().split("\\s+", 2);
        this.command = parts[0].toUpperCase();
        this.argument = parts.length > 1 ? parts[1].trim() : "";
    }

    public String getCommand() {
        return command;
    }

    public String getArgument() {
        return argument;
    }
}