package com.baidu.oped.apm.collector.dao.jdbc;

import com.baidu.oped.apm.BaseRepository;
import com.baidu.oped.apm.common.annotation.JdbcTables;
import com.baidu.oped.apm.common.entity.TraceEvent;
import org.springframework.stereotype.Repository;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcTraceEventDao extends BaseRepository<TraceEvent> {
    public JdbcTraceEventDao() {
        super(TraceEvent.class, JdbcTables.TRACE_EVENT);
    }

//    @Override
    protected String tableName() {
        return JdbcTables.TRACE_EVENT;
    }
}
