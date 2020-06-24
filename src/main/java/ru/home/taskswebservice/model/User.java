package ru.home.taskswebservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class User {
    @JsonIgnore
    private int id;
    @JsonIgnore
    private String username;
    @JsonIgnore
    private String password_hash;
    private String name;
    private String surname;
    private String email;
}
