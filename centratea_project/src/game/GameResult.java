package game;

public class GameResult {
    private final int centrate;
    private final int necentrate;
    private final int attempts;
    private final boolean win;

    public GameResult(int centrate, int necentrate, int attempts, boolean win) {
        this.centrate = centrate;
        this.necentrate = necentrate;
        this.attempts = attempts;
        this.win = win;
    }

    public int getCentrate() {
        return centrate;
    }

    public int getNecentrate() {
        return necentrate;
    }

    public int getAttempts() {
        return attempts;
    }

    public boolean isWin() {
        return win;
    }
}