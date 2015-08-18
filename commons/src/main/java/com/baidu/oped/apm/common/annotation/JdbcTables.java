package com.baidu.oped.apm.common.annotation;

/**
 * Created by mason on 8/17/15.
 */
public interface JdbcTables {
    String TRACE = "apm_span";
    String TRACE_EVENT = "apm_span_event";
    String AGENT_CPU_LOAD = "apm_agent_cpu_load";
    String AGENT_INFO = "apm_agent_info";
    String AGENT_STAT_MEMORY_GC = "apm_agent_stat_memory_gc";
    String ANNOTATION = "apm_annotation";
    String API_META_DATA = "apm_api_meta_data";
    String SERVER_META_DATA = "apm_server_meta_data";
    String SERVER_INFO = "apm_service_info";
    String SQL_META_DATA = "apm_sql_meta_data";
    String STRING_META_DATA = "apm_string_meta_data";
    String HOST_APPLICATION_MAP = "apm_host_application_map";
}
