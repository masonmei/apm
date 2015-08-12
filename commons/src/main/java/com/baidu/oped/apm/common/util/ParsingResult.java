
package com.baidu.oped.apm.common.util;

/**
 * class ParsingResult 
 *
 * @author meidongxu@baidu.com
 */
public interface ParsingResult {

    int ID_NOT_EXIST = 0;

    String getSql();

    String getOutput();

    int getId();

    boolean setId(int id);
}
