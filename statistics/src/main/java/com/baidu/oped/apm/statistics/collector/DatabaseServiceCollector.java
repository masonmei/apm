package com.baidu.oped.apm.statistics.collector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.SqlTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.baidu.oped.apm.statistics.collector.record.processor.DatabaseServiceProcessor;
import com.baidu.oped.apm.statistics.collector.record.reader.DatabaseServiceItemReader;
import com.baidu.oped.apm.statistics.collector.record.writer.SqlTransactionStatisticItemWriter;

/**
 * Created by mason on 9/2/15.
 */
@Component
public class DatabaseServiceCollector implements BaseCollector {

    @Autowired
    private DatabaseServiceItemReader databaseServiceItemReader;

    @Autowired
    private SqlTransactionStatisticItemWriter sqlTransactionStatisticItemWriter;

    @Autowired
    private DatabaseServiceProcessor databaseServiceProcessor;

    @Override
    public void collect(long periodStart, long periodInMills) {
        Iterable<TraceEvent> traceEvents = databaseServiceItemReader.readItems(periodStart, periodInMills);

        Iterable<SqlTransactionStatistic> sqlTransactionStatistics = databaseServiceProcessor.process(traceEvents);
        sqlTransactionStatisticItemWriter.writeItems(sqlTransactionStatistics, periodStart, periodInMills);
    }

    public void setDatabaseServiceItemReader(DatabaseServiceItemReader databaseServiceItemReader) {
        this.databaseServiceItemReader = databaseServiceItemReader;
    }

    public void setSqlTransactionStatisticItemWriter(SqlTransactionStatisticItemWriter
                                                             sqlTransactionStatisticItemWriter) {
        this.sqlTransactionStatisticItemWriter = sqlTransactionStatisticItemWriter;
    }

    public void setDatabaseServiceProcessor(DatabaseServiceProcessor databaseServiceProcessor) {
        this.databaseServiceProcessor = databaseServiceProcessor;
    }
}
