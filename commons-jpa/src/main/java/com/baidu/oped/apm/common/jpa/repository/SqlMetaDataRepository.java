package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.SqlMetaData;

/**
 * Created by mason on 8/23/15.
 */
public interface SqlMetaDataRepository extends JpaRepository<SqlMetaData, Long>,
                                                       QueryDslPredicateExecutor<SqlMetaData> {
    SqlMetaData findOneByAgentIdAndHashCodeAndStartTime(String agentId, int hashCode, long startTime);
}
