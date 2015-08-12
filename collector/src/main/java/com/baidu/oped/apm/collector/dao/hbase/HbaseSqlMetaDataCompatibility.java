
package com.baidu.oped.apm.collector.dao.hbase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.collector.dao.SqlMetaDataDao;
import com.baidu.oped.apm.common.hbase.HBaseAdminTemplate;
import com.baidu.oped.apm.common.hbase.HBaseTables;
import com.baidu.oped.apm.thrift.dto.TSqlMetaData;

/**
 * class HbaseSqlMetaDataCompatibility 
 *
 * @author meidongxu@baidu.com
 */
@Repository
@Deprecated
public class HbaseSqlMetaDataCompatibility implements SqlMetaDataDao {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
    private final boolean SQL_METADATA_VER2_EXISTED;
    private final boolean SQL_METADATA_EXISTED;

    @Autowired
    @Qualifier("hbaseSqlMetaDataPastVersionDao")
    private SqlMetaDataDao hbaseSqlMetaDataPastVersionDao;
    
    @Autowired
    @Qualifier("hbaseSqlMetaDataDao")
    private SqlMetaDataDao hbaseSqlMetaDataDao;
    
    @Autowired
    public HbaseSqlMetaDataCompatibility(HBaseAdminTemplate hBaseAdminTemplate) {
        SQL_METADATA_VER2_EXISTED = hBaseAdminTemplate.tableExists(HBaseTables.SQL_METADATA_VER2);
        
        if (SQL_METADATA_VER2_EXISTED == false) {
            logger.warn("Please create 'SqlMetaData_Ver2' table.");
        }
        
        SQL_METADATA_EXISTED = hBaseAdminTemplate.tableExists(HBaseTables.SQL_METADATA);
        
        if (SQL_METADATA_EXISTED == true) {
            logger.warn("SqlMetaData table exists. Recommend that only use SqlMetaData_Ver2 table.");
        }
        
        if(SQL_METADATA_EXISTED == false && SQL_METADATA_VER2_EXISTED == false) {
            throw new RuntimeException("Please check for sqlMetaData_ver2 table in HBase. You Should create 'SqlMetaData_Ver2' table.");
        }
    }
    
    @Override
    public void insert(TSqlMetaData sqlMetaData) {
        if (SQL_METADATA_VER2_EXISTED) {
            hbaseSqlMetaDataDao.insert(sqlMetaData);
            return;
        } 
        
        if (SQL_METADATA_EXISTED) {
            hbaseSqlMetaDataPastVersionDao.insert(sqlMetaData);
        }
    }
    
    public void setHbaseSqlMetaDataPastVersionDao(SqlMetaDataDao hbaseSqlMetaDataPastVersionDao) {
        this.hbaseSqlMetaDataPastVersionDao = hbaseSqlMetaDataPastVersionDao;
    }
    
    public void setHbaseSqlMetaDataDao(SqlMetaDataDao hbaseSqlMetaDataDao) {
        this.hbaseSqlMetaDataDao = hbaseSqlMetaDataDao;
    }
}
