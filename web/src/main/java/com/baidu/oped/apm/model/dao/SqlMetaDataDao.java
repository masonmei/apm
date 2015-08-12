
package com.baidu.oped.apm.model.dao;

import java.util.List;

import com.baidu.oped.apm.common.bo.SqlMetaDataBo;

/**
 * class SqlMetaDataDao 
 *
 * @author meidongxu@baidu.com
 */
public interface SqlMetaDataDao {
    List<SqlMetaDataBo> getSqlMetaData(String agentId, long time, int hashCode);
}
