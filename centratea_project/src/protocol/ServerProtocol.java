package protocol;

public class ServerProtocol {
    private ServerProtocol() {
    }

    public static final String OK_CONNECTED = "OK_CONNECTED";
    public static final String ERROR_NAME_TAKEN = "ERROR_NAME_TAKEN";
    public static final String ERROR_INVALID_NAME = "ERROR_INVALID_NAME";
    public static final String ERROR_INVALID_COMMAND = "ERROR_INVALID_COMMAND";
    public static final String ERROR_INVALID_GUESS = "ERROR_INVALID_GUESS";
    public static final String BYE = "BYE";

    public static String result(int centrate, int necentrate, int attempts) {
        return "RESULT centrate=" + centrate + " necentrate=" + necentrate + " attempts=" + attempts;
    }

    public static String win(String winner, int attempts) {
        return "WIN winner=" + winner + " attempts=" + attempts;
    }

    public static String reset() {
        return "RESET";
    }

    public static String info(String message) {
        return "INFO " + message;
    }
}
