package API;

import connection.DAO.DAOException;
import connection.DAO.DisciplineDAO;
import connection.DAO.FacultyDAO;
import connection.entities.Discipline;
import connection.entities.Faculty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LoggedUserService extends UserService{
    private Logger logger = Logger.getLogger("MyLog");

    public LoggedUserService() {
        super();
        FileHandler fh = null;
        try {
            fh = new FileHandler("D:/logs/logs.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
    }


    public ArrayList<Faculty> getFaculties() throws APIException {
        try {
            ArrayList<Faculty> faculties = super.getFaculties();
            logger.info("Get faculties");
            return faculties;
        } catch (APIException e) {
            logger.info("Get faculties failed: " + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }
    public void sortByAll(ArrayList<Faculty> faculties) {
        super.sortByAll(faculties);
        logger.info("Sort By all places");
    }

    public void sortByBudget(ArrayList<Faculty> faculties) {
        super.sortByBudget(faculties);
        logger.info("Sort By budget places");
    }

    public void sortByName(ArrayList<Faculty> faculties) {
        super.sortByName(faculties);
        logger.info("Sort by name");
    }

    public Faculty getFaculty(long id) throws APIException {
        try {
            Faculty faculty = super.getFaculty(id);
            logger.info("Get faculty" + id);
            return faculty;
        } catch (APIException e) {
            logger.info("Get Faculty failed: " + Arrays.toString(e.getStackTrace()));
            throw new APIException(e.getMessage(), e);
        }
    }

    public ArrayList<Discipline> getDisciplines() throws APIException {
        try {
            ArrayList<Discipline> disciplines = super.getDisciplines();
            logger.info("Get disciplines");
            return  disciplines;
        } catch (APIException e) {
            logger.info("Get disciplines failed: " + Arrays.toString(e.getStackTrace()));
            throw new APIException("Can't get discipline",e);
        }
    }
}
