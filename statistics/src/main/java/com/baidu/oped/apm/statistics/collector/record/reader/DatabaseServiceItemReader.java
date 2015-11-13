package com.baidu.oped.apm.statistics.collector.record.reader;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * Created by mason on 9/4/15.
 */
@Component
public class DatabaseServiceItemReader extends TraceEventItemReader {

    private static final List<Integer> SERVICE_TYPE_DB = Arrays.asList(2100, 2101, 2300, 2301, 2400, 2401);

    @Override
    protected List<Integer> serviceTypes() {
        return SERVICE_TYPE_DB;
    }
}
