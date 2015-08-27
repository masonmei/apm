package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.ExternalTransaction;

/**
 * Created by mason on 8/23/15.
 */
public interface ExternalTransactionRepository extends JpaRepository<ExternalTransaction, Long>,
                                                               QueryDslPredicateExecutor<ExternalTransaction> {
}
