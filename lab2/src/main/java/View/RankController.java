package View;

import Config.Pages;
import Config.Params;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

@WebServlet(name = "rank", value = "/rank")
public class RankController extends HttpServlet {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        logger.info("Auth POST");
        HttpSession session = req.getSession();

        String admin = getServletContext().getInitParameter(Params.ADMIN_FIELD);
        logger.info("Rank: " +  session.getAttribute(Params.LOGIN_FIELD));
        if(session.getAttribute(Params.LOGIN_FIELD).equals(admin)) {
            req.getRequestDispatcher(Pages.ADMIN_CONTROLLER).forward(req, resp);
            return;
        } else if(session.getAttribute(Params.LOGIN_FIELD) != null) {
            req.getRequestDispatcher(Pages.USER_CONTROLLER).forward(req, resp);
            return;
        } else {
            resp.sendRedirect(Pages.LOGIN_CONTROLLER);
            return;
        }
    }
}
