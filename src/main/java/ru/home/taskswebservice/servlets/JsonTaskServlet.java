package ru.home.taskswebservice.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.model.Task;

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
    public void init() {

        final Object taskDAO = getServletContext().getAttribute("taskDAO");
        this.taskDao = (TaskDaoJDBC) taskDAO;

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        req.setCharacterEncoding("UTF-8");
        final String id = req.getParameter("id");

        try {
            Task task = taskDao.findById(Long.parseLong(id)).orElseThrow(SQLException::new);
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
