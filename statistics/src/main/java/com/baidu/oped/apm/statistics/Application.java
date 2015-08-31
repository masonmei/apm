package com.baidu.oped.apm.statistics;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by mason on 8/29/15.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = {"com.baidu.oped.apm.common.jpa"})
@EntityScan(basePackages = {"com.baidu.oped.apm.common.jpa"})
public class Application {

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(false).build().run(args);
    }
}
