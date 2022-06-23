package connection.DAO;

import connection.entities.User;
import connection.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserDAO extends Util<User> {

    public UserDAO() {

        super();

    }

    public UserDAO(Connection connection) {
        super(connection);
    }

    private User userFromStatement(ResultSet result) throws SQLException {
        User user = new User();
        user.setId(result.getLong(1));
        user.setName(result.getString(2));
        user.setSurname(result.getString(3));
        user.setPatronimic(result.getString(4));
        user.setPassword(result.getString(5));
        user.setMail(result.getString(6));
        user.setRegion(result.getString(7));
        user.setCity(result.getString(8));
        user.setEducation(result.getString(9));
        user.setBlocked(result.getBoolean(10));
        return user;
    }

    private void statementFromUser(PreparedStatement statement, User record, Boolean full) throws SQLException {
        statement.setString(1, record.getName());
        statement.setString(2, record.getSurname());
        statement.setString(3, record.getPatronimic());
        statement.setString(4, record.getPassword());
        statement.setString(5, record.getMail());
        statement.setString(6, record.getRegion());
        statement.setString(7, record.getCity());
        statement.setString(8, record.getEducation());
        if(full) {
            System.out.println(record.isBlocked());
            statement.setBoolean(9, record.isBlocked());
        }
    }

    @Override
    public Optional<User> getById(Long id) throws DAOException {

        String query = """
        SELECT * FROM user
        WHERE id = ?
        """;

        User user = new User();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, id);

            reader.lock();

            try (ResultSet result = statement.executeQuery();) {
                if(result.next()) {
                    user = userFromStatement(result);
                }
            }
        } catch (SQLException throwables) {
            throw new DAOException("Can't to get user", throwables);
        } finally {
            reader.unlock();
        }

        return Optional.of(user);
    }

    public Optional<User> getByLogin(String login) throws DAOException {

        String query = """
        SELECT * FROM user
        WHERE mail = ?
        """;

        User user = new User();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, login);

            reader.lock();

            try (ResultSet result = statement.executeQuery();) {
                if(result.next()) {
                    user = userFromStatement(result);
                }
            }
        } catch (SQLException throwables) {
            throw new DAOException("Can't to get user", throwables);
        } finally {
            reader.unlock();
        }

        return Optional.of(user);
    }

    @Override
    public List<User> getAll() throws DAOException {

        String query = """
        SELECT * FROM user
        """;

        List<User> users = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            reader.lock();

            try(ResultSet result = statement.executeQuery();) {
                while (result.next()) {
                    User user = userFromStatement(result);
                    users.add(user);
                }
            }

        } catch (SQLException throwables) {
            throw new DAOException("Can't to get user", throwables);
        } finally {
            reader.unlock();
        }

        return users;
    }

    @Override
    public boolean add(User record) throws DAOException {

        String query = """
        INSERT INTO user (`name`, `surname`, `patronimic`, `password`, `mail`, `region`, `city`, `education`)
        VALUES (?, ?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){
            writer.lock();

            statementFromUser(statement, record, false);

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't to save user", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean update(User record) throws DAOException {

        String query = """
        UPDATE user SET `name` = ?, `surname` = ?, `patronimic` = ?, `password` = ?, `mail` = ?, `region` = ?, `city` = ?, `education`= ?, `blocked` = ?
        WHERE `id` = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statementFromUser(statement, record, true);
            statement.setLong(10, record.getId());

            writer.lock();

            statement.executeUpdate();


        } catch (SQLException throwables) {
            throw new DAOException("Not exists user", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean delete(Long id) throws DAOException {
        String query = """
        DELETE FROM user WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, id);

            writer.lock();

            statement.executeUpdate();


        } catch (SQLException throwables) {
            throw new DAOException("Not exists user", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

}
