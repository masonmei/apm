package com.baidu.oped.apm.statistics.collector.record.reader;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.baidu.oped.apm.common.jpa.entity.QTraceEvent;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.baidu.oped.apm.common.jpa.repository.TraceEventRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
public abstract class TraceEventItemReader extends BaseReader<TraceEvent> {

    @Autowired
    private TraceEventRepository traceEventRepository;

    @Override
    public Iterable<TraceEvent> readItems(long periodStart, long periodInMills) {
        QTraceEvent qTraceEvent = QTraceEvent.traceEvent;
        BooleanExpression acceptTimeCondition =
                qTraceEvent.collectorAcceptTime.between(periodStart, periodStart + periodInMills);
        BooleanExpression serviceTypeCondition = qTraceEvent.serviceType.in(serviceTypes());
        BooleanExpression whereCondition = acceptTimeCondition.and(serviceTypeCondition);
        return traceEventRepository.findAll(whereCondition);
    }

    protected abstract List<Integer> serviceTypes();

}
