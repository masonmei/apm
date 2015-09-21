package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.ApiMetaData;
import com.baidu.oped.apm.common.jpa.entity.QAgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.QApiMetaData;
import com.baidu.oped.apm.common.jpa.entity.QSqlMetaData;
import com.baidu.oped.apm.common.jpa.entity.QStringMetaData;
import com.baidu.oped.apm.common.jpa.entity.QTrace;
import com.baidu.oped.apm.common.jpa.entity.SqlMetaData;
import com.baidu.oped.apm.common.jpa.entity.StringMetaData;
import com.baidu.oped.apm.common.jpa.entity.Trace;
import com.baidu.oped.apm.common.jpa.repository.AgentInstanceMapRepository;
import com.baidu.oped.apm.common.jpa.repository.ApiMetaDataRepository;
import com.baidu.oped.apm.common.jpa.repository.ApplicationRepository;
import com.baidu.oped.apm.common.jpa.repository.InstanceRepository;
import com.baidu.oped.apm.common.jpa.repository.SqlMetaDataRepository;
import com.baidu.oped.apm.common.jpa.repository.StringMetaDataRepository;
import com.baidu.oped.apm.common.jpa.repository.TraceRepository;
import com.mysema.query.types.expr.BooleanExpression;

/**
 * Base Service for all operations.
 *
 * @author meidongxu@baidu.com
 */
public abstract class BaseService {

    @Autowired
    protected AgentInstanceMapRepository agentInstanceMapRepository;
    @Autowired
    protected ApiMetaDataRepository apiMetaDataRepository;
    @Autowired
    protected StringMetaDataRepository stringMetaDataRepository;
    @Autowired
    protected SqlMetaDataRepository sqlMetaDataRepository;
    @Autowired
    protected TraceRepository traceRepository;
    @Autowired
    protected ApplicationRepository applicationRepository;
    @Autowired
    protected InstanceRepository instanceRepository;

    protected Trace findTrace(String agentId, long startTimestamp, long spanId) {
        AgentInstanceMap map = findAgentInstanceMap(agentId, startTimestamp);
        QTrace qTrace = QTrace.trace;
        BooleanExpression spanIdCondition = qTrace.spanId.eq(spanId);
        BooleanExpression agentIdCondition = qTrace.agentId.eq(map.getId());

        BooleanExpression whereCondition = spanIdCondition.and(agentIdCondition);

        Trace one = traceRepository.findOne(whereCondition);
        if (one == null) {
            Trace trace = new Trace();
            trace.setAgentId(map.getId());
            trace.setSpanId(spanId);
            try {
                one = traceRepository.save(trace);
            } catch (DataAccessException exception) {
                one = traceRepository.findOne(whereCondition);
            }
        }
        return one;
    }

    protected AgentInstanceMap findAgentInstanceMap(String agentId, long startTimestamp) {
        QAgentInstanceMap qAgentInstanceMap = QAgentInstanceMap.agentInstanceMap;
        BooleanExpression agentIdCondition = qAgentInstanceMap.agentId.eq(agentId);
        BooleanExpression startTimeCondition = qAgentInstanceMap.agentStartTime.eq(startTimestamp);
        BooleanExpression whereCondition = agentIdCondition.and(startTimeCondition);

        AgentInstanceMap one = agentInstanceMapRepository.findOne(whereCondition);
        if (one == null) {
            AgentInstanceMap map = new AgentInstanceMap();
            map.setAgentId(agentId);
            map.setAgentStartTime(startTimestamp);
            try {
                one = agentInstanceMapRepository.saveAndFlush(map);
            } catch (DataAccessException exception) {
                one = agentInstanceMapRepository.findOne(whereCondition);
            }
        }
//        if (one.getAppId() == null || one.getInstanceId() == null) {
//            synchronized(locker) {
//                one = agentInstanceMapRepository.findOne(whereCondition);
//                if (one.getAppId() == null || one.getInstanceId() == null) {
//                    Application app = applicationRepository.saveAndFlush(new Application());
//                    one.setAppId(app.getId());
//
//                    Instance entity = new Instance();
//                    entity.setAppId(app.getId());
//                    entity.setStartTime(startTimestamp);
//                    Instance instance = instanceRepository.saveAndFlush(entity);
//                    one.setInstanceId(instance.getId());
//                    one = agentInstanceMapRepository.saveAndFlush(one);
//                }
//            }
//        }

        return one;
    }

    protected ApiMetaData findApiMetaData(Long agentId, int apiId) {
        QApiMetaData qApiMetaData = QApiMetaData.apiMetaData;
        BooleanExpression agentIdCondition = qApiMetaData.agentId.eq(agentId);
        BooleanExpression apiIdCondition = qApiMetaData.apiId.eq(apiId);
        BooleanExpression whereCondition = agentIdCondition.and(apiIdCondition);
        ApiMetaData one = apiMetaDataRepository.findOne(whereCondition);
        if (one == null) {
            ApiMetaData metaData = new ApiMetaData();
            metaData.setAgentId(agentId);
            metaData.setApiId(apiId);
            try {
                one = apiMetaDataRepository.saveAndFlush(metaData);
            } catch (DataAccessException exception) {
                one = apiMetaDataRepository.findOne(whereCondition);
            }
        }
        return one;
    }

    protected SqlMetaData findSqlMetaData(Long agentId, int sqlId) {
        QSqlMetaData qSqlMetaData = QSqlMetaData.sqlMetaData;
        BooleanExpression agentIdCondition = qSqlMetaData.agentId.eq(agentId);
        BooleanExpression apiIdCondition = qSqlMetaData.sqlId.eq(sqlId);
        BooleanExpression whereCondition = agentIdCondition.and(apiIdCondition);
        SqlMetaData one = sqlMetaDataRepository.findOne(whereCondition);
        if (one == null) {
            SqlMetaData metaData = new SqlMetaData();
            metaData.setAgentId(agentId);
            metaData.setSqlId(sqlId);
            try {
                one = sqlMetaDataRepository.saveAndFlush(metaData);
            } catch (DataAccessException exception) {
                one = sqlMetaDataRepository.findOne(whereCondition);
            }
        }
        return one;
    }

    protected StringMetaData findStringMetaData(Long agentId, int id) {
        QStringMetaData qStringMetaData = QStringMetaData.stringMetaData;
        BooleanExpression agentIdCondition = qStringMetaData.agentId.eq(agentId);
        BooleanExpression apiIdCondition = qStringMetaData.stringId.eq(id);
        BooleanExpression whereCondition = agentIdCondition.and(apiIdCondition);
        StringMetaData one = stringMetaDataRepository.findOne(whereCondition);
        if (one == null) {
            StringMetaData metaData = new StringMetaData();
            metaData.setAgentId(agentId);
            metaData.setStringId(id);
            try {
                one = stringMetaDataRepository.saveAndFlush(metaData);
            } catch (DataAccessException exception) {
                one = stringMetaDataRepository.findOne(whereCondition);
            }
        }
        return one;
    }
}
