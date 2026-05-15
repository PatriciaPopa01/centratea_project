package server;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ClientManager {
	private final Set<String> usernames;
	private final Set<ClientHandler> clients;

	public ClientManager() {
		this.usernames = ConcurrentHashMap.newKeySet();
		this.clients = ConcurrentHashMap.newKeySet();
	}

	public boolean addClient(String username, ClientHandler clientHandler) {
		boolean added = usernames.add(username);
		if (added) {
			clients.add(clientHandler);
		}
		return added;
	}

	public void removeClient(String username, ClientHandler clientHandler) {
		if (username != null) {
			usernames.remove(username);
		}
		clients.remove(clientHandler);
	}

	public void broadcast(String message) {
		for (ClientHandler client : clients) {
			client.sendMessage(message);
		}
	}

	public Collection<ClientHandler> getClients() {
		return clients;
	}
}