package com.obs.repo;

import com.obs.ApplicationServer;
import com.obs.TestUtil;
import com.obs.entity.User;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by ongbo on 1/25/2017.
 * TestContext framework will roll back a transaction for each test.
 * Use @Commit if the transaction is to be committed.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.config.name=server-test"}, classes = {ApplicationServer.class})
public class TestUserRepo {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    UserRepo userRepo;
    @Autowired
    RoleRepo roleRepo;

    @Test
    public void createUserWithoutRoleWithoutEntityGraph() {
        User user = TestUtil.createUser();
        userRepo.save(user);

        User dbUser = userRepo.findOne(user.getId());
        assertThatThrownBy(() -> {
            assertThat(dbUser).isEqualToIgnoringGivenFields(user, "createdDateTime", "updatedDateTime");
        }).isInstanceOf(LazyInitializationException.class)
                .hasMessage("failed to lazily initialize a collection of role: com.obs.entity.User.roles, could not initialize proxy - no Session");
    }


    @Test
    public void createUserWithoutRole() {
        User user = TestUtil.createUser();
        userRepo.save(user);

        User dbUser = userRepo.readByUsername(user.getUsername()).get();
        assertThat(dbUser).isEqualToIgnoringGivenFields(user, "createdDateTime", "updatedDateTime");
        assertThat(dbUser.getCreatedDateTime()).isEqualByComparingTo(dbUser.getUpdatedDateTime());
    }


    @Test
    public void createUser() {
        User user = TestUtil.createUser();
        user.getRoles().add(roleRepo.findByName("ROLE_ADMIN").get());
        userRepo.save(user);

        User dbUser = userRepo.readByUsername(user.getUsername()).get();
        assertThat(dbUser).isEqualToIgnoringGivenFields(user, "createdDateTime", "updatedDateTime");
        assertThat(dbUser.getCreatedDateTime()).isEqualByComparingTo(dbUser.getUpdatedDateTime());
        logger.debug("Created object {}", dbUser);
    }


    @Test
    public void updateUser() {
        User user = TestUtil.createUser();
        user.getRoles().add(roleRepo.findByName("ROLE_ADMIN").get());
        userRepo.save(user);

        // To ensure that the updated time will be different
        TestUtil.sleep(1000);

        User updateUser = userRepo.readByUsername(user.getUsername()).get();
        updateUser.setUsername(UUID.randomUUID().toString());
        userRepo.save(updateUser);

        User dbUser = userRepo.readByUsername(updateUser.getUsername()).get();
        assertThat(dbUser).isEqualToIgnoringGivenFields(updateUser, "updatedDateTime");
        assertThat(dbUser.getCreatedDateTime()).isLessThan(dbUser.getUpdatedDateTime());
        logger.debug("Created object {}", dbUser);
    }


    @Test
    public void pageableUsers() {
        for (int i = 1; i <= 10; i++) {
            User user = TestUtil.createUser();
            user.getRoles().add(roleRepo.findByName("ROLE_ADMIN").get());
            userRepo.save(user);
        }
        int pageSize = 5;

        // verify the id is in sequence
        userRepo.findAll(new PageRequest(0, pageSize)).getContent().stream()
                .map(user -> user.getId())
                .reduce((prevIndex, currentIndex) -> {
                    assertThat(prevIndex).isEqualTo(currentIndex - 1l);
                    return currentIndex;
                });
        // page 1
        User user = userRepo.findAll(new PageRequest(1, pageSize)).getContent().stream().findFirst().get();
        assertThat(user).hasFieldOrPropertyWithValue("id", 6l);

        // next page is 2
        Page<User> page2 = userRepo.findAll(new PageRequest(1, pageSize));
        assertThat(page2.nextPageable().getPageNumber()).isEqualTo(2);

        // get page 2
        PageRequest pageRequest = new PageRequest(page2.nextPageable().getPageNumber(), pageSize);
        user = userRepo.findAll(pageRequest).getContent().stream().findFirst().get();
        assertThat(user).hasFieldOrPropertyWithValue("id", Integer.valueOf(page2.nextPageable().getPageNumber() * pageSize + 1).longValue());
    }

    @Test
    public void pageableSortUsers() {
        for (int i = 1; i <= 10; i++) {
            User user = TestUtil.createUser();
            user.getRoles().add(roleRepo.findByName("ROLE_ADMIN").get());
            userRepo.save(user);
        }
        int pageSize = 5;

        // verify the id is in sequence
        userRepo.findAll(new PageRequest(0, pageSize, Sort.Direction.ASC, "username")).getContent().stream()
                .reduce((prevIndex, currentIndex) -> {
                    assertThat(prevIndex.getUsername().compareTo(currentIndex.getUsername()) < 0).isTrue();
                    logger.debug("user [id: {}, username: {}]", prevIndex.getId(), prevIndex.getUsername());
                    return currentIndex;
                });
    }
}
