package com.baidu.oped.apm.model.service;

import com.baidu.oped.apm.common.bo.SpanBo;
import com.baidu.oped.apm.model.dao.TraceDao;
import com.baidu.oped.apm.mvc.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mason on 8/13/15.
 */
@Service
public class TransactionService {
    @Autowired
    private TraceDao traceDao;

    public BusinessTransactions selectTransactions(List<TransactionId> traceIds, String applicationName, Range range,
                                                   String field) {
        Assert.notNull(traceIds, "TraceIds cannot be null while selecting transactions.");
        Assert.hasLength(applicationName, "ApplicationName cannot be null while selecting transactions.");
        Assert.notNull(range, "Range cannot be null while selecting transactions.");

        List<List<SpanBo>> selectedSpansList = traceDao.selectSpans(traceIds);

        BusinessTransactions transactions = new BusinessTransactions();
        selectedSpansList.stream()
                .flatMap(spanBos -> spanBos.stream())
                .filter(spanBo -> applicationName.equals(spanBo.getApplicationId()))
                .forEach(spanBo -> transactions.add(spanBo, field));
        return transactions;
    }

    public DataTrend packageDataTrend(BusinessTransactions businessTransactions, int period) {
        Map<String, BusinessTransaction> transactions = businessTransactions.getTransactions();

        List<Metric> metrics = new ArrayList<Metric>();
        metrics.add(new Metric("平均响应时间", "ms"));
        metrics.add(new Metric("吞吐量", "cpm"));
        metrics.add(new Metric("总影响时间", "ms"));
        metrics.add(new Metric("执行次数", ""));
        List<LegendTrend> legendTrends = new ArrayList<LegendTrend>();

        for (String key : transactions.keySet()) {
            List<Trace> traces = transactions.get(key).getTraces();
            Map<Long, Integer> pvMap = new HashMap<Long, Integer>();
            Map<Long, Long> rtMap = new HashMap<Long, Long>();
            for (Trace trace : traces) {
                long startTime = trace.getStartTime();
                long executionTime = trace.getExecutionTime();
                long periodTs = (startTime / period) * period;
                if (!pvMap.containsKey(periodTs)) {
                    pvMap.put(periodTs, 0);
                }
                if (!rtMap.containsKey(periodTs)) {
                    rtMap.put(periodTs, 0L);
                }
                pvMap.put(periodTs, pvMap.get(periodTs) + 1);
                rtMap.put(periodTs, rtMap.get(periodTs) + executionTime);
            }
            List<DataPoint> dataPoints = new ArrayList<>();
            for (Long ts : pvMap.keySet()) {
                List<Double> items = new ArrayList<>();
                Double allRt = Double.valueOf(rtMap.get(ts));
                Double pv = Double.valueOf(pvMap.get(ts));
                items.add(formatDouble(allRt / pv));
                items.add(formatDouble(pv / period));
                items.add(allRt);
                items.add(pv);
                dataPoints.add(new DataPoint(ts, items));
            }

            LegendTrend legendTrend = new LegendTrend(key, dataPoints);
            legendTrends.add(legendTrend);
        }
        DataTrend dataTrend = new DataTrend(metrics, legendTrends);
        return dataTrend;
    }

    private double formatDouble(double d) {
        return Double.parseDouble(new DecimalFormat("#.00").format(d));
    }

}
