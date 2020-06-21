package ru.home.taskswebservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class User {
    private int id;
    private String username;
    private String password_hash;

}
