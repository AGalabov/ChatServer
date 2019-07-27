package bg.uni.sofia.fmi.mjt.chat.user;

import static org.junit.Assert.*;

import org.junit.Test;

public class UserCommandsTest {

	@Test
	public void isConnectCommandShouldReturnTrue() {
		final String command = "connect";
		assertTrue(UserCommands.isConnectCommand(command));
	}
	
	@Test
	public void isConnectCommandShouldReturnFalse() {
		final String command = "register";
		assertFalse(UserCommands.isConnectCommand(command));
	}
	
	@Test
	public void isRegisterCommandShouldReturnTrue() {
		final String command = "register";
		assertTrue(UserCommands.isRegisterCommand(command));
	}
	
	@Test
	public void isRegisterCommandShouldReturnFalse() {
		final String command = "login";
		assertFalse(UserCommands.isRegisterCommand(command));
	}

	@Test
	public void isLoginCommandShouldReturnTrue() {
		final String command = "login";
		assertTrue(UserCommands. isLoginCommand(command));
	}
	
	@Test
	public void isLoginCommandShouldReturnFalse() {
		final String command = "list-users";
		assertFalse(UserCommands.isLoginCommand(command));
	}
}
