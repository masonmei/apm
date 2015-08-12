
package com.baidu.oped.apm.common.hbase;

import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.springframework.data.hadoop.hbase.ResultsExtractor;
import org.springframework.data.hadoop.hbase.RowMapper;
import org.springframework.util.Assert;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * class LimitRowMapperResultsExtractor 
 *
 * @author meidongxu@baidu.com
 */
public class LimitRowMapperResultsExtractor<T> implements ResultsExtractor<List<T>> {

    private static final LimitEventHandler EMPTY = new EmptyLimitEventHandler();

    private int limit = Integer.MAX_VALUE;
    private final RowMapper<T> rowMapper;
    private LimitEventHandler eventHandler;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    /**
     * Create a new RowMapperResultSetExtractor.
     *
     * @param rowMapper the RowMapper which creates an object for each row
     */
    public LimitRowMapperResultsExtractor(RowMapper<T> rowMapper, int limit) {
        this(rowMapper, limit, EMPTY);
    }

    /**
     * Create a new RowMapperResultSetExtractor.
     *
     * @param rowMapper the RowMapper which creates an object for each row
     */
    public LimitRowMapperResultsExtractor(RowMapper<T> rowMapper, int limit, LimitEventHandler eventHandler) {
        Assert.notNull(rowMapper, "RowMapper is required");
        Assert.notNull(eventHandler, "LimitEventHandler is required");
        this.rowMapper = rowMapper;
        this.limit = limit;
        this.eventHandler = eventHandler;
    }

    public List<T> extractData(ResultScanner results) throws Exception {
        final List<T> rs = new ArrayList<T>();
        int rowNum = 0;
        Result lastResult = null;

        for (Result result : results) {
            final T t = this.rowMapper.mapRow(result, rowNum);
            lastResult = result;
            if (t instanceof Collection) {
                rowNum += ((Collection<?>) t).size();
            } else if (t instanceof Map) {
                rowNum += ((Map<?, ?>) t).size();
            } else if (t == null) {
                // empty
            } else if (t.getClass().isArray()) {
                rowNum += Array.getLength(t);
            } else {
                rowNum++;
            }
            rs.add(t);
            if (rowNum >= limit) {
                break;
            }
        }

        eventHandler.handleLastResult(lastResult);
        return rs;
    }
}
