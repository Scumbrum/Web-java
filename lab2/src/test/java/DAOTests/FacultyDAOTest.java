package DAOTests;

import connection.DAO.DAOException;
import connection.DAO.DisciplineDAO;
import connection.DAO.FacultyDAO;
import connection.entities.Discipline;
import connection.entities.Faculty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.mail.NoSuchProviderException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class FacultyDAOTest {
    private FacultyDAO facultyDAO;
    private PreparedStatement preparedStatement;
    private Connection connection;
    private ResultSet resultSet;

    public FacultyDAOTest() throws NoSuchProviderException, ClassNotFoundException, SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        facultyDAO = new FacultyDAO(connection);
    }

    @Test
    void testGetById() throws SQLException, DAOException {
        Faculty faculty = new Faculty(1L, "TEF", 400, 500, "programm");
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(resultSet.getString(2)).thenReturn("TEF");
        when(resultSet.getInt(3)).thenReturn(400);
        when(resultSet.getInt(4)).thenReturn(500);
        when(resultSet.getString(5)).thenReturn("program");
        Optional<Faculty> optionalDiscipline =facultyDAO.getById(1L);
        verify(resultSet).next();
        Assertions.assertEquals(faculty.getId(), optionalDiscipline.get().getId());
    }
    @Test
    void testGetAll() throws SQLException, DAOException {
        Faculty faculty = new Faculty(1L, "TEF", 400, 500, "programm");
        ArrayList<Faculty> disciplines= new ArrayList<>();
        disciplines.add(faculty);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L);
        when(resultSet.getString(2)).thenReturn("TEF");
        when(resultSet.getInt(3)).thenReturn(400);
        when(resultSet.getInt(4)).thenReturn(500);
        when(resultSet.getString(5)).thenReturn("program");
        ArrayList<Faculty> disciplinesOut = (ArrayList<Faculty>) facultyDAO.getAll();
        Assertions.assertArrayEquals(disciplines.toArray(), disciplinesOut.toArray());
    }

    @Test
    void testAdd() throws SQLException, DAOException {
        Faculty faculty = new Faculty(1L, "TEF", 400, 500, "programm");
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        facultyDAO.add(faculty);
        verify(preparedStatement).setString(1, faculty.getName());
        verify(preparedStatement).setInt(2, faculty.getBudgetPlace());
        verify(preparedStatement).setInt(3, faculty.getAllPlace());
        verify(preparedStatement).setString(4, faculty.getDescription());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testUpdate() throws SQLException, DAOException {
        Faculty faculty = new Faculty(1L, "TEF", 400, 500, "programm");
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        facultyDAO.update(faculty);
        verify(preparedStatement).setLong(5, faculty.getId());
        verify(preparedStatement).setString(1, faculty.getName());
        verify(preparedStatement).setInt(2, faculty.getBudgetPlace());
        verify(preparedStatement).setInt(3, faculty.getAllPlace());
        verify(preparedStatement).setString(4, faculty.getDescription());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testDelete() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        facultyDAO.delete(1L);
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }
}
