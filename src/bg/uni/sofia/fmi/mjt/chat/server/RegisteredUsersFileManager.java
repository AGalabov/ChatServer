package bg.uni.sofia.fmi.mjt.chat.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class RegisteredUsersFileManager {

	private final String defaultUsersDatabaseFile = "registeredUsers.txt";
	private String databasePath;

	public RegisteredUsersFileManager() {
		databasePath = defaultUsersDatabaseFile;
		new File(databasePath);
	}
	
	public RegisteredUsersFileManager(String databasePath) {
		this.databasePath = databasePath;
		new File(databasePath);
	}

	public boolean canLogin(String username, String password) {
		try (BufferedReader br = new BufferedReader(new FileReader(databasePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				if (tokens[0].equals(username) && tokens[1].equals(password)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println(e.getClass() + " at RegisteredUsersFileManager::RegisteredUsersFileManager() " + e.getMessage());
		}
		return false;
	}

	public boolean register(String username, String password) {
		if (!exists(username)) {
			try (PrintWriter pw = new PrintWriter(new FileOutputStream(databasePath, true))) {
				pw.println(username + " " + password);
				pw.flush();
				return true;
			} catch (IOException e) {
				System.out.println(e.getClass() + " at RegisteredUsersFileManager::register() " + e.getMessage());
			}
		}
		return false;
	}

	private boolean exists(String username) {
		try (BufferedReader br = new BufferedReader(new FileReader(databasePath))) {
			String line;
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split(" ");
				if (tokens[0].equals(username)) {
					return true;
				}
			}
		} catch (IOException e) {
			System.out.println(e.getClass() + " at RegisteredUsersFileManager::exists() " + e.getMessage());
		}
		return false;
	}
}
