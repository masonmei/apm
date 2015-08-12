
package com.baidu.oped.apm.common.util;


import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

/**
 * class DefaultParsingResult 
 *
 * @author meidongxu@baidu.com
 */
public class DefaultParsingResult implements ParsingResult {

    public static final char SEPARATOR = ',';

    private static final AtomicIntegerFieldUpdater<DefaultParsingResult> ID_UPDATER = AtomicIntegerFieldUpdater.newUpdater(DefaultParsingResult.class, "id");

    private String sql;
    private StringBuilder output;
    private volatile int id = ParsingResult.ID_NOT_EXIST;


    public DefaultParsingResult() {

    }

    public DefaultParsingResult(String sql, StringBuilder output) {
        this.output = output;
        this.sql = sql;
    }

    @Override
    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public int getId() {
        return id;
    }

    public boolean setId(int id) {
        return ID_UPDATER.compareAndSet(this, ID_NOT_EXIST, id);
    }

    @Override
    public String getOutput() {
        if (output == null) {
            return "";
        }
        return output.toString();
    }

    /**
     * Must be invoked at least once. If not, generates a NullPointerException upon invoking appendOutputParam.
     */
    void appendOutputSeparator() {
        if (output == null) {
            this.output = new StringBuilder();
        } else {
            this.output.append(SEPARATOR);
        }
    }

    void appendOutputParam(String str) {
        this.output.append(str);
    }

    void appendSeparatorCheckOutputParam(char ch) {
        if (ch == ',') {
            this.output.append(",,");
        } else {
            this.output.append(ch);
        }
    }

    void appendOutputParam(char ch) {
        this.output.append(ch);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("DefaultParsingResult{");
        sb.append("sql='").append(sql).append('\'');
        sb.append(", output=").append(output);
        sb.append(", id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
