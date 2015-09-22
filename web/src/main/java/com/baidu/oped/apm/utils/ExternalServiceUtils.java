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
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.mvc.vo.ExternalServiceVo;
import com.baidu.oped.apm.mvc.vo.TimeRange;

/**
 * Created by mason on 9/8/15.
 */
public abstract class ExternalServiceUtils {

    public static List<ExternalServiceVo> topByAvgResponseTime(Iterable<ExternalServiceStatistic> statistics,
            Iterable<ExternalService> transactions, TimeRange timeRange, Integer limit) {
        Map<Long, ExternalService> rpcTransactionMap = new HashMap<>();

        StreamSupport.stream(transactions.spliterator(), false)
                .forEach(transaction -> rpcTransactionMap.put(transaction.getId(), transaction));

        Map<WebTransactionGroup, List<ExternalServiceStatistic>> groups =
                StreamSupport.stream(statistics.spliterator(), false)
                        .collect(Collectors.groupingBy(new Function<ExternalServiceStatistic, WebTransactionGroup>() {
                            @Override
                            public WebTransactionGroup apply(ExternalServiceStatistic statistic) {
                                Long transactionId = statistic.getExternalServiceId();
                                ExternalService webTransaction = rpcTransactionMap.get(transactionId);
                                WebTransactionGroup group = new WebTransactionGroup();
                                group.setAppId(webTransaction.getAppId());
                                group.setDisplayName(webTransaction.getDestinationId());
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
            DoubleSummaryStatistics errorSummaryStatistics =
                    webTransactionStatistics.stream().filter(statistic -> statistic.getError() != null)
                            .mapToDouble(ExternalServiceStatistic::getError).summaryStatistics();
            LongSummaryStatistics satisfiedSummaryStatistics =
                    webTransactionStatistics.stream().filter(statistic -> statistic.getSatisfied() != null)
                            .mapToLong(ExternalServiceStatistic::getSatisfied).summaryStatistics();
            LongSummaryStatistics toleratedSummaryStatistics =
                    webTransactionStatistics.stream().filter(statistic -> statistic.getTolerated() != null)
                            .mapToLong(ExternalServiceStatistic::getTolerated).summaryStatistics();
            LongSummaryStatistics frustratedSummaryStatistics =
                    webTransactionStatistics.stream().filter(statistic -> statistic.getFrustrated() != null)
                            .mapToLong(ExternalServiceStatistic::getFrustrated).summaryStatistics();

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

    static class WebTransactionGroup {
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

            WebTransactionGroup group = (WebTransactionGroup) o;

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
