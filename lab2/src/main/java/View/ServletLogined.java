package View;

import Config.Pages;
import Config.Params;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ServletLogined extends HttpServlet {

    protected void checkLogin(HttpServletResponse response, String loginField, String controller) throws IOException {
        if(loginField == null) {
            response.sendRedirect("." + controller);
            return;
        }
    }
}
