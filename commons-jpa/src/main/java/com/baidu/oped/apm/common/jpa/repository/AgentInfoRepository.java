package com.baidu.oped.apm.common.jpa.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

import com.baidu.oped.apm.common.jpa.entity.AgentInfo;

/**
 * Created by mason on 8/23/15.
 */
public interface AgentInfoRepository extends JpaRepository<AgentInfo, Long>, QueryDslPredicateExecutor<AgentInfo> {
    @Query("select a.applicationName from AgentInfo a")
    List<String> listApplicationNames();
}
