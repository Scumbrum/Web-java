package connection.DAO;

import connection.entities.Faculty;
import connection.entities.Statement;
import connection.entities.User;
import connection.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StatementDAO extends Util<Statement> {

    public StatementDAO() {
        super();
    }
    public StatementDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Optional<Statement> getById(Long id) throws DAOException {

        String query = """
        SELECT * FROM statement 
        JOIN user ON user.id = statement.user
        JOIN faculty ON faculty.id = statement.faculty
        WHERE statement.id = ?
        """;

        Statement statementU = new Statement();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, id);

            reader.lock();

            try(ResultSet result = statement.executeQuery()) {
                result.next();
                statementU.setId(result.getLong(1));
                statementU.setAverage(result.getShort(4));
                statementU.setStatus(result.getShort(5));
                statementU.setUser(createUser(result, 6));
                statementU.setFaculty(createFaculty(result, 16));
            }


        } catch (SQLException throwables) {
            throw new DAOException("Can't get statement", throwables);
        } finally {
            reader.unlock();
        }

        return Optional.of(statementU);
    }

    public Optional<Statement> getByData(User user, Faculty faculty) throws DAOException {

        String query = """
        SELECT * FROM statement 
        JOIN user ON user.id = statement.user
        JOIN faculty ON faculty.id = statement.faculty
        WHERE user = ? AND faculty = ? and status=1 and blocked=0
        """;

        Statement statementU = new Statement();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, user.getId());
            statement.setLong(2, faculty.getId());

            reader.lock();

            try(ResultSet result = statement.executeQuery();) {
                if(!result.next()){
                    return Optional.empty();
                }
                statementU.setId(result.getLong(1));
                statementU.setAverage(result.getShort(4));
                statementU.setStatus(result.getShort(5));
                statementU.setUser(createUser(result, 6));
                statementU.setFaculty(createFaculty(result, 16));
            }

        } catch (SQLException throwables) {
            throw new DAOException("Can't get statement", throwables);
        } finally {
            reader.unlock();
        }

        return Optional.of(statementU);
    }

    public List<Statement> getByUser(User user) throws DAOException {

        String query = """
        SELECT * FROM statement 
        JOIN user ON user.id = statement.user
        JOIN faculty ON faculty.id = statement.faculty
        WHERE user = ?
        """;
        return getStatementsWithParam(query, user.getId());
    }

    private List<Statement> getStatements(ResultSet result) throws SQLException {
        List<Statement> statements = new ArrayList<>();

        while (result.next()) {
            Statement statementU = new Statement();
            statementU.setId(result.getLong(1));
            statementU.setAverage(result.getShort(4));
            statementU.setStatus(result.getShort(5));
            statementU.setUser(createUser(result, 6));
            statementU.setFaculty(createFaculty(result, 16));
            statements.add(statementU);
        }

        return  statements;
    }

    private List<Statement> getStatementsWithParam(String query, Long id) throws DAOException {
        List<Statement> statements;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            reader.lock();
            statement.setLong(1, id);
            try (ResultSet result = statement.executeQuery()) {
                statements = getStatements(result);
            }

        } catch (SQLException throwables) {
            throw new DAOException("Can't get statement", throwables);
        } finally {
            reader.unlock();
        }
        return statements;
    }

    public List<Statement> getByFaculty(Faculty faculty) throws DAOException {

        String query = """
        SELECT * FROM statement 
        JOIN user ON user.id = statement.user
        JOIN faculty ON faculty.id = statement.faculty
        WHERE faculty = ? and status=1 and blocked=0
        """;

        return getStatementsWithParam(query,  faculty.getId());
    }

    private Faculty createFaculty(ResultSet result, Integer begin) throws SQLException {
        return new Faculty(
                result.getLong(begin++),
                result.getString(begin++),
                result.getInt(begin++),
                result.getInt(begin++),
                result.getString(begin)
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

    @Override
    public List<Statement> getAll() throws DAOException {

        String query = """
        SELECT * FROM statement 
        JOIN user ON user.id = statement.user
        JOIN faculty ON discipline.id = statement.faculty
        """;

        List<Statement> statements;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            reader.lock();

            try(ResultSet result = statement.executeQuery();) {
                statements = getStatements(result);
            }

        } catch (SQLException throwables) {
            throw new DAOException("Can't get statement", throwables);
        }   finally {
            reader.unlock();
        }

        return statements;
    }

    @Override
    public boolean add(Statement record) throws DAOException {

        String query = """
        INSERT INTO statement (`user`, `faculty`, `average`, `status`)
        VALUES (?, ?, ?, ?)
        """;

        try (Connection connection = getConnection();
            PreparedStatement statement = connection.prepareStatement(query);){

            statementNormalizer(statement, record);

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't add statement", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean update(Statement record) throws DAOException {

        String query = """
        UPDATE statement SET user = ?, faculty = ?, average = ?, status = ? WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){
            statementNormalizer(statement, record);
            statement.setLong(5, record.getId());

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't add statement", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

    private void statementNormalizer(PreparedStatement statement, Statement record) throws SQLException {
        statement.setLong(1, record.getUser().getId());
        statement.setLong(2, record.getFaculty().getId());
        if(record.getAverage() == null) {
            statement.setNull(3, Types.SMALLINT);
        } else  {
            statement.setShort(3, record.getAverage());
        }
        statement.setShort(4, record.getStatus());
    }

    @Override
    public boolean delete(Long id) throws DAOException {

        String query = """
        DELETE FROM statement WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, id);

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't delete statement", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

}
