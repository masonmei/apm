package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.BaseDto;
import com.baidu.oped.apm.JdbcTables;
import com.baidu.oped.apm.common.entity.Annotation;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcAnnotationDao extends BaseDto<Annotation> {
    @Override
    protected String tableName() {
        return JdbcTables.ANNOTATION;
    }
}
