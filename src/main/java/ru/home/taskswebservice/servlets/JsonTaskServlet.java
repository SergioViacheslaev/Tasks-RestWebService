package ru.home.taskswebservice.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.model.Task;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

public class JsonTaskServlet extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();
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

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        final String id = req.getParameter("id");

        try {
            Task task = taskDao.findById(Long.parseLong(id)).orElseThrow(SQLException::new);
            System.out.println(task.getDeadline_date());
            final String jsonTask = objectMapper.writeValueAsString(task);
            resp.setContentType("application/json; charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.write(jsonTask);

        } catch (SQLException e) {
            e.printStackTrace();
            resp.setContentType("text/HTML; charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.write("Не найдено задачи с таким ID");
        }


    }
}
