package com.baidu.oped.apm.common.utils;

/**
 * Created by mason on 8/26/15.
 */
public abstract class Constraints {
    public static final String TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final Double PERIOD_TO_MINUTE_FACTOR = 1.0 / (60 * 1000);
    public static final String PAGE_SIZE = "20";
    public static final String PAGE_NUMBER = "0";
    public static final String DEFAULT_BOOL = "false";
    public static final String DEFAULT_USER = "testuser";
    public static final String ORDER_DESC_ID = "-id";

    public enum CommonMetricValue implements MetricValue {
        COUNT("次数", ""),
        SUM("总数", ""),
        AVG("平均值", ""),
        MIN("最小值", ""),
        MAX("最大值", "");

        private String description;
        private String unit;

        CommonMetricValue(String description, String unit) {
            this.description = description;
            this.unit = unit;
        }

        @Override
        public String getName() {
            return name();
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public String getUnit() {
            return unit;
        }
    }

    public enum StatisticMetricValue implements MetricValue {
        RESPONSE_TIME("responseTime", "响应时间", ""),
        PV("pv", "PV", ""),
        CPM("cpm", "吞吐量", ""),
        ERROR("error", "错误数", ""),
        ERROR_RATE("errorRate", "错误率", ""),
        APDEX("apdex", "满意度", ""),
        SATISFIED("satisfied", "满意数", ""),
        TOLERATED("tolerated", "可容忍数", ""),
        FRUSTRATED("frustrated", "不能接受数", ""),
        MAX_RESPONSE_TIME("maxResponseTime", "最大响应时间", ""),
        MIN_RESPONSE_TIME("minResponseTime", "最小响应时间", ""),
        CPU_USAGE("cpuUsage", "Cpu使用", ""),
        MEMORY_USAGE("memoryUsage", "内容使用", "");

        private String fieldName;
        private String description;
        private String unit;

        StatisticMetricValue(String fieldName, String description, String unit) {
            this.fieldName = fieldName;
            this.description = description;
            this.unit = unit;
        }

        @Override
        public String getName() {
            return fieldName;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getDescription() {
            return description;
        }

        public String getUnit() {
            return unit;
        }
    }

    public enum JVMMetricName implements MetricName {
        HEAP_INIT("Memory/Heap/Init", ""),
        HEAP_USED("Memory/Heap/Used", ""),
        HEAP_MAX("Memory/Heap/Max", ""),
        HEAP_COMMITTED("Memory/Heap/Committed", ""),
        NON_HEAP_INIT("Memory/Non-Heap/Init", ""),
        NON_HEAP_USED("Memory/Non-Heap/Used", ""),
        NON_HEAP_MAX("Memory/Non-Heap/Max", ""),
        NON_HEAP_COMMITTED("Memory/Non-Heap/Committed", ""),
        TOTAL_INIT("Memory/Total/Init", ""),
        TOTAL_USED("Memory/Total/Used", ""),
        TOTAL_MAX("Memory/Total/Max", ""),
        TOTAL_COMMITTED("Memory/Total/Committed", "");

        private String description;
        private String unit;

        JVMMetricName(String description, String unit) {
            this.description = description;
            this.unit = unit;
        }

        public String getName() {
            return this.name();
        }

        public String getDescription() {
            return description;
        }

        public String getUnit() {
            return unit;
        }
    }

    public enum ThreadMetricName implements MetricName {
        TOTAL("Thread/Total", ""),
        DAEMON("Thread/Daemon", ""),
        DEADLOCK("Thread/Deadlock", "");

        private String description;
        private String unit;

        ThreadMetricName(String description, String unit) {
            this.description = description;
            this.unit = unit;
        }

        public String getName() {
            return this.name();
        }

        public String getDescription() {
            return description;
        }

        public String getUnit() {
            return unit;
        }
    }

    public enum GarbageMetricName implements MetricName {
        COUNT("Garbage/Count", ""),
        TIME("Garbage/time", "");
        private String description;
        private String unit;

        GarbageMetricName(String description, String unit) {
            this.description = description;
            this.unit = unit;
        }

        public String getName() {
            return this.name();
        }

        public String getDescription() {
            return description;
        }

        public String getUnit() {
            return unit;
        }
    }

    public enum CpuLoadMetricName implements MetricName {
        JVM("CpuLoad/Jvm", ""),
        SYSTEM("CpuLoad/System", "");

        private String description;
        private String unit;

        CpuLoadMetricName(String description, String unit) {
            this.description = description;
            this.unit = unit;
        }

        public String getName() {
            return this.name();
        }

        public String getDescription() {
            return description;
        }

        public String getUnit() {
            return unit;
        }
    }

    public interface MetricValue {
        String getName();

        String getDescription();

        String getUnit();
    }

    public interface MetricName {
        String getName();

        String getDescription();

        String getUnit();
    }

}
