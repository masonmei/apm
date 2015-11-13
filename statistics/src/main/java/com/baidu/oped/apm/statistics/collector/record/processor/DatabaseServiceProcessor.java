package com.baidu.oped.apm.statistics.collector.record.processor;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.baidu.oped.apm.common.trace.AnnotationKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.bo.IntStringStringValue;
import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.Annotation;
import com.baidu.oped.apm.common.jpa.entity.DatabaseType;
import com.baidu.oped.apm.common.jpa.entity.QAnnotation;
import com.baidu.oped.apm.common.jpa.entity.QSqlMetaData;
import com.baidu.oped.apm.common.jpa.entity.QSqlTransaction;
import com.baidu.oped.apm.common.jpa.entity.SqlMetaData;
import com.baidu.oped.apm.common.jpa.entity.SqlTransaction;
import com.baidu.oped.apm.common.jpa.entity.SqlTransactionStatistic;
import com.baidu.oped.apm.common.jpa.entity.TraceEvent;
import com.baidu.oped.apm.common.jpa.repository.AnnotationRepository;
import com.baidu.oped.apm.common.jpa.repository.SqlMetaDataRepository;
import com.baidu.oped.apm.common.jpa.repository.SqlTransactionRepository;
import com.baidu.oped.apm.statistics.utils.sql.SqlStatement;
import com.baidu.oped.apm.statistics.utils.sql.SqlUtils;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Created by mason on 9/1/15.
 */
@Component
public class DatabaseServiceProcessor extends BaseTraceEventProcessor<SqlTransactionStatistic> {

    @Autowired
    private SqlTransactionRepository sqlTransactionRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private SqlMetaDataRepository sqlMetaDataRepository;

    public void setSqlTransactionRepository(SqlTransactionRepository sqlTransactionRepository) {
        this.sqlTransactionRepository = sqlTransactionRepository;
    }

    public void setAnnotationRepository(AnnotationRepository annotationRepository) {
        this.annotationRepository = annotationRepository;
    }

    public void setSqlMetaDataRepository(SqlMetaDataRepository sqlMetaDataRepository) {
        this.sqlMetaDataRepository = sqlMetaDataRepository;
    }

    @Override
    protected SqlTransactionStatistic newStatisticInstance(EventGroup group) {
        DatabaseServiceEventGroup eventGroup = (DatabaseServiceEventGroup) group;
        QSqlTransaction qWebTransaction = QSqlTransaction.sqlTransaction;
        BooleanExpression appIdCondition = qWebTransaction.appId.eq(eventGroup.getAppId());
        BooleanExpression instanceIdCondition = qWebTransaction.instanceId.eq(eventGroup.getInstanceId());
        BooleanExpression destinationIdCondition = qWebTransaction.endPoint.eq(eventGroup.getEndPoint());
        BooleanExpression sqlCondition = qWebTransaction.sql.eq(((DatabaseServiceEventGroup) group).getSql());
        BooleanExpression whereCondition =
                appIdCondition.and(instanceIdCondition).and(destinationIdCondition).and(sqlCondition);

        SqlTransaction one = sqlTransactionRepository.findOne(whereCondition);
        if (one == null) {
            SqlTransaction sqlTransaction = new SqlTransaction();
            sqlTransaction.setAppId(eventGroup.getAppId());
            sqlTransaction.setInstanceId(eventGroup.getInstanceId());
            sqlTransaction.setEndPoint(eventGroup.getEndPoint());
            sqlTransaction.setSql(eventGroup.getSql());
            sqlTransaction.setDatabaseType(DatabaseType.DATABASE);
            processCategoryInfo(sqlTransaction);

            try {
                one = sqlTransactionRepository.save(sqlTransaction);
            } catch (DataAccessException exception) {
                one = sqlTransactionRepository.findOne(whereCondition);
            }
        }
        SqlTransactionStatistic webTransactionStatistic = new SqlTransactionStatistic();
        webTransactionStatistic.setSqlTransactionId(one.getId());
        return webTransactionStatistic;
    }

    private void processCategoryInfo(final SqlTransaction sqlTransaction) {
        String sql = sqlTransaction.getSql();
        SqlStatement parse = SqlUtils.parse(sql);
        sqlTransaction.setStatementType(parse.getStatementType());
        sqlTransaction.setTableName(parse.getTableNamesInString());
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
                        Annotation annotation = getAnnotations(t);
                        if (annotation != null) {
                            annotation.getByteValue();
                            IntStringStringValue decode =
                                    (IntStringStringValue) transcoder.decode((byte) 21, annotation.getByteValue());
                            SqlMetaData sqlMetaData = getSqlMetaData(t.getAgentId(), decode.getIntValue());
                            group.setSql(sqlMetaData.getSql());
                        }
                        return group;
                    }
                }));
    }

    private SqlMetaData getSqlMetaData(Long agentId, int sqlId) {
        QSqlMetaData qSqlMetaData = QSqlMetaData.sqlMetaData;
        BooleanExpression agentIdCondition = qSqlMetaData.agentId.eq(agentId);
        BooleanExpression sqlIdCondition = qSqlMetaData.sqlId.eq(sqlId);
        BooleanExpression whereCondition = agentIdCondition.and(sqlIdCondition);
        return sqlMetaDataRepository.findOne(whereCondition);
    }

    private Annotation getAnnotations(TraceEvent event) {
        QAnnotation qAnnotation = QAnnotation.annotation;
        BooleanExpression traceEventIdCondition = qAnnotation.traceEventId.eq(event.getId());
        int code = AnnotationKey.SQL_ID.getCode();
        BooleanExpression keyCondition = qAnnotation.key.eq(code);
        BooleanExpression whereCondition = traceEventIdCondition.and(keyCondition);

        return annotationRepository.findOne(whereCondition);
    }

    class DatabaseServiceEventGroup implements EventGroup {
        private Long appId;
        private Long instanceId;
        private String endPoint;
        private String sql;

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

        public String getSql() {
            return sql;
        }

        public void setSql(String sql) {
            this.sql = sql;
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
            if (endPoint != null ? !endPoint.equals(that.endPoint) : that.endPoint != null) {
                return false;
            }
            return !(sql != null ? !sql.equals(that.sql) : that.sql != null);

        }

        @Override
        public int hashCode() {
            int result = appId != null ? appId.hashCode() : 0;
            result = 31 * result + (instanceId != null ? instanceId.hashCode() : 0);
            result = 31 * result + (endPoint != null ? endPoint.hashCode() : 0);
            result = 31 * result + (sql != null ? sql.hashCode() : 0);
            return result;
        }
    }
}
