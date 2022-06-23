package DAOTests;

import connection.DAO.DAOException;
import connection.DAO.FacultyDAO;
import connection.DAO.ScoreDAO;
import connection.entities.Discipline;
import connection.entities.Faculty;
import connection.entities.Score;
import connection.entities.Statement;
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

public class ScroreDAOTest {
    private ScoreDAO scoreDAO;
    private PreparedStatement preparedStatement;
    private Connection connection;
    private ResultSet resultSet;

    public ScroreDAOTest() throws NoSuchProviderException, ClassNotFoundException, SQLException {
        connection = mock(Connection.class);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        scoreDAO = new ScoreDAO(connection);
    }

    @Test
    void testGetById() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.getLong(1)).thenReturn(1L);
        Optional<Score> optionalDiscipline =scoreDAO.getById(1L);
        verify(resultSet).next();
        Assertions.assertEquals(1L, optionalDiscipline.get().getId());
    }
    @Test
    void testGetAll() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(true).thenReturn(false);
        when(resultSet.getLong(1)).thenReturn(1L).thenReturn(2L);
        ArrayList<Score> scoresOut = (ArrayList<Score>) scoreDAO.getAll();
        List<Long> ids = scoresOut.stream().map(Score::getId).collect(Collectors.toList());
        Assertions.assertArrayEquals(new Long[]{1L, 2L}, ids.toArray());
    }

    @Test
    void testGetByStatement() throws SQLException, DAOException {
        Statement statement = new Statement();
        statement.setId(1L);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);
        scoreDAO.getByStatement(statement);
        verify(preparedStatement).setLong(1, statement.getId());
    }


    @Test
    void testAdd() throws SQLException, DAOException {
        Statement statement = new Statement();
        statement.setId(1L);
        Discipline discipline = new Discipline();
        discipline.setId(1L);
        Score score = new Score(discipline, statement, (short) 200);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        scoreDAO.add(score);
        verify(preparedStatement).setLong(1, score.getStatement().getId());
        verify(preparedStatement).setLong(2, score.getDiscipline().getId());
        verify(preparedStatement).setShort(3, score.getMark());
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testUpdate() throws SQLException, DAOException {
        Statement statement = new Statement();
        statement.setId(1L);
        Discipline discipline = new Discipline();
        discipline.setId(1L);
        Score score = new Score(discipline, statement, (short) 200);
        score.setId(1L);
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        scoreDAO.update(score);
        verify(preparedStatement).setLong(4, score.getId());
        verify(preparedStatement).setLong(1, score.getStatement().getId());
        verify(preparedStatement).setLong(2, score.getDiscipline().getId());
        verify(preparedStatement).setShort(3, score.getMark());
        verify(preparedStatement).executeUpdate();
    }
    @Test
    void testDelete() throws SQLException, DAOException {
        when(connection.prepareStatement(any(String.class))).thenReturn(preparedStatement);
        scoreDAO.delete(1L);
        verify(preparedStatement).setLong(1, 1L);
        verify(preparedStatement).executeUpdate();
    }
}
