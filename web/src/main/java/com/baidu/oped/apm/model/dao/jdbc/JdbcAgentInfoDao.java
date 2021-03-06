//package com.baidu.oped.apm.model.dao.jdbc;
//
//import com.baidu.oped.apm.common.entity.AgentInfo;
//import com.baidu.oped.apm.common.jpa.repository.AgentInfoRepository;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by mason on 8/16/15.
// */
//@Repository
//public class JdbcAgentInfoDao extends BaseRepository<AgentInfo> {
//    @Autowired
//    private AgentInfoRepository agentInfoRepository;
//
//    public List<String> selectAllApplicationNames() {
//        List<String> applicationNames = new ArrayList<>();
//        List<AgentInfo> agentInfos = this.findByAttrs("distinct(application_name)", null);
//        for (AgentInfo agentInfo : agentInfos) {
//            applicationNames.add(agentInfo.getApplicationName());
//        }
//        return applicationNames;
//    }
//
//
//    public List<AgentInfo> findAgentInfoByApplicationName(String applicationName) {
//        List<AgentInfo> agentInfos = this.findByAttr("applicationName", applicationName);
//        return agentInfos;
//    }
//
//}
