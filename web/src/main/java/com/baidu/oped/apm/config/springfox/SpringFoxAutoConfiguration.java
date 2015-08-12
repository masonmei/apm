/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.oped.apm.config.springfox;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.common.base.Predicates;

import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by mason on 7/8/15.
 */
@Configuration
@ConditionalOnProperty(
        prefix = "cloudwatch.boot.springfox",
        name = "enabled",
        havingValue = "true",
        matchIfMissing = false
)
@EnableSwagger2
public class SpringFoxAutoConfiguration {

    @Bean
    public Docket api() {
        ApiInfo apiInfo = new ApiInfo(
                "Fc Auto Ad API",
                "凤巢广告自动上下线REST API",
                null,
                "meidongxu@baidu.com",
                null,
                null,
                null);
        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("fc-auto-ad")
                .apiInfo(apiInfo)
                .select()
                .paths(Predicates.or(regex("/.*")))
                .build();
    }

}
