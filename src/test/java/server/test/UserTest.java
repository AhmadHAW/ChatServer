package server.test;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.GlobalConstantsAndValidation;
import server.servercomponent.GivenObjectNotValidException;
import server.servercomponent.NameNotValidException;
import server.servercomponent.User;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created by Ahmad on 31.05.2017.
 */
class UserTest {

    private User user1;
    private User user2;
    private User user3;
    private String userName1 = "user1";
    private String userName2 = "user2";
    private int port1 = 1;
    private int port2 = 2;
    private String inetAdressString1 = "203.0.113.195";
    private String inetAdressString2 = "211.1.131.193";
    private InetAddress inetAddress1;
    private InetAddress inetAddress2;
    @BeforeEach
    void setUp() throws UnknownHostException, NameNotValidException, GivenObjectNotValidException {
        inetAddress1 = InetAddress.getByName(inetAdressString1);
        inetAddress2 = InetAddress.getByName(inetAdressString2);
        user1 = new User(userName1,port1,inetAdressString1);
        user2 = new User(userName2,port2,inetAdressString2);
        user3 = new User(userName1,port2,inetAdressString2);
    }

    @Test
    void getUserNameTest(){
        assertEquals(userName1, user1.getUserName());
    }

    @Test
    void getPortTest(){
        assertEquals(port1, user1.getPort());
    }

    @Test
    void getIpAdressTest(){
        assertEquals(inetAddress1, user1.getIpAdress());
    }

    @Test
    void constructorUserNameNull(){
        Assertions.assertThrows(NameNotValidException.class, () -> {
            new User(null, port1, inetAdressString1);
        });
    }

    @Test
    void constructorUserNameNotValid(){
        Assertions.assertThrows(NameNotValidException.class, () -> {
            new User(".", port1, inetAdressString1);
        });
    }

    @Test
    void constructorPortBelowMin(){
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            new User(userName1, GlobalConstantsAndValidation.PORT_MIN-1, inetAdressString1);
        });
    }

    @Test
    void constructorPortAboveMax(){
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            new User(userName1, GlobalConstantsAndValidation.PORT_MAX+1, inetAdressString1);
        });
    }

    @Test
    void constructorIpAdressNull(){
        Assertions.assertThrows(NameNotValidException.class, () -> {
            new User(userName1, port1, null);
        });
    }

    @Test
    void constructorIpAdressNotValid(){
        Assertions.assertThrows(NameNotValidException.class, () -> {
            new User(userName1, port1, "22.12.21.111.311");
        });
    }

    @Test
    void setUserNamePositiveTest() throws NameNotValidException {
        user1.setUserName(userName2);
        assertEquals(userName2,user1.getUserName());
    }

    @Test
    void setUserNameNullTest() throws NameNotValidException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            user1.setUserName(null);
        });
    }

    @Test
    void setUserNameUnvalidNameTest() throws NameNotValidException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            user1.setUserName("Paul.Peter");
        });
    }

    @Test
    void setPortPositiveTest() throws GivenObjectNotValidException {
        user1.setPort(port2);
        assertEquals(port2,user1.getPort());
    }

    @Test
    void setPortBeloweMinTest() throws GivenObjectNotValidException {
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            user1.setPort(GlobalConstantsAndValidation.PORT_MIN-1);
        });
    }

    @Test
    void setPortAboveMaxTest() throws GivenObjectNotValidException {
        Assertions.assertThrows(GivenObjectNotValidException.class, () -> {
            user1.setPort(GlobalConstantsAndValidation.PORT_MAX+1);
        });
    }

    void setIpAdressPositiveTest() throws NameNotValidException, UnknownHostException {
        user1.setIpAdress(inetAdressString2);
        assertEquals(inetAddress2,user1.getIpAdress());
    }

    @Test
    void setIpAdressNullTest() throws NameNotValidException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            user1.setIpAdress(null);
        });
    }

    @Test
    void setIpAdressUnvalidNameTest() throws NameNotValidException {
        Assertions.assertThrows(NameNotValidException.class, () -> {
            user1.setIpAdress("22.1.231.121.2");
        });
    }

    @Test
    void equalsSameIdentityTest(){
        assertTrue(user1.equals(user1));
    }

    @Test
    void equalsSameNameTest(){
        assertTrue(user1.equals(user3));
    }

    @Test
    void equalsDifferentNameTest(){
        assertFalse(user1.equals(user2));
    }

    @Test
    void equalsDifferentTypeTest(){
        assertFalse(user1.equals("userName1"));
    }

    @Test
    void equalsNullTest(){
        assertFalse(user1.equals(null));
    }

    @Test
    void hashCodeSameIdentity(){
        assertEquals(user1.hashCode(), user1.hashCode());
    }

    @Test
    void hashCodeSameName(){
        assertEquals(user1.hashCode(), user3.hashCode());
    }

}