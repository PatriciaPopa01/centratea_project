package protocol;

public class ClientProtocol {
    private ClientProtocol() {
    }

    public static String connect(String username) {
        return "CONNECT " + username;
    }

    public static String guess(String number) {
        return "GUESS " + number;
    }

    public static String quit() {
        return "QUIT";
    }
}