package com.baidu.oped.apm.statistics.collector.record.reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.QTrace;
import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.common.jpa.repository.TraceRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class TraceItemReader extends BaseReader<Trace> {

    @Autowired
    private TraceRepository traceRepository;

    protected TraceItemReader(long periodStart, long periodInMills) {
        super(periodStart, periodInMills);
    }

    public Iterable<Trace> readItems() {
        QTrace qAgentStatCpuLoad = QTrace.trace;
        BooleanExpression condition = qAgentStatCpuLoad.collectorAcceptTime.between(getPeriodStart(), getPeriodEnd());
        return traceRepository.findAll(condition);
    }
}
