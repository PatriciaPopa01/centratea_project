package game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameState {
    private String secretNumber;
    private final Map<String, Integer> attemptsByClient;

    public GameState() {
        this.secretNumber = GameLogic.generateSecretNumber();
        this.attemptsByClient = new ConcurrentHashMap<>();
        System.out.println("[SERVER] Numar secret initial: " + secretNumber);
    }

    public synchronized GameResult processGuess(String username, String guess) {
        int attempts = attemptsByClient.getOrDefault(username, 0) + 1;
        attemptsByClient.put(username, attempts);

        int centrate = GameLogic.countCentrate(secretNumber, guess);
        int necentrate = GameLogic.countNecentrate(secretNumber, guess);
        boolean win = centrate == 4;

        return new GameResult(centrate, necentrate, attempts, win);
    }

    public synchronized void registerClient(String username) {
        attemptsByClient.put(username, 0);
    }

    public synchronized void removeClient(String username) {
        attemptsByClient.remove(username);
    }

    public synchronized void resetGame() {
        this.secretNumber = GameLogic.generateSecretNumber();
        attemptsByClient.replaceAll((client, attempts) -> 0);
        System.out.println("[SERVER] Joc resetat. Numar secret nou: " + secretNumber);
    }
}