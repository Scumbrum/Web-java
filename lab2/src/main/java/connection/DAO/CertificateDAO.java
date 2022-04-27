package connection.DAO;

import connection.entities.Certificate;
import connection.entities.Discipline;
import connection.entities.User;
import connection.Util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CertificateDAO extends Util<Certificate> {

    public CertificateDAO() {
        super();
    }


    @Override
    public Optional<Certificate> getById(Long id) throws DAOException {

        String query = """
        SELECT * FROM cerfticate
        JOIN discipline ON discipline.id = cerfticate.discipline
        JOIN user ON user.id = cerfticate.user
        WHERE id = ?
        """;

        Certificate certificate = new Certificate();
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, id);
            reader.lock();
            try(ResultSet result = statement.executeQuery();) {
                User user = createUser(result, 7);
                Discipline discipline = createDiscipline(result, 5);

                certificate.setId(result.getLong(1));
                certificate.setUser(user);
                certificate.setDiscipline(discipline);
                certificate.setMark(result.getShort(4));
            }

        } catch (SQLException throwables) {
            throw new DAOException("Can't get certificate");
        } finally {
            reader.unlock();
        }

        return Optional.of(certificate);
    }

    private User createUser(ResultSet resultSet, int begin) throws SQLException {
        return new User(
                resultSet.getLong(begin++),
                resultSet.getString(begin++),
                resultSet.getString(begin++),
                resultSet.getString(begin++),
                resultSet.getString(begin++),
                resultSet.getString(begin++),
                resultSet.getString(begin++),
                resultSet.getString(begin++),
                resultSet.getString(begin)
        );
    }

    private Discipline createDiscipline(ResultSet resultSet, int begin) throws SQLException {
        return new Discipline(
                resultSet.getLong(begin++),
                resultSet.getString(begin++)
                );
    }

    @Override
    public List<Certificate> getAll() throws DAOException {

        String query = """
        SELECT * FROM cerfticate
        JOIN discipline ON discipline.id = cerfticate.discipline
        JOIN user ON user.id = cerfticate.user
        """;

        ArrayList<Certificate> certificates = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            reader.lock();
            try(ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    Certificate certificate = new Certificate();
                    User user = createUser(result, 7);
                    Discipline discipline = createDiscipline(result, 5);

                    certificate.setId(result.getLong(1));
                    certificate.setUser(user);
                    certificate.setDiscipline(discipline);
                    certificate.setMark(result.getShort(4));
                    certificates.add(certificate);
                }

            }


        } catch (SQLException throwables) {
            throw new DAOException("Can't get certificate");
        } finally {
            reader.unlock();
        }

        return certificates;
    }

    @Override
    public boolean add(Certificate record) throws DAOException {

        String query = """
        INSERT INTO certificate (`user`, `discipline`, `mark`)
        VALUES (?, ?, ?)
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);){

            statement.setLong(1, record.getUser().getId());
            statement.setLong(2, record.getDiscipline().getId());
            statement.setShort(3, record.getMark());
            writer.lock();
            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't add certificate");
        } finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean update(Certificate record) throws DAOException {

        String query = """
        UPDATE certificate SET user = ?, discipline = ?, mark = ? WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery();){

            statement.setLong(1, record.getUser().getId());
            statement.setLong(2, record.getDiscipline().getId());
            statement.setShort(3, record.getMark());
            statement.setLong(4, record.getId());

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't update certificate");
        } finally {
            writer.unlock();
        }

        return true;
    }

    @Override
    public boolean delete(Long id) throws DAOException {

        String query = """
        DELETE FROM certificate WHERE id = ?
        """;

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet result = statement.executeQuery();){

            statement.setLong(1, id);

            writer.lock();

            statement.executeUpdate();

        } catch (SQLException throwables) {
            throw new DAOException("Can't delete certificate");
        } finally {
            writer.unlock();
        }

        return true;
    }

}
