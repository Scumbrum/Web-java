package API;

import connection.Config;
import connection.DAO.DAOException;
import connection.DAO.UserDAO;
import connection.entities.User;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.IOException;
import java.net.InterfaceAddress;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class AuthService {

    private UserDAO userDAO;

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public AuthService() {
        userDAO = new UserDAO();
    }
    public Boolean tryLoginUser(String login, String password) throws APIException {

        try{
            List<User> users = userDAO.getAll();

            Optional<User> exist = users.stream()
                    .filter(user -> user.getMail().equals(login) && !user.isBlocked())
                    .findFirst();

            if(exist.isPresent()) {
                User user = exist.get();
                String actualPassword = passwordTransform(user.getPassword(),Cipher.DECRYPT_MODE);
                return actualPassword.equals(password);
            }

        } catch (DAOException
                 |APIException e) {
            throw new APIException(e.getMessage(), e);
        }

        return false;
    }

    public Boolean tryRegUser(User user, String password) throws APIException {

        if(user.getPassword() == null || !user.getPassword().equals(password)){
            return false;
        }

        try {
            new InternetAddress(user.getMail());
        } catch (AddressException e) {
            return false;
        }

        try{

            List<User> users = userDAO.getAll();

            Optional<User> exists = users.stream()
                    .filter(exist -> exist.getMail().equals(user.getMail()))
                    .findFirst();

            if(exists.isPresent()) {
                return false;
            }

            String encodedPassword = passwordTransform(user.getPassword(),Cipher.ENCRYPT_MODE);
            user.setPassword(encodedPassword);
            userDAO.add(user);
        } catch (DAOException
                 |APIException e) {
            throw new APIException(e.getMessage(), e);
        }

        return true;
    }

    public Boolean checkLogin(String login) {
       if(login != null) {
           return true;
       }
       return false;
    }

    private String passwordTransform(String password, int type) throws APIException {
        Key key = new SecretKeySpec(Config.KEY, Config.ALGORITHM);
        try {
            Cipher cipher = Cipher.getInstance(Config.ALGORITHM);
            cipher.init(type, key);
            if(type == Cipher.DECRYPT_MODE) {
                return new String(cipher.doFinal(Base64.getDecoder()
                        .decode(password)));
            }
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(password.getBytes()));
        } catch (NoSuchPaddingException
                 |NoSuchAlgorithmException
                 |InvalidKeyException
                 |IllegalBlockSizeException
                 |BadPaddingException e) {
            throw new APIException(e.getMessage(), e);
        }
    }
}
