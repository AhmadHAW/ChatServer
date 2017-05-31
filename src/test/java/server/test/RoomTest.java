package server.test;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import server.servercomponent.*;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Ahmad on 31.05.2017.
 */
class RoomTest {

    private Room room1;
    private Room room2;
    private Room room3;
    private String roomName1 = "roomName1";
    private String roomName2= "roomName2";
    private Set<User> users1;
    Set<User> users2 = new HashSet<User>();
    private User user1;
    private User user2;
    private String userName1 = "user1";
    private int port1 = 1;
    private String ipAdresse1 = "203.0.113.195";
    private String userName2 = "user2";
    private int port2 = 2;
    private String ipAdresse2 = "211.1.131.193";

    @BeforeEach
    void setUp() throws NameNotValidException, UnknownHostException, GivenObjectNotValidException {
        user1 = new User(userName1, port1, ipAdresse1);
        users1 = new HashSet<User>();
        users1.add(new User(userName1,port1,ipAdresse1));
        users2.add(new User(userName1,port1,ipAdresse1));
        room1 = new Room(roomName1);
        room2 = new Room(roomName2);
    }

    @Test
    void getRoomNameTest() {
        assertEquals(roomName1,room1.getRoomName());
    }

    @Test
    void getUsersTest() {
        assertTrue(room1.getUsers().isEmpty());
    }

    @Test
    void roomConstructorPositiveTest() throws NameNotValidException {
        room1 = new Room(roomName1, users1);
        assertEquals(roomName1, room1.getRoomName());
        assertEquals(users1,room1.getUsers());
        /**
         * Hier teste ich ob auch die Identität oder der Inhalt die gleiche ist.
         */
        assertTrue(users1!=room1.getUsers());
    }

    @Test
    void setRoomNamePositive() throws NameNotValidException {
        room1.setRoomName(roomName2);
        assertEquals(roomName2,room1.getRoomName());
    }

    @Test
    void setRoomNameNull() throws NameNotValidException {

        Assertions.assertThrows(NameNotValidException.class, () -> {
            room1.setRoomName(null);
        });
    }

    @Test
    void setRoomNameNoValidName() throws NameNotValidException {

        Assertions.assertThrows(NameNotValidException.class, () -> {
            room1.setRoomName(".-");
        });
    }


    @Test
    void setUsersPositive() throws NameNotValidException {
        room1.setRoomName(roomName2);
        assertEquals(roomName2,room1.getRoomName());
    }

    @Test
    void setUsersNull() throws NameNotValidException {

        Assertions.assertThrows(NameNotValidException.class, () -> {
            room1.setUsers(null);
        });
    }

    @Test
    void setUsersNullUser() throws NameNotValidException {
        users2.add(null);
        room1.setUsers(users2);
        assertEquals(users1,room1.getUsers());

    }

    @Test
    void addUserPositive() throws GivenObjectNotValidException {
        room1.addUser(user1);
        assertEquals(users1,room1.getUsers());
    }

    @Test
    void addUserPositiveSame() throws NameNotValidException, UnknownHostException, GivenObjectNotValidException {
        room1.addUser(user1);
        user2 = new User(userName1, port1, ipAdresse1);
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            room1.addUser(user2);
        });
    }

    @Test
    void addUserNull() throws GivenObjectNotValidException {
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            room1.addUser(null);
        });

    }

    @Test
    void removeUserPositive() throws GivenObjectNotValidException, UserNotExistException {
        room1.addUser(user1);
        room1.removeUser(user1);
        assertFalse(room1.getUsers().contains(user1));
    }

    @Test
    void removeUserNamePositive() throws NameNotValidException, UserNotExistException, GivenObjectNotValidException {
        room1.addUser(user1);
        room1.removeUser(user1.getUserName());
        assertFalse(room1.getUsers().contains(user1));
    }

    @Test
    void removeUserNull() throws GivenObjectNotValidException, UserNotExistException {
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            room1.removeUser((User) null);
        });

    }

    @Test
    void removeNameNull() throws GivenObjectNotValidException, UserNotExistException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            room1.removeUser((String) null);
        });

    }

    @Test
    void removeUserNotFound() throws GivenObjectNotValidException, UserNotExistException {
        Assertions.assertThrows(UserNotExistException.class, () -> {
            room1.removeUser(user1);
        });

    }

    @Test
    void removeUserNameNotFound() throws GivenObjectNotValidException, UserNotExistException {
        Assertions.assertThrows(UserNotExistException.class, () -> {
            room1.removeUser(user1.getUserName());
        });

    }

    @Test
    void equalsSameIdentity()
    {
        assertTrue(room1.equals(room1));
    }

    @Test
    void equalsSameRoomName() throws NameNotValidException {
        room2= new Room(roomName1);
        assertTrue(room2.equals(room1));
    }

    @Test
    void equalsDifferentRoomName() throws NameNotValidException {
        room2= new Room(roomName2);
        assertFalse(room2.equals(room1));
    }

    @Test
    void equalsDifferentType() throws NameNotValidException {
        room2= new Room(roomName2);
        assertFalse(room1.equals("roomName1"));
    }

    @Test
    void equalsNull() throws NameNotValidException {
        room2= new Room(roomName2);
        assertFalse(room1.equals(null));
    }

    @Test
    void hashCodeSameIdentity()
    {
        assertTrue(room1.hashCode()==(room1.hashCode()));
    }

    @Test
    void hashCodeSameRoomName() throws NameNotValidException {
        room2= new Room(roomName1);
        assertTrue(room2.hashCode()==(room1.hashCode()));
    }

}