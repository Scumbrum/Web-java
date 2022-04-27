package connection;

import connection.DAO.DAOException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public abstract class Util<Dimension> {

    public ReadWriteLock lock = new ReentrantReadWriteLock();
    public Lock writer = lock.writeLock();
    public Lock reader = lock.readLock();

    public Util() {

    }

    public Connection getConnection() {
        Connection connection = null;
        try {

            Class.forName(Config.DATABASE_DRIVER);
            String databasePath = Config.DATABASE_URL + ":" + Config.DATABASE_PORT + "/" + Config.DATABASE_SCHEMA;
            connection = DriverManager.getConnection(databasePath, Config.DATABASE_USER, Config.DATABASE_PASSWORD);

        } catch (ClassNotFoundException | SQLException e ) {
            e.printStackTrace();
        }
        return  connection;
    }

    public abstract Optional<Dimension> getById(Long id) throws DAOException;

    public abstract List<Dimension> getAll() throws DAOException;

    public abstract boolean add(Dimension record) throws DAOException;

    public abstract boolean update(Dimension record) throws DAOException;

    public abstract  boolean delete(Long id) throws DAOException;

}
