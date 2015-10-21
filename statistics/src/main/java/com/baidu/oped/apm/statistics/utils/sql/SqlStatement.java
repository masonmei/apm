package com.baidu.oped.apm.statistics.utils.sql;

import com.baidu.oped.apm.common.jpa.entity.StatementType;
import com.baidu.oped.apm.common.utils.StringUtils;

/**
 * Created by mason on 10/20/15.
 */
public class SqlStatement {

    private final StatementType statementType;
    private final String[] tableNames;

    public SqlStatement(final String sql) {
        String firstWord = StringUtils.getSection(sql, 0);
        statementType = StatementType.get(firstWord);
        tableNames = getTableNames(sql, statementType);
    }

    public StatementType getStatementType() {
        return statementType;
    }

    public String[] getTableNames() {
        return tableNames;
    }

    public String getTableNamesInString() {
        return String.join(",", tableNames);
    }

    private String[] getTableNames(final String sql, StatementType type) {
        if (type == null) {
            return new String[0];
        }
        switch (type) {
            case SELECT:
                return getTableNamesInSelectStatement(sql);
            case INSERT:
                return new String[] {StringUtils.getSection(sql, 2)};
            case UPDATE:
                return new String[] {StringUtils.getSection(sql, 1)};
            case DELETE:
                return new String[] {StringUtils.getSection(sql, 2)};
            default:
                return new String[0];
        }
    }

    private String[] getTableNamesInSelectStatement(String sql) {
        String section = StringUtils.getSection(sql, "from", "where");
        return StringUtils.getSections(section, ",\\s*", "\\s+", 0);
    }
}
