package bg.uni.sofia.fmi.mjt.chat.user;

import java.awt.geom.IllegalPathStateException;
import java.io.File;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import bg.uni.sofia.fmi.mjt.chat.client.Client;
import bg.uni.sofia.fmi.mjt.chat.client.DownloadManager;
import bg.uni.sofia.fmi.mjt.chat.exceptions.FileAccessDeniedException;
import bg.uni.sofia.fmi.mjt.chat.exceptions.FileCompatiabilityException;
import bg.uni.sofia.fmi.mjt.chat.exceptions.CommandParametersCountException;
import bg.uni.sofia.fmi.mjt.chat.messages.Message;

public class UserControlSystem {
	private Client client;
	private Scanner scanner;
	private CommandSeparator commandSeparator;
	private Message messageForFile;
	private final int MAX_SIZE_OF_FILE = 120 * 1024 * 1024; // 15MB
	private boolean isLogged;
	private boolean hasReceivedFile;

	public UserControlSystem(Scanner scanner) {
		this.scanner = scanner;
		this.commandSeparator = new CommandSeparator();
		isLogged = false;
		hasReceivedFile = false;
	}

	public boolean isLogged() {
		return isLogged;
	}

	public void setLogged(boolean logged) {
		this.isLogged = logged;
	}

	public void setMessageForFile(Message message) {
		this.hasReceivedFile = true; // there is a file to be downloaded
		this.messageForFile = message; // the message containing the message is saved here
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		new UserControlSystem(scanner).start();
		scanner.close();
	}

	public void start() {
		connect();
		authenticate();
		String line;
		do {
			if (!isLogged) {
				break;
			}
			line = scanner.nextLine();
		} while (isLogged && analyzeCurrentLine(line));
	}

	private void connect() {
		String line, command;
		final String askForConnection = "Insert a connect command!";
		do {
			System.out.println(askForConnection);
			line = scanner.nextLine();
			commandSeparator.separateLine(line);
			command = commandSeparator.getCommandToken();
		} while (!UserCommands.isConnectCommand(command));
		try {
			connectMessage(command, commandSeparator.getSecondToken(),
					Integer.parseInt(commandSeparator.getThirdToken()));
			TimeUnit.MILLISECONDS.sleep(10);
		} catch (NumberFormatException | CommandParametersCountException | InterruptedException e) {
			System.out.println(e.getMessage());
			connect();
		}

	}

	private void authenticate() {
		String line, command;
		final String askForAuthentication = "Insert a register or login command!";
		do {
			System.out.println(askForAuthentication);
			line = scanner.nextLine();
			commandSeparator.separateLine(line);
			command = commandSeparator.getCommandToken();
			try {
				getInSystem(command, commandSeparator.getSecondToken(), commandSeparator.getThirdToken());
				TimeUnit.MILLISECONDS.sleep(10);
			} catch (FileAccessDeniedException | CommandParametersCountException | InterruptedException ex) {
				System.out.println(ex.getMessage());
			}
		} while (!isLogged);

	}

	private boolean analyzeCurrentLine(String line) {

		commandSeparator.separateLine(line);
		String command = commandSeparator.getCommandToken();

		try {
			switch (command) {
			case UserCommands.DISCONNECT:
				isLogged = false;
			case UserCommands.LIST_USERS:
			case UserCommands.LIST_ROOMS:
			case UserCommands.STOP_SERVER:
				globalServerMessage(command);
				break;
			case UserCommands.CREATE_ROOM:
			case UserCommands.DELETE_ROOM:
			case UserCommands.JOIN_ROOM:
			case UserCommands.LEAVE_ROOM:
			case UserCommands.LIST_ROOM_USERS:
				roomSpecificMessage(command, commandSeparator.getSecondToken());
				break;
			case UserCommands.SEND:
			case UserCommands.SEND_GROUP:
				sendMessage(command, commandSeparator.getSecondToken(), commandSeparator.getMessageFromIndex(2));
				break;
			case UserCommands.SEND_FILE:
				sendFile(command, commandSeparator.getSecondToken(), commandSeparator.getMessageFromIndex(2));
				break;
			case UserCommands.ACCEPT_FILE:
				acceptFile(command);
				break;
			case UserCommands.DECLINE_FILE:
				declineFile(command);
				break;
			default:
				System.out.println("There is no such command.");
			}
		} catch (CommandParametersCountException | IllegalPathStateException | FileAccessDeniedException
				| FileCompatiabilityException e) {
			System.out.println(e.getMessage());
		}
		return isLogged;
	}

	private void connectMessage(String command, String server, int port) {
		client = new Client(server, port, this);
		client.start();
		client.send(new Message(command, "", "", ""));
	}

	private void getInSystem(String command, String username, String password) throws FileAccessDeniedException {
		client.send(new Message(command, username, password, ""));
	}

	private void globalServerMessage(String command) throws FileAccessDeniedException {
		client.send(new Message(command, "", "", ""));
	}

	private void roomSpecificMessage(String command, String roomName) throws FileAccessDeniedException {
		client.send(new Message(command, "", roomName, ""));
	}

	private void sendMessage(String command, String receiver, String message) throws FileAccessDeniedException {
		client.send(new Message(command, "", message, receiver));
	}

	private void sendFile(String command, String receiver, String filePath)
			throws IllegalPathStateException, FileAccessDeniedException, FileCompatiabilityException {
		File file = new File(filePath);
		if (!file.isDirectory() && file.exists()) {
			if (file.length() <= MAX_SIZE_OF_FILE) {
				client.send(new Message(command, "", file.getName(), receiver));
				client.setFile(file);
			} else {
				throw new FileCompatiabilityException("The file is too big.");
			}
		} else {
			throw new IllegalPathStateException("Path is directory or non-existent file.");
		}
	}

	private void acceptFile(String command) throws IllegalPathStateException, FileAccessDeniedException {
		if (hasReceivedFile) { // if we do have a file to accept
			System.out.println("Specify path for the file to be downloaded to: ");
			String location = scanner.nextLine();
			File dir = new File(location);
			if (dir.isDirectory()) {
				@SuppressWarnings("resource")
				DownloadManager dwd = new DownloadManager(location + "\\" + messageForFile.getContent());
				dwd.start();
				hasReceivedFile = false; // shows the file would not be available anymore
				client.send(new Message(command, "", String.valueOf(dwd.getPort()), messageForFile.getSender()));
			} else {
				throw new IllegalPathStateException("The path specified is not a directory.");
			}
		} else {
			throw new FileAccessDeniedException("You haven't received a file.");
		}
	}

	private void declineFile(String command) throws FileAccessDeniedException {
		if (hasReceivedFile) { // if we do have a file to decline
			client.send(new Message(command, "", "declined", messageForFile.getSender()));
			hasReceivedFile = false; // shows the file would not be available anymore
		} else {
			throw new FileAccessDeniedException("You haven't received a file.");
		}
	}
}
