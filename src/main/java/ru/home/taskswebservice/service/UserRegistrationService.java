package ru.home.taskswebservice.service;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import ru.home.taskswebservice.dao.UserDaoJDBC;
import ru.home.taskswebservice.model.User;
import ru.home.taskswebservice.service.exceptions.EmailAlreadyRegisteredException;
import ru.home.taskswebservice.service.exceptions.UserAlreadyRegisteredException;

import java.sql.SQLException;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class UserRegistrationService implements UserRegistration {
    private final String USER_ALREADY_REGISTERED_MSG = "Пользователь с таким username уже зарегистрирован !";
    private final String EMAIL_ALREADY_REGISTERED_MSG = "Пользователь с таким email уже зарегистрирован !";
    private UserDaoJDBC userDaoJDBC;


    @Override
    public int registerUser(String username, String password, String name, String surname, String email)
            throws SQLException, UserAlreadyRegisteredException, EmailAlreadyRegisteredException {

        final Optional<User> foundUserByUsername = userDaoJDBC.findByUsername(username);
        if (foundUserByUsername.isPresent()) {
            throw new UserAlreadyRegisteredException(USER_ALREADY_REGISTERED_MSG);
        }

        final Optional<User> foundUserByEmail = userDaoJDBC.findByEmail(email);
        if (foundUserByEmail.isPresent()) {
            throw new EmailAlreadyRegisteredException(EMAIL_ALREADY_REGISTERED_MSG);
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword_hash(DigestUtils.md5Hex(password));
        user.setName(name);
        user.setSurname(surname);
        user.setEmail(email);

        return userDaoJDBC.insertUser(user);
    }
}
