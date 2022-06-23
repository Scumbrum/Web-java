package API;
import connection.entities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LoggedClientService extends ClientService{
    private Logger logger = Logger.getLogger("MyLog");

    public LoggedClientService() {
        super();
        FileHandler fh = null;
        try {
            fh = new FileHandler("D:/logs/logs.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
    }

    public ArrayList<Discipline> getAdditionDisciplines(ArrayList<Discipline> disciplines) throws APIException {
        try {
            ArrayList<Discipline> addition = super.getAdditionDisciplines(disciplines);
            logger.info("Rest disciplines" + addition);
            return addition;
        } catch (APIException e) {
            logger.info("Get disciplines failed" + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(),e);
        }
    }

    public ArrayList<Discipline> getDisciplines(ArrayList<Long> exists) throws APIException {
        try {
            ArrayList<Discipline> disciplines = super.getDisciplines(exists);
            logger.info("Current disciplines" + disciplines);
            return disciplines;
        } catch (APIException e) {
            logger.info("Get disciplines failed" + Arrays.toString(e.getStackTrace()));
            throw new APIException("Can't get disciplines",e);
        }
    }

    public ArrayList<Discipline> getDisciplines(Integer amount) throws APIException {
        try {
            ArrayList<Discipline> disciplines = super.getDisciplines(amount);
            logger.info("Get " + amount + " disciplines");
            return disciplines;
        } catch (APIException e) {
            logger.info("Get disciplines failed" + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(),e);
        }
    }

    public void addStatements(HashMap<Long, Short> marks, Faculty faculty, String login) throws APIException {
        try {
            super.addStatements(marks, faculty, login);
            logger.info("Add statement of " + faculty);
        } catch (APIException e) {
            logger.info("Add statement failed" + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }

    public ArrayList<Statement> getStatements(String login) throws APIException {
        try {
            ArrayList<Statement> statements = super.getStatements(login);
            logger.info("Get statements " + login);
            return  statements;
        }catch (APIException e) {
            logger.info("Get statement failed" + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }
}
