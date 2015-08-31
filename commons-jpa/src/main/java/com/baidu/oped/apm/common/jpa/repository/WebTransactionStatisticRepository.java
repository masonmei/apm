package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;

/**
 * Created by mason on 8/23/15.
 */
public interface WebTransactionStatisticRepository extends JpaRepository<WebTransactionStatistic, Long>,
                                                       QueryDslPredicateExecutor<WebTransactionStatistic> {
}
