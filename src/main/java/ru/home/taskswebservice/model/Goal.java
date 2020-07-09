package ru.home.taskswebservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

/** Цели
 */
@Data
@NoArgsConstructor
public class Goal {
    private int id;
    private String name;
    private String description;
}
