package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.baidu.oped.apm.common.jpa.entity.AgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.ApiMetaData;
import com.baidu.oped.apm.common.jpa.entity.Application;
import com.baidu.oped.apm.common.jpa.entity.Instance;
import com.baidu.oped.apm.common.jpa.entity.QAgentInstanceMap;
import com.baidu.oped.apm.common.jpa.entity.QApiMetaData;
import com.baidu.oped.apm.common.jpa.entity.QApplication;
import com.baidu.oped.apm.common.jpa.entity.QInstance;
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

    protected Application findApplication(String appName, String appType, String userId) {
        QApplication qApplication = QApplication.application;
        BooleanExpression appNameCondition = qApplication.appName.eq(appName);
        BooleanExpression appTypeCondition = qApplication.appType.eq(appType);
        BooleanExpression userIdCondition = qApplication.userId.eq(userId);
        BooleanExpression whereCondition = appNameCondition.and(appTypeCondition).and(userIdCondition);
        Application one = applicationRepository.findOne(whereCondition);
        if (one == null) {
            Application application = new Application();
            application.setAppName(appName);
            application.setAppType(appType);
            application.setUserId(userId);
            try {
                one = applicationRepository.save(application);
            } catch (DataAccessException exception) {
                one = applicationRepository.findOne(whereCondition);
            }
        }
        return one;
    }

    protected Instance findInstance(Long appId, String ip, Integer port, int instanceType) {
        QInstance qInstance = QInstance.instance;
        BooleanExpression appIdCondition = qInstance.appId.eq(appId);
        BooleanExpression ipCondition = qInstance.ip.eq(ip);
        BooleanExpression portCondition = qInstance.port.eq(port);
        BooleanExpression instanceTypeCondition = qInstance.instanceType.eq(instanceType);
        BooleanExpression whereCondition =
                appIdCondition.and(ipCondition).and(portCondition).and(instanceTypeCondition);
        Instance one = instanceRepository.findOne(whereCondition);
        if (one == null) {
            Instance instance = new Instance();
            instance.setAppId(appId);
            instance.setIp(ip);
            instance.setPort(port);
            instance.setInstanceType(instanceType);
            try {
                one = instanceRepository.save(instance);
            } catch (DataAccessException exception) {
                one = instanceRepository.findOne(whereCondition);
            }
        }
        return one;
    }

}
