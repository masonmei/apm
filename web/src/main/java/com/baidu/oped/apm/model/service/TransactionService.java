package com.baidu.oped.apm.model.service;

import com.baidu.oped.apm.model.dao.jdbc.JdbcTraceDao;
import com.baidu.oped.apm.mvc.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Created by mason on 8/13/15.
 */
@Service
public class TransactionService {

    @Autowired
    private JdbcTraceDao traceDao;

    public BusinessTransactions selectTransactions(String applicationName, String agentId, Range range,
                                                   String field) {

        List<com.baidu.oped.apm.common.entity.Trace> selectedSpansList = traceDao.queryTraceList(applicationName, agentId, range.getFrom(), range.getTo());

        BusinessTransactions transactions = new BusinessTransactions();
        selectedSpansList.stream()
                .forEach(trace -> transactions.add(trace, field));


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

            Collections.sort(dataPoints, new Comparator<DataPoint>() {
                @Override
                public int compare(DataPoint o1, DataPoint o2) {
                    if (o1.getTimestamp() < o2.getTimestamp()) {
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
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
