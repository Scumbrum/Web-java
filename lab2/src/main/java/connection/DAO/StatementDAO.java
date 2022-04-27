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
                statementU.setFaculty(createFaculty(result, 15));
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
        WHERE user = ? AND faculty = ?
        """;

        Statement statementU = new Statement();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, user.getId());
            statement.setLong(2, faculty.getId());

            reader.lock();

            try(ResultSet result = statement.executeQuery();) {
                result.next();
                statementU.setId(result.getLong(1));
                statementU.setAverage(result.getShort(4));
                statementU.setStatus(result.getShort(5));
                statementU.setUser(createUser(result, 6));
                statementU.setFaculty(createFaculty(result, 15));
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

        List<Statement> statements = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, user.getId());

            reader.lock();

            try(ResultSet result = statement.executeQuery();) {
                while (result.next()) {
                    Statement statementU = new Statement();
                    statementU.setId(result.getLong(1));
                    statementU.setAverage(result.getShort(4));
                    statementU.setStatus(result.getShort(5));
                    statementU.setUser(createUser(result, 6));
                    statementU.setFaculty(createFaculty(result, 15));
                    statements.add(statementU);
                }

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
        WHERE faculty = ?
        """;

        List<Statement> statements = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, faculty.getId());

            reader.lock();

            try(ResultSet result = statement.executeQuery();) {
                while (result.next()) {
                    Statement statementU = new Statement();
                    statementU.setId(result.getLong(1));
                    statementU.setAverage(result.getShort(4));
                    statementU.setStatus(result.getShort(5));
                    statementU.setUser(createUser(result, 6));
                    statementU.setFaculty(createFaculty(result, 15));
                    statements.add(statementU);
                }
            }

        } catch (SQLException throwables) {
            throw new DAOException("Can't get statement", throwables);
        }finally {
            reader.unlock();
        }

        return statements;
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
                result.getString(begin)
        );
    }

    @Override
    public List<Statement> getAll() throws DAOException {

        String query = """
        SELECT * FROM statement 
        JOIN user ON user.id = statement.user
        JOIN faculty ON discipline.id = statement.faculty
        """;

        List<Statement> statements = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            reader.lock();

            try(ResultSet result = statement.executeQuery();) {
                while (result.next()) {
                    Statement statementU = new Statement();
                    statementU.setId(result.getLong(1));
                    statementU.setAverage(result.getShort(4));
                    statementU.setStatus(result.getShort(5));
                    statementU.setUser(createUser(result, 6));
                    statementU.setFaculty(createFaculty(result, 15));
                    statements.add(statementU);
                }
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

            statement.setLong(1, record.getUser().getId());
            statement.setLong(2, record.getFaculty().getId());
            if(record.getAverage() == null) {
                statement.setNull(3, Types.SMALLINT);
            } else  {
                statement.setShort(3, record.getAverage());
            }
            statement.setShort(4, record.getStatus());

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

            statement.setLong(1, record.getUser().getId());
            statement.setLong(2, record.getFaculty().getId());
            if(record.getAverage() == null) {
                statement.setNull(3, Types.SMALLINT);
            } else  {
                statement.setShort(3, record.getAverage());
            }
            statement.setShort(4, record.getStatus());
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
