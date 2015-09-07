package com.baidu.oped.apm.common.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.InstanceStat;

/**
 * Created by mason on 8/23/15.
 */
public interface InstanceStatRepository extends JpaRepository<InstanceStat, Long>,
                                                        QueryDslPredicateExecutor<InstanceStat> {
    @Query("select min(stat.timestamp) from InstanceStat stat")
    Long minTimestamp();
}
