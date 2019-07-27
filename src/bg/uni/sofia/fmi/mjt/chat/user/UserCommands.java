package bg.uni.sofia.fmi.mjt.chat.user;

public class UserCommands {
	public static final String CONNECT = "connect";
	public static final String REGISTER = "register";
	public static final String LOGIN = "login";
	public static final String DISCONNECT = "disconnect";
	public static final String LIST_USERS = "list-users";
	public static final String SEND = "send";
	public static final String SEND_GROUP = "send-group";
	public static final String SEND_FILE = "send-file";
	public static final String ACCEPT_FILE = "accept";
	public static final String DECLINE_FILE = "decline";
	public static final String CREATE_ROOM = "create-room";
	public static final String DELETE_ROOM = "delete-room";
	public static final String JOIN_ROOM = "join-room";
	public static final String LEAVE_ROOM = "leave-room";
	public static final String LIST_ROOMS = "list-rooms";
	public static final String LIST_ROOM_USERS = "list-room-users";
	public static final String STOP_SERVER = "stop-server";

	public static boolean isConnectCommand(String command) {
		return command.equals(CONNECT);
	}

	public static boolean isLoginCommand(String command) {
		return command.equals(LOGIN);
	}

	public static boolean isRegisterCommand(String command) {
		return command.equals(REGISTER);
	}
}