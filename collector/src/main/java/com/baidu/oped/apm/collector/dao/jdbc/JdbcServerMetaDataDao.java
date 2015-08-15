package com.baidu.oped.apm.collector.dao.jdbc;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.common.bo.ServerMetaDataBo;

/**
 * Created by mason on 8/15/15.
 */
@Repository
public class JdbcServerMetaDataDao  extends BaseRepository<ServerMetaDataBo> {
    private static final String TABLE_NAME = "apm_server_meta_data";

    public JdbcServerMetaDataDao() {
        super(ServerMetaDataBo.class, TABLE_NAME);
    }
}
