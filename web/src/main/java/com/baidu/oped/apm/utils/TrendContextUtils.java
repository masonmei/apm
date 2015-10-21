package com.baidu.oped.apm.utils;

import static com.baidu.oped.apm.common.utils.NumberUtils.calculateRate;

import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.LongSummaryStatistics;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.baidu.oped.apm.common.jpa.entity.WebTransaction;
import com.baidu.oped.apm.common.jpa.entity.WebTransactionStatistic;
import com.baidu.oped.apm.mvc.vo.TimeRange;
import com.baidu.oped.apm.mvc.vo.TrendContext;

/**
 * Created by mason on 9/10/15.
 */
public abstract class TrendContextUtils {

    /**
     * Build TrendContent with transactions top by average response time.
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
            Iterable<WebTransaction> transactions, Iterable<WebTransactionStatistic> transactionsStatistic) {

        TrendContext<String> trendContext = new TrendContext<>(period * 1000, timeRange);

        Map<Long, WebTransaction> webTransactionMap = StreamSupport.stream(transactions.spliterator(), false)
                .collect(Collectors.toMap(WebTransaction::getId, (t) -> t));
        Map<String, List<WebTransactionStatistic>> transactionStatisticMap =
                StreamSupport.stream(transactionsStatistic.spliterator(), false)
                        .collect(Collectors.groupingBy(statistic -> {
                            Long transactionId = statistic.getTransactionId();
                            WebTransaction transaction = webTransactionMap.get(transactionId);
                            return transaction.getRpc();
                        }));

        transactionStatisticMap.entrySet().stream()
                .sorted(Comparator.comparing(new Function<Map.Entry<String, List<WebTransactionStatistic>>, Double>() {
                    @Override
                    public Double apply(Map.Entry<String, List<WebTransactionStatistic>> entry) {
                        DoubleSummaryStatistics responseSummaryStatistics =
                                entry.getValue().stream().filter(statistic -> statistic.getSumResponseTime() != null)
                                        .mapToDouble(WebTransactionStatistic::getSumResponseTime).summaryStatistics();
                        LongSummaryStatistics pvSummaryStatistics =
                                entry.getValue().stream().filter(statistic -> statistic.getPv() != null)
                                        .mapToLong(WebTransactionStatistic::getPv).summaryStatistics();
                        return calculateRate(responseSummaryStatistics.getSum(), pvSummaryStatistics.getSum());
                    }
                }).reversed()).limit(limit)
                .forEach(entry -> trendContext.addStatistics(entry.getKey(), timeRange, entry.getValue()));
        return trendContext;
    }

}
