package View;

import API.APIException;
import API.AdminService;
import API.RequestParserService;
import API.UserService;
import Config.Pages;
import Config.Params;
import Config.Actions;
import connection.entities.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@WebServlet(name = "AdminController", value = "/admin")
public class AdminController extends ServletLogined {
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
        logger.info("Admin GET");
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        logger.info("Admin POST");
        checkLogin(response, (String) request.getSession().getAttribute(Params.LOGIN_FIELD), Pages.LOGIN_CONTROLLER);
        String action = request.getParameter(Params.ACTION_FIELD);
        Faculty faculty;
        ArrayList<Faculty> faculties;
        Long id;
        ArrayList<Discipline> disciplines;
        ArrayList<Statement> statements;
        AdminService adminService = new AdminService();
        RequestParserService parserService = new RequestParserService();
        logger.info("Admin:" + action);
        try {
            switch (action) {
                case Actions.GO_TO_FACULTY_FORM:
                    request.setAttribute(Params.ADD_MARKER, true);
                    request.setAttribute(Params.TARGET_FACULTY, new Faculty());
                    toFacultyList(adminService, request, response);
                    return;
                case Actions.GO_TO_EDIT_FACULTY:
                    request.setAttribute(Params.ADD_MARKER, true);
                    id = Long.parseLong(request.getParameter(Params.ID_FIELD));
                    request.setAttribute(Params.TARGET_FACULTY, adminService.getFaculty(id));
                    request.setAttribute(Params.FACULTY_FIELD, adminService.getFacultiesWithout(id));
                    request.getRequestDispatcher(Pages.ADMIN_PAGE).forward(request, response);
                    return;
                case Actions.DELETE_FACULTY:
                    adminService.deleteFaculty(Long.parseLong(request.getParameter(Params.ID_FIELD)));
                    toFacultyList(adminService, request, response);
                    return;
                case Actions.EDIT_FACULTY:
                    faculty = new Faculty(Long.parseLong(request.getParameter(Params.ID_FIELD)),
                            request.getParameter(Params.NAME_FIELD),
                            Integer.parseInt(request.getParameter(Params.BUDGET_FIELD)),
                            Integer.parseInt(request.getParameter(Params.ALL_FIELD)),
                            request.getParameter(Params.DESCRIPTION_FIELD));
                    try {
                        adminService.editFaculty(faculty);
                    } catch (IllegalArgumentException e) {
                        request.setAttribute(Params.ERROR_FIELD, e.getMessage());
                    }
                    toFacultyList(adminService, request, response);
                case Actions.ADD_FACULTY:
                    faculty = new Faculty(request.getParameter(Params.NAME_FIELD),
                            Integer.parseInt(request.getParameter(Params.BUDGET_FIELD)),
                            Integer.parseInt(request.getParameter(Params.ALL_FIELD)),
                            request.getParameter(Params.DESCRIPTION_FIELD));
                    try {
                        adminService.addFaculty(faculty);
                    } catch (IllegalArgumentException e) {
                        request.setAttribute(Params.ERROR_FIELD, e.getMessage());
                    }
                    toFacultyList(adminService, request, response);
                    return;
                case Actions.GO_TO_STATEMENT:
                    faculties = adminService.getFaculties();
                    disciplines = adminService.getDisciplines();
                    request.setAttribute(Params.STATEMENTS_FIELD, faculties);
                    request.setAttribute(Params.DISCIPLINES_FIELD, disciplines);
                    request.getRequestDispatcher(Pages.ADMIN_PAGE).forward(request, response);
                    return;
                case Actions.TO_FACULTY_STATEMENTS:
                    faculties = adminService.getFaculties();
                    request.setAttribute(Params.STATEMENTS_FIELD, faculties);
                    id = Long.parseLong(request.getParameter(Params.ID_FIELD));
                    faculty = adminService.getFaculty(id);
                    disciplines = adminService.getDisciplines();
                    statements = adminService.getStatementOf(faculty);
                    request.setAttribute(Params.DISCIPLINES_FIELD, disciplines);
                    request.setAttribute(Params.ENTRANTS_FIELD, statements);
                    request.setAttribute(Params.CURR_FIELD, id);
                    request.getRequestDispatcher(Pages.ADMIN_PAGE).forward(request, response);
                    return;
                case Actions.SHOW_USER:
                    faculties = adminService.getFaculties();
                    disciplines = adminService.getDisciplines();
                    request.setAttribute(Params.DISCIPLINES_FIELD, disciplines);
                    id = Long.parseLong(request.getParameter(Params.ID_FIELD));
                    faculty = adminService.getFaculty(id);
                    request.setAttribute(Params.CURR_FIELD, id);
                    id = Long.parseLong(request.getParameter(Params.TARGET_FIELD));
                    ArrayList<Score> scores = adminService.getScore(id);
                    statements = adminService.getStatementOf(faculty);
                    request.setAttribute(Params.STATEMENTS_FIELD, faculties);
                    request.setAttribute(Params.ENTRANTS_FIELD, statements);
                    request.setAttribute(Params.TARGET_FIELD, id);
                    request.setAttribute(Params.SCORES_FIELD, scores);
                    request.getRequestDispatcher(Pages.ADMIN_PAGE).forward(request, response);
                    return;
                case Actions.DO_DELETE_USER:
                    faculties = adminService.getFaculties();
                    id = Long.parseLong(request.getParameter(Params.ID_FIELD));
                    faculty = adminService.getFaculty(id);
                    request.setAttribute(Params.CURR_FIELD, id);
                    id = Long.parseLong(request.getParameter(Params.TARGET_FIELD));
                    adminService.rejectStatement(id);
                    statements = adminService.getStatementOf(faculty);
                    disciplines = adminService.getDisciplines();
                    request.setAttribute(Params.DISCIPLINES_FIELD, disciplines);
                    request.setAttribute(Params.STATEMENTS_FIELD, faculties);
                    request.setAttribute(Params.ENTRANTS_FIELD, statements);
                    request.getRequestDispatcher(Pages.ADMIN_PAGE).forward(request, response);
                    return;
                case Actions.DO_FINALISE:
                    id = Long.parseLong(request.getParameter(Params.ID_FIELD));
                    ArrayList<Long> chosen = parserService.longArrayParse(request.getParameterMap(), Params.DISCIPLINE_FIELD);
                    adminService.finalise(chosen, id);
                case Actions.SHOW_FACULTIES:
                default:
                    toFacultyList(adminService, request, response);
                    return;
            }
        } catch (APIException e) {
            logger.severe(Arrays.toString(e.getStackTrace()));
            response.sendError(505);
        }

    }

    private void toFacultyList(UserService service, HttpServletRequest request, HttpServletResponse response) throws APIException, ServletException, IOException {
        ArrayList<Faculty> faculties = service.getFaculties();
        request.setAttribute(Params.FACULTY_FIELD, faculties);
        request.getRequestDispatcher(Pages.ADMIN_PAGE).forward(request, response);
    }
}
