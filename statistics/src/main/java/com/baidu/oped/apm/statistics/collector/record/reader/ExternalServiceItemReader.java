package com.baidu.oped.apm.statistics.collector.record.reader;

import static com.baidu.oped.apm.common.ServiceType.HTTP_CLIENT;
import static com.baidu.oped.apm.common.ServiceType.HTTP_CLIENT_INTERNAL;
import static com.baidu.oped.apm.common.ServiceType.JDK_HTTPURLCONNECTOR;
import static com.baidu.oped.apm.common.ServiceType.NIMM_CLIENT;
import static com.baidu.oped.apm.common.ServiceType.NPC_CLIENT;
import static com.baidu.oped.apm.common.ServiceType.NPC_CLIENT_INTERNAL;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.baidu.oped.apm.common.ServiceType;

/**
 * Created by mason on 9/4/15.
 */
@Component
public class ExternalServiceItemReader extends TraceEventItemReader {

    private static final ServiceType[] SERVICE_TYPE_EXTERNAL =
            new ServiceType[] {
                  HTTP_CLIENT,
                  HTTP_CLIENT_INTERNAL,
                  JDK_HTTPURLCONNECTOR,
                  NPC_CLIENT,
                  NPC_CLIENT_INTERNAL,
                  NIMM_CLIENT
            };

    @Override
    protected List<Integer> serviceTypes() {
        return Arrays.stream(SERVICE_TYPE_EXTERNAL)
                       .map(serviceType -> (int) serviceType.getCode())
                       .collect(Collectors.toList());
    }
}
