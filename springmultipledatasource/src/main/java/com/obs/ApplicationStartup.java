package com.obs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class ApplicationStartup
        implements ApplicationListener<ApplicationReadyEvent> {
    @Autowired
    List<DataSource> dataSources;
    /**
     * This event is executed as late as conceivably possible to indicate that
     * the application is ready to service requests.
     */
    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        System.out.println("Application started");
        dataSources.stream().forEach(dataSource -> {
            System.out.println("dataSource = " + dataSource);
        });
    }
}