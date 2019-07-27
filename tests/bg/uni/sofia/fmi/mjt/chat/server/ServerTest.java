package bg.uni.sofia.fmi.mjt.chat.server;

import static org.junit.Assert.*;

import org.junit.Test;

import bg.uni.sofia.fmi.mjt.chat.server.Server;

public class ServerTest {

	private static final Server s  = new Server(3333);

	@Test
	public void serverNullPointerTest() {
		assertNotNull(s);
	}
	
	/*
	 * if the Responder was returning messages and the controller sent
	 * them then I would be able to assert the generated Responses 
	 * with equals
	 */

}
