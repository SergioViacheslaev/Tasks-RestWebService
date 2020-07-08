package ru.home.taskswebservice.servlets;


import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.dao.UserDaoJDBC;
import ru.home.taskswebservice.dao.datasource.DataSourceHikariPostgreSQL;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManager;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManagerJdbc;
import ru.home.taskswebservice.service.Authentication;
import ru.home.taskswebservice.service.UserAuthenticationService;
import ru.home.taskswebservice.service.UserRegistration;
import ru.home.taskswebservice.service.UserRegistrationService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;


/**
 * Загружается до создания всех сервлетов.
 */
@WebListener
public class ContextListener implements ServletContextListener {

    private Authentication authenticationService;
    private UserRegistration userRegistrationService;
    private TaskDaoJDBC taskDaoJDBC;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        DataSource dataSource = DataSourceHikariPostgreSQL.getHikariDataSource();
        SessionManager sessionManager = new SessionManagerJdbc(dataSource);
        UserDaoJDBC userDaoJDBC = new UserDaoJDBC(sessionManager);
        this.taskDaoJDBC = new TaskDaoJDBC(sessionManager);
        authenticationService = new UserAuthenticationService(userDaoJDBC);
        userRegistrationService = new UserRegistrationService(userDaoJDBC);

        servletContext.setAttribute("userAuthService", authenticationService);
        servletContext.setAttribute("userRegistrationService", userRegistrationService);
        servletContext.setAttribute("taskDAO", taskDaoJDBC);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}

