package server.facade;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.web.client.RestTemplate;
import server.GlobalConstantsAndValidation;
import server.servercomponent.*;

@RestController
public class FacadeController {

	private final String BASEPATH = "/chatbot";
	private final String ROOM_RESOURCES = "/rooms";
	private final String USER_RESOURCES = "/users";
	private final String USER_RESOURCE = "/users/{userName}";
	private final String ROOM_RESOURCE = "/rooms/{roomName}";
	private final String ROOM_USER_RESOURCE = "/rooms/{roomName}/users/{userName}";
	private final String NAME_REGEX = "[A-Za-z0-9]+";
	private final int PORT_MIN = 0;
	private final int PORT_MAX = 65535;

	@Autowired
	private ServerService serverService;

	@RequestMapping(value = BASEPATH + ROOM_RESOURCES, method = RequestMethod.GET)
	public ResponseEntity<?> getRooms() {
		Set<String> rooms = serverService.getAllRooms();
		return new ResponseEntity<>(rooms, HttpStatus.ACCEPTED);

	}

	@RequestMapping(value = BASEPATH + USER_RESOURCES, method = RequestMethod.GET)
	public ResponseEntity<?> getUsers() {
		Set<User> users = serverService.getAllUsers();
		return new ResponseEntity<>(users, HttpStatus.ACCEPTED);

	}

	@RequestMapping(value = BASEPATH + USER_RESOURCE, method = RequestMethod.GET)
	public ResponseEntity<?> getUser(@PathVariable String userName) {
		User user = null;
		try {
			user = serverService.getUser(userName);
		} catch (UserNotExistException e) {
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.NOT_FOUND);
		}
		catch (NameNotValidException e) {
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.PRECONDITION_FAILED);
		}
		return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = BASEPATH + USER_RESOURCE, method = RequestMethod.DELETE)
	public ResponseEntity<?> loggOut(@PathVariable String userName) {
if(!GlobalConstantsAndValidation.isValidName(userName)) {
	return new ResponseEntity<>("Der Username "+ userName+ "ist nicht gültig.",
			HttpStatus.PRECONDITION_FAILED);

}try {
		User user = serverService.getUser(userName);
		serverService.removeUser(userName);
		return new ResponseEntity<>(HttpStatus.ACCEPTED);
	} catch (UserNotExistException e) {
		return new ResponseEntity<>(e.getMessage(),
				HttpStatus.NOT_FOUND);
	} catch (GivenObjectNotValidException|NameNotValidException e) {
			return new ResponseEntity<>(e.getMessage(),
					HttpStatus.PRECONDITION_FAILED);
		}
	}


	@RequestMapping(value = BASEPATH + USER_RESOURCES, method = RequestMethod.POST)
	public ResponseEntity<?> createUser(@RequestBody User user) {
		if(user == null){
			return new ResponseEntity<>("Der übergebene User darf nicht null sein.", HttpStatus.PRECONDITION_FAILED);
		}if (!user.isValid()) {
			return new ResponseEntity<>("Felder des übergebenen Users sind ungültig belegt.", HttpStatus.PRECONDITION_FAILED);
		}

		try {
			serverService.createUser(user);
		} catch (GivenObjectNotValidException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		} catch (EntityAlreadyExistsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(user,HttpStatus.CREATED);

	}

	@RequestMapping(value = BASEPATH + ROOM_RESOURCES, method = RequestMethod.POST)


	public ResponseEntity<?> createRoom(@RequestBody Room room) {
		if (room == null || !room.isValid()) {
			return new ResponseEntity<>("Der Raum " + room.getRoomName()+" konnte nicht erstellt werden.", HttpStatus.PRECONDITION_FAILED);
		}

		try {
			serverService.createRoom(room);
			return new ResponseEntity<>(room, HttpStatus.CREATED);
		} catch (GivenObjectNotValidException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		} catch (EntityAlreadyExistsException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}


	}
RestTemplate rt = new RestTemplate();
	@RequestMapping(value = BASEPATH + ROOM_RESOURCE, method = RequestMethod.POST)
	public ResponseEntity<?> joinRoom(@PathVariable String roomName, @RequestBody User user) {

		if (!GlobalConstantsAndValidation.isValidName(roomName)) {
			return new ResponseEntity<>("Der Raumname "+ roomName +" ist nicht erlaubt.", HttpStatus.PRECONDITION_FAILED);
		}
		try {
			Room room = serverService.getRoom(roomName);
			Set<User> users = new HashSet<User>();
			for(User u: room.getUsers()){
				users.add(u);
			}
			serverService.joinRoom(roomName, user);
			return new ResponseEntity<>(users, HttpStatus.ACCEPTED);
		} catch (RoomNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (UserNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (GivenObjectNotValidException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (NameNotValidException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		}


	}

	@RequestMapping(value = BASEPATH + ROOM_USER_RESOURCE, method = RequestMethod.DELETE)
	public ResponseEntity<?> leaveRoom(@PathVariable String roomName, @PathVariable String userName) {

		if(!GlobalConstantsAndValidation.isValidName(userName)) {
			return new ResponseEntity<>("Der Username "+ userName+ "ist nicht gültig.",
					HttpStatus.PRECONDITION_FAILED);

		}
		if(!GlobalConstantsAndValidation.isValidName(roomName)) {
			return new ResponseEntity<>("Der Roomname "+ roomName+ "ist nicht gültig.",
					HttpStatus.PRECONDITION_FAILED);

		}
		try {
			serverService.leaveRoom(roomName, userName);
		} catch (RoomNotExistException | UserNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (UserNotInRoomException|GivenObjectNotValidException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (NameNotValidException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		}
		return new ResponseEntity<>("Der User " + userName + " hat den Raum verlassen", HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = BASEPATH + ROOM_USER_RESOURCE, method = RequestMethod.GET)
	public ResponseEntity<?> userMayBeNotThere(@PathVariable String userName) {
		if(!GlobalConstantsAndValidation.isValidName(userName)) {
			return new ResponseEntity<>("Der Username "+ userName+ "ist nicht gültig.",
					HttpStatus.PRECONDITION_FAILED);

		}
		try {
			serverService.isUserReacheable(userName);
		} catch (UserNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		} catch (UserNotRespondingException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		} catch (NameNotValidException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		}
		return new ResponseEntity<>("Der User mit Username:" + userName + "sollte erreichbar sein",
				HttpStatus.ACCEPTED);
	}

	@RequestMapping(value = BASEPATH + USER_RESOURCE, method = RequestMethod.POST)
	public ResponseEntity<?> setTCPPort(@PathVariable String userName, @RequestBody String tcpPort) {
		if(!GlobalConstantsAndValidation.isValidName(userName)) {
			return new ResponseEntity<>("Der Username "+ userName+ "ist nicht gültig.",
					HttpStatus.PRECONDITION_FAILED);

		}
		try {
			User user = serverService.getUser(userName);
			user.setTcpPort(tcpPort);
		} catch (UserNotExistException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
		}  catch (GivenObjectNotValidException|NameNotValidException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
		}
		return new ResponseEntity<>("Der User hat nun den TCPPort :" + tcpPort+ " belegt.",
				HttpStatus.CREATED);
	}

	@RequestMapping(value = GlobalConstantsAndValidation.TEST_RESOURCES+"/user", method = RequestMethod.GET)
	public User blubb() {
		try {
			return new User("userName1", 2, "20.10.110.211");
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (NameNotValidException e) {
			e.printStackTrace();
		} catch (GivenObjectNotValidException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = GlobalConstantsAndValidation.TEST_RESOURCES+"/roomWithoutContent", method = RequestMethod.GET)
	public Room blubb2() {
		try {
			return new Room("roomName1");
		} catch (NameNotValidException e) {
			e.printStackTrace();
		}
		return null;
	}

	@RequestMapping(value = GlobalConstantsAndValidation.TEST_RESOURCES+"/roomWithContent", method = RequestMethod.GET)
	public Room blubb3() {
		try {
			Room room = new Room("roomName1");
			room.addUser(new User("userName1", 2, "20.10.110.211"));
			return room;
		} catch (NameNotValidException e) {
			e.printStackTrace();
		} catch (GivenObjectNotValidException e) {
			e.printStackTrace();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		return null;
	}



}
