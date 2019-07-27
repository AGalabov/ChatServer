package bg.uni.sofia.fmi.mjt.chat.user;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserControlSystemTest {
	/*
	private static UserControlSystem ucs;

	@Before
	public static void setLogged() {
		String input = "connect localhost 4444";
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
		ucs = new UserControlSystem(new Scanner(System.in));
		ucs.start();
	}
	
	@Test
	public static void checkConnected() {
		assertTrue(ucs.isLogged());
	}
	
	@After
	public static void disconnect() {
		String login = "login Alex Galabov";
		String disconnect = "disconnect";
		String input = login + "\n" + disconnect;
		InputStream in = new ByteArrayInputStream(input.getBytes());
		System.setIn(in);
	}

	
	@Test
	public void analyzeCurrentLineDefaultTest() {
		final String line = "connect localhost 4444";
		assertTrue(ucs.analyzeCurrentLine(line));
	}

	@Test(expected = NullPointerException.class)
	public void analyzeCurrentLineListUsersTest() {
		final String line = "list-users";
		assertTrue(ucs.analyzeCurrentLine(line));
	}

	@Test
	public void analyzeCurrentLineSendTest() {
		final String line = "send Alex Hi bro";
		assertTrue(ucs.analyzeCurrentLine(line));
	}

	@Test
	public void analyzeCurrentLineDisconnectTest() {
		final String line = "disconnect";
		assertFalse(ucs.analyzeCurrentLine(line));
	}*/

}
