package ru.home.taskswebservice.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.home.taskswebservice.model.Task;
import ru.home.taskswebservice.util.TaskUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonTaskServlet extends HttpServlet {
    private Map<Integer, Task> tasks;

    @Override
    public void init() throws ServletException {

        final Object task = getServletContext().getAttribute("tasks");

        if (!(task instanceof ConcurrentHashMap)) {
            throw new IllegalStateException("You're repo does not initialize!");
        } else {
            this.tasks = (ConcurrentHashMap<Integer, Task>) task;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        final String id = req.getParameter("id");

        if (TaskUtils.idIsInvalid(id, tasks)) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        final Task task = tasks.get(Integer.parseInt(id));
        final String jsonTask = new ObjectMapper().writeValueAsString(task);

        resp.setContentType("application/json; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.write(jsonTask);
    }
}
