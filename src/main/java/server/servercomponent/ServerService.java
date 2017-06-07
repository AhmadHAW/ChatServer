package server.servercomponent;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.SynchronousQueue;

import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import server.GlobalConstantsAndValidation;

import javax.print.attribute.standard.MediaSize;

@Service
public class ServerService {
	private Set<Room> rooms = new HashSet<Room>();
	private Set<User> users = new HashSet<User>();
	private RestTemplate rt = new RestTemplate();

	public Set<String> getAllRooms() {
		Set<String> roomNames = new HashSet<>();
		for (Room r : rooms) {
			roomNames.add(r.getRoomName());
		}
		return roomNames;
	}

	public Set<User> getAllUsers() {
		Set<User> users = new HashSet<User>();
		for(User user: this.users){
			users.add(user);
		}
		return users;
	}

	public void createUser(User user) throws GivenObjectNotValidException, EntityAlreadyExistsException {
		if(user == null){
			throw new GivenObjectNotValidException("Der übergebene User darf nicht null sein.");
		}
		if(!user.isValid()){
			throw new GivenObjectNotValidException("Alle Felder des Users müssen gültig belegt sein.");
		}
		if(users.contains(user)){
			throw new EntityAlreadyExistsException("Der User "+user.getUserName()+" existiert bereits.");
		}
			users.add(user);

	}

	public void removeUser(String userName) throws UserNotExistException, GivenObjectNotValidException, NameNotValidException {
		if(!GlobalConstantsAndValidation.isValidName(userName)){
			throw new NameNotValidException("Der Username "+ userName +" ist nicht gültig.");
		}
		Optional<User> opt = users.stream().filter(t -> t.getUserName().equals(userName)).findFirst();
		if(!opt.isPresent()){
		throw new UserNotExistException("Der User mit dem Usernamen "+userName+" konnte nicht gefunden werden.");
		}
		User user = opt.get();

		for(Room room: rooms){
			Set<User> usersInRoom = room.getUsers();
			if (usersInRoom.contains(user)){
				try {
					leaveRoom(room.getRoomName(), user.getUserName());
				} catch (RoomNotExistException e) {
					e.printStackTrace();
				} catch (UserNotInRoomException e) {
					e.printStackTrace();
				}
			}
		}
		users.remove(user);
	}

	public User getUser(String userName) throws UserNotExistException, NameNotValidException {
		if(!GlobalConstantsAndValidation.isValidName(userName)){
			throw new NameNotValidException("Der Username "+userName+" ist nicht erlaubt.");
		}
		Optional<User> opt = users.stream().filter(t -> t.getUserName().equals(userName)).findFirst();
		if(!opt.isPresent()){
			throw new UserNotExistException("Der User mit dem Usernamen "+userName+" konnte nicht gefunden werden.");
		}
		return (User)opt.get();
	}

	public Room getRoom(String roomName) throws NameNotValidException, RoomNotExistException {

		if(!GlobalConstantsAndValidation.isValidName(roomName))
		{
			throw new NameNotValidException("Der Name "+ roomName +" ist nicht gültig.");
		}
		Optional<Room> opt = rooms.stream().filter(t -> t.getRoomName().equals(roomName)).findFirst();
		if(!opt.isPresent())
		{
			throw new RoomNotExistException("Der Raum mit Namen "+roomName+" existiert nicht.");
		}
		return opt.get();
	}

	public void createRoom(Room room) throws GivenObjectNotValidException, EntityAlreadyExistsException {

		if(room == null){
			throw new GivenObjectNotValidException("Der übergebene Raum darf nicht null sein.");
		}
		if(!room.isValid()){
			throw new GivenObjectNotValidException("Alle Felder des Raumes müssen gültig belegt sein.");
		}
		if(rooms.contains(room))
		{
			throw new EntityAlreadyExistsException("Der Raum"+room.getRoomName()+" existiert bereits.");
		}
		rooms.add(room);
	}

	public Room joinRoom(String roomName, User user) throws RoomNotExistException, UserNotExistException, NameNotValidException, GivenObjectNotValidException {
		if(user == null||!user.isValid()){
			throw new GivenObjectNotValidException("Der User ist null oder Felder des Users sind null");
		}
		if(!GlobalConstantsAndValidation.isValidName(roomName)){
			throw new NameNotValidException("Der Raumname "+roomName+" ist nicht gültig,");
		}
		Optional<Room> opt =  rooms.stream().filter(t -> t.getRoomName().equals(roomName)).findFirst();
		if (!opt.isPresent()) {
			throw new RoomNotExistException("Der Raum mit Raumnamen: " + roomName + " existiert nicht.");
		}
		String userName = user.getUserName();
		Optional<User> optUser =  users.stream().filter(t -> t.getUserName().equals(userName)).findFirst();
		if (!optUser.isPresent()) {
			throw new UserNotExistException("Der User mit Usernamen: " + user.getUserName() + " existiert nicht.");
		}
		Room room = opt.get();
		user = optUser.get();
		if(!users.contains(user))
		{
			throw new UserNotExistException("Der User "+userName+" ist nicht angemeldet und kann sich daher nicht in einen Raum einschreiben.");
		}
		for(User userInRoom: room.getUsers())
		{

			String url = "http:/"+userInRoom.getIpAdress()+":"+userInRoom.getTcpPort()+GlobalConstantsAndValidation.CLIENT_ROOM_RESOURCES+"/"+room.getRoomName();
			try{rt.put(url,user);}
			catch (HttpClientErrorException e) {
				throw new GivenObjectNotValidException(e.getStatusCode() + ": " + e.getResponseBodyAsString());
			}
		}
		room.addUser(user);
		return room;
	}

	public void leaveRoom(String roomName, String userName) throws RoomNotExistException, UserNotExistException, NameNotValidException, GivenObjectNotValidException, UserNotInRoomException {
		if(!GlobalConstantsAndValidation.isValidName(roomName)){
			throw new NameNotValidException("Der Raumname "+roomName+" ist ungültig.");
		}

		if(!GlobalConstantsAndValidation.isValidName(userName)){
			throw new NameNotValidException("Der Username "+userName+" ist ungültig.");
		}
		Optional<User> opt1=  users.stream().filter(t -> t.getUserName().equals(userName)).findFirst();
		if(!opt1.isPresent()){
			throw new UserNotExistException("Der User "+userName+" ist nicht existent.");
		}

		Optional<Room> opt2=  rooms.stream().filter(t -> t.getRoomName().equals(roomName)).findFirst();
		if(!opt2.isPresent()){
			throw new RoomNotExistException("Der Raum "+roomName+" ist nicht existent.");
		}
		Room room = opt2.get();
		User user = opt1.get();
		if(!room.getUsers().contains(user)){
			throw new UserNotInRoomException("Der User "+ userName+" befand sich nicht in dem Raum");
		}

		try{
			room.removeUser(user);
			for(User userInRoom: room.getUsers()){
				String url = "http:/"+userInRoom.getIpAdress()+":"+userInRoom.getTcpPort()+GlobalConstantsAndValidation.CLIENT_ROOM_RESOURCES+"/"+roomName+GlobalConstantsAndValidation.CLIENT_USER_RESOURCES+"/"+userName;
				rt.delete(url);
			}
		}catch (HttpClientErrorException e) {
			room.addUser(user);
			throw new GivenObjectNotValidException(e.getStatusCode() + ": " + e.getResponseBodyAsString());
		}
	}

	public void isUserReacheable(String userName) throws UserNotExistException, UserNotRespondingException, NameNotValidException {
		if(!GlobalConstantsAndValidation.isValidName(userName)){
			throw new NameNotValidException("Der Username "+userName+" ist ungültig.");
		}
		Optional<User> opt=  users.stream().filter(t -> t.getUserName().equals(userName)).findFirst();
		if (!opt.isPresent()) {
			throw new UserNotExistException("Der User mit Username: " + userName + " existiert nicht.");
		}
		User user = opt.get();
		InetAddress ipAdress = user.getIpAdress();

		try {
			if (ipAdress.isReachable(100)) {
				throw new UserNotRespondingException(
						"Der User mit Username: " + userName + " kann nicht erreicht werden.");

			}
		} catch (IOException e) {
			throw new UserNotRespondingException("Der User mit Username: " + userName + " kann nicht erreicht werden.");
		}
	}

}
