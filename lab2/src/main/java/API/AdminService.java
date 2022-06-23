package API;

import connection.Config;
import connection.DAO.DAOException;
import connection.DAO.FacultyDAO;
import connection.DAO.ScoreDAO;
import connection.DAO.StatementDAO;
import connection.entities.Faculty;
import connection.entities.Score;
import connection.entities.Statement;
import connection.entities.User;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.DataSource;
public class AdminService extends UserService{
    private Session session;

    private Transport transport;
    public AdminService(FacultyDAO facultyDAO, StatementDAO statementDAO, ScoreDAO scoreDAO, Transport transport) throws NoSuchProviderException {
        this.session = setupServerProperties();
        this.statementDAO = statementDAO;
        this.facultyDAO = facultyDAO;
        this.scoreDAO = scoreDAO;
        this.transport = transport;
    }

    public ArrayList<User> getUsers() throws APIException {
        ArrayList<User> users;
        try {
            users = (ArrayList<User>) userDAO.getAll();
        } catch (DAOException e) {
            throw new APIException(e.getMessage(), e);
        }
        return users;
    }

    public void blockUser(Long id) throws APIException {
        try {
            Optional<User> userOptional = userDAO.getById(id);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                user.setBlocked(true);
                userDAO.update(user);
            }
        } catch (DAOException e) {
            throw new APIException(e.getMessage(), e);
        }
    }
    public void unblockUser(Long id) throws APIException {
        try {
            Optional<User> userOptional = userDAO.getById(id);
            if(userOptional.isPresent()) {
                User user = userOptional.get();
                System.out.println("unblock");
                user.setBlocked(false);
                userDAO.update(user);
            }
        } catch (DAOException e) {
            throw new APIException(e.getMessage(), e);
        }
    }
    private class ScoreComparator implements Comparator<ArrayList<Score>> {

        @Override
        public int compare(ArrayList<Score> o1, ArrayList<Score> o2) {
            if(getAverage(o1) > getAverage(o2)) {
                return -1;
            } else if (getAverage(o1) < getAverage(o2)) {
                return 1;
            } else {
                return o1.get(0).getStatement().getUser().getName().compareTo(o2.get(0).getStatement().getUser().getName());
            }
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }

    public AdminService(){
        super();
        this.session = setupServerProperties();
        try {
            this.transport = session.getTransport("smtp");
        } catch (NoSuchProviderException e) {
            throw new InternalError("Can't create notificator", e);
        }

    }

    public void addFaculty(Faculty faculty) throws APIException {
        try{
            Boolean exist = checkExist(faculty);

            if(exist) {
                throw new IllegalArgumentException("Exist faculty name");
            }

            facultyDAO.add(faculty);
        } catch (DAOException e) {
            throw new APIException("Can't add faculty", e);
        }
    }

    public void deleteFaculty(long id) throws APIException {
        try{
            facultyDAO.delete(id);
        } catch (DAOException e) {
            throw new APIException("Can't delete faculty", e);
        }
    }

    public ArrayList<Faculty> getFacultiesWithout(Long id) throws APIException {
        ArrayList<Faculty> faculties = getFaculties();
        return (ArrayList<Faculty>) faculties
                .stream()
                .filter(faculty -> !Objects.equals(faculty.getId(), id))
                .collect(Collectors.toList());
    }

    public void editFaculty(Faculty faculty) throws APIException {
        try{
            Boolean exist = checkExist(faculty);

            if(exist) {
                throw new IllegalArgumentException("Exist faculty name");
            }

            facultyDAO.update(faculty);
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
            HashMap<Statement, ArrayList<Score>> averages = getAverages(statements, scores);
            HashMap<ArrayList<Score>, Integer> positions = getPositions(averages);
            distribution(faculty, averages, positions);
        } catch (DAOException e) {
            throw new APIException("Can't finalise", e);
        }
    }

    private void distribution(Faculty faculty, HashMap<Statement, ArrayList<Score>> averages, HashMap<ArrayList<Score>, Integer> positions) throws DAOException, APIException {
        AtomicReference<Boolean> success = new AtomicReference<>(true);
        averages.forEach((statement, array) -> {
                try {
                    if (positions.get(array)+1 <= faculty.getBudgetPlace()) {
                        statement.setStatus((short) 4);
                        statement.setAverage((short) getAverage(array));
                        sendEmail(statement.getUser(), 4);
                    } else if (positions.get(array)+1 <= faculty.getAllPlace()) {
                        statement.setStatus((short) 2);
                        statement.setAverage((short) getAverage(array));
                        sendEmail(statement.getUser(), 2);
                    } else if (positions.get(array)+1 > faculty.getAllPlace()) {
                        statement.setStatus((short) 3);
                        statement.setAverage((short) getAverage(array));
                        sendEmail(statement.getUser(), 3);
                    }
                } catch (MessagingException e) {
                    System.out.println(e.getMessage());
                    success.set(false);
                }
        });
        if(!success.get()) {
            throw new APIException("Email's were not sent");
        }
        for(Statement statement: averages.keySet()) {

            statementDAO.update(statement);
        }
    }

    private void sendEmail(User user, int status) throws MessagingException {
        Session session = setupServerProperties();
        MimeMessage mime = new MimeMessage(session);

        String emailSubject = "Results of enrollment";
        String body = null;
        if(status == 4) {
            body = "Yu are on budget";
        } else if(status == 2) {
            body = "You are on contract";
        } else  {
            body = "You are restricted";
        }
        mime.setSubject(emailSubject);
        mime.addRecipient(Message.RecipientType.TO, new InternetAddress(user.getMail()));
        MimeBodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(body, "text/html");
        MimeMultipart mimeMultipart = new MimeMultipart();
        mimeMultipart.addBodyPart(bodyPart);
        mime.setContent(mimeMultipart);
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        sendNotification(mime, session);
    }

    private Session setupServerProperties() {
        Properties properties = System.getProperties();
        properties.put("mail.smtp.port",Config.MAIL_PORT);
        properties.put("mail.smtp.ssl.trust","smtp.gmail.com");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.starttls.enable","true");
        return Session.getDefaultInstance(properties, null);
    }

    private void sendNotification(MimeMessage message, Session session) throws MessagingException {
        String fromUser = Config.MAIL_USER;
        String fromUserPassword = Config.MAIL_PASSWORD;
        String host = "smtp.gmail.com";
        if(!transport.isConnected()){
            transport.connect(host, fromUser, fromUserPassword);
        }
        transport.sendMessage(message, message.getAllRecipients());
    }

    private HashMap<ArrayList<Score>, Integer> getPositions(HashMap<Statement, ArrayList<Score>> averages) {
        ScoreComparator scoreComparator = new ScoreComparator();
        ArrayList<ArrayList<Score>> scoresArray = (ArrayList<ArrayList<Score>>) averages.keySet()
                .stream()
                .map(averages::get)
                .sorted(scoreComparator)
                .collect(Collectors.toList());
        HashMap<ArrayList<Score>, Integer> positions = new HashMap<>();
        for(int i = 0; i < scoresArray.size(); i++) {
            positions.put(scoresArray.get(i), i);
        }
        System.out.println(positions);
        return positions;
    }

    private HashMap<Statement, ArrayList<Score>> getAverages(ArrayList<Statement> statements, ArrayList<Score> scores)  {
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
