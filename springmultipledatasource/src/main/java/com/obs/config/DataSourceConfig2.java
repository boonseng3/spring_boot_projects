package com.obs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

/**
 * Created by ongbo on 2/8/2017.
 */
@Configuration
@EnableJpaRepositories(
        basePackages = "com.obs.datasource2.repo",
        entityManagerFactoryRef = "entityManagerFactory2",
        transactionManagerRef = "transactionManager2"
)
public class DataSourceConfig2 {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Value("${spring.datasources[1].type}")
    String dataSourceType;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasources[1]")
    public DataSource secondaryDataSource1() {
        try {
            return DataSourceBuilder.create().type((Class<? extends DataSource>) Class.forName(dataSourceType)).build();
        } catch (ClassNotFoundException e) {
            logger.error("Exception creating dataSource", e);
        }
        return DataSourceBuilder.create().build();
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory2(EntityManagerFactoryBuilder builder) {

        LocalContainerEntityManagerFactoryBean factory = builder
                .dataSource(secondaryDataSource1())
                .packages("com.obs.datasource2.entity")
                .persistenceUnit("ds2")
                .build();
        return factory;
    }

    @Bean
    public PlatformTransactionManager transactionManager2(EntityManagerFactoryBuilder builder) {

        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory2(builder).getObject());
        return transactionManager;
    }

    @Bean
    public JdbcTemplate jdbcTemplate2() {
        return new JdbcTemplate(secondaryDataSource1());
    }
}
