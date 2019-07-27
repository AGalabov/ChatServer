package bg.uni.sofia.fmi.mjt.chat.server;

import static org.junit.Assert.*;

import org.junit.Test;

public class RegisteredUsersFileManagerTest {

	private static final RegisteredUsersFileManager users = new RegisteredUsersFileManager("testFile.txt");

	@Test
	public void databaseNullPointerTest() {
		assertNotNull(users);
	}
	
	@Test
	public void canLoginSuccessfullTest() {
		final String username = "user";
		final String password = "pass";
		assertTrue(users.canLogin(username, password));
	}

	@Test
	public void canLoginWrongUsernameTest() {
		final String username = "username";
		final String password = "pass";
		assertFalse(users.canLogin(username, password));
	}

	@Test
	public void canLoginWrongPasswordTest() {
		final String username = "user";
		final String password = "password";
		assertFalse(users.canLogin(username, password));
	}

	@Test
	public void canRegisterTest() {
		final String username = "user";
		final String password = "pass";
		assertFalse(users.register(username, password));
	}
}
