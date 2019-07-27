package bg.uni.sofia.fmi.mjt.chat.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class ChatRoomsHistoryManager {

	private String historyFilePath;
	private File historyFile;
	private static final String HISTORY_FOLDER = "ChatHistory/";
	private static final String TXT_EXTENSION = ".txt";

	public ChatRoomsHistoryManager(String groupName) {
		this.historyFilePath = HISTORY_FOLDER + groupName + TXT_EXTENSION;
		this.historyFile = new File(historyFilePath);
	}

	public void saveData(String line) {
		try (PrintWriter pw = new PrintWriter(new FileOutputStream(historyFilePath, true))) {
			pw.println(line);
			pw.flush();
		} catch (IOException e) {
			System.out.println(e.getClass() + " at ChatRoomsHistoryManager::ChatRoomsHistoryManager() " + e.getMessage());
		}
	}

	public String readHistory() {
		String history = "";
		final String newRow = "\n";
		try (BufferedReader br = new BufferedReader(new FileReader(historyFilePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				history += line + newRow;
			}
		} catch (IOException e) {
			System.out.println(e.getClass() + " at ChatRoomsHistoryManager::readHistory() " + e.getMessage());
		}
		return history;
	}

	public File getHistoryFile() {
		return historyFile;
	}
	
	public void deleteHistoryFile() {
		historyFile.delete();
	}
}

