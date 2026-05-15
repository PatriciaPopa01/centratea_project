package server;

import game.GameState;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
	private static final int DEFAULT_PORT = 12345;

	private final int port;
	private final ClientManager clientManager;
	private final GameState gameState;
	private final ExecutorService clientPool;

	public Server(int port) {
		this.port = port;
		this.clientManager = new ClientManager();
		this.gameState = new GameState();
		this.clientPool = Executors.newCachedThreadPool();
	}

	public void start() {
		try (ServerSocket serverSocket = new ServerSocket(port)) {
			System.out.println("[SERVER] Pornit pe portul " + port);

			while (true) {
				Socket clientSocket = serverSocket.accept();
				ClientHandler clientHandler = new ClientHandler(clientSocket, clientManager, gameState);
				clientPool.execute(clientHandler);
			}
		} catch (IOException exception) {
			System.out.println("[SERVER] Eroare: " + exception.getMessage());
		} finally {
			clientPool.shutdown();
		}
	}

	public static void main(String[] args) {
		int port = DEFAULT_PORT;

		if (args.length > 0) {
			try {
				port = Integer.parseInt(args[0]);
			} catch (NumberFormatException exception) {
				System.out.println("Port invalid. Se foloseste portul implicit " + DEFAULT_PORT);
			}
		}

		new Server(port).start();
	}
}