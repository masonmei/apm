package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.Trace;

/**
 * Created by mason on 8/23/15.
 */
public interface TraceRepository extends JpaRepository<Trace, Long>, QueryDslPredicateExecutor<Trace> {
    Trace findOneByAgentIdAndAgentStartTimeAndSpanId(String agentId, long agentStartTime, long spanId);
}