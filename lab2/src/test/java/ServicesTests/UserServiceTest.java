package ServicesTests;

import API.APIException;
import API.UserService;
import connection.DAO.DAOException;
import connection.DAO.DisciplineDAO;
import connection.DAO.FacultyDAO;
import connection.entities.Discipline;
import connection.entities.Faculty;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import java.util.ArrayList;
import java.util.Optional;

public class UserServiceTest {
    FacultyDAO facultyDAO;
    DisciplineDAO disciplineDAO;

    private UserService adminService;

    public UserServiceTest() {
        facultyDAO = mock(FacultyDAO.class);
        disciplineDAO = mock(DisciplineDAO.class);
        adminService = new UserService(facultyDAO, disciplineDAO);
    }

    @Test
    void testGetFaculties() throws DAOException, APIException {
        ArrayList<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty("Google", 100, 200, "normal"));
        BDDMockito.given(facultyDAO.getAll()).willReturn(faculties);
        Assertions.assertArrayEquals(adminService.getFaculties().toArray(), faculties.toArray());
    }

    @Test
    void testGetFaculty1() throws DAOException, APIException {
        Faculty faculty = new Faculty(1L,"Google", 100, 200, "normal");
        BDDMockito.given(facultyDAO.getById(1L)).willReturn(Optional.of(faculty));
        Assertions.assertTrue(adminService.getFaculty(1L).equals(faculty));
    }

    @Test
    void testGetFaculty2() throws DAOException, APIException {
        BDDMockito.given(facultyDAO.getById(1L)).willReturn(Optional.empty());
        Assertions.assertNull(adminService.getFaculty(1L));
    }

    @Test
    void testGetDisciplines() throws DAOException, APIException {
        ArrayList<Discipline> disciplines = new ArrayList<>();
        disciplines.add(new Discipline("Sandora"));
        BDDMockito.when(disciplineDAO.getAll()).thenReturn(disciplines);
        Assertions.assertArrayEquals(disciplines.toArray(), adminService.getDisciplines().toArray());
    }


    @Test
    void testSortName() throws DAOException, APIException {
        ArrayList<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty(1L,"Google", 100, 200, "normal"));
        faculties.add(new Faculty(2L,"AAA", 100, 200, "normal"));

        ArrayList<Faculty> facultiesSort = new ArrayList<>();
        facultiesSort.add(new Faculty(2L,"AAA", 100, 200, "normal"));
        facultiesSort.add(new Faculty(1L,"Google", 100, 200, "normal"));
        BDDMockito.when(facultyDAO.getAll()).thenReturn(faculties);
        adminService.sortByName(faculties);
        Assertions.assertArrayEquals(facultiesSort.toArray(), faculties.toArray());
    }

    @Test
    void testSortAll() throws DAOException, APIException {
        ArrayList<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty(1L,"Google", 100, 100, "normal"));
        faculties.add(new Faculty(2L,"AAA", 100, 200, "normal"));

        ArrayList<Faculty> facultiesSort = new ArrayList<>();
        facultiesSort.add(new Faculty(2L,"AAA", 100, 200, "normal"));
        facultiesSort.add(new Faculty(1L,"Google", 100, 100, "normal"));
        BDDMockito.when(facultyDAO.getAll()).thenReturn(faculties);
        adminService.sortByAll(faculties);
        Assertions.assertArrayEquals(facultiesSort.toArray(), faculties.toArray());
    }

    @Test
    void testSortBudget() throws DAOException, APIException {
        ArrayList<Faculty> faculties = new ArrayList<>();
        faculties.add(new Faculty(1L,"Google", 100, 100, "normal"));
        faculties.add(new Faculty(2L,"AAA", 200, 200, "normal"));

        ArrayList<Faculty> facultiesSort = new ArrayList<>();
        facultiesSort.add(new Faculty(2L,"AAA", 200, 200, "normal"));
        facultiesSort.add(new Faculty(1L,"Google", 100, 100, "normal"));
        BDDMockito.when(facultyDAO.getAll()).thenReturn(faculties);
        adminService.sortByBudget(faculties);
        Assertions.assertArrayEquals(facultiesSort.toArray(), faculties.toArray());
    }

}
