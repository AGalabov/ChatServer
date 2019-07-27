package bg.uni.sofia.fmi.mjt.chat.user;

import bg.uni.sofia.fmi.mjt.chat.exceptions.CommandParametersCountException;

public class CommandSeparator {

	private String[] tokens;
	private int size;
	private final String SPACE = " ";

	public CommandSeparator() {
	}

	public void separateLine(String line) {
		this.tokens = line.split(SPACE);
		this.size = tokens.length;
	}

	public String getCommandToken() {
		return tokens[0];
	}

	public String getSecondToken() throws CommandParametersCountException {
		final int expectedLenght = 2;
		if (tokens.length < expectedLenght) {
			throw new CommandParametersCountException("Invalid number command line arguments.");
		}
		return tokens[1];
	}

	public String getThirdToken() throws CommandParametersCountException {
		final int expectedLenght = 3;
		if (tokens.length < expectedLenght) {
			throw new CommandParametersCountException("Invalid number command line arguments.");
		}
		return tokens[2];
	}

	public String getMessageFromIndex(int index) throws CommandParametersCountException {
		if (index > 0 && index < size) {
			StringBuilder msg = new StringBuilder();
			for (int i = index; i < size - 1; i++) {
				msg.append(tokens[i] + " ");
			}
			msg.append(tokens[size - 1]);
			return msg.toString();
		} else {
			throw new CommandParametersCountException("Invalid startIndex exception.");
		}

	}

	public String[] getTokens() {
		return tokens;
	}
}
