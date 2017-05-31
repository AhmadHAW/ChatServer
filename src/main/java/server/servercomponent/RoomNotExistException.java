package server.servercomponent;

public class RoomNotExistException extends Exception {

	RoomNotExistException(String message) {
		super(message);
	}
}
