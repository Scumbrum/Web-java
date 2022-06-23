package DAOTests;

import API.APIException;
import API.AdminService;
import connection.Config;
import connection.DAO.*;
import connection.entities.Discipline;
import connection.entities.Faculty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.mail.NoSuchProviderException;
import javax.mail.Transport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class DisciplineDAOTest {

    private DisciplineDAO disciplineDAO;
    private PreparedStatement preparedStatement;
    private Connection connection;
    private ResultSet resultSet;

    public DisciplineDAOTest() throws NoSuchProviderException, ClassNotFoundException, SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        disciplineDAO = new DisciplineDAO(connection);
    }

    @Test
    void testGetById() throws SQLException, DAOException {
        Discipline discipline = new Discipline(1L,"math");
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(resultSet.getString(2)).thenReturn("math");
        Optional<Discipline> optionalDiscipline = disciplineDAO.getById(1L);
        verify(resultSet).next();
        Assertions.assertEquals(discipline.getId(), optionalDiscipline.get().getId());
    }
    @Test
    void testGetAll() throws SQLException, DAOException {
        Discipline discipline = new Discipline(1L,"math");
        ArrayList<Discipline> disciplines= new ArrayList<>();
        disciplines.add(discipline);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(resultSet.getString(2)).thenReturn("math");
        ArrayList<Discipline> disciplinesOut = (ArrayList<Discipline>) disciplineDAO.getAll();
        Assertions.assertArrayEquals(disciplines.toArray(), disciplinesOut.toArray());
    }

    @Test
    void testAdd() throws SQLException, DAOException {
        Discipline discipline = new Discipline(1L,"math");
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        disciplineDAO.add(discipline);
        verify(preparedStatement).setString(1, discipline.getName());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testUpdate() throws SQLException, DAOException {
        Discipline discipline = new Discipline(1L,"math");
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        disciplineDAO.update(discipline);
        verify(preparedStatement).setString(1, discipline.getName());
        verify(preparedStatement).setLong(2, discipline.getId());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testDelete() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        disciplineDAO.delete(1L);
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }
}
