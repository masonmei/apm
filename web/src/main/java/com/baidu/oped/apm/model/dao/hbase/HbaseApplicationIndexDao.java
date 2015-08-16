//
//package com.baidu.oped.apm.model.dao.hbase;
//
//import com.baidu.oped.apm.common.hbase.HBaseTables;
//import com.baidu.oped.apm.common.hbase.HbaseOperations2;
//import com.baidu.oped.apm.model.dao.ApplicationIndexDao;
//import com.baidu.oped.apm.mvc.vo.Application;
//import org.apache.hadoop.hbase.client.Delete;
//import org.apache.hadoop.hbase.client.Get;
//import org.apache.hadoop.hbase.client.Scan;
//import org.apache.hadoop.hbase.util.Bytes;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.data.hadoop.hbase.RowMapper;
//import org.springframework.stereotype.Repository;
//import org.springframework.util.StringUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * class HbaseApplicationIndexDao
// *
// * @author meidongxu@baidu.com
// */
//@Repository
//public class HbaseApplicationIndexDao implements ApplicationIndexDao {
//
//    @Autowired
//    private HbaseOperations2 hbaseOperations2;
//
//    @Autowired
//    @Qualifier("applicationNameMapper")
//    private RowMapper<List<Application>> applicationNameMapper;
//
////    @Autowired
////    @Qualifier("agentIdMapper")
//    private RowMapper<List<String>> agentIdMapper;
//
//    @Override
//    public List<Application> selectAllApplicationNames() {
//        Scan scan = new Scan();
//        scan.setCaching(30);
//        List<List<Application>> results = hbaseOperations2.find(HBaseTables.APPLICATION_INDEX, scan, applicationNameMapper);
//        List<Application> applications = new ArrayList<Application>();
//        for (List<Application> result : results) {
//            applications.addAll(result);
//        }
//        return applications;
//    }
//
////    @Override
//    public List<String> selectAgentIds(String applicationName) {
//        if (applicationName == null) {
//            throw new NullPointerException("applicationName must not be null");
//        }
//        byte[] rowKey = Bytes.toBytes(applicationName);
//
//        Get get = new Get(rowKey);
//        get.addFamily(HBaseTables.APPLICATION_INDEX_CF_AGENTS);
//
//        return hbaseOperations2.get(HBaseTables.APPLICATION_INDEX, get, agentIdMapper);
//    }
//
////    @Override
//    public void deleteApplicationName(String applicationName) {
//        byte[] rowKey = Bytes.toBytes(applicationName);
//        Delete delete = new Delete(rowKey);
//        hbaseOperations2.delete(HBaseTables.APPLICATION_INDEX, delete);
//    }
//
////    @Override
//    public void deleteAgentId(String applicationName, String agentId) {
//        if (StringUtils.isEmpty(applicationName)) {
//            throw new IllegalArgumentException("applicationName cannot be empty");
//        }
//        if (StringUtils.isEmpty(agentId)) {
//            throw new IllegalArgumentException("agentId cannot be empty");
//        }
//        byte[] rowKey = Bytes.toBytes(applicationName);
//        Delete delete = new Delete(rowKey);
//        byte[] qualifier = Bytes.toBytes(agentId);
//        delete.addColumn(HBaseTables.APPLICATION_INDEX_CF_AGENTS, qualifier);
//        hbaseOperations2.delete(HBaseTables.APPLICATION_INDEX, delete);
//    }
//}
