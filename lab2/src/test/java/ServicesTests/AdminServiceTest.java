package ServicesTests;

import API.APIException;
import API.AdminService;
import connection.DAO.*;
import connection.entities.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import javax.mail.NoSuchProviderException;
import javax.mail.Transport;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class AdminServiceTest {
    FacultyDAO facultyDAO;
    StatementDAO statementDAO;
    ScoreDAO scoreDAO;
    Transport transport;

    private AdminService adminService;

    public AdminServiceTest() throws NoSuchProviderException {
        facultyDAO = mock(FacultyDAO.class);
        statementDAO = mock(StatementDAO.class);
        scoreDAO = mock(ScoreDAO.class);
        transport = mock(Transport.class);
        adminService = new AdminService(facultyDAO, statementDAO, scoreDAO, transport);
    }

    @Test
    void testAddFaculty() throws DAOException, APIException {
        Faculty faculty = new Faculty(1L, "ss", 1, 10, "ss");
        Faculty faculty2 = new Faculty(2L, "gg", 2, 20, "sss");
        Faculty faculty3 = new Faculty(3L, "good", 5, 10, "ddd");
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);
        faculties.add(faculty2);
        when(facultyDAO.getAll()).thenReturn(faculties);
        adminService.addFaculty(faculty3);
        verify(facultyDAO).add(faculty3);
    }

    @Test
    void testAddFaculty1() throws DAOException, APIException {
        Faculty faculty = new Faculty(1L, "ss", 1, 10, "ss");
        Faculty faculty2 = new Faculty(2L, "gg", 2, 20, "sss");
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);
        faculties.add(faculty2);
        when(facultyDAO.getAll()).thenReturn(faculties);
        Assertions.assertThrows(IllegalArgumentException.class, () -> adminService.addFaculty(faculty2));
    }

    @Test
    void testDeleteFaculty() throws DAOException, APIException {
        adminService.deleteFaculty(1L);
        verify(facultyDAO).delete(1L);
    }

    @Test
    void testGetFacultiesWithout() throws DAOException, APIException {
        Faculty faculty = new Faculty(1L, "ss", 1, 10, "ss");
        Faculty faculty2 = new Faculty(2L, "gg", 2, 20, "sss");
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);
        faculties.add(faculty2);
        when(facultyDAO.getAll()).thenReturn(faculties);
        Assertions.assertEquals(adminService.getFacultiesWithout(1L).get(0), faculty2);
    }

    @Test
    void testEditFaculty() throws DAOException, APIException {
        Faculty faculty = new Faculty(1L, "ss", 1, 10, "ss");
        Faculty faculty2 = new Faculty(2L, "gg", 2, 20, "sss");
        Faculty faculty3 = new Faculty(3L, "good", 5, 10, "ddd");
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);
        faculties.add(faculty2);
        when(facultyDAO.getAll()).thenReturn(faculties);
        adminService.editFaculty(faculty3);
        verify(facultyDAO).update(faculty3);
    }

    @Test
    void testEditFaculty2() throws DAOException, APIException {
        Faculty faculty = new Faculty(1L, "ss", 1, 10, "ss");
        Faculty faculty2 = new Faculty(2L, "gg", 2, 20, "sss");
        List<Faculty> faculties = new ArrayList<>();
        faculties.add(faculty);
        faculties.add(faculty2);
        when(facultyDAO.getAll()).thenReturn(faculties);
        Assertions.assertThrows(IllegalArgumentException.class, () -> adminService.editFaculty(faculty2));
    }

    @Test
    void testGetStatementOf() throws DAOException, APIException {
        Faculty faculty = new Faculty(1L, "ss", 1, 10, "ss");
        ArrayList<Statement> statements = new ArrayList<>();
        User user = new User(1L, "ss", "ss", "ss", "ss", "gg@", "ss", "ss", "ss", false);
        User user2 = new User(2L, "ss", "ss", "ss", "ss", "ggg@", "ss", "ss", "ss", false);
        Statement statement = new Statement(user, faculty, (Short) null, (short) 1);
        Statement statement2 = new Statement(user2, faculty, (Short) null, (short) 3);
        statements.add(statement);
        statements.add(statement2);
        when(statementDAO.getByFaculty(faculty)).thenReturn(statements);
        Assertions.assertEquals(adminService.getStatementOf(faculty).get(0), statement);
    }

    @Test
    void testGetScore() throws DAOException, APIException {
        Statement statement = new Statement();
        statement.setId(1L);
        given(scoreDAO.getByStatement(statement)).willReturn(new ArrayList<>());
        adminService.getScore(1L);
        statement.setId(1L);
        verify(scoreDAO).getByStatement(statement);
    }

    @Test
    void testRejectStatement() throws DAOException, APIException {
        Optional<Statement> statement = Optional.of(new Statement());
        given(statementDAO.getById(1L)).willReturn(statement);
        adminService.rejectStatement(1L);
        ArgumentCaptor<Statement> argument = ArgumentCaptor.forClass(Statement.class);
        verify(statementDAO).update(argument.capture());
        Assertions.assertEquals((short) 3, argument.getValue().getStatus());
    }

    @Test
    void testFinalise() throws DAOException, APIException {
        Faculty faculty = new Faculty(1L, "ss", 1, 2, "ss");
        ArrayList<Statement> statements = new ArrayList<>();
        User user = new User(1L, "ss", "ss", "ss", "ss", "gg@ukr.net", "ss", "ss", "ss",false);
        User user2 = new User(2L, "ss", "ss", "ss", "ss", "ggg@ukr.net", "ss", "ss", "ss",false);
        User user3 = new User(3L, "ss", "ss", "ss", "ss", "long@ukr.net", "ss", "ss", "ss",false);
        Statement statement1 = new Statement(user, faculty, (Short) null, (short) 1);
        Statement statement2 = new Statement(user2, faculty, (Short) null, (short) 1);
        Statement statement3 = new Statement(user3, faculty, (Short) null, (short) 1);
        statements.add(statement1);
        statements.add(statement2);
        statements.add(statement3);
        ArrayList<Score> scores = new ArrayList<>();
        ArrayList<Discipline> disciplines = new ArrayList<>();
        disciplines.add(new Discipline(1L, "ggg"));
        disciplines.add(new Discipline(2L, "fff"));
        scores.add(new Score(disciplines.get(0), statement1, (short) 150));
        scores.add(new Score(disciplines.get(1), statement1, (short) 150));
        scores.add(new Score(disciplines.get(0), statement2, (short) 160));
        scores.add(new Score(disciplines.get(1), statement2, (short) 150));
        scores.add(new Score(disciplines.get(0), statement3, (short) 160));
        scores.add(new Score(disciplines.get(1), statement3, (short) 160));
        given(facultyDAO.getById(1L)).willReturn(Optional.of(faculty));
        given(statementDAO.getByFaculty(faculty)).willReturn(statements);
        when(scoreDAO.getAll()).thenReturn(scores);
        ArrayList<Long> id = new ArrayList<>();
        id.add(1L);
        id.add(2L);
        adminService.finalise(id, 1L);
        statement1.setStatus((short) 3);
        statement2.setStatus((short) 2);
        statement3.setStatus((short) 4);
        verify(statementDAO).update(statement1);
        verify(statementDAO).update(statement2);
        verify(statementDAO).update(statement3);
    }

}
