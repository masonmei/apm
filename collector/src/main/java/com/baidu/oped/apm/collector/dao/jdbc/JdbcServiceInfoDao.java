package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.common.bo.ServiceInfoBo;

/**
 * Created by mason on 8/15/15.
 */
@Repository
public class JdbcServiceInfoDao extends BaseRepository<ServiceInfoBo> {
    private static final String TABLE_NAME = "apm_service_info";

    public JdbcServiceInfoDao() {
        super(ServiceInfoBo.class, TABLE_NAME);
    }
}
