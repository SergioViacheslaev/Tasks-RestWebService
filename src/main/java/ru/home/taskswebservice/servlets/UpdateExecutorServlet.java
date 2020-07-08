package ru.home.taskswebservice.servlets;

import lombok.SneakyThrows;
import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.model.Task;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;

/**
 * @author Sergei Viacheslaev
 */
@WebServlet("/update_executor")
public class UpdateExecutorServlet extends HttpServlet {
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private TaskDaoJDBC taskDao;

    @Override
    public void init() throws ServletException {
        final Object taskDAO = getServletContext().getAttribute("taskDAO");
        this.taskDao = (TaskDaoJDBC) taskDAO;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        final int id = Integer.parseInt(req.getParameter("id"));
        final String executor_username = req.getParameter("executor_username");

        Task task = new Task();
        task.setId(id);


        try {
            taskDao.updateTaskExecutor(task, executor_username);
            resp.sendRedirect(req.getContextPath() + "/tasksmenu");
        } catch (SQLException e) {
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write("Произошла ошибка, исполнитель не обновлен\n" + e.getMessage());
        }


    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        final String id = req.getParameter("id");

        final Task task = taskDao.findById(Long.parseLong(id)).get();

        req.setAttribute("task", task);
        req.getRequestDispatcher("/WEB-INF/view/updateTaskExecutor.jsp")
                .forward(req, resp);
    }

}
