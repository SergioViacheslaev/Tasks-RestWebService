package ru.home.taskswebservice.service.resthandlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ru.home.taskswebservice.dao.GoalDao;
import ru.home.taskswebservice.dao.TaskDao;
import ru.home.taskswebservice.model.Goal;
import ru.home.taskswebservice.model.Task;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * GET handler
 *
 * @author Sergei Viacheslaev
 */
@Getter
@Setter
@AllArgsConstructor
public class RestApiGetHandlerService implements RestApiHandler {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private TaskDao taskDao;
    private GoalDao goalDao;

    @Override
    public Optional<String> handleRestRequest(String requestPath) throws SQLException, JsonProcessingException {
        if (requestPath.matches("^/tasks/\\d+$")) {
            String taskIdParam = parseID(requestPath);
            long taskId = Long.parseLong(taskIdParam);
            Task task = taskDao.findById(taskId).orElseThrow(SQLException::new);
            final String jsonTask = objectMapper.writeValueAsString(task);
            return Optional.ofNullable(jsonTask);

        } else if (requestPath.matches("^/goals/\\d+$")) {
            String goalIdParam = parseID(requestPath);
            long goalId = Long.parseLong(goalIdParam);
            Goal goal = goalDao.findById(goalId).orElseThrow(SQLException::new);
            final String jsonGoal = objectMapper.writeValueAsString(goal);
            return Optional.ofNullable(jsonGoal);

        } else if (requestPath.matches("^/tasks/$")) {
            final List<Task> allTasks = taskDao.findAll();
            return Optional.ofNullable(objectMapper.writeValueAsString(allTasks));

        } else if (requestPath.matches("^/goals/$")) {
            final List<Goal> goals = goalDao.findAll();
            return Optional.ofNullable(objectMapper.writeValueAsString(goals));
        }

        return Optional.empty();
    }

    @Override
    public long handleRestRequest(String requestPath, HttpServletRequest request) {
        throw new UnsupportedOperationException();
    }

    private String parseID(String requestPath) {
        String[] parts = requestPath.split("/");
        return parts[2];
    }
}
