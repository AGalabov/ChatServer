package bg.uni.sofia.fmi.mjt.chat.exceptions;

public class FileAccessDeniedException extends Exception {

	/**
	 * Thrown when an attempt to access a
	 * file, that can't be accessed, is made.
	 */
	private static final long serialVersionUID = 1L;

	public FileAccessDeniedException(String msg) {
		super(msg);
	}

}