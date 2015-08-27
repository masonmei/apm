package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;

/**
 * Created by mason on 8/23/15.
 */
public interface ApplicationStatisticRepository extends JpaRepository<ApplicationStatistic, Long>,
                                                                 QueryDslPredicateExecutor<ApplicationStatistic> {
}
