package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.QSqlTransaction;
import com.baidu.oped.apm.common.jpa.entity.SqlTransaction;
import com.baidu.oped.apm.common.jpa.entity.SqlTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.baidu.oped.apm.common.jpa.repository.SqlTransactionRepository;
import com.baidu.oped.apm.statistics.collector.ApdexDecider;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 9/1/15.
 */
@Component
public class DatabaseServiceProcessor extends BaseTraceEventProcessor<SqlTransactionStatistic> {
    @Autowired
    private SqlTransactionRepository sqlTransactionRepository;

    public void setSqlTransactionRepository(SqlTransactionRepository sqlTransactionRepository) {
        this.sqlTransactionRepository = sqlTransactionRepository;
    }

    @Override
    protected SqlTransactionStatistic newStatisticInstance(EventGroup group) {
        DatabaseServiceEventGroup eventGroup = (DatabaseServiceEventGroup) group;
        QSqlTransaction qWebTransaction = QSqlTransaction.sqlTransaction;
        BooleanExpression appIdCondition = qWebTransaction.appId.eq(eventGroup.getAppId());
        BooleanExpression instanceIdCondition = qWebTransaction.instanceId.eq(eventGroup.getInstanceId());
        BooleanExpression destinationIdCondition = qWebTransaction.endPoint.eq(eventGroup.getEndPoint());
        BooleanExpression whereCondition = appIdCondition.and(instanceIdCondition).and(destinationIdCondition);

        SqlTransaction one = sqlTransactionRepository.findOne(whereCondition);
        if (one == null) {
            SqlTransaction webTransaction = new SqlTransaction();
            webTransaction.setAppId(eventGroup.getAppId());
            webTransaction.setInstanceId(eventGroup.getInstanceId());
            webTransaction.setEndPoint(eventGroup.getEndPoint());

            try {
                one = sqlTransactionRepository.save(webTransaction);
            } catch (DataAccessException exception) {
                one = sqlTransactionRepository.findOne(whereCondition);
            }
        }
        SqlTransactionStatistic webTransactionStatistic = new SqlTransactionStatistic();
        webTransactionStatistic.setSqlTransactionId(one.getId());
        return webTransactionStatistic;
    }

    @Override
    protected Map<EventGroup, List<TraceEvent>> groupEvents(Iterable<TraceEvent> items) {
        final Map<Long, AgentInstanceMap> maps = getAgentInstanceMaps(items);
        return StreamSupport.stream(items.spliterator(), false)
                       .collect(Collectors.groupingBy(new Function<TraceEvent, DatabaseServiceEventGroup>() {
                           @Override
                           public DatabaseServiceEventGroup apply(TraceEvent t) {
                               DatabaseServiceEventGroup group = new DatabaseServiceEventGroup();
                               AgentInstanceMap map = maps.get(t.getAgentId());
                               group.setAppId(map.getAppId());
                               group.setInstanceId(map.getInstanceId());
                               group.setEndPoint(t.getEndPoint());
                               return group;
                           }
                       }));
    }

    class DatabaseServiceEventGroup implements EventGroup{
        private Long appId;
        private Long instanceId;
        private String endPoint;

        @Override
        public Long getAppId() {
            return appId;
        }

        public void setAppId(Long appId) {
            this.appId = appId;
        }

        @Override
        public Long getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(Long instanceId) {
            this.instanceId = instanceId;
        }

        public String getEndPoint() {
            return endPoint;
        }

        public void setEndPoint(String endPoint) {
            this.endPoint = endPoint;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            DatabaseServiceEventGroup that = (DatabaseServiceEventGroup) o;

            if (appId != null ? !appId.equals(that.appId) : that.appId != null) {
                return false;
            }
            if (instanceId != null ? !instanceId.equals(that.instanceId) : that.instanceId != null) {
                return false;
            }
            return !(endPoint != null ? !endPoint.equals(that.endPoint) : that.endPoint != null);

        }

        @Override
        public int hashCode() {
            int result = appId != null ? appId.hashCode() : 0;
            result = 31 * result + (instanceId != null ? instanceId.hashCode() : 0);
            result = 31 * result + (endPoint != null ? endPoint.hashCode() : 0);
            return result;
        }
    }
}
