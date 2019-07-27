package bg.uni.sofia.fmi.mjt.chat.client;

import java.io.*;
import java.net.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UploadManager extends Thread {

	private Socket socket;
	private FileInputStream inputStream;
	private OutputStream outputStream;
	private final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");

	public UploadManager(String serverAdress, int port, File filePath) {
		try {
			socket = new Socket(InetAddress.getByName(serverAdress), port);
			outputStream = socket.getOutputStream();
			inputStream = new FileInputStream(filePath);
		} catch (Exception e) {
			System.out.println(e.getClass() + " at DownloadClient::DownloadClient() " + e.getMessage());
		}
	}

	@Override
	public void run() {
		try {
			byte[] buffer = new byte[1024];
			int bytesRead;

			while ((bytesRead = inputStream.read(buffer)) >= 0) {
				outputStream.write(buffer, 0, bytesRead);
			}
			outputStream.flush();

			System.out.println("<SYSTEM> " + sdf.format(new Date()) + "\n" + "File upload complete.");
		} catch (Exception e) {
			System.out.println(e.getClass() + " at DownloadClient::run() " + e.getMessage());
		} finally {
			closeResources();
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
			System.out.println(e.getClass() + " at DownloadClient::closeResources() " + e.getMessage());
		}
	}
}