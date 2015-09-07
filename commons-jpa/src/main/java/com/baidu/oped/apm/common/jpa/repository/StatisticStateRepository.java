package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.StatisticState;

/**
 * Created by mason on 9/6/15.
 */
public interface StatisticStateRepository extends JpaRepository<StatisticState, Long>,
                                                          QueryDslPredicateExecutor<StatisticState> {
}
