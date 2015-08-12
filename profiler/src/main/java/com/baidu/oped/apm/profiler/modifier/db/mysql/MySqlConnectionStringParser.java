
package com.baidu.oped.apm.profiler.modifier.db.mysql;

import com.baidu.oped.apm.bootstrap.context.DatabaseInfo;
import com.baidu.oped.apm.common.ServiceType;
import com.baidu.oped.apm.profiler.modifier.db.ConnectionStringParser;
import com.baidu.oped.apm.profiler.modifier.db.DefaultDatabaseInfo;
import com.baidu.oped.apm.profiler.modifier.db.JDBCUrlParser;
import com.baidu.oped.apm.profiler.modifier.db.StringMaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * class MySqlConnectionStringParser 
 *
 * @author meidongxu@baidu.com
 */
public class MySqlConnectionStringParser implements ConnectionStringParser {

    // jdbc:mysql:loadbalance://10.22.33.44:3306,10.22.33.55:3306/MySQL?characterEncoding=UTF-8
    private static final String JDBC_MYSQL_LOADBALANCE = "jdbc:mysql:loadbalance:";

    @Override
    public DatabaseInfo parse(String url) {
        if (url == null) {
            return JDBCUrlParser.createUnknownDataBase(ServiceType.MYSQL, ServiceType.MYSQL_EXECUTE_QUERY, null);
        }

        if (isLoadbalanceUrl(url)) {
            return parseLoadbalancedUrl(url);
        }
        return parseNormal(url);
    }

    private DatabaseInfo parseLoadbalancedUrl(String url) {
        // jdbc:mysql://1.2.3.4:5678/test_db
        StringMaker maker = new StringMaker(url);
        maker.after("jdbc:mysql:");
        // 1.2.3.4:5678 In case of replication driver could have multiple values
        // We have to consider mm db too.
        String host = maker.after("//").before('/').value();

        // Decided not to cache regex. This is not invoked often so don't waste memory.
        String[] parsedHost = host.split(",");
        List<String> hostList = Arrays.asList(parsedHost);


        String databaseId = maker.next().afterLast('/').before('?').value();
        String normalizedUrl = maker.clear().before('?').value();
        return new DefaultDatabaseInfo(ServiceType.MYSQL, ServiceType.MYSQL_EXECUTE_QUERY, url, normalizedUrl, hostList, databaseId);
    }

    private boolean isLoadbalanceUrl(String url) {
        return url.regionMatches(true, 0, JDBC_MYSQL_LOADBALANCE, 0, JDBC_MYSQL_LOADBALANCE.length());
    }

    private DatabaseInfo parseNormal(String url) {
     // jdbc:mysql://1.2.3.4:5678/test_db
        StringMaker maker = new StringMaker(url);
        maker.after("jdbc:mysql:");
        // 1.2.3.4:5678 In case of replication driver could have multiple values
        // We have to consider mm db too.
        String host = maker.after("//").before('/').value();
        List<String> hostList = new ArrayList<String>(1);
        hostList.add(host);
        // String port = maker.next().after(':').before('/').value();

        String databaseId = maker.next().afterLast('/').before('?').value();
        String normalizedUrl = maker.clear().before('?').value();
        return new DefaultDatabaseInfo(ServiceType.MYSQL, ServiceType.MYSQL_EXECUTE_QUERY, url, normalizedUrl, hostList, databaseId);
    }
}
