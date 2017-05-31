package server.servercomponent;

public class UserNotExistException extends Exception {
	UserNotExistException(String message) {
		super(message);
	}
}
