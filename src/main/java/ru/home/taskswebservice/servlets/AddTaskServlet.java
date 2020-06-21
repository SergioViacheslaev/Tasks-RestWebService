package ru.home.taskswebservice.servlets;


import lombok.SneakyThrows;
import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.model.Task;
import ru.home.taskswebservice.util.TimeUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class AddTaskServlet extends HttpServlet {
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        final String title = req.getParameter("title");
        final String description = req.getParameter("description");
        final LocalDate deadline_date = TimeUtils.convertToLocalDateViaDate(sdf.parse(req.getParameter("deadline_date")));
        final boolean done = Boolean.parseBoolean(req.getParameter("done"));

        final Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setDeadline_date(deadline_date);
        task.setDone(done);

        taskDao.insertRecord(task);
        resp.sendRedirect(req.getContextPath() + "/tasksmenu");
    }


}
