package ru.home.taskswebservice.servlets;


import ru.home.taskswebservice.model.Task;
import ru.home.taskswebservice.util.TaskUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeleteTaskServlet extends HttpServlet {

    private Map<Integer, Task> tasks;

    @Override
    public void init() throws ServletException {

        final Object tasks = getServletContext().getAttribute("tasks");

        if (!(tasks instanceof ConcurrentHashMap)) {
            throw new IllegalStateException("Your repo does not initialize!");
        } else {
            this.tasks = (ConcurrentHashMap<Integer, Task>) tasks;
        }
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        if (TaskUtils.idIsNumber(req)) {
            tasks.remove(Integer.valueOf(req.getParameter("id")));
        }

        resp.sendRedirect(req.getContextPath() + "/");
    }
}
