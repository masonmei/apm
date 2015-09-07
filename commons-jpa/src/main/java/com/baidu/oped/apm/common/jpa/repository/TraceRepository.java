package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.Trace;

/**
 * Created by mason on 8/23/15.
 */
public interface TraceRepository extends JpaRepository<Trace, Long>, QueryDslPredicateExecutor<Trace> {
    @Query("select min(t.collectorAcceptTime) from Trace t")
    Long minTimestamp();
}
