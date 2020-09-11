import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import domain.enumeration.Command;
import domain.enumeration.ConnectionType;

public abstract class Window extends JFrame implements ActionListener, WindowListener {
	private static final long serialVersionUID = 1L;

	private Window window;

	private JTextArea messageOutput;
	private JTextField messageInput;

	protected Window(
		final ConnectionType connectionType,
		final String ip,
		final int port
	) {
		super(connectionType.toString());

		drawFrame();
		drawMessageInput();
		drawMessageOutput();

		window.revalidate();

		connect(ip, port);
	}

	// Used Listeners
	@Override
	public void actionPerformed(final ActionEvent event) {
		final String message = messageInput.getText();
		messageInput.setText("");

		sendMessage(message);
	}

	@Override
	public void windowClosing(final WindowEvent event) {
		sendMessage(Command.QUIT.getValue());
	}

	// Unused Listeners
	@Override
	public void windowActivated(final WindowEvent event) {

	}

	@Override
	public void windowClosed(final WindowEvent event) {

	}

	@Override
	public void windowDeactivated(final WindowEvent event) {

	}

	@Override
	public void windowDeiconified(final WindowEvent event) {

	}

	@Override
	public void windowIconified(final WindowEvent event) {

	}

	@Override
	public void windowOpened(final WindowEvent event) {

	}

	// Draw
	private void drawMessageInput() {
		messageInput = new JTextField();
		messageInput.addActionListener(window);

		window.add(messageInput, BorderLayout.SOUTH);
	}

	private void drawMessageOutput() {
		messageOutput = new JTextArea();
		messageOutput.setEditable(false);
		messageOutput.setLineWrap(true);
		messageOutput.setWrapStyleWord(true);

		window.add(new JScrollPane(messageOutput), BorderLayout.CENTER);
	}

	private void drawFrame() {
		window = this;
		window.addWindowListener(window);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(500, 250);
		window.setVisible(true);
	}

	// Abstract
	protected abstract void connect(final String ip, final int port);
	protected abstract void disconnect();
	protected abstract void sendMessage(final String message);

	//
	protected void offline() {
		messageInput.setEditable(false);
	}

	protected void printMessage(final String message) {
		messageOutput.append(message + "\n");
		messageOutput.setCaretPosition(messageOutput.getDocument().getLength());
	}
}