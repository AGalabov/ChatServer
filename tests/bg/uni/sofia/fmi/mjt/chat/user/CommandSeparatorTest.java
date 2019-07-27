package bg.uni.sofia.fmi.mjt.chat.user;

import static org.junit.Assert.*;

import org.junit.Test;

import bg.uni.sofia.fmi.mjt.chat.exceptions.CommandParametersCountException;

public class CommandSeparatorTest {

	private static CommandSeparator cr = new CommandSeparator();

	@Test
	public void commandManagerNullPointerTest() {
		CommandSeparator manager = new CommandSeparator();
		assertNotNull(manager);
	}
	
	@Test
	public void separateLinesShouldBeSuccessfull() throws CommandParametersCountException {
		final String command = "connect";
		final String secondToken = "localhost";
		final String thirdToken = "4444";
		final String line = command + " " + secondToken + " " + thirdToken;
		cr.separateLine(line);
		assertTrue(cr.getCommandToken().equals(command));
		assertTrue(cr.getSecondToken().equals(secondToken));
		assertTrue(cr.getThirdToken().equals(thirdToken));
	}

	@Test(expected = CommandParametersCountException.class)
	public void getSecondParamShouldThrowException() throws CommandParametersCountException {
		final String line = "list-room-users";
		cr.separateLine(line);
		cr.getSecondToken();
	}

	@Test(expected = CommandParametersCountException.class)
	public void getThirdParamShouldThrowException() throws CommandParametersCountException {
		final String line = "connect localhost";
		cr.separateLine(line);
		cr.getThirdToken();
	}

	@Test
	public void getMessageShouldReturnAMessage() throws CommandParametersCountException {
		String line = "send name Hello bro";
		cr.separateLine(line);
		assertTrue(cr.getMessageFromIndex(2).equals("Hello bro"));
	}

	@Test(expected = CommandParametersCountException.class)
	public void getMessageShouldThrowException() throws CommandParametersCountException {
		String line = "list-users";
		cr.separateLine(line);
		cr.getMessageFromIndex(2);
	}

}
