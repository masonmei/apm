package com.baidu.oped.apm.collector.dao.jdbc;

import com.baidu.oped.apm.BaseDto;
import com.baidu.oped.apm.common.annotation.JdbcTables;
import com.baidu.oped.apm.common.entity.ServerMetaData;
import org.springframework.stereotype.Repository;

/**
 * Created by mason on 8/15/15.
 */
@Repository
public class JdbcServerMetaDataDao extends BaseDto<ServerMetaData> {

    @Override
    protected String tableName() {
        return JdbcTables.SERVER_META_DATA;
    }

}
