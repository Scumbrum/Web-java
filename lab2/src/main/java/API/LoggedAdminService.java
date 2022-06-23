package API;

import connection.DAO.DAOException;
import connection.entities.Faculty;
import connection.entities.Score;
import connection.entities.Statement;
import connection.entities.User;

import javax.mail.NoSuchProviderException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class LoggedAdminService extends AdminService {
    private Logger logger = Logger.getLogger("MyLog");

    public LoggedAdminService() {
        super();
        FileHandler fh = null;
        try {
            fh = new FileHandler("D:/logs/logs.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
    }

    public void addFaculty(Faculty faculty) throws APIException {

        try{
            super.addFaculty(faculty);
            logger.info("Add faculty");
        } catch (APIException e) {
            logger.info("Failed add faculty:" + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }

    public void deleteFaculty(long id) throws APIException {
        try{
            super.deleteFaculty(id);
            logger.info("Delete faculty");
        } catch (APIException e) {
            logger.info("Delete faculty failed:" + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }

    public ArrayList<Faculty> getFacultiesWithout(Long id) throws APIException {
        return super.getFacultiesWithout(id);
    }

    public void editFaculty(Faculty faculty) throws APIException {
        try{
            super.editFaculty(faculty);
            logger.info("Edit faculty");
        } catch (APIException e) {
            logger.info("Edit faculty failed:" + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }

    public ArrayList<Statement> getStatementOf(Faculty faculty) throws APIException {

        logger.info("Get statement of" + faculty.toString());
        try {
            ArrayList<Statement> statements = super.getStatementOf(faculty);
            logger.info("Get statement of" + faculty);
            return statements;
        } catch (APIException e) {
            logger.info("Get statements failed:" + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }

    public ArrayList<Score> getScore(Long statementId) throws APIException {
        try {
            ArrayList<Score> scores = super.getScore(statementId);
            logger.info("Get score of statement: " + statementId);
            return scores;
        } catch (APIException e) {
            logger.info("Get scores failed:" + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }

    public void rejectStatement(Long statementId) throws APIException {
        try {
            super.rejectStatement(statementId);
            logger.info("Rejected statement: " + statementId);
        } catch (APIException e) {
            logger.info("Rejected statement failed: " + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }

    public void finalise(ArrayList<Long> disciplines, Long id) throws APIException {
        try {
            super.finalise(disciplines, id);
            logger.info("Finalising faculty: " + id);
        } catch (APIException e) {
            logger.info("Finalising failed: " + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }
}
