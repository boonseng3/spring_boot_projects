package com.obs;

import com.obs.entity.Post;
import com.obs.entity.Role;
import com.obs.entity.User;

import java.util.HashSet;
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
        return (User) new User().setUsername(username).setEmail(email).setEnabled(true).setPassword(password).setRoles(new HashSet<>())
                .setCreatedBy(userId).setUpdatedBy(userId);
    }

    /**
     * Return a user with default values with no roles
     *
     * @return Role object
     */
    public static Role createRole() {
        String name = UUID.randomUUID().toString();
        String description = name + " role";
        Long userId = 1l;
        return (Role) new Role().setName(name).setDescription(description).setCreatedBy(userId).setUpdatedBy(userId);
    }

    /**
     * Sleep for the x milliseconds
     *
     * @param millisec
     */
    public static void sleep(long millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Return a post with default values with no comments
     *
     * @return Post object
     */
    public static Post createPost() {
        String title = UUID.randomUUID().toString();
        String content = UUID.randomUUID().toString();
        Long userId = 1l;
        return (Post) new Post().setTitle(title).setContent(content).setComments(new HashSet<>()).setOrphanComments(new HashSet<>())
                .setCreatedBy(userId).setUpdatedBy(userId);
    }
}
