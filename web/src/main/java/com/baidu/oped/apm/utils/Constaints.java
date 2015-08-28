package com.baidu.oped.apm.utils;

/**
 * Created by mason on 8/26/15.
 */
public class Constaints {
    public static final String TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
    public static final String PAGE_SIZE = "20";
    public static final String PAGE_NUMBER = "1";
    public static final String DEFAULT_BOOL = "false";
    public static final String DEFAULT_USER = "testuser";

    public enum MetricName {
        RESPONSE_TIME("responseTime", "", ""),
        CPM("cpm", "", ""),
        ERROR_RATE("errorRate", "", ""),
        APDEX("apdex", "", ""),
        SATISFIED("satisfied", "", ""),
        TOLERATED("tolerated", "", ""),
        FRUSTRATED("frustrated", "", ""),
        MAX_RESPONSE_TIME("maxResponseTime", "", ""),
        MIN_RESPONSE_TIME("minResponseTime", "", ""),
        CPU_USAGE("cpuUsage", "", ""),
        MEMORY_USAGE("memoryUsage", "", "");

        private String fieldName;
        private String description;
        private String unit;

        MetricName(String fieldName, String description, String unit) {
            this.fieldName = fieldName;
            this.description = description;
            this.unit = unit;
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
}
