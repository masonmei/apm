//package com.baidu.oped.apm.mvc.vo;
//
//import static com.baidu.oped.apm.utils.NumberUtils.format;
//
//import org.springframework.util.Assert;
//
///**
// * Created by mason on 8/13/15.
// */
//public class TransactionSummary {
//    private String transactionName;
//    private String transactionId;
//    private double apdex;
//    private double pv;
//    private double cpm;
//    private int unsatisfyCount;
//    private double unsatisfyRate;
//    private Metric rtMetric;
//    private double timePercentage;
//
//    public TransactionSummary(BusinessTransaction transaction, Range range) {
//        Assert.notNull(transaction, "Cannot convert to summary with null");
//        Assert.notNull(range, "Range cannot be null while create transaction summary.");
//        this.transactionName = transaction.getRpc();
//        this.transactionId = this.transactionName;
//        this.unsatisfyCount = transaction.getToleratedCount();
//        this.pv = format(transaction.getCalls());
//        this.cpm = format(this.pv * 60 * 1024 / range.getRange());
//        this.rtMetric = new Metric();
//
//        if (this.pv > 0) {
//            this.apdex = format((transaction.getSatisfiedCount() + (transaction.getToleratedCount() / 2.0)) / this.pv);
//            this.unsatisfyRate = format(this.unsatisfyCount / this.pv);
//
//            this.rtMetric.setAvg(format(transaction.getTotalTime() / this.pv));
//            this.rtMetric.setCount(format(this.pv));
//            this.rtMetric.setMax(format(transaction.getMaxTime()));
//            this.rtMetric.setMin(format(transaction.getMinTime()));
//            this.rtMetric.setSum(format(transaction.getTotalTime()));
//        } else {
//            this.apdex = 1;
//            this.unsatisfyRate = format(0.0);
//        }
//    }
//
//    public String getTransactionName() {
//        return transactionName;
//    }
//
//    public void setTransactionName(String transactionName) {
//        this.transactionName = transactionName;
//    }
//
//    public String getTransactionId() {
//        return transactionId;
//    }
//
//    public void setTransactionId(String transactionId) {
//        this.transactionId = transactionId;
//    }
//
//    public double getApdex() {
//        return apdex;
//    }
//
//    public void setApdex(double apdex) {
//        this.apdex = apdex;
//    }
//
//    public double getPv() {
//        return pv;
//    }
//
//    public void setPv(double pv) {
//        this.pv = pv;
//    }
//
//    public double getCpm() {
//        return cpm;
//    }
//
//    public void setCpm(double cpm) {
//        this.cpm = cpm;
//    }
//
//    public int getUnsatisfyCount() {
//        return unsatisfyCount;
//    }
//
//    public void setUnsatisfyCount(int unsatisfyCount) {
//        this.unsatisfyCount = unsatisfyCount;
//    }
//
//    public double getUnsatisfyRate() {
//        return unsatisfyRate;
//    }
//
//    public void setUnsatisfyRate(double unsatisfyRate) {
//        this.unsatisfyRate = unsatisfyRate;
//    }
//
//    public Metric getRtMetric() {
//        return rtMetric;
//    }
//
//    public void setRtMetric(Metric rtMetric) {
//        this.rtMetric = rtMetric;
//    }
//
//    public double getTimePercentage() {
//        return timePercentage;
//    }
//
//    public void setTimePercentage(double timePercentage) {
//        this.timePercentage = timePercentage;
//    }
//}
