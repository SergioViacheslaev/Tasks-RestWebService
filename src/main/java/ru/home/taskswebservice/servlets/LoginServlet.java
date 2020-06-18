package ru.home.taskswebservice.servlets;


import org.apache.commons.codec.digest.DigestUtils;
import ru.home.taskswebservice.service.Authentication;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Sergei Viacheslaev
 */

@WebServlet(urlPatterns = "/login")
public class LoginServlet extends HttpServlet {
    private Authentication authService;

    @Override
    public void init() throws ServletException {
        this.authService = (Authentication) getServletContext().getAttribute("authService");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.getRequestDispatcher("/WEB-INF/view/login.html").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        System.out.println("DoPost works !");

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println(DigestUtils.md5Hex(password).toUpperCase());


        if (authService.isAuthenticated(username,password)) {
            request.getSession();
            response.sendRedirect("/tasks");
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/HTML");
            response.getWriter().write("Неверный логин пароль");
        }

    /*    if (username.equals("admin") && password.equals("admin")) {
            request.getSession();
            response.sendRedirect("/tasks");
        } else {
            response.setCharacterEncoding("UTF-8");
            response.setContentType("text/HTML");
            response.getWriter().write("Неверный логин пароль");
        }*/


    }


}
