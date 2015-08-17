package com.baidu.oped.apm.collector.dao.jdbc;

import com.baidu.oped.apm.BaseDto;
import com.baidu.oped.apm.JdbcTables;
import com.baidu.oped.apm.common.entity.ServiceInfo;
import org.springframework.stereotype.Repository;

/**
 * Created by yangbolin on 8/15/15.
 */
@Repository
public class JdbcServiceInfoDao extends BaseDto<ServiceInfo> {

    @Override
    protected String tableName() {
        return JdbcTables.SERVER_INFO;
    }

}
