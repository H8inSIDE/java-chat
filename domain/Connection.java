package domain;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.function.Consumer;

import domain.enumeration.Command;

public class Connection {
	private static final SimpleDateFormat TEMPLATE = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

	private BufferedReader input;
	private PrintWriter output;
	private Socket socket;

	public Connection(
		final String ip,
		final int port
	) {
		try {
			this.socket = new Socket(ip, port);
			connect();
		} catch (final Exception exception) {
			
		}
	}

	public Connection(final Socket socket) {
		try {
			this.socket = socket;
			connect();
		} catch (final Exception exception) {

		}
	}

	public void disconnect() {
		try {
			input.close();
			output.close();
			socket.close();
		} catch (final Exception exception) {

		}
	}

	public void readMessages(
		final Runnable disconnectCallback,
		final Consumer<String> messageCallback
	) {
		try {
			String message;

			while ((message = input.readLine()) != null) {
				processMessage(disconnectCallback, messageCallback, message.trim());
			}
		} catch (final Exception exception) {

		}
	}

	public void sendMessage(final String message) {
		output.println(message);
		output.flush();
	}

	public void sendMessage(
		final String author,
		final String message
	) {
		final String timestamp = TEMPLATE.format(new Date());
		final String aux = String.format("[%s] %s: %s", timestamp, author, message);

		sendMessage(aux);
	}

	private void connect() throws Exception {
		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(socket.getOutputStream());
	}

	private void processCommand(
		final String command,
		final Runnable disconnectCallback
	) {
		final String[] args = command.split("\\s+");

		if (Command.QUIT.getValue().equals(args[0])) {
			disconnectCallback.run();
		}
	}

	private void processMessage(
		final Runnable disconnectCallback,
		final Consumer<String> messageCallback,
		final String message
	) throws Exception {
		if (message.startsWith("/")) {
			processCommand(message, disconnectCallback);
		} else {
			messageCallback.accept(message);
		}
	}
}
