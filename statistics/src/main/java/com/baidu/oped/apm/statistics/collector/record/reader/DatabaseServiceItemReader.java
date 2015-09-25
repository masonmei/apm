package com.baidu.oped.apm.statistics.collector.record.reader;

import static com.baidu.oped.apm.common.ServiceType.CUBRID_EXECUTE_QUERY;
import static com.baidu.oped.apm.common.ServiceType.MSSQL_EXECUTE_QUERY;
import static com.baidu.oped.apm.common.ServiceType.MYSQL_EXECUTE_QUERY;
import static com.baidu.oped.apm.common.ServiceType.ORACLE_EXECUTE_QUERY;
import static com.baidu.oped.apm.common.ServiceType.UNKNOWN_DB_EXECUTE_QUERY;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.ServiceType;

/**
 * Created by mason on 9/4/15.
 */
@Component
public class DatabaseServiceItemReader extends TraceEventItemReader {

    private static final ServiceType[] SERVICE_TYPE_DB =
            new ServiceType[] {
                    UNKNOWN_DB_EXECUTE_QUERY,
                    MYSQL_EXECUTE_QUERY,
                    MSSQL_EXECUTE_QUERY,
                    ORACLE_EXECUTE_QUERY,
                    CUBRID_EXECUTE_QUERY
            };

    @Override
    protected List<Integer> serviceTypes() {
        return Arrays.stream(SERVICE_TYPE_DB).map(serviceType -> (int) serviceType.getCode())
                .collect(Collectors.toList());
    }
}
