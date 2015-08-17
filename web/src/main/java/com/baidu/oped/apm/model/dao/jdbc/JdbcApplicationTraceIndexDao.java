package com.baidu.oped.apm.model.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.model.dao.ApplicationTraceIndexDao;
import com.baidu.oped.apm.mvc.vo.Range;
import com.baidu.oped.apm.mvc.vo.TransactionId;

/**
 * Created by mason on 8/16/15.
 */
@Repository
public class JdbcApplicationTraceIndexDao implements ApplicationTraceIndexDao {
    @Override
    public List<TransactionId> scanTraceIndex(String applicationName, Range range, int limit) {
        return null;
    }
}
