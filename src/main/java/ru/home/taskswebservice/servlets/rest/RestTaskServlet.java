package ru.home.taskswebservice.servlets.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import ru.home.taskswebservice.dao.TaskDaoJDBC;
import ru.home.taskswebservice.service.resthandlers.RestApiGetHandlerService;
import ru.home.taskswebservice.service.resthandlers.RestApiHandler;

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
    private TaskDaoJDBC taskDao;
    private RestApiHandler restApiGetHandler;

    @Override
    public void init() throws ServletException {
        final Object taskDAO = getServletContext().getAttribute("taskDAO");
        final Object restApiHandlerService = getServletContext().getAttribute("restApiGetHandlerService");
        this.taskDao = (TaskDaoJDBC) taskDAO;
        this.restApiGetHandler = (RestApiGetHandlerService) restApiHandlerService;

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

    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


    }


}
