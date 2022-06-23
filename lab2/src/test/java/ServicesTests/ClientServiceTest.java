package ServicesTests;

import API.APIException;
import API.ClientService;
import API.UserService;
import connection.DAO.*;
import connection.entities.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import javax.sql.rowset.Predicate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class ClientServiceTest {

    StatementDAO statementDAO;
    DisciplineDAO disciplineDAO;
    ScoreDAO scoreDAO;
    UserDAO userDAO;

    private ClientService clientService;

    public ClientServiceTest() {
        statementDAO = mock(StatementDAO.class);
        disciplineDAO = mock(DisciplineDAO.class);
        scoreDAO = mock(ScoreDAO.class);
        userDAO = mock(UserDAO.class);
        clientService = new ClientService(statementDAO, disciplineDAO, scoreDAO, userDAO);
    }

    @Test
    void testGetStatements() throws DAOException, APIException {
        User user = new User(1L, "ss", "ss", "ss", "ss", "gg@", "ss", "ss", "ss",false);
        given(userDAO.getByLogin("gg@")).willReturn(Optional.of(user));
        Statement statement = new Statement(user, new Faculty("ss", 0 ,0, "ss"));
        List<Statement> statementList = new ArrayList<>();
        statementList.add(statement);
        given(statementDAO.getByUser(user)).willReturn(statementList);
        Assertions.assertArrayEquals(clientService.getStatements("gg@").toArray(), statementList.toArray());
    }
//
    @Test
    void testAddStatement() throws DAOException, APIException {
        User user = new User(1L, "ss", "ss", "ss", "ss", "gg@", "ss", "ss", "ss",false);
        Faculty faculty = new Faculty("ss", 0 ,0, "ss");
        HashMap<Long, Short> marks = new HashMap<>();
        marks.put(1l, (short) 100);
        marks.put(2l, (short) 200);
        given(userDAO.getByLogin("gg@")).willReturn(Optional.of(user));
        Statement statement = new Statement(user, faculty);
        when(statementDAO.getByData(user, faculty)).thenReturn(Optional.empty()).thenReturn(Optional.of(statement));
        clientService.addStatements(marks, faculty, "gg@");
        verify(statementDAO).add(statement);
        List<Statement> statementList = new ArrayList<>();
        statementList.add(statement);
        given(statementDAO.getByUser(user)).willReturn(statementList);
        for(Long i : marks.keySet()) {
            Discipline discipline = new Discipline();
            discipline.setId(i);
            verify(scoreDAO).add(new Score(discipline,statement, marks.get(i)));
        }
    }

    @Test
    void testAddStatement2() throws DAOException, APIException {
        User user = new User(1L, "ss", "ss", "ss", "ss", "gg@", "ss", "ss", "ss",false);
        Faculty faculty = new Faculty("ss", 0 ,0, "ss");
        HashMap<Long, Short> marks = new HashMap<>();
        marks.put(1l, (short) 100);
        marks.put(2l, (short) 300);
        Assertions.assertThrows(IllegalArgumentException.class, ()->clientService.addStatements(marks, faculty, "gg@"));
    }


    @Test
    void testGetDisciplines1() throws DAOException, APIException {
        Discipline discipline = new Discipline(1L, "Viktoria");
        given(disciplineDAO.getById(1L)).willReturn(Optional.of(discipline));
        ArrayList<Long> exists = new ArrayList<>();
        exists.add(1L);
        Assertions.assertTrue(clientService.getDisciplines(exists).contains(discipline));
    }

    @Test
    void testGetDisciplines2() throws DAOException, APIException {
        List<Discipline> disciplines = new ArrayList<>();
        disciplines.add(new Discipline(1L, "Viktoria"));
        disciplines.add(new Discipline(2L, "Viktoriaa"));
        when(disciplineDAO.getAll()).thenReturn(disciplines);
        Assertions.assertEquals(clientService.getDisciplines(1).size(), 1);
    }

    @Test
    void testGetAdditionalDisciplines() throws DAOException, APIException {
        List<Discipline> disciplines = new ArrayList<>();
        disciplines.add(new Discipline(1L, "Viktoria"));
        disciplines.add(new Discipline(2L, "Viktoriaa"));
        when(disciplineDAO.getAll()).thenReturn(disciplines);
        ArrayList<Discipline> exists = new ArrayList<>();
        exists.add(new Discipline(1L, "Viktoria"));
        disciplines = disciplines
                .stream()
                .filter(discipline -> !exists.contains(discipline))
                .collect(Collectors.toList());
        Assertions.assertArrayEquals(clientService.getAdditionDisciplines(exists).toArray(), disciplines.toArray());
    }
}
