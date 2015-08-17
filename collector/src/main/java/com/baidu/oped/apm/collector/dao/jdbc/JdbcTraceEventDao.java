package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.BaseDto;
import com.baidu.oped.apm.JdbcTables;
import com.baidu.oped.apm.common.entity.TraceEvent;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcTraceEventDao extends BaseDto<TraceEvent> {
    @Override
    protected String tableName() {
        return JdbcTables.TRACE_EVENT;
    }
}
