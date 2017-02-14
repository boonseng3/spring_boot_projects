package com.obs.repo;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;
import com.obs.ApplicationServer;
import com.obs.TestUtil;
import com.obs.datasource1.entity.User;
import com.obs.datasource1.repo.UserRepo;
import com.obs.datasource2.entity.AlternateUser;
import com.obs.datasource2.repo.AlternateUserRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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
    AlternateUserRepo alternateUserRepo;
    @Autowired
    JdbcTemplate jdbcTemplate1;
    @Autowired
    JdbcTemplate jdbcTemplate2;


    @Test
    @Transactional(transactionManager = "transactionManager1")
    public void verifyDataSource1Table() {
        Integer count = jdbcTemplate1.queryForObject("SELECT Count(*) FROM user", Integer.class);
        assertThat(count).isGreaterThanOrEqualTo(0);

        // dataSource2 table should not exist
        assertThatThrownBy(() -> {
            jdbcTemplate1.queryForObject("SELECT Count(*) FROM alternate_user", Integer.class);
        })
                .isInstanceOf(BadSqlGrammarException.class)
                .hasCause(new MySQLSyntaxErrorException("Table 'multiple_ds_1_test.alternate_user' doesn't exist"));
    }

    @Test
    @Transactional(transactionManager = "transactionManager2")
    public void verifyDataSource2Table() {
        Integer count = jdbcTemplate2.queryForObject("SELECT Count(*) FROM alternate_user", Integer.class);
        assertThat(count).isGreaterThanOrEqualTo(0);

        // dataSource1 table should not exist
        assertThatThrownBy(() -> {
            jdbcTemplate2.queryForObject("SELECT Count(*) FROM user", Integer.class);
        })
                .isInstanceOf(BadSqlGrammarException.class)
                .hasCause(new MySQLSyntaxErrorException("Table 'multiple_ds_2_test.user' doesn't exist"));
    }

    @Test
    @Transactional(transactionManager = "transactionManager1")
    public void createUserInDs1() {
        User user = TestUtil.createUser();
        userRepo.save(user);

        User dbUser = userRepo.findByUsername(user.getUsername()).get();
        assertThat(dbUser).isEqualToIgnoringGivenFields(user, "createdDateTime", "updatedDateTime");
        Integer count = jdbcTemplate1.queryForObject("SELECT Count(*) FROM user", Integer.class);
        assertThat(count).isGreaterThanOrEqualTo(1);
    }

    @Test
    @Transactional(transactionManager = "transactionManager2")
    public void createUserInDs2() {
        AlternateUser user = TestUtil.createUser1();
        alternateUserRepo.save(user);

        AlternateUser dbUser = alternateUserRepo.findByUsername(user.getUsername()).get();
        assertThat(dbUser).isEqualToIgnoringGivenFields(user, "createdDateTime", "updatedDateTime");
        Integer count = jdbcTemplate2.queryForObject("SELECT Count(*) FROM alternate_user", Integer.class);
        assertThat(count).isGreaterThanOrEqualTo(1);
    }
}
