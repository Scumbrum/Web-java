package connection.DAO;

import connection.entities.*;
import connection.Util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ScoreDAO extends Util<Score> {

    public ScoreDAO() {
        super();
    }
    public ScoreDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Optional<Score> getById(Long id) throws DAOException {

        String query = """
        SELECT * FROM test_score
        JOIN statement ON statement.id = test_score.statement
        JOIN  user ON user.id = statement.user
        JOIN faculty ON faculty.id = statement.faculty
        JOIN discipline ON discipline.id = test_score.discipline
        WHERE test_score.id = ?
        """;

        Score score = new Score();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){
            statement.setLong(1, id);

            reader.lock();

            try(ResultSet result = statement.executeQuery();) {
                result.next();
                score.setId(result.getLong(1));
                score.setStatement(createStatement(result, 5));
                score.setDiscipline(createDiscipline(result, 25));
                score.setMark(result.getShort(4));
            }

        } catch (SQLException | IOException throwables) {
            throw new DAOException("Can't get score", throwables);
        } finally {
            reader.unlock();
        }

        return Optional.of(score);
    }

    private Discipline createDiscipline(ResultSet result, Integer begin) throws SQLException {
        return new Discipline((result.getLong(begin++)),
                result.getString(begin));
    }

    private Statement createStatement(ResultSet resultSet, Integer begin) throws SQLException, IOException {
        return new Statement(
                resultSet.getLong(begin),
                createUser(resultSet, begin + 5),
                createFaculty(resultSet, begin + 15)
        );
    }

    private User createUser(ResultSet result, Integer begin) throws SQLException {
        return new User(
                result.getLong(begin++),
                result.getString(begin++),
                result.getString(begin++),
                result.getString(begin++),
                result.getString(begin++),
                result.getString(begin++),
                result.getString(begin++),
                result.getString(begin++),
                result.getString(begin++),
                result.getBoolean(begin)
        );
    }

    public Faculty createFaculty(ResultSet result, Integer begin) throws IOException, SQLException {
        return new Faculty(
                result.getLong(begin++),
                result.getString(begin++),
                result.getInt(begin++),
                result.getInt(begin++),
                result.getString(begin)
        );
    }

    @Override
    public List<Score> getAll() throws DAOException {

        String query = """
        SELECT * FROM test_score
        JOIN statement ON statement.id = test_score.statement
        JOIN  user ON user.id = statement.user
        JOIN faculty ON faculty.id = statement.faculty
        JOIN discipline ON discipline.id = test_score.discipline
        """;

        List<Score> scores = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            reader.lock();

            try(ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    Score score = new Score();
                    score.setId(result.getLong(1));
                    score.setStatement(createStatement(result, 5));
                    score.setDiscipline(createDiscipline(result, 25));
                    score.setMark(result.getShort(4));
                    scores.add(score);
                }
            }

        } catch (SQLException | IOException throwables) {
            throw new DAOException("Can't get score", throwables);
        } finally {
            reader.unlock();
        }

        return scores;
    }

    public List<Score> getByStatement(Statement statementU) throws DAOException {

        String query = """
        SELECT * FROM test_score
        JOIN statement ON statement.id = test_score.statement
        JOIN  user ON user.id = statement.user
        JOIN faculty ON faculty.id = statement.faculty
        JOIN discipline ON discipline.id = test_score.discipline
        WHERE statement = ?
        """;

        List<Score> scores = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            statement.setLong(1, statementU.getId());

            reader.lock();

            try(ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Score score = new Score();
                    score.setId(result.getLong(1));
                    score.setStatement(createStatement(result, 5));
                    score.setDiscipline(createDiscipline(result, 25));
                    score.setMark(result.getShort(4));
                    scores.add(score);
                }
            }

        } catch (SQLException | IOException throwables) {
            System.out.println(throwables.getMessage());
            throw new DAOException("Can't get score", throwables);
        } finally {
            reader.unlock();
        }

        return scores;
    }


    @Override
    public boolean add(Score record) throws DAOException {

        String query = """
        INSERT INTO test_score (`statement`, `discipline`, `score`)
        VALUES (?, ?, ?)
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, record.getStatement().getId());
            statement.setLong(2, record.getDiscipline().getId());
            statement.setShort(3, record.getMark());
            writer.lock();
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't add score", throwables);
        }finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean update(Score record) throws DAOException {

        String query = """
        UPDATE test_score SET statement = ?, discipline = ?, score = ? WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, record.getStatement().getId());
            statement.setLong(2, record.getDiscipline().getId());
            statement.setShort(3, record.getMark());
            statement.setLong(4, record.getId());
            writer.lock();
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't update score", throwables);
        }finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean delete(Long id) throws DAOException {

        String query = """
        DELETE FROM test_score WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, id);

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't delete score", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }
}

