package DAOTests;

import connection.DAO.DAOException;
import connection.DAO.ScoreDAO;
import connection.DAO.StatementDAO;
import connection.entities.*;
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

public class StatementDAOTest {
    private StatementDAO statementDAO;
    private PreparedStatement preparedStatement;
    private Connection connection;
    private ResultSet resultSet;

    public StatementDAOTest() throws NoSuchProviderException, ClassNotFoundException, SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        statementDAO = new StatementDAO(connection);
    }

    @Test
    void testGetById() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getLong(1)).thenReturn(1L);
        Optional<Statement> optionalDiscipline = statementDAO.getById(1L);
        verify(resultSet).next();
        Assertions.assertEquals(1L, optionalDiscipline.get().getId());
    }
    @Test
    void testGetAll() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L).thenReturn(2L);
        ArrayList<Statement> scoresOut = (ArrayList<Statement>) statementDAO.getAll();
        List<Long> ids = scoresOut.stream().map(Statement::getId).collect(Collectors.toList());
        Assertions.assertArrayEquals(new Long[]{1L, 2L}, ids.toArray());
    }

    @Test
    void testGetByUser() throws SQLException, DAOException {
        User user = new User();
        user.setId(1L);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        statementDAO.getByUser(user);
        verify(preparedStatement).setLong(1, user.getId());
    }

    @Test
    void testGetByData() throws SQLException, DAOException {
        User user = new User();
        user.setId(1L);
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        statementDAO.getByData(user, faculty);
        verify(preparedStatement).setLong(1, user.getId());
        verify(preparedStatement).setLong(2, faculty.getId());
    }

    @Test
    void testGetByFaculty() throws SQLException, DAOException {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        verify(preparedStatement).setLong(1, faculty.getId());
    }


    @Test
    void testAdd() throws SQLException, DAOException {
        User user = new User();
        user.setId(1L);
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        Statement statement = new Statement(user, faculty,(short)0,(short)1);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        statementDAO.add(statement);
        verify(preparedStatement).setLong(1, statement.getUser().getId());
        verify(preparedStatement).setLong(2, statement.getFaculty().getId());
        verify(preparedStatement).setShort(3, statement.getAverage());
        verify(preparedStatement).setShort(4, statement.getStatus());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testUpdate() throws SQLException, DAOException {
        User user = new User();
        user.setId(1L);
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        Statement statement = new Statement(1L,user, faculty,(short)0,(short)1);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        statementDAO.update(statement);
        verify(preparedStatement).setLong(1, statement.getUser().getId());
        verify(preparedStatement).setLong(2, statement.getFaculty().getId());
        verify(preparedStatement).setShort(3, statement.getAverage());
        verify(preparedStatement).setShort(4, statement.getStatus());
        verify(preparedStatement).setLong(5, statement.getId());
        verify(preparedStatement).executeUpdate();
    }
    @Test
    void testDelete() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        statementDAO.delete(1L);
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }
}
