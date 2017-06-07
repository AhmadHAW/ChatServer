package server;

/**
 * Created by Ahmad on 31.05.2017.
 */
public class GlobalConstantsAndValidation {

    public final static String CLIENT_ROOM_RESOURCES = "/chatbot/client/rooms";
    public final static String CLIENT_USER_RESOURCES = "/users";
    public final static String TEST_RESOURCES = "/test";
    public final static String NAME_REGEX = "\\w+";
    public final static String IP_ADRESS_REGEX = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
    public final static String MESSAGE_REGEX = ".+";
    public final static int PORT_MIN = 0;
    public final static int PORT_MAX = 65535;
    public final static int MAXMESSAGESIZE = 508;


    public static boolean isMessageValid(String message){
        return message!=null&&message.matches(GlobalConstantsAndValidation.MESSAGE_REGEX);
    }

    public static boolean isValidName(String name){
        return name != null && name.matches(GlobalConstantsAndValidation.NAME_REGEX);
    }

    public static boolean isValidIpAdress(String ipAdresse){
        return ipAdresse != null && ipAdresse.matches(GlobalConstantsAndValidation.IP_ADRESS_REGEX);
    }

    public static boolean isValidPort(int port){
        return port >= PORT_MIN && port<=PORT_MAX;
    }
}
