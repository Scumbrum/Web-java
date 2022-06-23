package connection;


import connection.DAO.DAOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class Util<Dimension> {

    public ReadWriteLock lock = new ReentrantReadWriteLock();
    public Lock writer = lock.writeLock();
    public Lock reader = lock.readLock();
    private Connection connection;

    public Util(Connection connection) {
        this.connection = connection;
    }

    public Util() {
        this.connection = null;
    }

    public Connection getConnection() {
        if(this.connection!=null) {
        return this.connection;
        }
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
