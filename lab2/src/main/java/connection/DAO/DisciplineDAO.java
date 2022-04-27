package connection.DAO;

import connection.entities.Discipline;
import connection.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DisciplineDAO extends Util<Discipline> {

    public DisciplineDAO() {
        super();
    }

    @Override
    public Optional<Discipline> getById(Long id) throws DAOException {

        String query = """
        SELECT * FROM discipline
        WHERE id = ?
        """;

        Discipline discipline = new Discipline();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, id);

            reader.lock();

            try(ResultSet result = statement.executeQuery();){
                result.next();
                discipline.setId(result.getLong(1));
                discipline.setName(result.getString(2));
            }

        } catch (SQLException throwables) {
            throw new DAOException("Can't get discipline", throwables);
        } finally {
            reader.unlock();
        }

        return Optional.of(discipline);
    }

    @Override
    public List<Discipline> getAll() throws DAOException {

        String query = """
        SELECT * FROM discipline
        """;

        List<Discipline> disciplines = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){

            reader.lock();

            try(ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Discipline discipline = new Discipline();
                    discipline.setId(result.getLong(1));
                    discipline.setName(result.getString(2));
                    disciplines.add(discipline);
                }
            }

        } catch (SQLException throwables) {
            throw new DAOException("Can't get discipline", throwables);
        } finally {
            reader.unlock();
        }

        return disciplines;
    }

    @Override
    public boolean add(Discipline record) throws DAOException {

        String query = """
        INSERT INTO discipline (`name`)
        VALUES (?)
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery();){

            statement.setString(1, record.getName());

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't add discipline", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean update(Discipline record) throws DAOException {

        String query = """
        UPDATE discipline SET name = ? WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery();){

            statement.setString(1, record.getName());

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't update discipline", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean delete(Long id) throws DAOException {

        String query = """
        DELETE FROM discipline WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery();){

            statement.setLong(1, id);

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't delete discipline", throwables);
        } finally {
            writer.unlock();
        }

        return true;
    }

}
