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
    }

    public UserService(FacultyDAO facultyDAO, DisciplineDAO disciplineDAO) {
        this.facultyDAO = facultyDAO;
        this.disciplineDAO = disciplineDAO;
    }


    public ArrayList<Faculty> getFaculties() throws APIException {
        ArrayList<Faculty> faculties;
        try {
            faculties = (ArrayList<Faculty>) facultyDAO.getAll();
        } catch (DAOException e) {
            throw new APIException("Can't get faculties", e);
        }
        return faculties;
    }
    public void sortByAll(ArrayList<Faculty> faculties) {
        faculties.sort(Comparator.comparingInt(Faculty::getAllPlace).reversed());
    }

    public void sortByBudget(ArrayList<Faculty> faculties) {
        faculties.sort(Comparator.comparingInt(Faculty::getBudgetPlace).reversed());
    }

    public void sortByName(ArrayList<Faculty> faculties) {
        faculties.sort(Comparator.comparing(Faculty::getName));
    }

    public Faculty getFaculty(long id) throws APIException {
        try {
            Optional<Faculty> faculty = facultyDAO.getById(id);
            return faculty.orElse(null);
        } catch (DAOException e) {
            throw new APIException("Can't get faculty", e);
        }
    }

    public ArrayList<Discipline> getDisciplines() throws APIException {
        ArrayList<Discipline> disciplines;
        try {
            disciplines = (ArrayList<Discipline>) disciplineDAO.getAll();
        } catch (DAOException e) {
            throw new APIException("Can't get discipline",e);
        }
        return disciplines;
    }

    public void sortByReverseName(ArrayList<Faculty> faculties) {
        faculties.sort(Comparator.comparing(Faculty::getName).reversed());
    }
}
