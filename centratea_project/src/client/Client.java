package client;

import protocol.ClientProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private static final String DEFAULT_HOST = "localhost";
	private static final int DEFAULT_PORT = 12345;

	public static void main(String[] args) {
		String host = args.length > 0 ? args[0] : DEFAULT_HOST;
		int port = args.length > 1 ? Integer.parseInt(args[1]) : DEFAULT_PORT;

		try (Socket socket = new Socket(host, port);
			 BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
			 BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			 PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

			System.out.print("Introdu numele clientului: ");
			String username = console.readLine();
			out.println(ClientProtocol.connect(username));

			String connectResponse = in.readLine();
			System.out.println("Server: " + connectResponse);

			if (!"OK_CONNECTED".equals(connectResponse)) {
				return;
			}
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}