package com.baidu.oped.apm.statistics.collector.record.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.ApplicationStatistic;
import com.baidu.oped.apm.common.jpa.entity.InstanceStatistic;
import com.baidu.oped.apm.common.jpa.entity.QInstanceStatistic;
import com.baidu.oped.apm.common.jpa.repository.InstanceStatisticRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 8/31/15.
 */
@Component
public class InstanceStatisticItemWriter extends BaseWriter<InstanceStatistic> {

    @Autowired
    private InstanceStatisticRepository instanceStatisticRepository;

    protected InstanceStatisticItemWriter(long periodStart, long periodInMills) {
        super(periodStart, periodInMills);
    }

    @Override
    protected void writeItem(InstanceStatistic item) {
        QInstanceStatistic qApplicationStatistic = QInstanceStatistic.instanceStatistic;
        BooleanExpression appIdCondition = qApplicationStatistic.instanceId.eq(item.getInstanceId());
        BooleanExpression periodCondition = qApplicationStatistic.period.eq(item.getPeriod());
        BooleanExpression timestampCondition = qApplicationStatistic.timestamp.eq(item.getTimestamp());

        BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
        InstanceStatistic existStatistic = instanceStatisticRepository.findOne(whereCondition);

        if(existStatistic == null){
            instanceStatisticRepository.save(existStatistic);
        } else {
            copyStatisticValue(item, existStatistic);
            instanceStatisticRepository.saveAndFlush(existStatistic);
        }
    }
}
