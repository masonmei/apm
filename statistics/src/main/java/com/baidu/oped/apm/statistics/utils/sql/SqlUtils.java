package com.baidu.oped.apm.statistics.utils.sql;

/**
 * Created by mason on 10/20/15.
 */
public class SqlUtils {
    /**
     * Parse a CURD sql statement to a SqlStatement object.
     *
     * @param sql the string sql statement
     *
     * @return the SqlStatement Object.
     */
    public static SqlStatement parse(String sql) {
        return new SqlStatement(sql);
    }
}
