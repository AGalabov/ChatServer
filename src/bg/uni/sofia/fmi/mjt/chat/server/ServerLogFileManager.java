package bg.uni.sofia.fmi.mjt.chat.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

public class ServerLogFileManager {

	private final String defaultLogFilePath = "serverLogFile.txt";
	private String logFilePath;

	public ServerLogFileManager() {
		this.logFilePath = defaultLogFilePath;
		new File(logFilePath);
	}
	
	public ServerLogFileManager(String logFilePath) {
		this.logFilePath = logFilePath;
		new File(logFilePath);
	}

	public void saveData(String line) {
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(logFilePath, true))) {
			pw.println(line);
			pw.flush();
		} catch (IOException e) {
			System.out.println(e.getClass() + " at ServerLogFileManager::saveData() " + e.getMessage());
		}
	}
}