package ru.home.taskswebservice.servlets;


import ru.home.taskswebservice.dao.UserDaoJDBC;
import ru.home.taskswebservice.dao.datasource.DataSourceHikariPostgreSQL;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManager;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManagerJdbc;
import ru.home.taskswebservice.model.Task;
import ru.home.taskswebservice.service.AuthenticationService;
import ru.home.taskswebservice.util.TaskUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;
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

        DataSource dataSource = DataSourceHikariPostgreSQL.getHikariDataSource();
        SessionManager sessionManager = new SessionManagerJdbc(dataSource);
        UserDaoJDBC userDaoJDBC = new UserDaoJDBC(sessionManager);
        authenticationService = new AuthenticationService(userDaoJDBC);


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