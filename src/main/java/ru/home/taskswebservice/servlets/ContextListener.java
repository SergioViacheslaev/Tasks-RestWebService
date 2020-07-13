package ru.home.taskswebservice.servlets;


import ru.home.taskswebservice.dao.*;
import ru.home.taskswebservice.dao.datasource.DataSourceHikariPostgreSQL;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManager;
import ru.home.taskswebservice.dao.jdbc.sessionmanager.SessionManagerJdbc;
import ru.home.taskswebservice.service.Authentication;
import ru.home.taskswebservice.service.UserAuthenticationService;
import ru.home.taskswebservice.service.UserRegistration;
import ru.home.taskswebservice.service.UserRegistrationService;
import ru.home.taskswebservice.service.resthandlers.*;

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
    private TaskDao taskDaoJDBC;
    private GoalDao goalDaoJDBC;
    private RestApiHandler restApiGetHandlerService;
    private RestApiHandler restApiPostHandlerService;
    private RestApiHandler restApiPutHandlerService;
    private RestApiHandler restApiDeleteHandlerService;

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {

        final ServletContext servletContext =
                servletContextEvent.getServletContext();

        DataSource dataSource = DataSourceHikariPostgreSQL.getHikariDataSource();
        SessionManager sessionManager = new SessionManagerJdbc(dataSource);
        UserDaoJDBC userDaoJDBC = new UserDaoJDBC(sessionManager);
        this.taskDaoJDBC = new TaskDaoJDBC(sessionManager);
        this.goalDaoJDBC = new GoalDaoJDBC(sessionManager);
        authenticationService = new UserAuthenticationService(userDaoJDBC);
        userRegistrationService = new UserRegistrationService(userDaoJDBC);

        restApiGetHandlerService = new RestApiGetHandlerService(taskDaoJDBC, goalDaoJDBC);
        restApiPostHandlerService = new RestApiPostHandlerService(taskDaoJDBC, goalDaoJDBC);
        restApiPutHandlerService = new RestApiPutHandlerService(taskDaoJDBC, goalDaoJDBC);
        restApiDeleteHandlerService = new RestApiDeleteHandlerService(taskDaoJDBC, goalDaoJDBC);

        servletContext.setAttribute("userAuthService", authenticationService);
        servletContext.setAttribute("userRegistrationService", userRegistrationService);
        servletContext.setAttribute("taskDAO", taskDaoJDBC);
        servletContext.setAttribute("restApiGetHandlerService", restApiGetHandlerService);
        servletContext.setAttribute("restApiPostHandlerService", restApiPostHandlerService);
        servletContext.setAttribute("restApiPutHandlerService", restApiPutHandlerService);
        servletContext.setAttribute("restApiDeleteHandlerService", restApiDeleteHandlerService);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}

