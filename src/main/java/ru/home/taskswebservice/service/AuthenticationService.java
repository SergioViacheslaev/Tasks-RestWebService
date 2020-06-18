package ru.home.taskswebservice.service;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
@Setter
@NoArgsConstructor
@Slf4j
public class AuthenticationService implements Authentication {
    private Map<String, String> usersAuthenticationData = new ConcurrentHashMap<>();

    {
        usersAuthenticationData.put("admin", DigestUtils.md5Hex("admin123"));
        log.info("DB: {}", usersAuthenticationData);
    }


    @Override
    public boolean isUsernamePresent(String userName) {
        return usersAuthenticationData.containsKey(userName);
    }

    @Override
    public boolean isAuthenticated(String username, String inputPassword) {
        if (!isUsernamePresent(username)) return false;

        return usersAuthenticationData.get(username).equals(DigestUtils.md5Hex(inputPassword));
    }
}
