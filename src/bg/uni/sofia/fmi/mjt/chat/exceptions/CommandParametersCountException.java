package bg.uni.sofia.fmi.mjt.chat.exceptions;

public class CommandParametersCountException extends Exception {
	
	/**
	 * Thrown when number of arguments expected 
	 * to perform a command is not provided.
	 */
	private static final long serialVersionUID = 1L;

	public CommandParametersCountException(String msg) {
		super(msg);
	}

}