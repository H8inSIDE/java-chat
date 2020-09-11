import domain.Connection;
import domain.enumeration.ConnectionType;

public class Client extends Window {
	private static final long serialVersionUID = 1L;

	private Connection connection;

	public Client(
		final String ip,
		final int port
	) {
		super(ConnectionType.CLIENT, ip, port);
	}

	@Override
	protected void connect(
		final String ip,
		final int port
	) {
		connection = new Connection(ip, port);
		connection.readMessages(this::disconnect, this::printMessage);
	}

	@Override
	protected void disconnect() {
		try {
			offline();

			connection.disconnect();
		} catch (final Exception exception) {

		}
	}

	@Override
	protected void sendMessage(final String message) {
		connection.sendMessage(message);
	}

	public static void main(final String[] args) {
		final String ip = args.length > 0
			? args[0]
			: "localhost";

		final int port = args.length > 1
			? Integer.parseInt(args[1])
			: 3000;

		new Client(ip, port);
	}
}
