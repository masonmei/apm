package com.baidu.oped.apm.collector.dao.jdbc;

import com.baidu.oped.apm.BaseRepository;
import com.baidu.oped.apm.JdbcTables;
import com.baidu.oped.apm.common.entity.Annotation;
import org.springframework.stereotype.Repository;

/**
 * Created by mason on 8/17/15.
 */
@Repository
public class JdbcAnnotationDao extends BaseRepository<Annotation> {
    public JdbcAnnotationDao() {
        super(Annotation.class, JdbcTables.ANNOTATION);
    }

//    @Override
    protected String tableName() {
        return JdbcTables.ANNOTATION;
    }
}
