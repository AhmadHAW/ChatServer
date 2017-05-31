package server.servercomponent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import server.GlobalConstantsAndValidation;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class User {
	private String userName;
	private int port;
	private InetAddress ipAdress;

	@JsonIgnore
	private int tcpPort;

	public User(String userName, int port, String ipAdress) throws UnknownHostException, NameNotValidException, GivenObjectNotValidException {
		if(!GlobalConstantsAndValidation.isValidName(userName)){
			throw new NameNotValidException("Der Username "+userName+" ist nicht gültig");
		}
		if(!GlobalConstantsAndValidation.isValidPort(port)){
			throw new GivenObjectNotValidException("Der Port "+port+" muss zwichen "+GlobalConstantsAndValidation.PORT_MIN+" und "+GlobalConstantsAndValidation.PORT_MAX+" liegen.");
		}
		if(!GlobalConstantsAndValidation.isValidIpAdress(ipAdress)){
			throw new NameNotValidException("Die IpAdresse "+ipAdress+" ist nicht gültig.");
		}
		this.userName = userName;
		this.port = port;
		this.ipAdress = InetAddress.getByName(ipAdress);
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) throws NameNotValidException {
		if(!GlobalConstantsAndValidation.isValidName(userName)){
			throw new NameNotValidException("Der Username "+userName+" ist nicht gültig");
		}
		this.userName = userName;
	}

	public int getPort() {
		return port;
	}

	public int getTcpPort() {
		return tcpPort;
	}

	public void setTcpPort(int tcpPort) throws GivenObjectNotValidException {
		if(!GlobalConstantsAndValidation.isValidPort(tcpPort)){
			throw new GivenObjectNotValidException("Der Port "+tcpPort+" muss zwichen "+GlobalConstantsAndValidation.PORT_MIN+" und "+GlobalConstantsAndValidation.PORT_MAX+" liegen.");
		}this.tcpPort = tcpPort;
	}

	public void setPort(int port) throws GivenObjectNotValidException {
		if(!GlobalConstantsAndValidation.isValidPort(port)){
			throw new GivenObjectNotValidException("Der Port "+port+" muss zwichen "+GlobalConstantsAndValidation.PORT_MIN+" und "+GlobalConstantsAndValidation.PORT_MAX+" liegen.");
		}this.port = port;
	}

	public InetAddress getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) throws UnknownHostException, NameNotValidException {
		if(!GlobalConstantsAndValidation.isValidIpAdress(ipAdress)){
			throw new NameNotValidException("Die IpAdresse "+ipAdress+" ist nicht gültig.");
		}
		this.ipAdress = InetAddress.getByName(ipAdress);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@JsonIgnore
	public boolean isCorrect() {
		return GlobalConstantsAndValidation.isValidName(userName)&&GlobalConstantsAndValidation.isValidPort(port)&&GlobalConstantsAndValidation.isValidPort(tcpPort);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
