package View;

import API.AuthService;
import API.LoggedAuthService;
import Config.Actions;
import Config.Pages;
import Config.Params;
import connection.entities.User;
import API.APIException;

import java.io.*;
import java.util.Arrays;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "login", value = "/login")

public class AuthController extends HttpServlet {

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


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        logger.info("Auth GET");
        doPost(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();

        logger.info("Auth POST");

        String action = request.getParameter(Params.ACTION_FIELD);
        String password = request.getParameter(Params.PASSWORD_FIELD);
        String passwordSecond = request.getParameter(Params.PASSWORD_SECOND_FIELD);
        String login = request.getParameter(Params.LOGIN_FIELD);
        Boolean isValid = false;
        AuthService authService = new LoggedAuthService();

        logger.info("Auth:" + action);

        if(action == null) {
            action = "";
        }

        try {
            switch (action) {
                case Actions.GO_TO_LOGIN:
                    request.getRequestDispatcher(Pages.LOGIN_PAGE).forward(request, response);
                    return;
                case Actions.GO_TO_REG:
                    request.getRequestDispatcher(Pages.REG_PAGE).forward(request, response);
                    return;
                case Actions.DO_REG:
                    User user = new User(
                            request.getParameter(Params.NAME_FIELD),
                            request.getParameter(Params.SURNAME_FIELD),
                            request.getParameter(Params.PATRONYMIC_FIELD),
                            login,
                            request.getParameter(Params.REGION_FIELD),
                            request.getParameter(Params.CITY_FIELD),
                            request.getParameter(Params.EDUCATION_FIELD),
                            request.getParameter(Params.PASSWORD_FIELD)
                    );

                    isValid = authService.tryRegUser(user, passwordSecond);

                    if (!isValid) {
                        request.setAttribute(Params.ERROR_FIELD, "Can't execute registration");
                        request.getRequestDispatcher(Pages.REG_PAGE).forward(request, response);
                        return;
                    }

                    session.setAttribute(Params.LOGIN_FIELD, login);
                    response.sendRedirect(Pages.RANK_CONTROLLER);
                    return;
                case Actions.DO_LOGIN:
                    isValid = authService.tryLoginUser(login, password);
                    if (!isValid) {
                        response.setStatus(401);
                        request.setAttribute(Params.ERROR_FIELD, "Can't login");
                        request.getRequestDispatcher(Pages.LOGIN_PAGE).forward(request, response);
                        return;
                    }

                    session.setAttribute(Params.LOGIN_FIELD, login);
                    request.getRequestDispatcher(Pages.RANK_CONTROLLER).forward(request, response);
                    return;
                case Actions.DO_LOGOUT:
                    request.getSession().removeAttribute(Params.LOGIN_FIELD);
                    response.sendRedirect(Pages.LOGIN_CONTROLLER);
                    return;
                default:
                    if(session.getAttribute(Params.LOGIN_FIELD) == null) {
                        request.getRequestDispatcher(Pages.LOGIN_PAGE).forward(request, response);
                    } else  {
                        response.sendRedirect(Pages.RANK_CONTROLLER);
                    }
            }
        } catch (APIException e) {
            logger.severe(Arrays.toString(e.getStackTrace()));
            request.setAttribute(Params.ERROR_FIELD, e.getMessage());
            request.getRequestDispatcher(Pages.LOGIN_PAGE).forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            logger.severe(Arrays.toString(e.getStackTrace()));
            response.sendError(505);
        }
    }

    public void destroy() {
    }
}
