package API;

import connection.DAO.DAOException;
import connection.DAO.UserDAO;
import connection.entities.User;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LoggedAuthService extends AuthService{
    private Logger logger = Logger.getLogger("MyLog");

    public LoggedAuthService() {
        super();
        FileHandler fh = null;
        try {
            fh = new FileHandler("D:/logs/logs.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
    }
    public Boolean tryLoginUser(String login, String password) throws APIException {
        Boolean success;
        try {
            success = super.tryLoginUser(login, password);
            logger.info("Success login" + login);
        } catch (APIException e) {
            logger.info("Failed login");
            throw new APIException(e.getMessage());
        }
        return  success;
    }

    public Boolean tryRegUser(User user, String password) throws APIException {
        try {
            Boolean success = super.tryRegUser(user, password);
            if(!success) {
                logger.info("Failed registration");
            } else {
                logger.info("Success registration " + user);
            }
            return success;
        } catch (APIException e) {
            logger.info("Failed registration");
            throw new APIException(e.getMessage(), e);
        }
    }

    public Boolean checkLogin(String login) {
        Boolean success = super.checkLogin(login);
        if(success) {
            logger.info("Success authorise " + login);
        } else {
            logger.info("Failed authorise");
        }
        return success;
    }
}
