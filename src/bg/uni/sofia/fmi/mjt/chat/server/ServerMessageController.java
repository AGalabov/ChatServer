package bg.uni.sofia.fmi.mjt.chat.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import bg.uni.sofia.fmi.mjt.chat.messages.Message;

class ServerMessageController extends Thread {

	private Server server;
	private Socket socket;
	private String username;
	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;

	public ServerMessageController(Server server, Socket socket) {
		this.server = server;
		this.socket = socket;
		this.username = "";
		try {
			inputStream = new ObjectInputStream(socket.getInputStream());
			outputStream = new ObjectOutputStream(socket.getOutputStream());
			outputStream.flush();
		} catch (IOException e) {
			System.out
					.println(e.getClass() + " at ServerMessageController::ServerMessageController() " + e.getMessage());
		}

	}

	public void run() {
		try {
			while (true) {
				Message message = (Message) inputStream.readObject();
				server.manageMessages(message, socket);
				if (message.getCommand().equals("disconnect")) {
					break;
				}
			}
		} catch (Exception e) {
			System.out.println(e.getClass() + " at ServerMessageController::run() " + e.getMessage());
			server.removeClient(this);
		} finally {
			closeResources();
		}
	}

	public Socket getSocket() {
		return this.socket;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void sendMessage(Message msg) {
		try {
			outputStream.writeObject(msg);
			outputStream.flush();
		} catch (IOException e) {
			System.out.println(e.getClass() + " at ServerMessageController::sendMessage() " + e.getMessage());
		}
	}

	@SuppressWarnings("deprecation")
	public void closeResources() {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
			if (outputStream != null) {
				outputStream.close();
			}
			if (socket != null) {
				socket.close();
			}
			this.stop();
		} catch (IOException e) {
			System.out.println(e.getClass() + " at ServerMessageController::closeResources() " + e.getMessage());
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServerMessageController other = (ServerMessageController) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
