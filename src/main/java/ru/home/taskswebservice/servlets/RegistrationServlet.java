package ru.home.taskswebservice.servlets;

import ru.home.taskswebservice.service.UserRegistration;
import ru.home.taskswebservice.service.exceptions.EmailAlreadyRegisteredException;
import ru.home.taskswebservice.service.exceptions.UserAlreadyRegisteredException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * @author Sergei Viacheslaev
 */
@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    private UserRegistration registrationService;

    @Override
    public void init() throws ServletException {
        this.registrationService = (UserRegistration) getServletContext().getAttribute("userRegistrationService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/registrationUser.html").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("text/HTML");

        String username = req.getParameter("username");
        String password = req.getParameter("password");
        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String email = req.getParameter("email").toLowerCase();

        try {
            registrationService.registerUser(username, password, name, surname, email);
            resp.getWriter().write("Пользователь успешно зарегистрирован !");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (UserAlreadyRegisteredException e) {
            resp.getWriter().write("Пользователь с таким username уже зарегистрирован !");
        } catch (EmailAlreadyRegisteredException e) {
            resp.getWriter().write("Пользователь с таким email уже зарегистрирован ");
        }


    }
}
