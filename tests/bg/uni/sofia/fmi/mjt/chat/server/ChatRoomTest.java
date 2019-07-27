package bg.uni.sofia.fmi.mjt.chat.server;

import static org.junit.Assert.*;

import java.io.File;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class ChatRoomTest {

	private static ChatRoom chatRoom;
	
	
	@Before
	public void createRoom() {
		chatRoom = new ChatRoom(null, "Test");
	}
	
	@Test
	public void chatRoomNullPointerTest() {
		assertNotNull(chatRoom);
	}
	
	@Test
	public void messaggeSendingTest() {
		final String message = "Hello world";
		chatRoom.saveHistoryData(message);
		assertTrue(chatRoom.readHistoryData().equals(message + "\n"));
	}
	
	@Test
	public void createChatRoomHistoryFileTest() {
		final File groupHistoryFile = chatRoom.getMessageHistoryManager().getHistoryFile();
		chatRoom.saveHistoryData("");
		assertTrue(groupHistoryFile.exists());
	}
	
	@Test
	public void createChatRoomHistoryFileDeleteTest() {
		final File groupHistoryFile = chatRoom.getMessageHistoryManager().getHistoryFile();
		chatRoom.getMessageHistoryManager().deleteHistoryFile();
		assertFalse(groupHistoryFile.exists());
	}
	
	@AfterClass
	public static void deleteChatRoomHistoryFileTest() {
		chatRoom.getMessageHistoryManager().deleteHistoryFile();
	}
	
}
