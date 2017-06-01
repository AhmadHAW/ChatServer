package server.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.servercomponent.*;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Ahmad on 31.05.2017.
 */
class ServerServiceTest {
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
    private ServerService serverService;

    @BeforeEach
    void setUp() throws NameNotValidException, UnknownHostException, GivenObjectNotValidException {
        user1 = new User(userName1, port1, ipAdresse1);
        users1 = new HashSet<User>();
        users1.add(new User(userName1,port1,ipAdresse1));
        users2.add(new User(userName1,port1,ipAdresse1));
        room1 = new Room(roomName1);
        room2 = new Room(roomName2);
        serverService=new ServerService();
    }

    @Test
    void addRoomPositiveTest() throws GivenObjectNotValidException, EntityAlreadyExistsException {
        assertTrue(serverService.getAllRooms().isEmpty());
        serverService.createRoom(room1);
        assertTrue(serverService.getAllRooms().contains(room1.getRoomName()));
    }

    @Test
    void addRoomNullTest() throws GivenObjectNotValidException {
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            serverService.createRoom(null);
        });
    }
    @Test
    void addRoomTwiceTest() throws GivenObjectNotValidException, EntityAlreadyExistsException {
        serverService.createRoom(room1);
        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> {
            serverService.createRoom(room1);
        });
    }

    @Test
    void addRoomUnvalidFieldsTest() throws GivenObjectNotValidException {
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            serverService.createUser(new User());
        });
    }

    @Test
    void getAllRoomsNotSameIdentityTest() throws GivenObjectNotValidException, EntityAlreadyExistsException {
        assertTrue(serverService.getAllRooms().isEmpty());
        serverService.createRoom(room1);
        serverService.getAllRooms().clear();
        assertFalse(serverService.getAllRooms().isEmpty());
    }

    @Test
    void addUserPositiveTest() throws GivenObjectNotValidException, EntityAlreadyExistsException {
        assertTrue(serverService.getAllUsers().isEmpty());
        serverService.createUser(user1);
        assertTrue(serverService.getAllUsers().contains(user1));
    }

    @Test
    void addUserNullTest() throws GivenObjectNotValidException {
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            serverService.createUser(null);
        });
    }

    @Test
    void addUserUnvalidFieldsTest() throws GivenObjectNotValidException {
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            serverService.createUser(new User());
        });
    }

    @Test
    void addUserTwiceTest() throws GivenObjectNotValidException, EntityAlreadyExistsException {
        serverService.createUser(user1);
        Assertions.assertThrows(EntityAlreadyExistsException.class, () -> {
            serverService.createUser(user1);
        });
    }

    @Test
    void getAllUsersNotSameIdentityTest() throws GivenObjectNotValidException, EntityAlreadyExistsException {
        assertTrue(serverService.getAllUsers().isEmpty());
        serverService.createUser(user1);
        serverService.getAllUsers().clear();
        assertFalse(serverService.getAllUsers().isEmpty());
    }

    @Test
    void getRoomPositive() throws EntityAlreadyExistsException, GivenObjectNotValidException, RoomNotExistException, NameNotValidException {
        serverService.createRoom(room1);
        assertEquals(room1,serverService.getRoom(roomName1));
    }

    @Test
    void getRoomNull() throws EntityAlreadyExistsException, GivenObjectNotValidException, RoomNotExistException, NameNotValidException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.getRoom(null);
        });
    }

    @Test
    void getRoomUnvalidName() throws EntityAlreadyExistsException, GivenObjectNotValidException, RoomNotExistException, NameNotValidException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.getRoom(".");
        });
    }

    @Test
    void getUserPositive() throws EntityAlreadyExistsException, GivenObjectNotValidException, NameNotValidException, UserNotExistException {
        serverService.createUser(user1);
        assertEquals(user1,serverService.getUser(userName1));
    }

    @Test
    void getUserNull() throws EntityAlreadyExistsException, GivenObjectNotValidException, NameNotValidException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.getUser(null);
        });
    }

    @Test
    void getUserUnvalidName() throws EntityAlreadyExistsException, GivenObjectNotValidException, NameNotValidException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.getUser(".");
        });
    }

    @Test
    void userJoinRoomPositive() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {
        serverService.createRoom(room1);
        serverService.createRoom(room2);
        serverService.createUser(user1);
        serverService.joinRoom(roomName1, user1);
        assertTrue(serverService.getRoom(roomName1).getUsers().contains(user1));
    }
    @Test
    void userJoinRoomRoomNotFound() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createUser(user1);
        Assertions.assertThrows(RoomNotExistException.class, () -> {
            serverService.joinRoom(roomName1,user1);
        });
    }

    @Test
    void userJoinRoomUserNotFound() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createRoom(room1);
        Assertions.assertThrows(UserNotExistException.class, () -> {
            serverService.joinRoom(roomName1,user1);
        });
    }
    @Test
    void userJoinRoomRoomNull() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createUser(user1);
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.joinRoom(null,user1);
        });
    }
    @Test
    void userJoinRoomUserNull() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createRoom(room1);
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            serverService.joinRoom(roomName1,null);
        });
    }

    @Test
    void userJoinRoomRoomNameNotValid() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createUser(user1);
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.joinRoom("",user1);
        });
    }
    @Test
    void userJoinRoomUserNotValid() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createRoom(room1);
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            serverService.joinRoom(roomName1,new User());
        });
    }

    @Test
    void userLoggsOutPositive() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {
        serverService.createRoom(room1);
        serverService.createRoom(room2);
        serverService.createUser(user1);
        serverService.joinRoom(roomName1,user1);
        serverService.joinRoom(roomName2,user1);
        serverService.removeUser(userName1);
        assertFalse(serverService.getRoom(roomName1).getUsers().contains(user1));
        assertFalse(serverService.getRoom(roomName2).getUsers().contains(user1));
        assertFalse(serverService.getAllUsers().contains(user1));
    }

    @Test
    void userLoggsOutNull() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.removeUser(null);
        });
    }

    @Test
    void userLoggsOutNotValidName() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.removeUser(".");
        });
    }

    @Test
    void userLoggsOutNotFound() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {
        Assertions.assertThrows(UserNotExistException.class, () -> {
            serverService.removeUser(userName1);
        });
    }

    @Test
    void userLeaveRoomPositive() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException, UserNotInRoomException {
        serverService.createRoom(room1);
        serverService.createUser(user1);
        serverService.joinRoom(roomName1, user1);
        serverService.leaveRoom(roomName1,userName1);
        assertFalse(serverService.getRoom(roomName1).getUsers().contains(user1));
        assertTrue(serverService.getAllUsers().contains(user1));
    }

    @Test
    void userLeaveRoomRoomNotFound() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createUser(user1);
        Assertions.assertThrows(RoomNotExistException.class, () -> {
            serverService.leaveRoom(roomName1,userName1);
        });
    }

    @Test
    void userLeaveRoomUserNotFound() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createRoom(room1);
        Assertions.assertThrows(UserNotExistException.class, () -> {
            serverService.leaveRoom(roomName1,userName1);
        });
    }
    @Test
    void userLeaveRoomRoomNull() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createUser(user1);
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.leaveRoom(null,userName1);
        });
    }
    @Test
    void userLeaveRoomUserNull() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createRoom(room1);
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.leaveRoom(roomName1,null);
        });
    }

    @Test
    void userLeaveRoomRoomNameNotValid() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createUser(user1);
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.leaveRoom("",userName1);
        });
    }
    @Test
    void userLeaveRoomUserNotValid() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {

        serverService.createRoom(room1);
        Assertions.assertThrows(NameNotValidException.class, () -> {
            serverService.leaveRoom(roomName1,".");
        });
    }

    @Test
    void userLeaveRoomUserNotInRoom() throws EntityAlreadyExistsException, GivenObjectNotValidException, UserNotExistException, RoomNotExistException, NameNotValidException {
        serverService.createRoom(room1);
        serverService.createUser(user1);
        Assertions.assertThrows(UserNotInRoomException.class, () -> {
            serverService.leaveRoom(roomName1,userName1);
        });

    }
}