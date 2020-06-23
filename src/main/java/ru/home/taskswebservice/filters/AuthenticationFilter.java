package ru.home.taskswebservice.filters;


import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@WebFilter(urlPatterns = "/*")
public class AuthenticationFilter implements Filter {


    @Override
    public void doFilter(final ServletRequest request,
                         final ServletResponse response,
                         final FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.startsWith("/login") || path.startsWith("/showDeveloperInfo") || path.startsWith("/images")) {
            filterChain.doFilter(request, response);
            return;
        }

        //Get current session, NOT create new if it's not exist.
        HttpSession session = req.getSession(false);

        if (session == null) {
            req.getRequestDispatcher("/WEB-INF/view/login.html").forward(req, res);
        } else {
            filterChain.doFilter(request, response);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }


}
