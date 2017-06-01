package server.servercomponent;

/**
 * Created by Ahmad on 31.05.2017.
 */
public class EntityAlreadyExistsException extends Exception{
    public EntityAlreadyExistsException(String message) {

        super(message);
    }
}
