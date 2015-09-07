package com.baidu.oped.apm.statistics.collector.record.reader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.InstanceStat;
import com.baidu.oped.apm.common.jpa.entity.QInstanceStat;
import com.baidu.oped.apm.common.jpa.repository.InstanceStatRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class StatItemReader extends BaseReader<InstanceStat> {

    @Autowired
    private InstanceStatRepository instanceStatRepository;

    public Iterable<InstanceStat> readItems(long periodStart, long periodInMills) {
        QInstanceStat qInstanceStat = QInstanceStat.instanceStat;
        BooleanExpression condition = qInstanceStat.timestamp.between(periodStart, periodStart + periodInMills);
        return instanceStatRepository.findAll(condition);
    }
}
