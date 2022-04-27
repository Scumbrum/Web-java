package API;

import connection.DAO.*;
import connection.entities.Discipline;
import connection.entities.Faculty;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Optional;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class UserService {

    protected Logger logger = Logger.getLogger("MyLog");

    protected FacultyDAO facultyDAO;
    protected DisciplineDAO disciplineDAO;
    protected StatementDAO statementDAO;
    protected UserDAO userDAO;
    protected ScoreDAO scoreDAO;

    public UserService() {
        facultyDAO = new FacultyDAO();
        disciplineDAO = new DisciplineDAO();
        statementDAO = new StatementDAO();
        userDAO = new UserDAO();
        scoreDAO = new ScoreDAO();
        FileHandler fh = null;
        try {
            fh = new FileHandler("D:/logs/logs.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
    }

    public UserService(FacultyDAO facultyDAO, DisciplineDAO disciplineDAO) {
        FileHandler fh = null;
        try {
            fh = new FileHandler("D:/logs/logs.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
        this.facultyDAO = facultyDAO;
        this.disciplineDAO = disciplineDAO;
    }

    public ArrayList<Faculty> getFaculties() throws APIException {
        ArrayList<Faculty> faculties;
        try {
            faculties = (ArrayList<Faculty>) facultyDAO.getAll();
            logger.info("Get faculties");
        } catch (DAOException e) {
            throw new APIException("Can't get faculties", e);
        }
        return faculties;
    }
    public void sortByAll(ArrayList<Faculty> faculties) {
        faculties.sort(Comparator.comparingInt(Faculty::getAllPlace).reversed());
        logger.info("Sort By all places");
    }

    public void sortByBudget(ArrayList<Faculty> faculties) {
        faculties.sort(Comparator.comparingInt(Faculty::getBudgetPlace).reversed());
        logger.info("Sort By budget places");
    }

    public void sortByName(ArrayList<Faculty> faculties) {
        faculties.sort(Comparator.comparing(Faculty::getName));
        logger.info("Sort by name");
    }

    public Faculty getFaculty(long id) throws APIException {
        try {
            Optional<Faculty> faculty = facultyDAO.getById(id);
            logger.info("Get faculty" + faculty.orElse(null));
            return faculty.orElse(null);
        } catch (DAOException e) {
            throw new APIException("Can't get faculty", e);
        }
    }

    public ArrayList<Discipline> getDisciplines() throws APIException {
        ArrayList<Discipline> disciplines;
        try {
            disciplines = (ArrayList<Discipline>) disciplineDAO.getAll();
            logger.info("Get disciplines");
        } catch (DAOException e) {
            throw new APIException("Can't get discipline",e);
        }
        return disciplines;
    }
}
