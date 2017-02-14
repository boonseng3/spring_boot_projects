package com.obs.repo;

import com.obs.TestUtil;
import com.obs.entity.User;
import org.hibernate.LazyInitializationException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by ongbo on 2/14/2017.
 */
public class TestNonTransactional {
    @Autowired
    UserRepo userRepo;

    /**
     * Test that the column default values is inserted correctly for created/updated datetime.
     * Notice that there is no @Transactional hence, no transaction and will rely on the
     * database default auto-commit value.
     */
    @Test
    public void testCreatedUpdatedDatetime() {
        User user = TestUtil.createUser();
        userRepo.save(user);

        User dbUser = userRepo.readByUsername(user.getUsername()).get();
        assertThat(dbUser).isEqualToIgnoringGivenFields(user, "createdDateTime", "updatedDateTime");
        // created datetime equal to updated datetime
        assertThat(dbUser.getCreatedDateTime()).isEqualByComparingTo(dbUser.getUpdatedDateTime());

        // ensure the update will differ by at least 1 sec
        TestUtil.sleep(1000);

        dbUser.setEmail(UUID.randomUUID().toString());
        userRepo.save(dbUser);

        dbUser = userRepo.readByUsername(user.getUsername()).get();
        // created datetime less than updated datetime
        assertThat(dbUser.getCreatedDateTime()).isLessThan(dbUser.getUpdatedDateTime());
    }

    /**
     * Test that LazyInitializationException thrown when accessing the role attribute without entity graph.
     * Notice that there is no transactional annotation, hence the transaction for saving has been committed and closed.
     */
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
}
