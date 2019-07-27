package bg.uni.sofia.fmi.mjt.chat.client;

import java.text.SimpleDateFormat;
import java.util.Date;

import bg.uni.sofia.fmi.mjt.chat.user.UserCommands;
import bg.uni.sofia.fmi.mjt.chat.user.UserControlSystem;
import bg.uni.sofia.fmi.mjt.chat.messages.Message;

public class ResponsePrintManager {

	private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private UserControlSystem ucs;

	public ResponsePrintManager() {
	}

	public ResponsePrintManager(UserControlSystem ucs) {
		this.ucs = ucs;
	}

	public void printReceivedMessage(Message receivedMessage) {
		if (receivedMessage.getContent().equals("You are successfully logged in.")
				|| receivedMessage.getContent().equals("You successfully registered in.")) {
			ucs.setLogged(true);
		}
		if (receivedMessage.getCommand().equals(UserCommands.DISCONNECT)) {
			ucs.setLogged(false);
		}
		final String message = receivedMessage.getSender() + " " + sdf.format(new Date()) + "\n"
				+ receivedMessage.getContent();
		System.out.println(message);
	}

	public void sentFileMessage(Message receivedMessage, Client client) {
		System.out.println("<" + receivedMessage.getSender() + "> Do you accept the " + receivedMessage.getContent()
				+ " file?");
		client.getUserControlSystem().setMessageForFile(receivedMessage);
	}

	public void acceptFileMessage(Message receivedMessage, Client client) {
		int port = Integer.parseInt(receivedMessage.getContent());
		String IPAddress = receivedMessage.getSender();
		UploadManager downloadClient = new UploadManager(IPAddress, port, client.getFile());
		downloadClient.start();
	}

}
