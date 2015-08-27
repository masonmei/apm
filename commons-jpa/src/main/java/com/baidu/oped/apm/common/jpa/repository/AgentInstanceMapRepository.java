package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;

/**
 * Created by mason on 8/27/15.
 */
public interface AgentInstanceMapRepository extends JpaRepository<AgentInstanceMap, Long>,
                                                            QueryDslPredicateExecutor<AgentInstanceMap> {
}
