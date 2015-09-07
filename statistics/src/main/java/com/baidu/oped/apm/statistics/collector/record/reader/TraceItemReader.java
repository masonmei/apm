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

    public Iterable<Trace> readItems(long periodStart, long periodInMills) {
        QTrace qAgentStatCpuLoad = QTrace.trace;
        BooleanExpression condition =
                qAgentStatCpuLoad.collectorAcceptTime.between(periodStart, periodStart + periodInMills);
        return traceRepository.findAll(condition);
    }
}
