package ru.home.taskswebservice.servlets;

import lombok.extern.slf4j.Slf4j;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Sergei Viacheslaev
 */
@Slf4j
@WebServlet(urlPatterns = "/registration")
public class RegistrationServlet extends HttpServlet {
    private static final String USER_REGISTRATION_SUCCESS = "Пользователь успешно зарегистрирован !";
    private static final String USERNAME_ALREADY_REGISTERED = "Пользователь с таким username уже зарегистрирован !";
    private static final String EMAIL_ALREADY_REGISTERED = "Пользователь с таким email уже зарегистрирован";
    private static final String EMAIL_INCORRECTLY_FILLED = "Неверно заполнен адрес электронной почты !";

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
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

        if (validateEmail(email)) {
            try {
                registrationService.registerUser(username, password, name, surname, email);
                resp.getWriter().write(USER_REGISTRATION_SUCCESS);
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (UserAlreadyRegisteredException e) {
                resp.getWriter().write(USERNAME_ALREADY_REGISTERED);
            } catch (EmailAlreadyRegisteredException e) {
                resp.getWriter().write(EMAIL_ALREADY_REGISTERED);
            }
        } else {
            resp.getWriter().write(EMAIL_INCORRECTLY_FILLED);
        }


    }


    private boolean validateEmail(String emailStr) {
        log.info("User's EMAIL input: {}", emailStr);
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }
}
