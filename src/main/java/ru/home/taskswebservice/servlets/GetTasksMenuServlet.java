package ru.home.taskswebservice.servlets;

import lombok.SneakyThrows;
import ru.home.taskswebservice.dao.TaskDaoJDBC;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/tasksmenu")
public class GetTasksMenuServlet extends HttpServlet {

    private TaskDaoJDBC taskDao;

    @Override
    public void init() throws ServletException {

        final Object taskDAO = getServletContext().getAttribute("taskDAO");

        if (!(taskDAO instanceof TaskDaoJDBC)) {
            throw new IllegalStateException("Your repo does not initialize!");
        } else {
            this.taskDao = (TaskDaoJDBC) taskDAO;
        }
    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String username = (String) req.getSession(false).getAttribute("username");
        req.setAttribute("tasks", taskDao.getAllById(username));
        req.getRequestDispatcher("/WEB-INF/view/tasksMenuPage.jsp").forward(req, resp);
    }
}