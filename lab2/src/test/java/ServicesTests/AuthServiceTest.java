package ServicesTests;

import API.APIException;
import API.AdminService;
import API.AuthService;
import connection.DAO.*;
import connection.entities.Discipline;
import connection.entities.Faculty;
import connection.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.mail.NoSuchProviderException;
import javax.mail.Transport;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class AuthServiceTest {
    UserDAO userDAO;

    private AuthService authService;

    public AuthServiceTest() throws NoSuchProviderException {
       userDAO = mock(UserDAO.class);
       authService = new AuthService(userDAO);
    }

    @Test
    void testLogin() throws DAOException, APIException {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setMail("hghg@gmail.com");
        user1.setPassword("67");
        User user2 = new User();
        user2.setMail("hgphg@gmail.com");
        user2.setPassword("67");
        users.add(user1);
        users.add(user2);
        when(userDAO.getAll()).thenReturn(users);
        Assertions.assertFalse(authService.tryLoginUser("hghg@gmail.com", "123"));
        Assertions.assertFalse(authService.tryLoginUser("hghpg@gmail.com", "67"));
        Assertions.assertTrue(authService.tryLoginUser("hghg@gmail.com", "67"));
    }

    @Test
    void testReg() throws DAOException, APIException {
        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setMail("hghg@gmail.com");
        user1.setPassword("67");
        User user2 = new User();
        user2.setMail("hgphg@gmail.com");
        user2.setPassword("67");
        users.add(user1);
        users.add(user2);
        when(userDAO.getAll()).thenReturn(users);
        User testUser = new User();
        testUser.setPassword("1");
        testUser.setMail("hghg@gmail.com");
        Assertions.assertFalse(authService.tryRegUser(testUser, "1"));
        testUser.setMail("pghg@gmail.com");
        Assertions.assertFalse(authService.tryRegUser(testUser, "2"));
        Assertions.assertTrue(authService.tryRegUser(testUser, "1"));
    }
}
