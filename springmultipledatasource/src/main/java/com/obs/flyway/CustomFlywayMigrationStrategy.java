package com.obs.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by ongbo on 2/10/2017.
 */
public class CustomFlywayMigrationStrategy implements FlywayMigrationStrategy {
    @Autowired
    private List<DataSource> dataSources;
    @Autowired
    private FlywayConfigByDataSource flywayConfigByDataSource;


    @Override
    public void migrate(Flyway flyway) {
        flywayConfigByDataSource.getDatasources().stream().map(flywayByDataSource -> {
            DataSource ds = dataSources.stream().filter(dataSource -> {
                try {
                    return flywayByDataSource.getUrl().equals(dataSource.getConnection().getMetaData().getURL());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }).findFirst().get();
            flywayByDataSource.setDataSource(ds);
            return flywayByDataSource;
        }).forEach(flywayByDataSource -> {
//                    flywayByDataSource.clean();
            flywayByDataSource.migrate();
        });
    }
}
