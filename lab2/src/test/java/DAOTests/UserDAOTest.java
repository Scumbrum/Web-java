package DAOTests;

import connection.DAO.DAOException;
import connection.DAO.ScoreDAO;
import connection.DAO.UserDAO;
import connection.entities.Discipline;
import connection.entities.Score;
import connection.entities.Statement;
import connection.entities.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.mail.NoSuchProviderException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class UserDAOTest {
    private UserDAO userDAO;
    private PreparedStatement preparedStatement;
    private Connection connection;
    private ResultSet resultSet;

    public UserDAOTest() throws NoSuchProviderException, ClassNotFoundException, SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        userDAO = new UserDAO(connection);
    }

    @Test
    void testGetById() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(resultSet.next()).thenReturn(true);
        Optional<User> optionalUser =userDAO.getById(1L);

        Assertions.assertEquals(1L, optionalUser.get().getId());
    }
    @Test
    void testGetAll() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L).thenReturn(2L);
        ArrayList<User> scoresOut = (ArrayList<User>) userDAO.getAll();
        List<Long> ids = scoresOut.stream().map(User::getId).collect(Collectors.toList());
        Assertions.assertArrayEquals(new Long[]{1L, 2L}, ids.toArray());
    }

    @Test
    void testGetByLogin() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        userDAO.getByLogin("lll");
        when(resultSet.next()).thenReturn(true);
        verify(preparedStatement).setString(1, "lll");
    }


    @Test
    void testAdd() throws SQLException, DAOException {
        User user = new User("vlad", "chorniy", "igorovych", "gg@", "y", "Y", "Y", "SAD");
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        userDAO.add(user);
        verify(preparedStatement).setString(1, user.getName());
        verify(preparedStatement).setString(2, user.getSurname());
        verify(preparedStatement).setString(3, user.getPatronimic());
        verify(preparedStatement).setString(5, user.getMail());
        verify(preparedStatement).setString(6, user.getRegion());
        verify(preparedStatement).setString(7, user.getCity());
        verify(preparedStatement).setString(8, user.getEducation());
        verify(preparedStatement).setString(4, user.getPassword());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testUpdate() throws SQLException, DAOException {
        User user = new User("vlad", "chorniy", "igorovych", "gg@", "y", "Y", "Y", "SAD");
        user.setId(1L);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        userDAO.update(user);
        verify(preparedStatement).setString(1, user.getName());
        verify(preparedStatement).setString(2, user.getSurname());
        verify(preparedStatement).setString(3, user.getPatronimic());
        verify(preparedStatement).setString(5, user.getMail());
        verify(preparedStatement).setString(6, user.getRegion());
        verify(preparedStatement).setString(7, user.getCity());
        verify(preparedStatement).setString(8, user.getEducation());
        verify(preparedStatement).setString(4, user.getPassword());
        verify(preparedStatement).setBoolean(9, user.isBlocked());
        verify(preparedStatement).setLong(10, user.getId());
        verify(preparedStatement).executeUpdate();
    }
    @Test
    void testDelete() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        userDAO.delete(1L);
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }
}
