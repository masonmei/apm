
package com.baidu.oped.apm.mvc.vo;

import com.baidu.oped.apm.common.util.TransactionIdUtils;
import com.baidu.oped.apm.common.utils.ApdexUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * class BusinessTransaction 
 *
 * @author meidongxu@baidu.com
 */
public class BusinessTransaction {
    private final List<Trace> traces = new ArrayList<Trace>();
    private final String rpc;
    private final double apdex_t = 1;

    private int calls = 0;
    private int error = 0;
    private long totalTime = 0;
    private long maxTime = 0;
    private long minTime = 0;
    private int satisfiedCount = 0;
    private int toleratedCount = 0;
    private int frustratedCount = 0;

    @Override
    public String toString() {
        return "BusinessTransaction{" +
                "frustratedCount=" + frustratedCount +
                ", traces=" + traces +
                ", rpc='" + rpc + '\'' +
                ", apdex_t=" + apdex_t +
                ", calls=" + calls +
                ", error=" + error +
                ", totalTime=" + totalTime +
                ", maxTime=" + maxTime +
                ", minTime=" + minTime +
                ", satisfiedCount=" + satisfiedCount +
                ", toleratedCount=" + toleratedCount +
                '}';
    }


    public BusinessTransaction(com.baidu.oped.apm.common.jpa.entity.Trace span) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }

        this.rpc = span.getRpc();

        long elapsed = span.getElapsed();
        totalTime = maxTime = minTime = elapsed;

        String traceIdString = TransactionIdUtils.formatString(span.getTraceAgentId(), span.getTraceAgentStartTime(), span.getTraceTransactionSequence());
        Trace trace = new Trace(traceIdString, elapsed, span.getCollectorAcceptTime(), span.getErrCode());
        this.traces.add(trace);
        calls++;
        if(span.getErrCode() > 0) {
            error++;
        }
        calculateApdex(elapsed);
    }

    public void add(com.baidu.oped.apm.common.jpa.entity.Trace span) {
        if (span == null) {
            throw new NullPointerException("span must not be null");
        }

        long elapsed = span.getElapsed();

        totalTime += elapsed;
        if (maxTime < elapsed) {
            maxTime = elapsed;
        }
        if (minTime > elapsed) {
            minTime = elapsed;
        }
        calculateApdex(elapsed);

        String traceIdString = TransactionIdUtils.formatString(span.getTraceAgentId(), span.getTraceAgentStartTime(),
                                                                      span.getTraceTransactionSequence());
        Trace trace = new Trace(traceIdString, elapsed, span.getCollectorAcceptTime(), span.getErrCode());
        this.traces.add(trace);

        if(span.getErrCode() > 0) {
            error++;
        }

        //if (span.getParentSpanId() == -1) {
            calls++;
        //}
    }

    private void calculateApdex(double responseTime){
        ApdexUtils.Level level = ApdexUtils.Level.getLevel(apdex_t, responseTime);
        switch (level){
            case SATISFIED:
                satisfiedCount++;
                break;
            case TOLERATED:
                toleratedCount++;
                break;
            case FRUSTRATED:
            default:
                frustratedCount++;
                break;
        }
    }

    public String getRpc() {
        return rpc;
    }

    public List<Trace> getTraces() {
        return traces;
    }

    public int getCalls() {
        return calls;
    }

    public long getTotalTime() {
        return totalTime;
    }

    public long getMaxTime() {
        return maxTime;
    }

    public long getMinTime() {
        return minTime;
    }

    public int getError() {
        return error;
    }

    public int getSatisfiedCount() {
        return satisfiedCount;
    }

    public int getToleratedCount() {
        return toleratedCount;
    }

    public int getFrustratedCount() {
        return frustratedCount;
    }
}
