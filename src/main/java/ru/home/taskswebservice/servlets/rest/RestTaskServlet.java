package ru.home.taskswebservice.servlets.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.service.resthandlers.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * @author Sergei Viacheslaev
 */
@Slf4j
@WebServlet(urlPatterns = "/rest/*")
public class RestTaskServlet extends HttpServlet {
    private static final String TASK_ADD_ERROR = "Произошла ошибка, задача не добавлена !\n";
    private static final String TASK_EXECUTOR_NOT_FOUND = "Исполнитель с таким username не найден в БД\n";
    private static final String TASK_UPDATE_ERROR = "Произошла ошибка, задача не обновлена\n";
    private static final String TASK_CREATED_SUCCESS_JSON = "{ \"task_id\" : \"%d\" }";
    private TaskDaoJDBC taskDao;
    private RestApiHandler restApiGetHandler;
    private RestApiHandler restApiPostHandler;
    private RestApiHandler restApiPutHandler;
    private RestApiHandler restApiDeleteHandler;

    @Override
    public void init() throws ServletException {
        final Object taskDAO = getServletContext().getAttribute("taskDAO");
        final Object restApiGetHandlerService = getServletContext().getAttribute("restApiGetHandlerService");
        final Object restApiPostHandlerService = getServletContext().getAttribute("restApiPostHandlerService");
        final Object restApiPutHandlerService = getServletContext().getAttribute("restApiPutHandlerService");
        final Object restApiDeleteHandlerService = getServletContext().getAttribute("restApiDeleteHandlerService");

        this.taskDao = (TaskDaoJDBC) taskDAO;
        this.restApiGetHandler = (RestApiGetHandlerService) restApiGetHandlerService;
        this.restApiPostHandler = (RestApiPostHandlerService) restApiPostHandlerService;
        this.restApiPutHandler = (RestApiPutHandlerService) restApiPutHandlerService;
        this.restApiDeleteHandler = (RestApiDeleteHandlerService) restApiDeleteHandlerService;
    }


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Пришел запрос {} на URI: {}", req.getMethod(), req.getRequestURI());
        String pathInfo = req.getPathInfo();
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/HTML; charset=UTF-8");

        try {
            String user_response = restApiGetHandler.handleRestRequest(pathInfo).orElseThrow(SQLException::new);
            resp.setContentType("application/json; charset=UTF-8");
            resp.setStatus(200);
            PrintWriter out = resp.getWriter();
            out.write(user_response);

        } catch (SQLException e) {
            e.printStackTrace();
            PrintWriter out = resp.getWriter();
            out.write("Не найдено задачи с таким ID");
            resp.setStatus(404);

        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Пришел запрос {} на URI: {}", req.getMethod(), req.getRequestURI());
        String pathInfo = req.getPathInfo();
        req.setCharacterEncoding("UTF-8");


        try {
            long generated_id = restApiPostHandler.handleRestRequest(pathInfo, req);
            resp.setContentType("application/json; charset=UTF-8");
            resp.getWriter().write(String.format(TASK_CREATED_SUCCESS_JSON,generated_id));
            resp.setStatus(201);
        } catch (SQLException e) {
            e.printStackTrace();
            if (e.getMessage().contains("\"user_id\" нарушает ограничение NOT NULL")) {
                resp.setContentType("application/json; charset=UTF-8");
                resp.getWriter().write(TASK_ADD_ERROR + TASK_EXECUTOR_NOT_FOUND);
                resp.setStatus(404);
                return;
            }

            resp.setContentType("text/HTML; charset=UTF-8");
            resp.getWriter().write(TASK_ADD_ERROR + e.getMessage());
            resp.setStatus(400);
        }
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.info("Пришел запрос {} на URI: {}", req.getMethod(), req.getRequestURI());
        String pathInfo = req.getPathInfo();
        req.setCharacterEncoding("UTF-8");
        resp.setContentType("text/HTML; charset=UTF-8");

        try {
            restApiPutHandler.handleRestRequest(pathInfo, req);
            resp.setStatus(200);
        } catch (SQLException e) {
            resp.setStatus(400);
            resp.getWriter().write(TASK_UPDATE_ERROR + e.getMessage());
        }
    }


}
