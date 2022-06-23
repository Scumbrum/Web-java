package View;

import API.AuthService;
import API.LoggedAuthService;
import Config.Pages;
import Config.Params;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(servletNames = {"AdminController", "rank", "UserController"})
public class LoginFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        AuthService authService = new AuthService();
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession(false);
        if(session == null) {
            request.getRequestDispatcher(Pages.LOGIN_PAGE).forward(request, response);
            return;
        }
        String login = (String) session.getAttribute(Params.LOGIN_FIELD);
        boolean valid = authService.checkLogin(login);
        if(!valid) {
            response.sendRedirect(Pages.LOGIN_CONTROLLER);
    }
        else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }
}
