package View;

import Config.Pages;
import Config.Params;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(servletNames = "UserController")
public class UserFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        if(session == null) {
            request.getRequestDispatcher(Pages.LOGIN_PAGE).forward(request, response);
            return;
        }
        String login = (String) session.getAttribute(Params.LOGIN_FIELD);
        if(login.equals(request.getServletContext().getInitParameter(Params.ADMIN_FIELD))) {
            response.sendRedirect(Pages.RANK_CONTROLLER);
        }
        else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
