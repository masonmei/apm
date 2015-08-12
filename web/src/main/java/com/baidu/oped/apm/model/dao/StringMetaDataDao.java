
package com.baidu.oped.apm.model.dao;

import java.util.List;

import com.baidu.oped.apm.common.bo.StringMetaDataBo;

/**
 * class StringMetaDataDao 
 *
 * @author meidongxu@baidu.com
 */
public interface StringMetaDataDao {
    List<StringMetaDataBo> getStringMetaData(String agentId, long time, int stringId);
}
