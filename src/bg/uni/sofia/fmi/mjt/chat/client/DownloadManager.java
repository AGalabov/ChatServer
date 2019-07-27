package bg.uni.sofia.fmi.mjt.chat.client;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DownloadManager extends Thread {

	private ServerSocket serverSocket;
	private Socket socket;
	private int port;
	private String pathToSaveTo;
	private InputStream inputStream;
	private FileOutputStream outputStream;
	private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public DownloadManager(String pathToSaveTo) {
		try {
			serverSocket = new ServerSocket(2500);
			port = serverSocket.getLocalPort();
			this.pathToSaveTo = pathToSaveTo;
		} catch (IOException e) {
			System.out.println(e.getClass() + " at DownloadPeerServer::DownloadPeerServer() " + e.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			socket = serverSocket.accept();

			inputStream = socket.getInputStream();
			new File(pathToSaveTo);
			outputStream = new FileOutputStream(pathToSaveTo);

			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = inputStream.read(buffer)) >= 0) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.flush();
			
			System.out.println("<SYSTEM> " + sdf.format(new Date()) + "\n" + "Download complete.");

		} catch (Exception e) {
			System.out.println(e.getClass() + " at DownloadPeerServer::run() " + e.getMessage());

		} finally {
			closeResources();
		}
	}

	public int getPort() {
		return this.port;
	}

	@SuppressWarnings("deprecation")
	public void closeResources() {
		try {
			if (outputStream != null) {
				outputStream.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			if (socket != null) {
				socket.close();
			}
			if (serverSocket != null) {
				serverSocket.close();
			}

			this.stop();
		} catch (IOException e) {
			System.out.println(e.getClass() + " at DownloadPeerServer::closeResources() " + e.getMessage());
		}
	}
}