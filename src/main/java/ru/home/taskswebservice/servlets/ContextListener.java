package ru.home.taskswebservice.servlets;


import ru.home.taskswebservice.model.Task;
import ru.home.taskswebservice.service.AuthenticationService;
import ru.home.taskswebservice.util.TaskUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Загружается до создания всех сервлетов.
 * Поле {@link #tasks} будет доступно для всех сервлетов.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    private Map<Integer, Task> tasks;
    private AtomicInteger idCounter;
    private AuthenticationService authenticationService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        tasks = new ConcurrentHashMap<>();
        idCounter = new AtomicInteger(4);
        authenticationService = new AuthenticationService();


        servletContext.setAttribute("tasks", tasks);
        servletContext.setAttribute("idCounter", idCounter);
        servletContext.setAttribute("authService", authenticationService);

        List<Task> taskList = TaskUtils.generateTasks();
        taskList.forEach(task -> this.tasks.put(task.getId(), task));

    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        //Close resource.
        tasks = null;
    }
}