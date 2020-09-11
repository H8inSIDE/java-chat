import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

import domain.Connection;
import domain.enumeration.Command;
import domain.enumeration.ConnectionType;

public class Server {
	private Users users;

	public Server(final int port) {
		try {
			final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

			final ServerSocket serverSocket = serverSocketChannel.socket();
			serverSocket.bind(new InetSocketAddress(port));

			users = new Users();

			System.out.println("Server running on port " + port);

			while (true) {
				final Socket socket = serverSocket.accept();

				final ConnectionThread connectionThread = new ConnectionThread(this, socket);
				connectionThread.start();
			}
		} catch (final Exception exception) {
			
		}
	}

	public static void main(final String[] args) throws Exception  {
		new Server(3000);
	}

	public Users getUsers() {
		return users;
	}
}

class ConnectionThread extends Thread {
	private Connection connection;
    private Server server;

    public ConnectionThread(
        final Server server,
        final Socket socket
    ) {
		this.connection = new Connection(socket);
		this.server = server;

        server.getUsers().addUser(this);
    }

    @Override
	public void run() {
        connection.readMessages(
			this::disconnect,
			this::processMessage
		);
    }

    //
	// public String getIp() {
	// 	return socket
	// 		.getInetAddress()
	// 		.toString()
	// 		.substring(1);
	// }

	public void sendMessage(
		final String author,
		final String message
	) {
		connection.sendMessage(author, message);
	}

	public void sendMessage(final String message) {
		connection.sendMessage(message);
	}

	//
	private void disconnect() {
		sendMessage(Command.QUIT.getValue());

		connection.disconnect();
		server.getUsers().removeUser(this);
	}

	private void processMessage(final String message) {
		server.getUsers().sendMessage(getName(), message);
	}
}