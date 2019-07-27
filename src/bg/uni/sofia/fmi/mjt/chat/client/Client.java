package bg.uni.sofia.fmi.mjt.chat.client;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import bg.uni.sofia.fmi.mjt.chat.user.UserCommands;
import bg.uni.sofia.fmi.mjt.chat.user.UserControlSystem;
import bg.uni.sofia.fmi.mjt.chat.messages.Message;

public class Client extends Thread {

	private Socket socket;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private ResponsePrintManager printManager;
	private File file;
	private UserControlSystem ucs;
	private boolean isConnected;

	public Client(String serverAddress, int port, UserControlSystem ucs) {
		try {
			socket = new Socket(InetAddress.getByName(serverAddress), port);
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
			inputStream = new ObjectInputStream(socket.getInputStream());
			isConnected = true;
			printManager = new ResponsePrintManager(ucs);
			this.ucs = ucs;
		} catch (IOException e) {
			System.out.println(e.getClass() + " at Client::Client() " + e.getMessage());
			ucs.start();
		}
	}

	@Override
	public void run() {
		try {

			while (isConnected) {
				Message msg = (Message) inputStream.readObject();
				switch (msg.getCommand()) {
				case UserCommands.SEND_FILE:
					printManager.sentFileMessage(msg, this);
					break;
				case UserCommands.ACCEPT_FILE:
					printManager.acceptFileMessage(msg, this);
					break;
				case UserCommands.DISCONNECT:
					isConnected = false;// no break so that it sends the message
				default:
					printManager.printReceivedMessage(msg);
				}
			}
		} catch (ClassNotFoundException | IOException e) {
			System.out.println(e.getClass() + " at Client::run() " + e.getMessage());
		} finally {
			closeResources();
		}
	}

	public UserControlSystem getUserControlSystem() {
		return ucs;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public File getFile() {
		return this.file;
	}

	public void setisConnected(boolean isConnected) {
		this.isConnected = isConnected;
	}

	public void send(Message msg) {
		try {
			outputStream.writeObject(msg);
			outputStream.flush();
		} catch (IOException e) {
			System.out.println(e.getClass() + " at Client::send() " + e.getMessage());
		}
	}

	public void closeResources() {
		try {
			if (socket != null) {
				socket.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (ucs != null) {
				ucs.setLogged(false);
			}

		} catch (IOException e) {
			System.out.println(e.getClass() + " at Client::closeResources() " + e.getMessage());
		}
	}
}
