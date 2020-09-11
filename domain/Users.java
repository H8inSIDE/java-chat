package domain;

import java.util.LinkedList;
import java.util.List;

import domain.enumeration.ConnectionType;

public class Users {
	private List<ConnectionThread> users;

	public Users() {
		users = new LinkedList<>();
	}

	public void addUser(final ConnectionThread user) {
		users.add(user);

		sendMessage(
			ConnectionType.SERVER.toString(),
			user.getName() + " has joined the server."
		);
	}

	public void removeUser(final ConnectionThread user) {
		users.remove(user);

		sendMessage(
			ConnectionType.SERVER.toString(),
			user.getName() + " has left the server."
		);
	}

	public void sendMessage(
		final String author,
		final String message
	) {
		users
			.parallelStream()
			.forEach(user -> user.sendMessage(author, message));
	}
}
