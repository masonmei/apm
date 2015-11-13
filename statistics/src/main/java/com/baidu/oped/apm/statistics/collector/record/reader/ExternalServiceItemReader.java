package com.baidu.oped.apm.statistics.collector.record.reader;


import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;


/**
 * Created by mason on 9/4/15.
 */
@Component
public class ExternalServiceItemReader extends TraceEventItemReader {

    private static final List<Integer> SERVICE_TYPE_EXTERNAL =
            Arrays.asList(9050, 9051, 9052, 9053, 9054, 9055, 9058, 9059);

    @Override
    protected List<Integer> serviceTypes() {
        return SERVICE_TYPE_EXTERNAL;
    }
}
