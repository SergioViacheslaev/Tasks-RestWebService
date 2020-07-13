package ru.home.taskswebservice.servlets;


import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManagerException;
import ru.home.taskswebservice.model.Task;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AddTaskServlet extends HttpServlet {
    private static final String TASK_ADD_ERROR = "Произошла ошибка, задача не добавлена !\n";
    private static final String TASK_EXECUTOR_NOT_FOUND = "Исполнитель с таким username не найден в БД\n";

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private TaskDaoJDBC taskDao;

    @Override
    public void init() {

        final Object taskDAO = getServletContext().getAttribute("taskDAO");
        this.taskDao = (TaskDaoJDBC) taskDAO;

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json; charset=UTF-8");

        final String title = req.getParameter("title");
        final String description = req.getParameter("description");
        final LocalDate deadline_date = LocalDate.parse(req.getParameter("deadline_date"), formatter);
        final boolean doneCheckbox = req.getParameter("done") != null;
        String executorUsername = req.getParameter("username");

        final Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline_date(deadline_date);
        task.setDone(doneCheckbox);

        try {
            taskDao.insertTaskWithoutGoal(task, executorUsername);
            resp.sendRedirect(req.getContextPath() + "/tasksmenu");

        } catch (SQLException | SessionManagerException e) {
            if (e.getMessage().contains("\"user_id\" нарушает ограничение NOT NULL")) {
                resp.setContentType("application/json; charset=UTF-8");
                resp.getWriter().write(TASK_ADD_ERROR + TASK_EXECUTOR_NOT_FOUND);
                return;
            }

            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write(TASK_ADD_ERROR + e.getMessage());
        }
    }

}



