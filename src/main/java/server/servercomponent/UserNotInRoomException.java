package server.servercomponent;

import javax.jws.soap.SOAPBinding;

/**
 * Created by Ahmad on 31.05.2017.
 */
public class UserNotInRoomException extends Exception {
    public UserNotInRoomException(String message){
        super(message);
    }
}
