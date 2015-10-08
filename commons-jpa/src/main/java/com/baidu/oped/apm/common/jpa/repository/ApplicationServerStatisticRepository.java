package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.ApplicationServerStatistic;

/**
 * Created by mason on 9/28/15.
 */
public interface ApplicationServerStatisticRepository
        extends JpaRepository<ApplicationServerStatistic, Long>, QueryDslPredicateExecutor<ApplicationServerStatistic> {
}
