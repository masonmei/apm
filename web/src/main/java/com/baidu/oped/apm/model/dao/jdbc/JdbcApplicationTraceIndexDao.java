//package com.baidu.oped.apm.model.dao.jdbc;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import org.springframework.stereotype.Repository;
//
//import com.baidu.oped.apm.BaseRepository;
//import com.baidu.oped.apm.common.entity.ApplicationTraceIndex;
//import com.baidu.oped.apm.model.dao.ApplicationTraceIndexDao;
//import com.baidu.oped.apm.mvc.vo.Range;
//import com.baidu.oped.apm.mvc.vo.TransactionId;
//
///**
// * Created by mason on 8/16/15.
// */
//@Repository
//public class JdbcApplicationTraceIndexDao extends BaseRepository<ApplicationTraceIndex> implements
// ApplicationTraceIndexDao {
//
//
//    @Override
//    public List<TransactionId> scanTraceIndex(String applicationName, Range range, int limit) {
//        String select = "agent_id, agent_start_time, transaction_sequence";
//        String condition = "application_name=? AND accepted_time BETWEEN ? AND ? limit ?";
//
//        List<Object> params = new ArrayList<>();
//        params.add(applicationName);
//        params.add(range.getFrom());
//        params.add(range.getTo());
//        params.add(limit);
//
//        List<ApplicationTraceIndex> result = findBy(select, condition, params);
//        if(result != null){
//            return result.stream().map(index ->
//                new TransactionId(index.getAgentId(), index.getAgentStartTime(), index.getTransactionSequence())
//            ).collect(Collectors.toList());
//        }
//
//        return Collections.emptyList();
//    }
//}
////