package ru.home.taskswebservice.servlets;


import lombok.SneakyThrows;
import ru.home.taskswebservice.dao.TaskDaoJDBC;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteTaskServlet extends HttpServlet {

    private TaskDaoJDBC taskDao;

    @Override
    public void init() {

        final Object taskDAO = getServletContext().getAttribute("taskDAO");
        this.taskDao = (TaskDaoJDBC) taskDAO;

    }

    @SneakyThrows
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {

        req.setCharacterEncoding("UTF-8");
        long task_id = Long.parseLong(req.getParameter("id"));

        taskDao.deleteTaskById(task_id);
        resp.sendRedirect(req.getContextPath() + "/tasksmenu");
    }
}
