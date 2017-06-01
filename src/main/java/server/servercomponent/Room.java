package server.servercomponent;

import server.GlobalConstantsAndValidation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Room {
	private String roomName;
	private Set<User> users = new HashSet<User>();

	public Room(String roomName, Set<User> users) throws NameNotValidException {
		super();
		if(!GlobalConstantsAndValidation.isValidName(roomName)){
			throw new NameNotValidException("Der Raumname "+roomName+" ist ungültig.");
		}
		if(users==null) {
			throw new NameNotValidException("Die Userliste darf nicht null sein.");
		}
		for(User user: users){
			if(user != null){
				this.users.add(user);
			}
		}

		this.roomName = roomName;

	}

	public Room(){

	}

	public Room(String roomName) throws NameNotValidException {
		super();
		if(!GlobalConstantsAndValidation.isValidName(roomName)){
			throw new NameNotValidException("Der Raumname "+roomName+" ist ungültig.");
		}

		this.roomName = roomName;
		this.users = new HashSet<User>();

	}

	public String getRoomName() {
		return roomName;
	}

	public void setRoomName(String roomName) throws NameNotValidException {
		if(!GlobalConstantsAndValidation.isValidName(roomName)){
			throw new NameNotValidException("Der Raumname "+roomName+" ist ungültig.");
		}
		this.roomName = roomName;
	}

	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) throws NameNotValidException {
		if (users == null){
			throw new NameNotValidException("Die Userliste darf nicht null sein.");
		}
		this.users.clear();
		for(User user : users){
			if(user != null){

				this.users.add(user);
			}
		}
	}

	public void addUser(User user) throws GivenObjectNotValidException {
		if(user == null){
			throw new GivenObjectNotValidException("Der User darf nicht null sein.");

		}
		if (users.contains(user)) {
			throw new GivenObjectNotValidException("Der User: " + user.getUserName() + "befindet sich bereits im Raum");
		}
		users.add(user);
	}

	public void removeUser(User user) throws UserNotExistException, GivenObjectNotValidException {
		if(user == null){
			throw new GivenObjectNotValidException("Der User darf nicht null sein");
		}
		if (!users.contains(user)){
			throw new UserNotExistException("Der User existiert nicht");
		}
		users.remove(user);


	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((roomName == null) ? 0 : roomName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Room other = (Room) obj;
		if (roomName == null) {
			if (other.roomName != null)
				return false;
		} else if (!roomName.equals(other.roomName))
			return false;
		return true;
	}

	public void removeUser(String userName) throws UserNotExistException, NameNotValidException {
		if(userName == null){
			throw new NameNotValidException("Der UserName darf nicht null sein");
		}
		for (User user : users) {
			if (user.getUserName().equals(userName)) {
				users.remove(user);
				return;
			}
		}
		throw new UserNotExistException("Der User: " + userName + " wurde nicht gefunden.");
	}

    public boolean isValid() {
		return roomName!=null||users!=null&&GlobalConstantsAndValidation.isValidName(roomName);
    }
}
