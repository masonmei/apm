package com.baidu.oped.apm.model.service;

import com.baidu.oped.apm.common.jpa.entity.*;
import com.baidu.oped.apm.common.jpa.repository.*;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TrendContext;
import com.baidu.oped.apm.utils.PageUtils;
import com.baidu.oped.apm.utils.TimeUtils;
import com.mysema.query.types.expr.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.baidu.oped.apm.utils.TimeUtils.toMillisSecond;

/**
 * Created by mason on 9/6/15.
 */
@Service
public class AutomaticService {

    @Autowired
    private WebTransactionRepository webTransactionRepository;

    @Autowired
    private WebTransactionStatisticRepository webTransactionStatisticRepository;

    @Autowired
    private SqlTransactionRepository sqlTransactionRepository;

    @Autowired
    private SqlTransactionStatisticRepository sqlTransactionStatisticRepository;

    @Autowired
    private ExternalTransactionRepository externalTransactionRepository;

    @Autowired
    private ExternalTransactionStatisticRepository externalTransactionStatisticRepository;

    @Autowired
    private AgentInstanceMapRepository agentInstanceMapRepository;

    @Autowired
    private TraceRepository traceRepository;

    @Autowired
    private TraceEventRepository traceEventRepository;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private InstanceRepository instanceRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private ApiMetaDataRepository apiMetaDataRepository;

    /**
     * Get the metric data of given app.
     *
     * @param appId        the application id
     * @param timeRanges   a set of from ~ to
     * @param period       in Second
     * @param serviceTypes the service types
     * @return TrendContext with metric data of the application
     */
    public TrendContext getMetricDataOfApp(Long appId, List<TimeRange> timeRanges, Long period,
                                           ServiceType... serviceTypes) {
        TrendContext<ServiceType> trendContext =
                new TrendContext<>(period * 1000, timeRanges.toArray(new TimeRange[timeRanges.size()]));
        Arrays.stream(serviceTypes).forEach(serviceType -> {
            switch (serviceType) {
                case WEB:
                    Map<TimeRange, Iterable<WebTransactionStatistic>> webTransactionMetricDataOfApp =
                            getWebTransactionStatisticOfApp(appId, timeRanges, period);
                    trendContext.addWebTransactionData(serviceType, webTransactionMetricDataOfApp);
                    break;
                case DB:
                    Map<TimeRange, Iterable<SqlTransactionStatistic>> sqlTransactionMetricDataOfApp =
                            getSqlTransactionStatisticOfApp(appId, timeRanges, period);
                    trendContext.addDatabaseServiceData(serviceType, sqlTransactionMetricDataOfApp);
                    break;
                case EXTERNAL:
                    Map<TimeRange, Iterable<ExternalServiceStatistic>> externalServiceMetricDataOfApp =
                            getExternalServiceStatisticOfApp(appId, timeRanges, period);
                    trendContext.addExternalServiceData(serviceType, externalServiceMetricDataOfApp);
                    break;
                case CACHE:
                case BACKGROUND:
                default:
                    throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        return trendContext;
    }

    /**
     * Get the metric data of given instance.
     *
     * @param instanceId   the instance id
     * @param timeRanges   a set of from ~ to
     * @param period       in Second
     * @param serviceTypes the service types
     * @return TrendContext with metric data of the instance
     */
    public TrendContext<ServiceType> getMetricDataOfInstance(Long instanceId, List<TimeRange> timeRanges, Long period,
                                                             ServiceType... serviceTypes) {
        TrendContext<ServiceType> trendContext =
                new TrendContext<>(period * 1000, timeRanges.toArray(new TimeRange[timeRanges.size()]));
        Arrays.stream(serviceTypes).forEach(serviceType -> {
            switch (serviceType) {
                case WEB:
                    Map<TimeRange, Iterable<WebTransactionStatistic>> webTransactionMetricDataOfApp =
                            getWebTransactionStatisticOfInstance(instanceId, timeRanges, period);
                    trendContext.addWebTransactionData(serviceType, webTransactionMetricDataOfApp);
                    break;
                case DB:
                    Map<TimeRange, Iterable<SqlTransactionStatistic>> sqlTransactionMetricDataOfApp =
                            getSqlTransactionStatisticOfInstance(instanceId, timeRanges, period);
                    trendContext.addDatabaseServiceData(serviceType, sqlTransactionMetricDataOfApp);
                    break;
                case EXTERNAL:
                    Map<TimeRange, Iterable<ExternalServiceStatistic>> externalServiceMetricDataOfApp =
                            getExternalServiceStatisticOfInstance(instanceId, timeRanges, period);
                    trendContext.addExternalServiceData(serviceType, externalServiceMetricDataOfApp);
                    break;
                case CACHE:
                case BACKGROUND:
                default:
                    throw new UnsupportedOperationException("Not supported yet.");
            }
        });
        return trendContext;
    }

    //============================== Gorgeous Dividing Line For WebTransactionStatistic ===============================

    /**
     * Return the given WebTransactionStatistic of the given application.
     *
     * @param appId      application id
     * @param timeRanges timeRanges
     * @param period     period in second
     * @return WebTransactionStatistic of application
     */
    public Map<TimeRange, Iterable<WebTransactionStatistic>> getWebTransactionStatisticOfApp(
            Long appId, List<TimeRange> timeRanges, Long period) {

        Iterable<WebTransaction> webTransactions = getWebTransactionsOfApp(appId);
        return getTimeRangeWtsMap(timeRanges, webTransactions, period);
    }

    /**
     * Return the given WebTransactionStatistic of the given instance.
     *
     * @param instanceId instance id
     * @param timeRanges timeRanges
     * @param period     period in Second
     * @return WebTransactionStatistic of instance
     */
    public Map<TimeRange, Iterable<WebTransactionStatistic>> getWebTransactionStatisticOfInstance(
            Long instanceId, List<TimeRange> timeRanges, Long period) {

        Iterable<WebTransaction> webTransactions = getWebTransactionsOfInstance(instanceId);
        return getTimeRangeWtsMap(timeRanges, webTransactions, period);
    }

    /**
     * Get WebTransactions' statistic of the given timeRange
     *
     * @param webTransactions webTransactions
     * @param timeRange       timeRange
     * @param period          periodInSecond
     * @return
     */
    public Iterable<WebTransactionStatistic> getWebTransactionsStatistic(
            Iterable<WebTransaction> webTransactions, TimeRange timeRange, Long period) {
        final Long periodInMillis = period * 1000;

        List<Long> webTransactionIds =
                StreamSupport.stream(webTransactions.spliterator(), false).map(WebTransaction::getId)
                        .collect(Collectors.toList());

        QWebTransactionStatistic qWebTransactionStatistic = QWebTransactionStatistic.webTransactionStatistic;
        BooleanExpression appIdCondition = qWebTransactionStatistic.transactionId.in(webTransactionIds);
        BooleanExpression periodCondition = qWebTransactionStatistic.period.eq(periodInMillis);
        BooleanExpression timestampCondition = qWebTransactionStatistic.timestamp
                .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

        BooleanExpression whereCondition = appIdCondition.and(periodCondition).and(timestampCondition);
        return webTransactionStatisticRepository.findAll(whereCondition);
    }

    private Map<TimeRange, Iterable<WebTransactionStatistic>> getTimeRangeWtsMap(
            List<TimeRange> timeRanges, Iterable<WebTransaction> webTransactions, Long periodInSecond) {

        Map<TimeRange, Iterable<WebTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            Iterable<WebTransactionStatistic> currentRangeResult =
                    getWebTransactionsStatistic(webTransactions, timeRange, periodInSecond);
            result.put(timeRange, currentRangeResult);
        });
        return result;
    }

    //============================== Gorgeous Dividing Line For SqlTransactionStatistic ===============================

    /**
     * Return the given SqlTransactionStatistic of the given app.
     *
     * @param appId      application id
     * @param timeRanges timeRanges
     * @param period     period in second
     * @return SqlTransactionStatistic of the application
     */
    public Map<TimeRange, Iterable<SqlTransactionStatistic>> getSqlTransactionStatisticOfApp(
            Long appId, List<TimeRange> timeRanges, Long period) {

        Iterable<SqlTransaction> sqlTransactions = getSqlTransactionsOfApp(appId);
        return getTimeRangeStsMap(timeRanges, sqlTransactions, period);
    }


    /**
     * Return the given SqlTransactionStatistic of the given instance.
     *
     * @param instanceId instance id
     * @param timeRanges timeRanges
     * @param period     period in second
     * @return SqlTransactionStatistic of the instance
     */
    public Map<TimeRange, Iterable<SqlTransactionStatistic>> getSqlTransactionStatisticOfInstance(
            Long instanceId, List<TimeRange> timeRanges, Long period) {

        Iterable<SqlTransaction> sqlTransactions = getSqlTransactionsOfInstance(instanceId);
        return getTimeRangeStsMap(timeRanges, sqlTransactions, period);
    }

    /**
     * Get DatabaseServices' statistic of the given timeRange in period
     *
     * @param sqlTransactions sqlTransactions
     * @param timeRange       timeRange
     * @param period          periodInSecond
     * @return the DatabaseServices' statistic
     */
    public Iterable<SqlTransactionStatistic> getSqlTransactionStatistic(Iterable<SqlTransaction> sqlTransactions,
                                                                        TimeRange timeRange, Long period) {
        final Long periodInMillis = period * 1000;
        List<Long> sqlTransactionIds =
                StreamSupport.stream(sqlTransactions.spliterator(), false).map(SqlTransaction::getId)
                        .collect(Collectors.toList());
        QSqlTransactionStatistic qSqlTransactionStatistic = QSqlTransactionStatistic.sqlTransactionStatistic;
        BooleanExpression sqlTransactionIdCondition = qSqlTransactionStatistic.sqlTransactionId.in(sqlTransactionIds);
        BooleanExpression periodCondition = qSqlTransactionStatistic.period.eq(periodInMillis);
        BooleanExpression timestampCondition = qSqlTransactionStatistic.timestamp
                .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

        BooleanExpression whereCondition = sqlTransactionIdCondition.and(periodCondition).and(timestampCondition);

        return sqlTransactionStatisticRepository.findAll(whereCondition);
    }

    private Map<TimeRange, Iterable<SqlTransactionStatistic>> getTimeRangeStsMap(
            List<TimeRange> timeRanges, Iterable<SqlTransaction> sqlTransactions, Long periodInSecond) {
        Map<TimeRange, Iterable<SqlTransactionStatistic>> result = new HashMap<>();
        timeRanges.stream().forEach(timeRange -> {
            Iterable<SqlTransactionStatistic> currentRangeResult =
                    getSqlTransactionStatistic(sqlTransactions, timeRange, periodInSecond);
            result.put(timeRange, currentRangeResult);
        });
        return result;
    }

    //============================== Gorgeous Dividing Line For ExternalServiceStatistic ===============================

    /**
     * Return the given ExternalServiceStatistic of the given app.
     *
     * @param appId      application id
     * @param timeRanges timeRanges
     * @param period     period in second
     * @return ExternalServiceStatistic of the application
     */
    public Map<TimeRange, Iterable<ExternalServiceStatistic>> getExternalServiceStatisticOfApp(
            Long appId, List<TimeRange> timeRanges, Long period) {
        Iterable<ExternalService> externalServices = getExternalServicesOfApp(appId);

        return getTimeRangeEssMap(timeRanges, externalServices, period);
    }

    /**
     * Return the given ExternalServiceStatistic of the given instance.
     *
     * @param instanceId instance id
     * @param timeRanges timeRanges
     * @param period     period in second
     * @return ExternalServiceStatistic of the instance
     */
    public Map<TimeRange, Iterable<ExternalServiceStatistic>> getExternalServiceStatisticOfInstance(
            Long instanceId, List<TimeRange> timeRanges, Long period) {
        Iterable<ExternalService> externalServices = getExternalServicesOfInstance(instanceId);

        return getTimeRangeEssMap(timeRanges, externalServices, period);
    }

    /**
     * Get ExternalServices' statistic of the given timeRange in period
     *
     * @param externalServices externalServices
     * @param timeRange        timeRange
     * @param period           periodInSecond
     * @return the externalServices' statistic
     */
    public Iterable<ExternalServiceStatistic> getExternalServiceStatistic(Iterable<ExternalService> externalServices,
                                                                          TimeRange timeRange, Long period) {
        final Long periodInMillis = period * 1000;
        List<Long> externalServiceIds =
                StreamSupport.stream(externalServices.spliterator(), false).map(ExternalService::getId)
                        .collect(Collectors.toList());
        QExternalServiceStatistic qExternalServiceStatistic = QExternalServiceStatistic.externalServiceStatistic;
        BooleanExpression externalServiceIdCondition =
                qExternalServiceStatistic.externalServiceId.in(externalServiceIds);
        BooleanExpression periodCondition = qExternalServiceStatistic.period.eq(periodInMillis);
        BooleanExpression timestampCondition = qExternalServiceStatistic.timestamp
                .between(toMillisSecond(timeRange.getFrom()), toMillisSecond(timeRange.getTo()));

        BooleanExpression whereCondition = externalServiceIdCondition.and(periodCondition).and(timestampCondition);

        return externalTransactionStatisticRepository.findAll(whereCondition);
    }

    private Map<TimeRange, Iterable<ExternalServiceStatistic>> getTimeRangeEssMap(
            List<TimeRange> timeRanges, Iterable<ExternalService> externalServices, Long periodInSecond) {
        Map<TimeRange, Iterable<ExternalServiceStatistic>> result = new HashMap<>();

        timeRanges.stream().forEach(timeRange -> {
            Iterable<ExternalServiceStatistic> currentRangeResult =
                    getExternalServiceStatistic(externalServices, timeRange, periodInSecond);
            result.put(timeRange, currentRangeResult);
        });

        return result;
    }

    /**
     * Get ExternalServices of given application
     *
     * @param appId application id
     * @return ExternalServices of application
     */
    public Iterable<ExternalService> getExternalServicesOfApp(Long appId) {
        QExternalService qExternalService = QExternalService.externalService;
        BooleanExpression appIdCondition = qExternalService.appId.eq(appId);
        return externalTransactionRepository.findAll(appIdCondition);
    }

    /**
     * Get ExternalServices of given instance
     *
     * @param instanceId instance id
     * @return ExternalServices of instance
     */
    public Iterable<ExternalService> getExternalServicesOfInstance(Long instanceId) {
        QExternalService qExternalService = QExternalService.externalService;
        BooleanExpression instanceIdCondition = qExternalService.instanceId.eq(instanceId);
        return externalTransactionRepository.findAll(instanceIdCondition);
    }

    /**
     * Get SqlTransactions of given application
     *
     * @param appId application id
     * @return SqlTransactions of application
     */
    public Iterable<SqlTransaction> getSqlTransactionsOfApp(Long appId) {
        QSqlTransaction qSqlTransaction = QSqlTransaction.sqlTransaction;
        BooleanExpression appIdCondition = qSqlTransaction.appId.eq(appId);
        return sqlTransactionRepository.findAll(appIdCondition);
    }

    /**
     * get SqlTransactions of given instance
     *
     * @param instanceId instance id
     * @return sqlTransaction of instance
     */
    public Iterable<SqlTransaction> getSqlTransactionsOfInstance(Long instanceId) {
        QSqlTransaction qSqlTransaction = QSqlTransaction.sqlTransaction;
        BooleanExpression instanceIdCondition = qSqlTransaction.instanceId.eq(instanceId);
        return sqlTransactionRepository.findAll(instanceIdCondition);
    }

    /**
     * Get WebTransactions of the given application
     *
     * @param appId application id
     * @return transaction of application
     */
    public Iterable<WebTransaction> getWebTransactionsOfApp(Long appId) {
        QWebTransaction qWebTransaction = QWebTransaction.webTransaction;
        BooleanExpression appIdCondition = qWebTransaction.appId.eq(appId);
        return webTransactionRepository.findAll(appIdCondition);
    }

    /**
     * Get WebTransactions of the given instance
     *
     * @param instanceId instance id
     * @return transactions of instance
     */
    public Iterable<WebTransaction> getWebTransactionsOfInstance(Long instanceId) {
        QWebTransaction qWebTransaction = QWebTransaction.webTransaction;
        BooleanExpression instanceIdCondition = qWebTransaction.instanceId.eq(instanceId);
        return webTransactionRepository.findAll(instanceIdCondition);
    }

    /**
     * Get AgentInstanceMap of given application
     *
     * @param appId application id
     * @return AgentInstanceMap of application
     */
    public Iterable<AgentInstanceMap> getAgentInstanceMapOfApp(Long appId) {
        QAgentInstanceMap qAgentInstanceMap = QAgentInstanceMap.agentInstanceMap;
        BooleanExpression appIdCondition = qAgentInstanceMap.appId.eq(appId);
        return agentInstanceMapRepository.findAll(appIdCondition);
    }

    /**
     * Get AgentInstanceMap of given instance
     *
     * @param instanceId instance id
     * @return AgentInstanceMap of instance
     */
    public Iterable<AgentInstanceMap> getAgentInstanceMapOfInstance(Long instanceId) {
        QAgentInstanceMap qAgentInstanceMap = QAgentInstanceMap.agentInstanceMap;
        BooleanExpression instanceIdCondition = qAgentInstanceMap.instanceId.eq(instanceId);
        return agentInstanceMapRepository.findAll(instanceIdCondition);
    }

    /**
     * Get Trace of given application
     *
     * @param appId     application id
     * @param timeRange timeRange
     * @param page      page
     * @param size      page size
     * @param orderBy   orderBy
     * @return Trace of current page
     */
    public Iterable<Trace> getTraceOfApp(
            Long appId, TimeRange timeRange, Integer page, Integer size, String orderBy) {
        Iterable<AgentInstanceMap> agentInstanceMaps = getAgentInstanceMapOfApp(appId);
        List<Long> mapIds =
                StreamSupport.stream(agentInstanceMaps.spliterator(), false)
                        .map(AgentInstanceMap::getId)
                        .collect(Collectors.toList());

        QTrace qTrace = QTrace.trace;
        BooleanExpression agentIdCondition = qTrace.agentId.in(mapIds);
        BooleanExpression timeCondition =
                qTrace.collectorAcceptTime.between(
                        TimeUtils.toMillisSecond(timeRange.getFrom()),
                        TimeUtils.toMillisSecond(timeRange.getTo()));
        Sort sort = PageUtils.toSort(orderBy);
        PageRequest pageRequest = new PageRequest(page, size, sort);
        BooleanExpression whereCondition = agentIdCondition.and(timeCondition);
        return traceRepository.findAll(whereCondition, pageRequest);
    }

    /**
     * Get Trace of given instance
     *
     * @param instanceId instance id
     * @param timeRange  timeRange
     * @param page       page
     * @param size       page size
     * @param orderBy    orderBy
     * @return Trance of instance
     */
    public Iterable<Trace> getTranceOfInstance(
            Long instanceId, TimeRange timeRange, Integer page, Integer size, String orderBy) {
        Iterable<AgentInstanceMap> agentInstanceMaps = getAgentInstanceMapOfInstance(instanceId);
        List<Long> mapIds =
                StreamSupport.stream(agentInstanceMaps.spliterator(), false)
                        .map(AgentInstanceMap::getId)
                        .collect(Collectors.toList());

        QTrace qTrace = QTrace.trace;
        BooleanExpression agentIdCondition = qTrace.agentId.in(mapIds);
        BooleanExpression timeCondition =
                qTrace.collectorAcceptTime.between(
                        TimeUtils.toMillisSecond(timeRange.getFrom()),
                        TimeUtils.toMillisSecond(timeRange.getTo()));
        Sort sort = PageUtils.toSort(orderBy);
        PageRequest pageRequest = new PageRequest(page, size, sort);
        BooleanExpression whereCondition = agentIdCondition.and(timeCondition);
        return traceRepository.findAll(whereCondition, pageRequest);
    }

    /**
     * Get the instance of the given app.
     *
     * @param appId the application database id
     * @return instances of application
     */
    public Iterable<Instance> getApplicationInstances(Long appId) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving instances of.");
        QInstance qInstance = QInstance.instance;
        BooleanExpression appIdCondition = qInstance.appId.eq(appId);
        return instanceRepository.findAll(appIdCondition);
    }

    /**
     * Get Application with id
     *
     * @param appId app database id
     * @return
     */
    public Application getApplication(Long appId) {
        Assert.notNull(appId, "ApplicationId must not be null while retrieving with id.");
        Assert.state(appId > 0, "ApplicationId must be positive");
        return applicationRepository.findOne(appId);
    }

    public Instance getInstance(Long instanceId) {
        Assert.notNull(instanceId, "InstanceId must not be null while retrieving with id");
        Assert.state(instanceId > 0, "InstanceId must be positive");
        return instanceRepository.findOne(instanceId);
    }

    /**
     * Get the AgentInstanceMap with Identity
     *
     * @param id agentInstanceMap id.
     * @return AgentInstanceMap
     */
    public AgentInstanceMap getAgentInstanceMapWithId(Long id) {
        return agentInstanceMapRepository.findOne(id);
    }

//    /**
//     * Build the call stack context of the given traceId
//     *
//     * @param traceId trace id
//     * @return Call stack context.
//     */
//    public CallStackContext getCallStackContext(Long traceId) {
//        final CallStackContext context = new CallStackContext();
//
//        // set root trace
//        Trace trace = traceRepository.findOne(traceId);
//        Assert.notNull(trace, "Invalid traceId for retrieving call stack.");
//        context.setRootTrace(trace);
//
//        // Set Application and Instance
//        AgentInstanceMap agentInstanceMap = getAgentInstanceMapWithId(trace.getAgentId());
//        context.setApplication(getApplication(agentInstanceMap.getAppId()));
//        context.setInstance(getInstance(agentInstanceMap.getInstanceId()));
//
//
//        // Set Traces
//        QTrace qTrace = QTrace.trace;
//        BooleanExpression agentIdCondition = qTrace.traceAgentId.eq(trace.getTraceAgentId());
//        BooleanExpression agentStartTimeCondition = qTrace.traceAgentStartTime.eq(trace.getTraceAgentStartTime());
//        BooleanExpression tranSeqCondition = qTrace.traceTransactionSequence.eq(trace.getTraceTransactionSequence());
//        BooleanExpression whereCondition = agentIdCondition.and(agentStartTimeCondition).and(tranSeqCondition);
//        Iterable<Trace> traces = traceRepository.findAll(whereCondition);
//        context.setTraces(traces);
//
//        // Set TraceEvents
//        QTraceEvent qTraceEvent = QTraceEvent.traceEvent;
//        agentIdCondition = qTraceEvent.traceAgentId.eq(trace.getTraceAgentId());
//        agentStartTimeCondition = qTraceEvent.traceAgentStartTime.eq(trace.getTraceAgentStartTime());
//        tranSeqCondition = qTraceEvent.traceTransactionSequence.eq(trace.getTraceTransactionSequence());
//        whereCondition = agentIdCondition.and(agentStartTimeCondition).and(tranSeqCondition);
//        Iterable<TraceEvent> traceEvents = traceEventRepository.findAll(whereCondition);
//        context.setTraceEvents(traceEvents);
//
//        // Set Annotations
//        List<Long> traceIds = StreamSupport.stream(traces.spliterator(), false)
//                .map(Trace::getId)
//                .collect(Collectors.toList());
//        List<Long> traceEventIds = StreamSupport.stream(traceEvents.spliterator(), false)
//                .map(TraceEvent::getId)
//                .collect(Collectors.toList());
//        QAnnotation qAnnotation = QAnnotation.annotation;
//        BooleanExpression traceEventIdCondition = qAnnotation.traceEventId.in(traceEventIds);
//        BooleanExpression traceIdCondition = qAnnotation.traceId.in(traceIds);
//        whereCondition = traceEventIdCondition.or(traceIdCondition);
//        Iterable<Annotation> annotations = annotationRepository.findAll(whereCondition);
//        context.setAnnotations(annotations);
//
//        // Set ApiMetaDatas
//        Set<Long> apiIds = new HashSet<>();
//        traces.forEach(trace1 -> apiIds.add(trace1.getApiId()));
//        traceEvents.forEach(traceEvent -> apiIds.add(traceEvent.getApiId()));
//        QApiMetaData qApiMetaData = QApiMetaData.apiMetaData;
//        BooleanExpression idCondition = qApiMetaData.id.in(apiIds);
//        Iterable<ApiMetaData> apiMetaDatas = apiMetaDataRepository.findAll(idCondition);
//        context.setApiMetaDatas(apiMetaDatas);
//
//        //TODO：add
//
//        return context;
//    }

}
