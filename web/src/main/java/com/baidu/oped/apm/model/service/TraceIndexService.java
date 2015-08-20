package com.baidu.oped.apm.model.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.baidu.oped.apm.model.dao.ApplicationTraceIndexDao;
import com.baidu.oped.apm.mvc.vo.Range;
import com.baidu.oped.apm.mvc.vo.TransactionId;

/**
 * Created by mason on 8/13/15.
 */
@Service
public class TraceIndexService {
    private static final Logger LOG = LoggerFactory.getLogger(TraceIndexService.class);

    @Autowired
    private ApplicationTraceIndexDao traceIndexDao;

    public List<TransactionId> selectTraceIds(String applicationName, Range range) {
        Assert.hasLength(applicationName, "ApplicationName cannot be null while selecting trace ids.");
        Assert.notNull(range, "Range cannot be null while selecting trace ids.");
        return traceIndexDao.scanTraceIndex(applicationName, range, -1);
    }
}
