package bg.uni.sofia.fmi.mjt.chat.server;

import java.net.Socket;
import java.util.List;

import bg.uni.sofia.fmi.mjt.chat.messages.Message;

public class ServerResponseGenerator {

	private Server server;
	private RegisteredUsersFileManager registeredUsersFile;
	private ServerLogFileManager serverLogFile;
	private final String SEND = "send";
	private final String SYSTEM = "<SYSTEM>";
	private final String ADMIN_USERNAME = "admin";

	public ServerResponseGenerator(Server server) {
		this.server = server;
		this.registeredUsersFile = new RegisteredUsersFileManager();
		this.serverLogFile = new ServerLogFileManager();
		serverLogFile.saveData("START SERVER");
	}

	private ServerMessageController getClientByUsername(String username) {
		for (ServerMessageController smc : server.getClients()) {
			if (smc.getUsername().equals(username)) {
				return smc;
			}
		}
		return null;
	}

	private ServerMessageController getClientBySocket(Socket socket) {
		for (ServerMessageController smc : server.getClients()) {
			if (smc.getSocket().equals(socket)) {
				return smc;
			}
		}
		return null;
	}

	private ChatRoom getChatRoomByName(String name) {
		for (ChatRoom cr : server.getRooms()) {
			if (cr.getRoomName().equals(name)) {
				return cr;
			}
		}
		return null;
	}

	public void respondToConnect(Message receivedMessage, Socket socket) {
		getClientBySocket(socket).sendMessage(new Message(SEND, SYSTEM, "You have successfully connected.", ""));
		serverLogFile.saveData("NEW CONNECTION");
	}

	public void respondToRegister(Message receivedMessage, Socket socket) {
		ServerMessageController messageController = getClientBySocket(socket);
		String message;
		if (registeredUsersFile.register(receivedMessage.getSender(), receivedMessage.getContent())) {
			messageController.setUsername(receivedMessage.getSender());
			message = "You successfully registered in.";
			serverLogFile.saveData("REGISTRATION: " + receivedMessage.getSender());
		} else {
			message = "Registration failed. The username already exists.";
		}
		messageController.sendMessage(new Message(SEND, SYSTEM, message, receivedMessage.getSender()));
	}

	public void respondToLogin(Message receivedMessage, Socket socket) {
		ServerMessageController testController = getClientByUsername(receivedMessage.getSender());
		ServerMessageController messageController = getClientBySocket(socket);
		String message;
		if (testController == null) {
			if (registeredUsersFile.canLogin(receivedMessage.getSender(), receivedMessage.getContent())) {
				messageController.setUsername(receivedMessage.getSender());
				message = "You are successfully logged in.";
				serverLogFile.saveData("LOGIN: " + receivedMessage.getSender());
			} else {
				message = "Login failed. Wrong username or password.";
			}
		} else {
			message = "This account is already in use.";
		}
		messageController.sendMessage(new Message(SEND, SYSTEM, message, receivedMessage.getSender()));
	}

	public void respondToListUsers(Message receivedMessage, Socket socket) {
		int counter = 1;
		final String newRow = "\n";
		String message = "Online users:" + newRow;
		for (ServerMessageController srt : server.getClients()) {
			message += counter + ") " + srt.getUsername() + newRow;
			counter++;
		}
		getClientBySocket(socket).sendMessage(new Message(SEND, SYSTEM, message, ""));
	}

	public void respondToCreateRoom(Message receivedMessage, Socket socket) {
		ServerMessageController messageController = getClientBySocket(socket);
		ChatRoom chatRoom = new ChatRoom(messageController, receivedMessage.getContent());
		String message;
		if (!server.getRooms().contains(chatRoom)) {
			message = "You created chat room <" + chatRoom.getRoomName() + ">.";
			server.addRoom(chatRoom);
			serverLogFile.saveData("NEW ROOM: " + chatRoom.getRoomName());
			chatRoom.saveHistoryData("Chat room " + receivedMessage.getContent() + " has been created");
		} else {
			message = "Name is already used.";
		}
		messageController.sendMessage(new Message(SEND, SYSTEM, message, ""));
	}

	private void notifyDeletedRoom(ChatRoom chatRoom, ServerMessageController srt) {
		final String messageToMembers = "Chat room <" + chatRoom.getRoomName() + "> has been deleted.";
		Message newMessage = new Message(SEND, SYSTEM, messageToMembers, "");
		chatRoom.removeUser(srt);
		sendToChatRoom(chatRoom, newMessage);
	}

	public void respondToDeleteRoom(Message receivedMessage, Socket socket) {
		ChatRoom chatRoom = getChatRoomByName(receivedMessage.getContent());
		ServerMessageController messageController = getClientBySocket(socket);
		String message;
		if (chatRoom != null) {
			if (chatRoom.getCreator().equals(messageController)) {
				message = "You deleted chat room <" + chatRoom.getRoomName() + ">.";
				notifyDeletedRoom(chatRoom, messageController);
				server.removeRoom(chatRoom);
				serverLogFile.saveData("DELETED ROOM: " + chatRoom.getRoomName());
			} else {
				message = "You have no permissions to do that.";
			}
		} else {
			message = "There is no chat room with this name.";
		}
		messageController.sendMessage(new Message(SEND, SYSTEM, message, ""));
	}

	public void respondToJoinRoom(Message receivedMessage, Socket socket) {
		ChatRoom chatRoom = getChatRoomByName(receivedMessage.getContent());
		String message = "";
		ServerMessageController messageController = getClientBySocket(socket);
		if (chatRoom != null) {
			if (!chatRoom.containsUser(messageController)) {
				message = chatRoom.readHistoryData();
				final String sender = "<" + chatRoom.getRoomName() + ">";
				;
				final String messageToMembers = messageController.getUsername() + " joined room <"
						+ chatRoom.getRoomName() + ">.";
				Message newMessage = new Message(SEND, sender, messageToMembers, "");
				sendToChatRoom(chatRoom, newMessage);
				chatRoom.addUser(messageController);
			} else {
				message = "You are already in the chat room.";
			}

		} else {
			message = "There is no such chat room.";
		}
		messageController.sendMessage(new Message(SEND, SYSTEM, message, messageController.getUsername()));
	}

	public void respondToLeaveRoom(Message receivedMessage, Socket socket) {
		ChatRoom chatRoom = getChatRoomByName(receivedMessage.getContent());
		ServerMessageController messageController = getClientBySocket(socket);
		String message;
		if (chatRoom != null) {
			if (chatRoom.containsUser(messageController)) {
				if (chatRoom.getCreator().equals(messageController)) {
					message = "You deleted chat room <" + chatRoom.getRoomName() + ">.";
					serverLogFile.saveData("DELETED ROOM: " + chatRoom.getRoomName());
					notifyDeletedRoom(chatRoom, messageController);
					server.removeRoom(chatRoom);
				} else {
					message = "You left chat room " + receivedMessage.getContent();
					chatRoom.removeUser(messageController);
					final String sender = "<" + chatRoom.getRoomName() + ">";
					
					final String messageToMembers = messageController.getUsername() + " has left room <"
							+ chatRoom.getRoomName() + ">.";
					Message newMessage = new Message(SEND, sender, messageToMembers, "");
					sendToChatRoom(chatRoom, newMessage);
				}
			} else {
				message = "You are not in this room.";
			}
		} else {
			message = "There is no such chat room.";
		}
		messageController.sendMessage(new Message(SEND, SYSTEM, message, ""));
	}

	public void respondToListRooms(Message receivedMessage, Socket socket) {
		List<ChatRoom> list = server.getRooms();
		ServerMessageController messageController = getClientBySocket(socket);
		int counter = 1;
		final String newRow = "\n";
		String message = "Active rooms:" + newRow;
		String activeUsersCountMessage;
		for (ChatRoom cr : list) {
			activeUsersCountMessage = " with " + cr.getRoomUsersCount() + " active users";
			message += counter + ") " + cr.getRoomName() + activeUsersCountMessage + newRow;
			counter++;
		}
		if (counter == 1) {
			message = "No online chat rooms." + newRow;
		}
		messageController.sendMessage(new Message(SEND, SYSTEM, message, ""));
	}

	public void respondToListRoomUsers(Message receivedMessage, Socket socket) {
		ChatRoom chatRoom = getChatRoomByName(receivedMessage.getContent());
		ServerMessageController messageController = getClientBySocket(socket);
		int counter = 1;
		final String newRow = "\n";
		String message;
		if (chatRoom != null) {
			message = "Active users in chat room " + chatRoom.getRoomName() + ":" + newRow;
			for (ServerMessageController user : chatRoom.getRoomUsers()) {
				message += counter + ") " + user.getUsername() + newRow;
				counter++;
			}
		} else {
			message = "There is no such chat room.";
		}
		messageController.sendMessage(new Message(SEND, SYSTEM, message, ""));
	}

	public void respondToMessage(Message receivedMessage, Socket socket) {
		ServerMessageController receiver = getClientByUsername(receivedMessage.getReceiver());
		if (receiver != null) {
			final String sender = "<" + getClientBySocket(socket).getUsername() + ">";
			final String receiverName = "<" + receivedMessage.getReceiver() + ">";
			receiver.sendMessage(new Message(SEND, sender, receivedMessage.getContent(), receiverName));
		} else {
			ServerMessageController messageController = getClientBySocket(socket);
			messageController.sendMessage(new Message(SEND, SYSTEM, "There is no active user with that name", ""));
		}
	}

	public void respondToGroupMessage(Message receivedMessage, Socket socket) {
		ServerMessageController messageController = getClientBySocket(socket);
		ChatRoom chatRoom = getChatRoomByName(receivedMessage.getReceiver());
		String message = messageController.getUsername() + ": " + receivedMessage.getContent();
		if (chatRoom != null) {
			if (chatRoom.containsUser(messageController)) {
				final String sender = "<" + chatRoom.getRoomName() + ">";
				Message newMsg = new Message(SEND, sender, message, "");
				sendToChatRoom(chatRoom, newMsg);
				return;
			} else {
				message = "You are not in the room.";
			}
		} else {
			message = "There is no room with that name";
		}
		messageController.sendMessage(new Message(SEND, SYSTEM, message, ""));
	}

	public void respondToSendFile(String command, Message receivedMessage, Socket socket) {
		ServerMessageController messageController = getClientBySocket(socket);
		ServerMessageController reciever = getClientByUsername(receivedMessage.getReceiver());
		if (reciever != null) {
			reciever.sendMessage(
					new Message(command, messageController.getUsername(), receivedMessage.getContent(), ""));
		} else {
			final String message = "There is no such user.";
			messageController.sendMessage(new Message(SEND, receivedMessage.getReceiver(), message, ""));
		}
	}

	public void respondToAcceptFIle(String command, Message receivedMessage, Socket socket) {
		String IP = getClientBySocket(socket).getSocket().getInetAddress().getHostAddress();
		getClientByUsername(receivedMessage.getReceiver())
				.sendMessage(new Message(command, IP, receivedMessage.getContent(), ""));
	}

	public void respondToDeclineFIle(Message receivedMessage, Socket socket) {
		ServerMessageController messageController = getClientBySocket(socket);
		final String message = receivedMessage.getReceiver() + " rejected the file.";
		final String sender = "<" + messageController.getUsername() + ">";
		getClientByUsername(receivedMessage.getReceiver()).sendMessage(new Message(SEND, sender, message, ""));
	}

	public void respondToDisconnect(Message receivedMessage, Socket socket) {
		ServerMessageController messageController = getClientBySocket(socket);
		final String message = "Goodbye, " + messageController.getUsername() + "!";
		serverLogFile.saveData("DISCONNECT: " + messageController.getUsername());
		server.removeClient(messageController);
		messageController
				.sendMessage(new Message(receivedMessage.getCommand(), SYSTEM, message, receivedMessage.getSender()));
	}

	public void respondToStopServer(Message receivedMessage, Socket socket) {
		ServerMessageController messageController = getClientBySocket(socket);
		if (messageController.getUsername().equals(ADMIN_USERNAME)) {
			serverLogFile.saveData("STOP SERVER");
			server.stopServer();
		} else {
			messageController.sendMessage(new Message(SEND, SYSTEM, "You are not authorized to do this!", ""));
		}

	}

	private void sendToChatRoom(ChatRoom chatRoom, Message receivedMessage) {
		chatRoom.saveHistoryData(receivedMessage.getContent());
		for (ServerMessageController smc : chatRoom.getRoomUsers()) {
			smc.sendMessage(receivedMessage);
		}
	}

}
