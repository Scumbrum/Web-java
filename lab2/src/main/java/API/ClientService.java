package API;

import connection.DAO.DAOException;
import connection.entities.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

public class ClientService extends UserService{
    public ArrayList<Discipline> getAdditionDisciplines(ArrayList<Discipline> disciplines) throws APIException {
        ArrayList<Discipline> addition;
        try {
            addition = (ArrayList<Discipline>) disciplineDAO.getAll()
                    .stream()
                    .filter(discipline ->
                            !disciplines
                                    .stream()
                                    .anyMatch(discipline1 -> Objects.equals(discipline1.getId(), discipline.getId()))
                    )
                    .collect(Collectors.toList());
            logger.info("Rest disciplines" + addition);
        } catch (DAOException e) {
            throw new APIException("Can't get addition disciplines",e);
        }
        return addition;
    }

    public ArrayList<Discipline> getDisciplines(ArrayList<Long> exists) throws APIException {
        ArrayList<Discipline> disciplines = new ArrayList<>();
        try {
           for (Long exist: exists) {
               disciplines.add(disciplineDAO.getById(exist).get());
           }
            logger.info("Current disciplines" + disciplines);
        } catch (DAOException e) {
            throw new APIException("Can't get disciplines",e);
        }
        return disciplines;
    }

    public ArrayList<Discipline> getDisciplines(Integer amount) throws APIException {
        ArrayList<Discipline> disciplines;
        try {
           disciplines = (ArrayList<Discipline>) disciplineDAO.getAll()
                   .stream()
                   .limit(amount)
                   .collect(Collectors.toList());
           logger.info("Get " + amount + " disciplines");
        } catch (DAOException e) {
            throw new APIException("Can't get disciplines",e);
        }
        return disciplines;
    }

    public void addStatements(HashMap<Long, Short> marks, Faculty faculty, String login) throws APIException {
        validateMarks(marks);
        try {
            User user = userDAO.getByLogin(login).get();
            statementDAO.add(new Statement(user, faculty));
            Statement statement = statementDAO.getByData(user, faculty).get();
            for(Long id: marks.keySet()) {
                Discipline discipline = new Discipline();
                discipline.setId(id);
                Score score = new Score(discipline, statement, marks.get(id));
                scoreDAO.add(score);
            }
            logger.info("Add statement " +statement);
        } catch (DAOException e) {
            e.printStackTrace();
            throw new APIException("Can't create statement", e);
        }
    }

    private void validateMarks(HashMap<Long, Short> marks) {
        Boolean nonValid = marks.keySet()
                .stream()
                .map(marks::get)
                .anyMatch(map -> map < 0 || map > 200);
        if(nonValid) {
            throw new IllegalArgumentException("Mark can be only in range (0 - 200)");
        }
    }

    public ArrayList<Statement> getStatements(String login) throws APIException {
        ArrayList<Statement> statements;

        try {
            User user = userDAO.getByLogin(login).get();
            statements = (ArrayList<Statement>) statementDAO.getByUser(user);
            logger.info("Get statements " + user);
        }catch (DAOException e) {
            throw new APIException("Can't create statement");
        }

        return statements;
    }
}
