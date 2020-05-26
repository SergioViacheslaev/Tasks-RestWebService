package ru.home.taskswebservice.model;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**Задачи
 *
 * @author Sergei Viacheslaev
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "title", "description"})
public class Task {
    private int id;
    private String title;
    private String description;
}
