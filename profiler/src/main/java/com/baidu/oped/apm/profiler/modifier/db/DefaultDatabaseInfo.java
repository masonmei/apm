
package com.baidu.oped.apm.profiler.modifier.db;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;
import com.baidu.oped.apm.common.ServiceType;

import java.util.List;

/**
 * class DefaultDatabaseInfo 
 *
 * @author meidongxu@baidu.com
 */
public class DefaultDatabaseInfo implements DatabaseInfo {

    private ServiceType type = ServiceType.UNKNOWN_DB;
    private ServiceType executeQueryType = ServiceType.UNKNOWN_DB_EXECUTE_QUERY;
    private String databaseId;
    private String realUrl; // URL before refinement
    private String normalizedUrl;
    private List<String> host;
    private String multipleHost;

    public DefaultDatabaseInfo(ServiceType type, ServiceType executeQueryType, String realUrl, String normalizedUrl, List<String> host, String databaseId) {
        if (type == null) {
            throw new NullPointerException("type must not be null");
        }
        if (executeQueryType == null) {
            throw new NullPointerException("executeQueryType must not be null");
        }
        this.type = type;
        this.executeQueryType = executeQueryType;
        this.realUrl = realUrl;
        this.normalizedUrl = normalizedUrl;
        this.host = host;
        this.multipleHost = merge(host);
        this.databaseId = databaseId;
    }

    private String merge(List<String> host) {
        if (host.isEmpty()) {
            return "";
        }
        String single = host.get(0);
        StringBuilder sb = new StringBuilder();
        sb.append(single);
        for(int i =1; i<host.size(); i++) {
            sb.append(',');
            sb.append(host.get(i));
        }
        return sb.toString();
    }


    @Override
    public List<String> getHost() {
        // With replication, this is not simple because there could be multiple hosts or ports.
        return host;
    }

    @Override
    public String getMultipleHost() {
        return multipleHost;
    }

    @Override
    public String getDatabaseId() {
        return databaseId;
    }

    @Override
    public String getRealUrl() {
        return realUrl;
    }

    @Override
    public String getUrl() {
        return normalizedUrl;
    }

    @Override
    public ServiceType getType() {
        return type;
    }

    @Override
    public ServiceType getExecuteQueryType() {
        return executeQueryType;
    }

    @Override
    public String toString() {
        return "DatabaseInfo{" +
                "type=" + type +
                ", executeQueryType=" + executeQueryType +
                ", databaseId='" + databaseId + '\'' +
                ", realUrl='" + realUrl + '\'' +
                ", normalizedUrl='" + normalizedUrl + '\'' +
                ", host=" + host +
                '}';
    }
}
