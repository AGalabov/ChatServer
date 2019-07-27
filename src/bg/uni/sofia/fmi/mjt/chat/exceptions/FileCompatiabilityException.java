package bg.uni.sofia.fmi.mjt.chat.exceptions;

public class FileCompatiabilityException extends Exception {

	/**
	 * Thrown when an attempt to upload a file, not 
	 * meeting the requirements(size), is made.
	 */
	private static final long serialVersionUID = 1L;

	public FileCompatiabilityException(String msg) {
		super(msg);
	}

}