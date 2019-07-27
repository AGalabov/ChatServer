package bg.uni.sofia.fmi.mjt.chat.server;

import java.util.ArrayList;
import java.util.List;

import bg.uni.sofia.fmi.mjt.chat.server.ServerMessageController;

public class ChatRoom {

	private String groupName;
	private ServerMessageController creator;
	private List<ServerMessageController> roomUsers;
	private ChatRoomsHistoryManager historyManager;

	public ChatRoom(ServerMessageController creator, String groupName) {
		this.creator = creator;
		this.groupName = groupName;
		roomUsers = new ArrayList<>();
		roomUsers.add(this.creator);
		historyManager = new ChatRoomsHistoryManager(groupName);
	}

	public String getRoomName() {
		return groupName;
	}

	public ServerMessageController getCreator() {
		return creator;
	}

	public int getRoomUsersCount() {
		return roomUsers.size();
	}

	public List<ServerMessageController> getRoomUsers() {
		return roomUsers;
	}

	public ChatRoomsHistoryManager getMessageHistoryManager() {
		return historyManager;
	}
	
	public void addUser(ServerMessageController smr) {
		roomUsers.add(smr);
	}
	
	public void removeUser(ServerMessageController smr) {
		roomUsers.remove(smr);
	}
	
	public boolean containsUser(ServerMessageController smr) {
		return roomUsers.contains(smr);
	}
	
	public String readHistoryData() {
		return historyManager.readHistory();
	}

	public void saveHistoryData(String historyMessage) {
		historyManager.saveData(historyMessage);
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((groupName == null) ? 0 : groupName.hashCode());
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
		ChatRoom other = (ChatRoom) obj;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		return true;
	}
}

