package com.baidu.oped.apm.statistics.collector.record.writer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.InstanceServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.QInstanceServerStatistic;
import com.baidu.oped.apm.common.jpa.entity.StatisticType;
import com.baidu.oped.apm.common.jpa.repository.InstanceServerStatisticRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 9/28/15.
 */
@Component
public class InstanceServerStatisticItemWriter extends BaseWriter<InstanceServerStatistic> {

    @Autowired
    private InstanceServerStatisticRepository instanceServerStatisticRepository;

    public void setInstanceServerStatisticRepository(
            InstanceServerStatisticRepository instanceServerStatisticRepository) {
        this.instanceServerStatisticRepository = instanceServerStatisticRepository;
    }

    @Override
    protected void writeItem(InstanceServerStatistic item) {
        QInstanceServerStatistic qInstanceServerStatistic = QInstanceServerStatistic.instanceServerStatistic;
        BooleanExpression instanceIdCondition = qInstanceServerStatistic.instanceId.eq(item.getInstanceId());
        BooleanExpression periodCondition = qInstanceServerStatistic.period.eq(item.getPeriod());
        BooleanExpression timestampCondition = qInstanceServerStatistic.timestamp.eq(item.getTimestamp());

        BooleanExpression whereCondition = instanceIdCondition.and(periodCondition).and(timestampCondition);
        InstanceServerStatistic existStatistic = instanceServerStatisticRepository.findOne(whereCondition);

        if (existStatistic == null) {
            instanceServerStatisticRepository.save(item);
        } else {
            copyStatisticValue(item, existStatistic);
            instanceServerStatisticRepository.saveAndFlush(existStatistic);
        }
    }

    @Override
    protected StatisticType getStatisticType() {
        return StatisticType.INSTANCE_STAT;
    }
}
