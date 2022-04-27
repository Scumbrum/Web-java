package API;

import connection.DAO.DAOException;
import connection.DAO.UserDAO;
import connection.entities.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class AuthService {

    private Logger logger = Logger.getLogger("MyLog");

    private UserDAO userDAO;

    public AuthService() {
        userDAO = new UserDAO();
        FileHandler fh = null;
        try {
            fh = new FileHandler("D:/logs/logs.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
    }
    public Boolean tryLoginUser(String login, String password) throws APIException {

        try{
            List<User> users = userDAO.getAll();

            Optional<User> exist = users.stream()
                    .filter(user -> user.getMail().equals(login) && user.getPassword().equals(password))
                    .findFirst();


            if(exist.isPresent()) {
                logger.info("Success login" + exist.get());
                return true;
            }

        } catch (DAOException e) {
            throw new APIException(e.getMessage(), e);
        }

        logger.info("Failed login");

        return false;
    }

    public Boolean tryRegUser(User user, String password) throws APIException {

        if(!user.getPassword().equals(password)){
            logger.info("Failed registration");
            return false;
        }

        if(!user.getMail().contains("@")) {
            logger.info("Failed registration");
            return false;
        }

        try{

            List<User> users = userDAO.getAll();

            Optional<User> exists = users.stream()
                    .filter(exist -> exist.getMail().equals(user.getMail()))
                    .findFirst();

            if(exists.isPresent()) {
                logger.info("Failed registration");
                return false;
            }
            userDAO.add(user);
            logger.info("Failed registration" + user);
        } catch (DAOException e) {
            throw new APIException(e.getMessage(), e);
        }

        return true;
    }

    public Boolean checkLogin(String login) {

       if(login != null) {
           logger.info("Success authorise" + login);
           return true;
       }
        logger.info("Failed authorise");
       return false;
    }
}
