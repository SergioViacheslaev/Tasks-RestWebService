package ru.home.taskswebservice.service;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import ru.home.taskswebservice.dao.UserDaoJDBC;
import ru.home.taskswebservice.model.User;

import java.sql.SQLException;
import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
@Slf4j
public class UserAuthenticationService implements Authentication {
    private UserDaoJDBC userDaoJDBC;

    @Override
    public boolean isAuthenticated(String username, String inputPassword) {
        try {
            final Optional<User> optionalUser = userDaoJDBC.findByEmail(username);
            if (optionalUser.isEmpty()) return false;

            final User user = optionalUser.get();
            log.info("User from DB: {}", user);

            return user.getPassword_hash().equals(DigestUtils.md5Hex(inputPassword));
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

    }
}
