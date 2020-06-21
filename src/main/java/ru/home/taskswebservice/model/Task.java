package ru.home.taskswebservice.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Задачи
 *
 * @author Sergei Viacheslaev
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "title", "description", "deadline_date", "done"})
public class Task {
    private int id;
    private String title;
    private String description;
    private LocalDate deadline_date;
    private boolean done;
}
