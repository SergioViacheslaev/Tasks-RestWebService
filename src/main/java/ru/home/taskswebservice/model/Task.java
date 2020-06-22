package ru.home.taskswebservice.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.home.taskswebservice.util.LocalDateDeserializer;
import ru.home.taskswebservice.util.LocalDateSerializer;

import java.time.LocalDate;

/**
 * Задачи
 *
 * @author Sergei Viacheslaev
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "title", "description", "deadline_date", "executor", "done"})
public class Task {
    private int id;
    private String title;
    private String description;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate deadline_date;
    private boolean done;
    private User executor;
}
