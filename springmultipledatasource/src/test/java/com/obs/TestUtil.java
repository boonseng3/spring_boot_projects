package com.obs;

import com.obs.datasource1.entity.User;
import com.obs.datasource2.entity.AlternateUser;

import java.util.UUID;

/**
 * Created by ongbo on 1/26/2017.
 */
public class TestUtil {

    /**
     * Return a user with default values with no roles
     *
     * @return User object
     */
    public static User createUser() {
        String username = UUID.randomUUID().toString();
        String email = username + "@obs.com";
        String password = UUID.randomUUID().toString();
        Long userId = 1l;
        return (User) new User().setUsername(username).setEmail(email).setEnabled(true).setPassword(password)
                .setCreatedBy(userId).setUpdatedBy(userId);
    }

    public static AlternateUser createUser1() {
        String username = UUID.randomUUID().toString();
        String email = username + "@obs.com";
        String password = UUID.randomUUID().toString();
        Long userId = 1l;
        return (AlternateUser) new AlternateUser().setUsername(username).setEmail(email).setEnabled(true).setPassword(password)
                .setCreatedBy(userId).setUpdatedBy(userId);
    }
}
