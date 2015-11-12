
package com.baidu.oped.apm.collector.receiver;

import com.baidu.oped.apm.collector.handler.AgentInfoHandler;
import com.baidu.oped.apm.collector.handler.ApiMetaDataHandler;
import com.baidu.oped.apm.collector.handler.RequestResponseHandler;
import com.baidu.oped.apm.collector.handler.SimpleHandler;
import com.baidu.oped.apm.collector.handler.SqlMetaDataHandler;
import com.baidu.oped.apm.collector.handler.StringMetaDataHandler;
import com.baidu.oped.apm.thrift.dto.*;

import org.apache.thrift.TBase;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * class TcpDispatchHandler 
 *
 * @author meidongxu@baidu.com
 */
@Component
public class TcpDispatchHandler extends AbstractDispatchHandler {

    @Autowired
    private AgentInfoHandler agentInfoHandler;

    @Autowired
    private SqlMetaDataHandler sqlMetaDataHandler;

    @Autowired
    private ApiMetaDataHandler apiMetaDataHandler;

    @Autowired
    private StringMetaDataHandler stringMetaDataHandler;

    public TcpDispatchHandler() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    @Override
    RequestResponseHandler getRequestResponseHandler(TBase<?, ?> tBase) {
        if (tBase instanceof TSqlMetaData) {
            return sqlMetaDataHandler;
        }
        if (tBase instanceof TApiMetaData) {
            return apiMetaDataHandler;
        }
        if (tBase instanceof TStringMetaData) {
            return stringMetaDataHandler;
        }
        if (tBase instanceof TAgentInfo) {
            return agentInfoHandler;
        }
        return null;
    }

    @Override
    SimpleHandler getSimpleHandler(TBase<?, ?> tBase) {

        if (tBase instanceof TAgentInfo) {
            return agentInfoHandler;
        }

        return null;
    }
}
