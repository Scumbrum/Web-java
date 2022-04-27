package API;

import connection.DAO.DAOException;
import connection.entities.Faculty;
import connection.entities.Score;
import connection.entities.Statement;

import java.util.*;
import java.util.stream.Collectors;

public class AdminService extends UserService{

    private class ScoreComparator implements Comparator<ArrayList<Score>> {

        @Override
        public int compare(ArrayList<Score> o1, ArrayList<Score> o2) {
            if(getAverage(o1) > getAverage(o2)) {
                return 1;
            } else if (getAverage(o1) < getAverage(o2)) {
                return -1;
            } else {
                return o1.get(0).getStatement().getUser().getName().compareTo(o2.get(0).getStatement().getUser().getName());
            }
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    public AdminService() {
        super();
    }

    public void addFaculty(Faculty faculty) throws APIException {
        try{
            Boolean exist = checkExist(faculty);

            if(exist) {
                throw new IllegalArgumentException("Exist faculty name");
            }

            facultyDAO.add(faculty);
            logger.info("Add faculty");
        } catch (DAOException e) {
            throw new APIException("Can't add faculty", e);
        }
    }

    public void deleteFaculty(long id) throws APIException {
        try{
            facultyDAO.delete(id);
            logger.info("Delete faculty");
        } catch (DAOException e) {
            throw new APIException("Can't add faculty", e);
        }
    }

    public ArrayList<Faculty> getFacultiesWithout(Long id) throws APIException {
        ArrayList<Faculty> faculties = getFaculties();
        return (ArrayList<Faculty>) faculties
                .stream()
                .filter(faculty -> faculty.getId()!= id)
                .collect(Collectors.toList());
    }

    public void editFaculty(Faculty faculty) throws APIException {
        try{
            Boolean exist = checkExist(faculty);

            if(exist) {
                throw new IllegalArgumentException("Exist faculty name");
            }

            facultyDAO.update(faculty);
            logger.info("Edit faculty");
        } catch (DAOException e) {
            throw new APIException("Can't add faculty", e);
        }
    }

    private Boolean checkExist(Faculty faculty) throws DAOException {
        ArrayList<Faculty> faculties = (ArrayList<Faculty>) facultyDAO.getAll();
        return faculties.stream().anyMatch(faculty1 -> faculty1.equals(faculty));
    }

    public ArrayList<Statement> getStatementOf(Faculty faculty) throws APIException {
        ArrayList<Statement> statements;
        logger.info("Get statement of" + faculty.toString());
        try {
            statements = (ArrayList<Statement>) statementDAO.getByFaculty(faculty);
            statements = (ArrayList<Statement>) statements
                    .stream()
                    .filter(statement -> statement.getStatus() != 3)
                    .collect(Collectors.toList());
        } catch (DAOException e) {
            throw new APIException("Can't get Statement", e);
        }
        return statements;
    }

    public ArrayList<Score> getScore(Long statementId) throws APIException {
        ArrayList<Score> scores;
        try {
            Statement statement = new Statement();
            statement.setId(statementId);
            scores = (ArrayList<Score>) scoreDAO.getByStatement(statement);
            logger.info("Get score of statement" + statement);
        } catch (DAOException e) {
            throw new APIException("Can't get Statement", e);
        }

        return scores;
    }

    public void rejectStatement(Long statementId) throws APIException {
        try {
            Statement statement = statementDAO.getById(statementId).get();
            statement.setStatus((short) 3);
            statementDAO.update(statement);
            logger.info("Rejected statement" + statement);
        } catch (DAOException e) {
            throw new APIException("Can't get Statement", e);
        }
    }

    public void finalise(ArrayList<Long> disciplines, Long id) throws APIException {
        try {
            Faculty faculty = facultyDAO.getById(id).get();
            ArrayList<Statement> statements = (ArrayList<Statement>) statementDAO.getByFaculty(faculty);
            ArrayList<Score> scores = getScoreOf(statements);
            statements = doReject(scores, disciplines, statements);
            HashMap<Statement, ArrayList<Score>> averages = getAverages(statements, scores, disciplines);
            HashMap<ArrayList<Score>, Integer> positions = getPositions(averages);
            distribution(faculty, averages, positions);
            logger.info("Finalising faculty" + faculty);
        } catch (DAOException e) {
            throw new APIException("Can't finalise", e);
        }
    }

    private void distribution(Faculty faculty, HashMap<Statement, ArrayList<Score>> averages, HashMap<ArrayList<Score>, Integer> positions) throws DAOException {
        averages.forEach((statement, array) -> {
            if(positions.get(array) <= faculty.getBudgetPlace()) {
                statement.setStatus((short) 4);
            } else if (positions.get(array) <= faculty.getAllPlace()) {
                statement.setStatus((short) 2);
            } else if (positions.get(array) > faculty.getAllPlace()) {
                statement.setStatus((short) 3);
            }
        });
        for(Statement statement: averages.keySet()) {
            statementDAO.update(statement);
        }
    }

    private HashMap<ArrayList<Score>, Integer> getPositions(HashMap<Statement, ArrayList<Score>> averages) {
        ScoreComparator scoreComparator = new ScoreComparator();
        ArrayList<ArrayList<Score>> scoresArray = (ArrayList<ArrayList<Score>>) averages.keySet()
                .stream()
                .map(averages::get)
                .sorted(scoreComparator::compare)
                .collect(Collectors.toList());
        HashMap<ArrayList<Score>, Integer> positions = new HashMap<>();
        for(int i = 0; i < scoresArray.size(); i++) {
            positions.put(scoresArray.get(i), i);
        }
        return positions;
    }

    private HashMap<Statement, ArrayList<Score>> getAverages(ArrayList<Statement> statements, ArrayList<Score> scores, ArrayList<Long> disciplines)  {
        return (HashMap<Statement, ArrayList<Score>>) statements
                .stream()
                .collect(Collectors.toMap(
                        statement -> statement,
                        statement -> getScoreOf(statement, scores)));
    }

    private double getAverage(ArrayList<Score> scores) {
        return scores
                .stream()
                .mapToDouble(Score::getMark)
                .average()
                .getAsDouble();
    }


    private ArrayList<Score> getScoreOf(ArrayList<Statement> statements) throws DAOException {
        ArrayList<Statement> nonRejected = (ArrayList<Statement>) statements
                .stream()
                .filter(statement -> statement.getStatus()!=3)
                .collect(Collectors.toList());
        ArrayList<Score> scores = (ArrayList<Score>) scoreDAO.getAll();
        return (ArrayList<Score>) scores
                .stream()
                .filter(score -> nonRejected.contains(score.getStatement()))
                .collect(Collectors.toList());
    }

    private ArrayList<Score> getScoreOf(Statement statement, ArrayList<Score> scores) {
        return (ArrayList<Score>) scores
                .stream()
                .filter(score -> statement.equals(score.getStatement()))
                .collect(Collectors.toList());
    }

    private ArrayList<Statement> doReject(ArrayList<Score> scores, ArrayList<Long> disciplines, ArrayList<Statement> statements) throws DAOException {
        ArrayList<Statement> rejected = (ArrayList<Statement>) statements
                .stream()
                .filter(statement -> !isContain(statement, scores, disciplines))
                .collect(Collectors.toList());
        System.out.println(rejected);
        for (Statement statement: rejected) {
                statement.setStatus((short) 3);
                statementDAO.update(statement);
        }
        return (ArrayList<Statement>) statements
                .stream()
                .filter(statement -> !rejected.contains(statement))
                .collect(Collectors.toList());
    }

    private boolean isContain(Statement statement, ArrayList<Score> scores, ArrayList<Long> disciplines) {
        return disciplines
                .stream()
                .allMatch(discipline -> scores
                        .stream()
                        .filter(score -> score.getStatement().equals(statement))
                        .anyMatch(score -> score.getDiscipline().getId().equals(discipline)));
    }
}
