package ru.home.taskswebservice.servlets;

import lombok.SneakyThrows;
import ru.home.taskswebservice.dao.TaskDaoJDBC;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Sergei Viacheslaev
 */
@WebServlet(urlPatterns = "/showalltasks")
public class ShowAllTasksServlet extends HttpServlet {
    private TaskDaoJDBC taskDao;

    @Override
    public void init() {

        final Object taskDAO = getServletContext().getAttribute("taskDAO");
        this.taskDao = (TaskDaoJDBC) taskDAO;

    }


    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        req.setAttribute("tasks", taskDao.findAll());
        req.getRequestDispatcher("/WEB-INF/view/allTasksPage.jsp").forward(req, resp);
    }
}
