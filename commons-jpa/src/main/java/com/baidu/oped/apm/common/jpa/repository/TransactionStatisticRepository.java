package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.Transaction;
import com.baidu.oped.apm.common.jpa.entity.TransactionStatistic;

/**
 * Created by mason on 8/23/15.
 */
public interface TransactionStatisticRepository extends JpaRepository<TransactionStatistic, Long>,
                                                       QueryDslPredicateExecutor<TransactionStatistic> {
}
