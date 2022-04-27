package View;

import API.APIException;
import API.ClientService;
import API.RequestParserService;
import Config.Actions;
import Config.Pages;
import Config.Params;
import connection.entities.Discipline;
import connection.entities.Faculty;
import connection.entities.Statement;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@WebServlet(name = "UserController", value = "/user")
public class UserController extends ServletLogined {
    private Logger logger = Logger.getLogger("MyLog");

    @Override
    public void init() throws ServletException {
        super.init();
        FileHandler fh = null;
        try {
            fh = new FileHandler("D:/logs/logs.log");
        } catch (IOException e) {
            e.printStackTrace();
        }
        logger.addHandler(fh);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("UserController GET");
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("UserController POST");
        String login = (String) request.getSession().getAttribute(Params.LOGIN_FIELD);
        checkLogin(response, login, Pages.LOGIN_CONTROLLER);
        String action = request.getParameter(Params.ACTION_FIELD);
        ClientService userService = new ClientService();
        RequestParserService parserService = new RequestParserService();
        ArrayList<Faculty> faculties;
        Faculty faculty;
        ArrayList<Discipline> disciplines;
        ArrayList<Discipline> addition;
        logger.info("User:" + action);
        try {
            switch (action) {
                case Actions.SORT_ALL:
                    faculties = userService.getFaculties();
                    userService.sortByAll(faculties);
                    request.setAttribute(Params.FACULTY_FIELD, faculties);
                    request.getRequestDispatcher(Pages.USER_PAGE).forward(request, response);
                    return;
                case Actions.SORT_BUDGET:
                    faculties = userService.getFaculties();
                    userService.sortByBudget(faculties);
                    request.setAttribute(Params.FACULTY_FIELD, faculties);
                    request.getRequestDispatcher(Pages.USER_PAGE).forward(request, response);
                    return;
                case Actions.SORT_NAME:
                    faculties = userService.getFaculties();
                    userService.sortByName(faculties);
                    request.setAttribute(Params.FACULTY_FIELD, faculties);
                    request.getRequestDispatcher(Pages.USER_PAGE).forward(request, response);
                    return;
                case Actions.GO_TO_FACULTY_REG:
                    faculty = userService.getFaculty(Long.parseLong(request.getParameter(Params.ID_FIELD)));
                    disciplines = userService.getDisciplines(1);
                    addition = userService.getAdditionDisciplines(disciplines);
                    request.setAttribute(Params.TARGET_FACULTY, faculty);
                    request.setAttribute(Params.DISCIPLINES_FIELD, disciplines);
                    request.setAttribute(Params.ADDITION_FIELD, addition);
                    request.getRequestDispatcher(Pages.USER_PAGE).forward(request, response);
                    return;
                case Actions.ADD_DISCIPLINE:
                    faculty = userService.getFaculty(Long.parseLong(request.getParameter(Params.ID_FIELD)));
                    ArrayList<Long> exists = parserService.longArrayParse(request.getParameterMap(), Params.EXISTS_FIELD);
                    exists.add(Long.parseLong(request.getParameter(Params.DISCIPLINE_FIELD)));
                    disciplines = userService.getDisciplines(exists);
                    addition = userService.getAdditionDisciplines(disciplines);
                    request.setAttribute(Params.TARGET_FACULTY, faculty);
                    request.setAttribute(Params.DISCIPLINES_FIELD, disciplines);
                    request.setAttribute(Params.ADDITION_FIELD, addition);
                    request.getRequestDispatcher(Pages.USER_PAGE).forward(request, response);
                    return;
                case Actions.GO_TO_STATEMENT:
                    ArrayList<Statement> statements = userService.getStatements(login);
                    request.setAttribute(Params.STATEMENTS_FIELD, statements);
                    request.getRequestDispatcher(Pages.USER_PAGE).forward(request, response);
                    return;
                case Actions.DO_FACULTY_REG:
                    faculty = userService.getFaculty(Long.parseLong(request.getParameter(Params.ID_FIELD)));
                    HashMap <Long, Short> marks = parserService.getMarks(request.getParameterMap());
                    try {
                        userService.addStatements(marks, faculty, login);
                    } catch (IllegalArgumentException e) {
                        request.setAttribute(Params.ERROR_FIELD, e.getMessage());
                    }
                case Actions.SHOW_FACULTIES:
                default:
                    faculties = userService.getFaculties();
                    request.setAttribute(Params.FACULTY_FIELD, faculties);
                    request.getRequestDispatcher(Pages.USER_PAGE).forward(request,response);
            }
        } catch (APIException e) {
            logger.severe(Arrays.toString(e.getStackTrace()));
            response.sendError(505);
        }
    }
}
