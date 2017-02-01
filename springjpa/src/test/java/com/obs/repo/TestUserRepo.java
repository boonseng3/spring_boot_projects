package com.obs.repo;

import com.obs.ApplicationServer;
import com.obs.TestUtil;
import com.obs.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

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

    @Transactional
    @Test
    public void createUserWithoutRole() {
        User user = TestUtil.createUser();
        userRepo.save(user);

        User dbUser = userRepo.findOne(user.getId());
        // Requires transaction due to lazy loading
        assertThat(dbUser).isEqualToComparingFieldByFieldRecursively(user);
        logger.debug("Created object {}", dbUser);
    }

    @Transactional
    @Test
    public void createUserWithRole() {
        User user = TestUtil.createUser();
        user.getRoles().add(roleRepo.findByName("ROLE_ADMIN").get());
        userRepo.save(user);

        User dbUser = userRepo.findOne(user.getId());
        // Requires transaction due to lazy loading
        assertThat(dbUser).isEqualToComparingFieldByFieldRecursively(user);
        logger.debug("Created object {}", dbUser);
    }
}
