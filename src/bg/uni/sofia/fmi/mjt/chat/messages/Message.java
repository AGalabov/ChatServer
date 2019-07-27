package bg.uni.sofia.fmi.mjt.chat.messages;

import java.io.Serializable;

public class Message implements Serializable {

	/**
	 * Message consisted of 4 String parts: command, sender, content receiver
	 */
	private static final long serialVersionUID = -4829826005133149268L;
	private String command;
	private String sender;
	private String content;
	private String receiver;

	public Message(String command, String sender, String content, String receiver) {
		this.command = command;
		this.sender = sender;
		this.content = content;
		this.receiver = receiver;
	}

	public String getCommand() {
		return command;
	}

	public String getSender() {
		return sender;
	}

	public String getContent() {
		return content;
	}

	public String getReceiver() {
		return receiver;
	}
}

