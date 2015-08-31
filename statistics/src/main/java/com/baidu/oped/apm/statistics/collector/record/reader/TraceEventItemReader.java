package com.baidu.oped.apm.statistics.collector.record.reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.QTraceEvent;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.baidu.oped.apm.common.jpa.repository.TraceEventRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class TraceEventItemReader extends BaseReader<TraceEvent> {

    @Autowired
    private TraceEventRepository traceEventRepository;

    protected TraceEventItemReader(long periodStart, long periodInMills) {
        super(periodStart, periodInMills);
    }

    public Iterable<TraceEvent> readItems() {
        QTraceEvent qTraceEvent = QTraceEvent.traceEvent;
        BooleanExpression condition = qTraceEvent.collectorAcceptTime.between(getPeriodStart(), getPeriodEnd());
        return traceEventRepository.findAll(condition);
    }
}
