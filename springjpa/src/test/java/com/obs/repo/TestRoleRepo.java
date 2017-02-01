package com.obs.repo;

import com.obs.ApplicationServer;
import com.obs.TestUtil;
import com.obs.entity.Role;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ongbo on 1/25/2017.
 * TestContext framework will roll back a transaction for each test.
 * Use @Commit if the transaction is to be committed.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.config.name=server-test"}, classes = {ApplicationServer.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class TestRoleRepo {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RoleRepo roleRepo;

    @Test
    public void createRole() {
        Role role = TestUtil.createRole();
        roleRepo.save(role);

        Role dbRole = roleRepo.findOne(role.getId());
        assertThat(dbRole).isEqualToIgnoringGivenFields(role, "createdDateTime", "updatedDateTime");
        assertThat(dbRole.getCreatedDateTime()).isEqualByComparingTo(dbRole.getUpdatedDateTime());
        logger.debug("Created object {}", dbRole);
    }


    @Test
    public void updateRole() {
        Role role = TestUtil.createRole();
        roleRepo.save(role);

        // To ensure that the updated time will be different
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Role updatedRole = roleRepo.findOne(role.getId());
        updatedRole.setName(UUID.randomUUID().toString());
        updatedRole.setDescription(UUID.randomUUID().toString());
        roleRepo.save(updatedRole);

        Role dbRole = roleRepo.findOne(role.getId());
        assertThat(dbRole).isEqualToIgnoringGivenFields(updatedRole, "createdDateTime", "updatedDateTime");
        assertThat(dbRole.getCreatedDateTime()).isLessThan(dbRole.getUpdatedDateTime());
        logger.debug("Created object {}", dbRole);
    }
}
