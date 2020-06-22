package ru.home.taskswebservice.servlets;

import lombok.SneakyThrows;
import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.model.Task;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UpdateTaskServlet extends HttpServlet {
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
        final String title = req.getParameter("title");
        final String description = req.getParameter("description");
        final LocalDate deadline_date = LocalDate.parse(req.getParameter("deadline_date"), formatter);
        final boolean doneCheckbox = req.getParameter("done") != null;

        Task task = new Task();
        task.setId(id);
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline_date(deadline_date);
        task.setDone(doneCheckbox);

        try {
            taskDao.update(task);
            resp.sendRedirect(req.getContextPath() + "/tasksmenu");
        } catch (SQLException e) {
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write("Произошла ошибка, задача не обновлена\n" + e.getMessage());
        }


    }

    @SneakyThrows
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        final String id = req.getParameter("id");

        final Task task = taskDao.findById(Long.parseLong(id)).get();

        req.setAttribute("task", task);

        req.getRequestDispatcher("/WEB-INF/view/updateTaskPage.jsp")
                .forward(req, resp);
    }
}
