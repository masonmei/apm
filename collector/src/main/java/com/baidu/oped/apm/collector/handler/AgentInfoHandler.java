
package com.baidu.oped.apm.collector.handler;

import org.apache.thrift.TBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baidu.oped.apm.collector.dao.AgentInfoDao;
import com.baidu.oped.apm.collector.dao.ApplicationIndexDao;
import com.baidu.oped.apm.thrift.dto.TAgentInfo;
import com.baidu.oped.apm.thrift.dto.TResult;

/**
 * class AgentInfoHandler 
 *
 * @author meidongxu@baidu.com
 */
@Service("agentInfoHandler")
public class AgentInfoHandler implements SimpleHandler, RequestResponseHandler {

    private final Logger logger = LoggerFactory.getLogger(AgentInfoHandler.class.getName());

    @Autowired
    private AgentInfoDao agentInfoDao;

    @Autowired
    private ApplicationIndexDao applicationIndexDao;

    public void handleSimple(TBase<?, ?> tbase) {
        handleRequest(tbase);
    }

    @Override
    public TBase<?, ?> handleRequest(TBase<?, ?> tbase) {
        if (!(tbase instanceof TAgentInfo)) {
            logger.warn("invalid tbase:{}", tbase);
            // it happens to return null  not only at this BO(Business Object) but also at other BOs.

            return null;
        }

        try {
            TAgentInfo agentInfo = (TAgentInfo) tbase;

            logger.debug("Received AgentInfo={}", agentInfo);

            // agent info
            agentInfoDao.insert(agentInfo);

            // for querying agentid using applicationname
            applicationIndexDao.insert(agentInfo);

            return new TResult(true);

            // for querying applicationname using agentid
//            agentIdApplicationIndexDao.insert(agentInfo.getAgentId(), agentInfo.getApplicationName());
        } catch (Exception e) {
            logger.warn("AgentInfo handle error. Caused:{}", e.getMessage(), e);
            TResult result = new TResult(false);
            result.setMessage(e.getMessage());
            return result;
        }
    }

}
