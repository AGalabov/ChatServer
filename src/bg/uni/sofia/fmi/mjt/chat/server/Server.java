package bg.uni.sofia.fmi.mjt.chat.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import bg.uni.sofia.fmi.mjt.chat.user.UserCommands;
import bg.uni.sofia.fmi.mjt.chat.messages.Message;

public class Server {
	private boolean serverIsActive;
	private int port;
	private List<ServerMessageController> activeClients;
	private List<ChatRoom> activeRooms;
	private ServerSocket serverSocket;
	private ServerResponseGenerator responseGenerator;
	private static final int SERVER_PORT = 4444;

	public Server(int port) {
		this.port = port;
		this.activeClients = new ArrayList<>();
		this.activeRooms = new ArrayList<>();
		this.serverIsActive = true;
	}

	public static void main(String[] args) {
		Server server = new Server(SERVER_PORT);
		server.start();
	}

	public void start() {
		try {
			responseGenerator = new ServerResponseGenerator(this);
			serverSocket = new ServerSocket(port);
			Socket socket = serverSocket.accept();
			while (serverIsActive) {
				ServerMessageController messageController = new ServerMessageController(this, socket);
				activeClients.add(messageController);
				messageController.start();
				socket = serverSocket.accept();
			}
		} catch (IOException e) {
			System.out.println(e.getClass() + " at Server::start() " + e.getMessage());
		} finally {
			closeResources();
		}
	}

	private void closeResources() {
		final String message = "Server closed. Goodbye ! Press enter to exit";

		for (Iterator<ServerMessageController> iterator = activeClients.iterator(); iterator.hasNext();) {
			ServerMessageController smc = iterator.next();
			smc.sendMessage(new Message("disconnect", "<SYSTEM>", message, ""));
			iterator.remove();
		}

		for (Iterator<ChatRoom> iterator = activeRooms.iterator(); iterator.hasNext();) {
			ChatRoom chatRoom = iterator.next();
			removeRoom(chatRoom);
			iterator.remove();
		}

		if (serverSocket != null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.out.println(e.getClass() + " at Server::closeResources() " + e.getMessage());
			}
		}

	}

	public void stopServer() {
		serverIsActive = false;
		Socket socket;
		try {
			socket = new Socket(serverSocket.getInetAddress(), SERVER_PORT);
			socket.close();
		} catch (IOException e) {
			System.out.println(e.getClass() + " at Server::stopServer() " + e.getMessage());
		}

	}

	public synchronized void manageMessages(Message message, Socket socket) {
		String command = message.getCommand();

		switch (command) {
		case UserCommands.CONNECT:
			responseGenerator.respondToConnect(message, socket);
			break;
		case UserCommands.REGISTER:
			responseGenerator.respondToRegister(message, socket);
			break;
		case UserCommands.LOGIN:
			responseGenerator.respondToLogin(message, socket);
			break;
		case UserCommands.LIST_USERS:
			responseGenerator.respondToListUsers(message, socket);
			break;
		case UserCommands.CREATE_ROOM:
			responseGenerator.respondToCreateRoom(message, socket);
			break;
		case UserCommands.DELETE_ROOM:
			responseGenerator.respondToDeleteRoom(message, socket);
			break;
		case UserCommands.JOIN_ROOM:
			responseGenerator.respondToJoinRoom(message, socket);
			break;
		case UserCommands.LEAVE_ROOM:
			responseGenerator.respondToLeaveRoom(message, socket);
			break;
		case UserCommands.LIST_ROOMS:
			responseGenerator.respondToListRooms(message, socket);
			break;
		case UserCommands.LIST_ROOM_USERS:
			responseGenerator.respondToListRoomUsers(message, socket);
			break;
		case UserCommands.SEND:
			responseGenerator.respondToMessage(message, socket);
			break;
		case UserCommands.SEND_GROUP:
			responseGenerator.respondToGroupMessage(message, socket);
			break;
		case UserCommands.SEND_FILE:
			responseGenerator.respondToSendFile(command, message, socket);
			break;
		case UserCommands.ACCEPT_FILE:
			responseGenerator.respondToAcceptFIle(command, message, socket);
			break;
		case UserCommands.DECLINE_FILE:
			responseGenerator.respondToDeclineFIle(message, socket);
			break;
		case UserCommands.DISCONNECT:
			responseGenerator.respondToDisconnect(message, socket);
			break;
		case UserCommands.STOP_SERVER:
			responseGenerator.respondToStopServer(message, socket);
			break;
		}
	}

	public List<ServerMessageController> getClients() {
		return activeClients;
	}

	public List<ChatRoom> getRooms() {
		return activeRooms;
	}

	public void addRoom(ChatRoom chatRoom) {
		activeRooms.add(chatRoom);
	}

	public void removeRoom(ChatRoom chatRoom) {
		activeRooms.remove(chatRoom);
		chatRoom.getMessageHistoryManager().deleteHistoryFile();
	}

	public void removeClient(ServerMessageController srt) {
		activeClients.remove(srt);
	}

	public boolean containsClient(ServerMessageController srt) {
		return activeClients.contains(srt);
	}
}
