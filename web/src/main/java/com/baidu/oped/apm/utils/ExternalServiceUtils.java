package com.baidu.oped.apm.utils;

import static com.baidu.oped.apm.common.utils.NumberUtils.calculateRate;
import static com.baidu.oped.apm.common.utils.NumberUtils.format;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.baidu.oped.apm.common.jpa.entity.ExternalService;
import com.baidu.oped.apm.common.jpa.entity.ExternalServiceStatistic;
import com.baidu.oped.apm.mvc.vo.ExternalServiceVo;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TrendContext;

/**
 * Created by mason on 9/8/15.
 */
public abstract class ExternalServiceUtils {
    /**
     * Build TrendContent with external services top by average response time.
     *
     * @param period
     * @param limit
     * @param timeRange
     * @param transactions
     * @param transactionsStatistic
     *
     * @return
     */
    public static TrendContext<String> topByAvgResponseTime(Long period, Integer limit, TimeRange timeRange,
            Iterable<ExternalService> transactions, Iterable<ExternalServiceStatistic> transactionsStatistic) {

        TrendContext<String> trendContext = new TrendContext<>(period * 1000, timeRange);

        Map<Long, ExternalService> externalServiceMap = StreamSupport.stream(transactions.spliterator(), false)
                .collect(Collectors.toMap(ExternalService::getId, (t) -> t));
        Map<String, List<ExternalServiceStatistic>> transactionStatisticMap =
                StreamSupport.stream(transactionsStatistic.spliterator(), false)
                        .collect(Collectors.groupingBy(statistic -> {
                            Long serviceId = statistic.getExternalServiceId();
                            ExternalService transaction = externalServiceMap.get(serviceId);
                            return transaction.getUrl();
                        }));

        transactionStatisticMap.entrySet().stream()
                .sorted(Comparator.comparing(new Function<Map.Entry<String, List<ExternalServiceStatistic>>, Double>() {
                    @Override
                    public Double apply(Map.Entry<String, List<ExternalServiceStatistic>> entry) {
                        DoubleSummaryStatistics responseSummaryStatistics =
                                entry.getValue().stream().filter(statistic -> statistic.getSumResponseTime() != null)
                                        .mapToDouble(ExternalServiceStatistic::getSumResponseTime).summaryStatistics();
                        LongSummaryStatistics pvSummaryStatistics =
                                entry.getValue().stream().filter(statistic -> statistic.getPv() != null)
                                        .mapToLong(ExternalServiceStatistic::getPv).summaryStatistics();
                        return calculateRate(responseSummaryStatistics.getSum(), pvSummaryStatistics.getSum());
                    }
                })).limit(limit)
                .forEach(entry -> trendContext.addStatistics(entry.getKey(), timeRange, entry.getValue()));
        return trendContext;
    }

    /**
     * Get the top n external Services of the given services.
     *
     * @param statistics
     * @param externalServices
     * @param timeRange
     * @param limit
     *
     * @return
     */
    public static List<ExternalServiceVo> topByAvgResponseTime(Iterable<ExternalServiceStatistic> statistics,
            Iterable<ExternalService> externalServices, TimeRange timeRange, Integer limit) {
        Map<Long, ExternalService> rpcTransactionMap = new HashMap<>();

        StreamSupport.stream(externalServices.spliterator(), false)
                .forEach(transaction -> rpcTransactionMap.put(transaction.getId(), transaction));

        Map<TransactionGroup, List<ExternalServiceStatistic>> groups =
                StreamSupport.stream(statistics.spliterator(), false)
                        .collect(Collectors.groupingBy(new Function<ExternalServiceStatistic, TransactionGroup>() {
                            @Override
                            public TransactionGroup apply(ExternalServiceStatistic statistic) {
                                Long transactionId = statistic.getExternalServiceId();
                                ExternalService webTransaction = rpcTransactionMap.get(transactionId);
                                TransactionGroup group = new TransactionGroup();
                                group.setAppId(webTransaction.getAppId());
                                group.setDisplayName(webTransaction.getUrl());
                                return group;
                            }
                        }));
        List<ExternalServiceVo> result = new ArrayList<>();
        groups.forEach((group, webTransactionStatistics) -> {

            DoubleSummaryStatistics responseSummaryStatistics =
                    webTransactionStatistics.stream().filter(statistic -> statistic.getSumResponseTime() != null)
                            .mapToDouble(ExternalServiceStatistic::getSumResponseTime).summaryStatistics();
            DoubleSummaryStatistics maxSummaryStatistics =
                    webTransactionStatistics.stream().filter(statistic -> statistic.getMaxResponseTime() != null)
                            .mapToDouble(ExternalServiceStatistic::getMaxResponseTime).summaryStatistics();
            DoubleSummaryStatistics minSummaryStatistics =
                    webTransactionStatistics.stream().filter(statistic -> statistic.getMinResponseTime() != null)
                            .mapToDouble(ExternalServiceStatistic::getMinResponseTime).summaryStatistics();
            LongSummaryStatistics pvSummaryStatistics =
                    webTransactionStatistics.stream().filter(statistic -> statistic.getPv() != null)
                            .mapToLong(ExternalServiceStatistic::getPv).summaryStatistics();

            ExternalServiceVo transaction = new ExternalServiceVo();
            transaction.setAppId(group.getAppId());
            transaction.setDestination(group.getDisplayName());

            transaction.setPv(pvSummaryStatistics.getSum());
            transaction.setCpm(format(
                    calculateRate(pvSummaryStatistics.getSum(), timeRange.getDuration(ChronoUnit.MINUTES))));
            transaction.setMaxResponseTime(format(maxSummaryStatistics.getMax()));
            transaction.setMinResponseTime(format(minSummaryStatistics.getMin()));
            transaction.setResponseTime(
                    format(calculateRate(responseSummaryStatistics.getSum(), pvSummaryStatistics.getSum())));

            result.add(transaction);
        });

        return result.stream().sorted(Comparator.comparing(ExternalServiceVo::getResponseTime)).limit(limit)
                .collect(Collectors.toList());
    }

    static class TransactionGroup {
        private Long appId;
        private Long instanceId;
        private Long transactionId;
        private String displayName;

        public Long getAppId() {
            return appId;
        }

        public void setAppId(Long appId) {
            this.appId = appId;
        }

        public Long getInstanceId() {
            return instanceId;
        }

        public void setInstanceId(Long instanceId) {
            this.instanceId = instanceId;
        }

        public Long getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(Long transactionId) {
            this.transactionId = transactionId;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            TransactionGroup group = (TransactionGroup) o;

            if (appId != null ? !appId.equals(group.appId) : group.appId != null) {
                return false;
            }
            if (instanceId != null ? !instanceId.equals(group.instanceId) : group.instanceId != null) {
                return false;
            }
            if (transactionId != null ? !transactionId.equals(group.transactionId) : group.transactionId != null) {
                return false;
            }
            return !(displayName != null ? !displayName.equals(group.displayName) : group.displayName != null);

        }

        @Override
        public int hashCode() {
            int result = appId != null ? appId.hashCode() : 0;
            result = 31 * result + (instanceId != null ? instanceId.hashCode() : 0);
            result = 31 * result + (transactionId != null ? transactionId.hashCode() : 0);
            result = 31 * result + (displayName != null ? displayName.hashCode() : 0);
            return result;
        }
    }
}
