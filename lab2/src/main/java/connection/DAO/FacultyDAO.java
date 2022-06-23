package connection.DAO;

import connection.entities.Faculty;
import connection.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FacultyDAO extends Util<Faculty> {

    public FacultyDAO() {
        super();
    }

    public FacultyDAO(Connection connection) {
        super(connection);
    }

    @Override
    public Optional<Faculty> getById(Long id) throws DAOException {

        String query = """
        SELECT * FROM faculty
        WHERE id = ?
        """;

        Faculty faculty = new Faculty();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, id);

            reader.lock();

            try(ResultSet result = statement.executeQuery()) {
                result.next();
                faculty.setId(result.getLong(1));
                faculty.setName(result.getString(2));
                faculty.setBudgetPlace(result.getInt(3));
                faculty.setAllPlace(result.getInt(4));
                faculty.setDescription(result.getString(5));
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DAOException("Can't get faculty", throwables);
        } finally {
            reader.unlock();
        }

        return Optional.of(faculty);
    }

    @Override
    public List<Faculty> getAll() throws DAOException {

        String query = """
        SELECT * FROM faculty
        """;

        List<Faculty> faculties = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            reader.lock();

            try(ResultSet result = statement.executeQuery();) {
                while (result.next()) {
                    Faculty faculty = new Faculty();
                    faculty.setId(result.getLong(1));
                    faculty.setName(result.getString(2));
                    faculty.setBudgetPlace(result.getInt(3));
                    faculty.setAllPlace(result.getInt(4));
                    faculty.setDescription(result.getString(5));
                    faculties.add(faculty);
                }
            }


        } catch (SQLException throwables) {
            throw new DAOException("Can't get faculty", throwables);
        } finally {
            reader.unlock();
        }

        return faculties;
    }

    @Override
    public boolean add(Faculty record) throws DAOException {

        String query = """
        INSERT INTO faculty (`name`, `budget_place`, `all_place`, `description`)
        VALUES (?, ?, ?, ?)
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, record.getName());
            statement.setInt(2, record.getBudgetPlace());
            statement.setInt(3, record.getAllPlace());
            statement.setString(4, record.getDescription());

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't add faculty", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean update(Faculty record) throws DAOException {

        String query = """
        UPDATE faculty SET name = ?, budget_place = ?, all_place = ?, description = ? WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setString(1, record.getName());
            statement.setInt(2, record.getBudgetPlace());
            statement.setInt(3, record.getAllPlace());
            statement.setString(4, record.getDescription());
            statement.setLong(5, record.getId());

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't update faculty", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean delete(Long id) throws DAOException {

        String query = """
        DELETE FROM faculty WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, id);

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't delete faculty", throwables);
        } finally {
            writer.unlock();
        }
        return true;
    }

}
