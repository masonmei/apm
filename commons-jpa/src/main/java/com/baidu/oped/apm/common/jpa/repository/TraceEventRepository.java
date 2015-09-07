package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.TraceEvent;

/**
 * Created by mason on 8/23/15.
 */
public interface TraceEventRepository extends JpaRepository<TraceEvent, Long>, QueryDslPredicateExecutor<TraceEvent> {
    @Query("select min(event.collectorAcceptTime) from TraceEvent event")
    Long minTimestamp();
}
