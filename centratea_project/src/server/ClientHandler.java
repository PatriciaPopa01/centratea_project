package server;

import game.GameLogic;
import game.GameResult;
import game.GameState;
import protocol.CommandParser;
import protocol.ServerProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
	private static final Object WIN_LOCK = new Object();

	private final Socket socket;
	private final ClientManager clientManager;
	private final GameState gameState;
	private PrintWriter out;
	private String username;
	private boolean connected;

	public ClientHandler(Socket socket, ClientManager clientManager, GameState gameState) {
		this.socket = socket;
		this.clientManager = clientManager;
		this.gameState = gameState;
	}

	@Override
	public void run() {
		try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
			out = new PrintWriter(socket.getOutputStream(), true);

			String firstMessage = in.readLine();
			if (!handleConnect(firstMessage)) {
				return;
			}

			String message;
			while ((message = in.readLine()) != null && connected) {
				handleMessage(message);
			}
		} catch (IOException exception) {
			System.out.println("[SERVER] Client deconectat neasteptat: " + username);
		} finally {
			disconnect();
		}
	}

	private boolean handleConnect(String message) {
		CommandParser parser = new CommandParser(message);

		if (!"CONNECT".equals(parser.getCommand())) {
			sendMessage(ServerProtocol.ERROR_INVALID_COMMAND);
			return false;
		}

		String requestedUsername = parser.getArgument();
		if (requestedUsername.isBlank() || requestedUsername.contains(" ")) {
			sendMessage(ServerProtocol.ERROR_INVALID_NAME);
			return false;
		}

		this.username = requestedUsername;
		boolean added = clientManager.addClient(username, this);

		if (!added) {
			sendMessage(ServerProtocol.ERROR_NAME_TAKEN);
			return false;
		}

		connected = true;
		gameState.registerClient(username);
		sendMessage(ServerProtocol.OK_CONNECTED);
		clientManager.broadcast(ServerProtocol.info(username + " s-a conectat."));
		System.out.println("[SERVER] Client conectat: " + username);
		return true;
	}

	private void handleMessage(String message) {
		CommandParser parser = new CommandParser(message);

		switch (parser.getCommand()) {
			case "GUESS" -> handleGuess(parser.getArgument());
			case "QUIT" -> {
				sendMessage(ServerProtocol.BYE);
				connected = false;
			}
			default -> sendMessage(ServerProtocol.ERROR_INVALID_COMMAND);
		}
	}

	private void handleGuess(String guess) {
		if (!GameLogic.isValidGuess(guess)) {
			sendMessage(ServerProtocol.ERROR_INVALID_GUESS);
			return;
		}

		GameResult result = gameState.processGuess(username, guess);
		sendMessage(ServerProtocol.result(result.getCentrate(), result.getNecentrate(), result.getAttempts()));

		if (result.isWin()) {
			handleWin(result);
		}
	}

	private void handleWin(GameResult result) {
		synchronized (WIN_LOCK) {
			clientManager.broadcast(ServerProtocol.win(username, result.getAttempts()));
			gameState.resetGame();
			clientManager.broadcast(ServerProtocol.reset());
		}
	}

	public void sendMessage(String message) {
		if (out != null) {
			out.println(message);
		}
	}

	private void disconnect() {
		connected = false;
		clientManager.removeClient(username, this);
		gameState.removeClient(username);

		if (username != null) {
			clientManager.broadcast(ServerProtocol.info(username + " s-a deconectat."));
			System.out.println("[SERVER] Client eliminat: " + username);
		}

		try {
			socket.close();
		} catch (IOException ignored) {
		}
	}
}