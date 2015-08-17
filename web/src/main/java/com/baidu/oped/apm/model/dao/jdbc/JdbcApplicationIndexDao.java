package com.baidu.oped.apm.model.dao.jdbc;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.baidu.oped.apm.model.dao.ApplicationIndexDao;
import com.baidu.oped.apm.mvc.vo.Application;

/**
 * Created by mason on 8/16/15.
 */
@Repository
public class JdbcApplicationIndexDao implements ApplicationIndexDao {
    @Override
    public List<Application> selectAllApplicationNames() {
        return null;
    }

    @Override
    public List<String> selectAgentIds(String applicationName) {
        return null;
    }

    @Override
    public void deleteApplicationName(String applicationName) {

    }

    @Override
    public void deleteAgentId(String applicationName, String agentId) {

    }
}
