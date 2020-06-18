package ru.home.taskswebservice.model;

import lombok.Data;


@Data
public class User {
    private String user_name;
    private String hashed_password;
}
