package game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class GameLogic {
    private static final int NUMBER_LENGTH = 4;
    private static final Random RANDOM = new Random();

    private GameLogic() {
    }

    public static String generateSecretNumber() {
        List<Integer> digits = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            digits.add(i);
        }

        Collections.shuffle(digits, RANDOM);

        StringBuilder number = new StringBuilder();
        for (int i = 0; i < NUMBER_LENGTH; i++) {
            number.append(digits.get(i));
        }

        return number.toString();
    }

    public static boolean isValidGuess(String guess) {
        if (guess == null || guess.length() != NUMBER_LENGTH) {
            return false;
        }

        Set<Character> usedDigits = new HashSet<>();
        for (char digit : guess.toCharArray()) {
            if (!Character.isDigit(digit)) {
                return false;
            }

            if (!usedDigits.add(digit)) {
                return false;
            }
        }

        return true;
    }

    public static int countCentrate(String secretNumber, String guess) {
        int centrate = 0;

        for (int i = 0; i < NUMBER_LENGTH; i++) {
            if (secretNumber.charAt(i) == guess.charAt(i)) {
                centrate++;
            }
        }

        return centrate;
    }

    public static int countNecentrate(String secretNumber, String guess) {
        int necentrate = 0;

        for (int i = 0; i < NUMBER_LENGTH; i++) {
            char currentDigit = guess.charAt(i);

            if (secretNumber.charAt(i) != currentDigit && secretNumber.indexOf(currentDigit) != -1) {
                necentrate++;
            }
        }

        return necentrate;
    }
}